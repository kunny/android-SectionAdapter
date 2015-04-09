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
import android.util.AttributeSet;

public class SectionRecyclerView extends RecyclerView {

    int measuredWidth = -1;
    OnMeasureListener mMeasureListener;

    public SectionRecyclerView(Context context) {
        super(context);
    }

    public SectionRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SectionRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (adapter instanceof SectionAdapter) {
            super.setAdapter(adapter);
            setLayoutManager(getAdapter().getLayoutManager());
        } else {
            throw new IllegalArgumentException("Adapter should be instance of SectionAdapter.");
        }
    }

    @Override
    public SectionAdapter getAdapter() {
        return (SectionAdapter) super.getAdapter();
    }

    public void setOnMeasureListener(OnMeasureListener listener) {
        mMeasureListener = listener;
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        // Call OnMeasureListener.onMeasure only if width has changed
        if (measuredWidth != getMeasuredWidth()) {
            measuredWidth = getMeasuredWidth();
            mMeasureListener.onMeasure(measuredWidth, getMeasuredHeight());
        }

    }

    interface OnMeasureListener {
        void onMeasure(int measuredWidth, int measuredHeight);
    }
}
