/*
 * Copyright (C) 2015 Taeho Kim <jyte82@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.androidhuman.sectionadapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class SectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    int childCnt = 0;

    final ArrayList<Section> mSections;

    SparseArray<Section> mViewTypes;

    Context mContext;

    public SectionAdapter(Context context) {
        mContext = context;
        mSections = new ArrayList<>(5);
        mViewTypes = new SparseArray<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return findSectionByViewType(viewType).onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindViewHolderInSection(holder, position);
    }

    @Override
    public int getItemCount() {
        return childCnt;
    }

    @Override
    public int getItemViewType(int position) {
        for (Section section : mSections) {
            int views = section.getChildCount();
            if (position < views) {
                return section.getItemViewType(position);
            } else {
                position -= views;
            }
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public void add(Section<?, ?> section, RecyclerView view) {
        add(mSections.size() > 0 ? mSections.size() : 0, section, view);
    }

    public void add(int index, Section<?, ?> section, RecyclerView view) {
        synchronized (mSections) {
            List<RecyclerView.ItemDecoration> decors = section.getItemDecoration();
            if (decors != null) {
                for (RecyclerView.ItemDecoration decor : decors) {
                    view.addItemDecoration(decor);
                }
            }

            mSections.add(index, section);
            int[] viewTypes = section.getItemViewTypes();
            for (int viewType : viewTypes) {
                mViewTypes.append(viewType, section);
            }

            calculateOffsetForSection();
            updateMaxColSpan(view);
        }
    }

    public void set(int index, Section section) {
        synchronized (mSections) {
            mSections.set(index, section);
            calculateOffsetForSection();
        }
    }

    public void setupWithRecyclerView(RecyclerView view) {
        view.setLayoutManager(new SectionLayoutManager(mContext));
        view.setAdapter(this);
    }

    public void remove(int index) {
        synchronized (mSections) {
            Section section = mSections.remove(index);
            if (section != null) {
                int[] viewTypes = section.getItemViewTypes();
                for (int viewType : viewTypes) {
                    mViewTypes.delete(viewType);
                }
                calculateOffsetForSection();
            }
        }
    }

    private void bindViewHolderInSection(RecyclerView.ViewHolder holder, int position) {
        for (Section section : mSections) {
            int views = section.getChildCount();
            if (position < views) {
                section.onBindViewHolder(holder, position);
                return;
            } else {
                position -= views;
            }
        }
        throw new ArrayIndexOutOfBoundsException("Tried to bind invalid position " + position);
    }

    private void calculateOffsetForSection() {
        int offset = 0;
        for (Section section : mSections) {
            section.offset = offset;
            offset += section.getChildCount();
        }
        childCnt = offset;
    }

    private Section findSectionByViewType(int viewType) {
        Section section = mViewTypes.get(viewType);
        if (section == null) {
            throw new IllegalArgumentException(
                    "Could not find the Section with view type " + viewType);
        }
        return section;
    }

    private int getRequiredColumnCount(int position) {
        for (Section section : mSections) {
            int views = section.getChildCount();
            if (position < views) {
                return section.getItemSpan(position);
            } else {
                position -= views;
            }
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    private void updateMaxColSpan(RecyclerView view) {
        int max = 0;
        for (Section sec : mSections) {
            sec.updateMaxColSpan(mContext);
            int sectionMaxColSpan = sec.getColumnCount();
            max = Math.max(max, sectionMaxColSpan);
        }
        view.setLayoutManager(new SectionLayoutManager(mContext, max));
    }

    class SectionLayoutManager extends GridLayoutManager {

        public SectionLayoutManager(Context context) {
            this(context, 5);
        }

        public SectionLayoutManager(Context context, final int spanCount) {
            super(context, spanCount);
            setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int columnCount = getRequiredColumnCount(position);
                    return (columnCount == Section.FILL_PARENT) ?
                            spanCount : spanCount / columnCount;
                }
            });
        }
    }
}
