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

public class SectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    int childCnt = 0;
    ArrayList<Section> mSections;
    SparseArray<Section> mViewTypes;
    SectionLayoutManager mSectionLayoutManager;
    Context mContext;

    public SectionAdapter(Context context) {
        mContext = context;
        mSections = new ArrayList<>(5);
        mViewTypes = new SparseArray<>();
        mSectionLayoutManager = new SectionLayoutManager(context);
    }

    private Section findSectionByViewType(int viewType) {
        Section section = mViewTypes.get(viewType);
        if (section == null) {
            throw new IllegalArgumentException("Could not find the Section with view type " + viewType);
        }
        return section;
    }

    private void calculateOffsetForSection() {
        int offset = 0;
        for (Section section : mSections) {
            section.offset = offset;
            offset += section.getChildCount();
        }
        childCnt = offset;
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

    public void add(Section section) {
        add(mSections.size() > 0 ? mSections.size() : 0, section);
    }

    public void add(int index, Section section) {
        mSections.add(index, section);
        int[] viewTypes = section.getItemViewTypes();
        for(int viewType : viewTypes) {
            mViewTypes.append(viewType, section);
        }
        calculateOffsetForSection();
        notifyDataSetChanged();
    }

    public void remove(int index) {
        Section section = mSections.remove(index);
        if (section != null) {
            int[] viewTypes = section.getItemViewTypes();
            for (int viewType : viewTypes) {
                mViewTypes.delete(viewType);
            }
            calculateOffsetForSection();
            notifyDataSetChanged();
        }
    }

    public void set(int index, Section section) {
        mSections.set(index, section);
        calculateOffsetForSection();
        notifyDataSetChanged();
    }

    public void setMaxColSpan(int numColumns) {

        mSectionLayoutManager  = new SectionLayoutManager(mContext, numColumns);
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

    public SectionLayoutManager getLayoutManager() {
        return mSectionLayoutManager;
    }

    class SectionLayoutManager extends GridLayoutManager {

        public SectionLayoutManager(Context context) {
            this(context, Section.DEFAULT_NUM_COLUMNS);
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
