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
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public abstract class Section<T, E> {

    public static final int DEFAULT_NUM_COLUMNS = 3;

    public static final int FILL_PARENT = -10;

    protected int offset = 0;

    boolean mHeaderEnabled = false;

    int numColumnCount = DEFAULT_NUM_COLUMNS;

    int columnWidthInDp = -1;

    E mExtra;

    ArrayList<T> mItems;

    List<RecyclerView.ItemDecoration> mDecors;

    public Section() {
        mItems = new ArrayList<>();
    }

    public Section(E extra) {
        this();
        setExtra(extra);
    }

    public Section(boolean isHeaderEnabled) {
        this();
        mHeaderEnabled = isHeaderEnabled;
    }

    public final boolean isHeaderEnabled() {
        return mHeaderEnabled;
    }

    public final boolean isHeader(int position) {
        return mHeaderEnabled && (position == 0);
    }

    public Section setExtra(E extra) {
        mExtra = extra;
        return this;
    }

    public E getExtra() {
        return mExtra;
    }

    public void setColumnWidthInDp(int width) {
        columnWidthInDp = width;
    }

    public void updateMaxColSpan(Context context) {
        if (columnWidthInDp > 0) {
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            int displayWidthInDp = (int) (metrics.widthPixels / metrics.density);
            numColumnCount = Math.max(1, displayWidthInDp / columnWidthInDp);
            if (numColumnCount > 3 && numColumnCount % 2 == 1) {
                numColumnCount = numColumnCount - 1;
            }
        }
    }

    public void addItemDecoration(RecyclerView.ItemDecoration decor) {
        if (mDecors == null) {
            mDecors = new ArrayList<>();
        }
        mDecors.add(decor);
    }

    public List<RecyclerView.ItemDecoration> getItemDecoration() {
        return mDecors;
    }

    public void setItems(List<T> items) {
        if (mItems.size() != 0) {
            mItems.clear();
        }
        mItems.addAll(items);
    }

    public int getItemCount() {
        return mItems.size();
    }

    public T getItem(int position) {
        return mItems.get(isHeaderEnabled() ? position - 1 : position);
    }

    public List<T> getItems() {
        return mItems;
    }

    public int getChildCount() {
        return isHeaderEnabled() ? getItemCount() + 1 : getItemCount();
    }

    public abstract int getItemViewType(int position);

    public abstract int[] getItemViewTypes();

    public int getItemSpan(int position) {
        return isHeader(position) ? FILL_PARENT : numColumnCount;
    }

    public int getColumnCount() {
        return numColumnCount;
    }

    public abstract RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindViewHolder(RecyclerView.ViewHolder holder, int position);

    public static class DefaultHeaderHolder extends RecyclerView.ViewHolder {

        public static final int ITEM_TYPE = -880319;

        private View mRoot;

        public TextView mTvTitle;

        public static DefaultHeaderHolder newHolder(ViewGroup parent) {
            return newHolder(parent, true);
        }

        public static DefaultHeaderHolder newHolder(ViewGroup parent, boolean isMoreEnabled) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.section_header, parent, false);
            DefaultHeaderHolder holder = new DefaultHeaderHolder(v);
            holder.mRoot.setClickable(isMoreEnabled);
            holder.mRoot.findViewById(R.id.tv_section_header_more)
                    .setVisibility(isMoreEnabled ? View.VISIBLE : View.GONE);

            TypedValue value = new TypedValue();
            boolean resolved = parent.getContext().getTheme()
                    .resolveAttribute(R.attr.colorPrimary, value, true);
            if (resolved) {
                holder.setMoreButtonColor(value.data);
            } else {
                holder.setMoreButtonColor(Color.parseColor("455A64"));
            }

            return holder;
        }

        private DefaultHeaderHolder(View v) {
            super(v);
            mRoot = v.findViewById(R.id.rl_section_header);
            mTvTitle = (TextView) mRoot.findViewById(R.id.tv_section_header_title);
        }

        public void setMoreButtonColor(int color) {
            mRoot.findViewById(R.id.tv_section_header_more).setBackgroundColor(color);
        }

        public void setOnTitleClickListener(final OnTitleClickListener listener) {
            if (mRoot.isClickable()) {
                mRoot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onClick(v);
                    }
                });
            }
        }

        public interface OnTitleClickListener {

            void onClick(View v);
        }
    }

}
