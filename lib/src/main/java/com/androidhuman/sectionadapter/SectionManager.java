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
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.List;

public class SectionManager implements SectionRecyclerView.OnMeasureListener {

    private static final String TAG = SectionManager.class.getSimpleName();

    private Context mContext;
    private SectionRecyclerView mRecyclerView;
    private SectionAdapter mSectionAdapter;

    public SectionManager(SectionRecyclerView view, SectionAdapter adapter, int numColumns) {
        mContext = view.getContext();
        setRecyclerView(view);
        setSectionAdapter(adapter, numColumns);

        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(adapter.getLayoutManager());
    }

    public SectionManager(SectionRecyclerView view, SectionAdapter adapter) {
        mContext = view.getContext();
        setRecyclerView(view);
        setSectionAdapter(adapter);

        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(adapter.getLayoutManager());

    }

    public void setRecyclerView(SectionRecyclerView view) {
        mRecyclerView = view;
        mRecyclerView.setOnMeasureListener(this);
    }

    public void setSectionAdapter(SectionAdapter adapter, int numColumns) {
        mSectionAdapter = adapter;
        mSectionAdapter.setMaxColSpan(numColumns);
    }

    public void setSectionAdapter(SectionAdapter adapter) {
        mSectionAdapter = adapter;
    }

    private void updateMaxColSpan() {
        int max = 0;
        for (Section sec : mSectionAdapter.mSections) {
            sec.updateMaxColSpan(mContext);
            int sectionMaxColSpan = sec.getNumColumns();
            max = Math.max(max, sectionMaxColSpan);
        }
        mSectionAdapter.setMaxColSpan(max);
        mRecyclerView.setLayoutManager(mSectionAdapter.getLayoutManager());
    }

    public void add(Section<?> section) {
        add(mSectionAdapter.mSections.size() > 0 ?
                mSectionAdapter.mSections.size() : 0, section);
    }

    public void add(int index, Section<?> section) {
        synchronized (this) {
            List<RecyclerView.ItemDecoration> decors = section.getItemDecoration();
            if (decors != null) {
                for (RecyclerView.ItemDecoration decor : decors) {
                    mRecyclerView.addItemDecoration(decor);
                }
            }
            mSectionAdapter.add(index, section);
            updateMaxColSpan();
        }
    }

    public void addItemDecoration(RecyclerView.ItemDecoration decor) {
        mRecyclerView.addItemDecoration(decor);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration decor, int index) {
        mRecyclerView.addItemDecoration(decor, index);
    }

    public void removeItemDecoration(RecyclerView.ItemDecoration decor) {
        mRecyclerView.removeItemDecoration(decor);
    }

    public void set(int index, Section<?> section) {
        synchronized (this) {
            mSectionAdapter.set(index, section);
        }
    }

    public void set(Section<?> section) {
        int idx = findIndexBySection(section);
        if (idx == -1) {
            throw new IllegalArgumentException("Cannot find index for section " + section);
        }
        set(idx, section);
    }

    public void remove(Section<?> section) {
        synchronized (this) {
            if (exists(section)) {
                mSectionAdapter.mSections.remove(section);
            } else {
                Log.e(TAG, "Section " + section + " does not exists.");
            }
        }
    }

    public void remove(int index) {
        synchronized (this) {
            Section<?> section = mSectionAdapter.mSections.get(index);
            List<RecyclerView.ItemDecoration> decors = section.getItemDecoration();
            if (decors != null) {
                for (RecyclerView.ItemDecoration decor : decors) {
                    mRecyclerView.removeItemDecoration(decor);
                }
            }
            mSectionAdapter.remove(index);
        }
    }

    public boolean exists(Section<?> section) {
        synchronized (this) {
            return mSectionAdapter.mSections.contains(section);
        }
    }

    public int findIndexBySection(Section section) {
        synchronized (this) {
            int cnt = mSectionAdapter.mSections.size();
            for (int i=0; i<cnt; i++) {
                if (section.equals(mSectionAdapter.mSections.get(i))) {
                    return i;
                }
            }
            return -1;
        }
    }

    @Override
    public void onMeasure(int measuredWidth, int measuredHeight) {
        updateMaxColSpan();
    }
}
