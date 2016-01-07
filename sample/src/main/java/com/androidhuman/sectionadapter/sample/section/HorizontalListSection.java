package com.androidhuman.sectionadapter.sample.section;

import com.androidhuman.sectionadapter.Section;
import com.androidhuman.sectionadapter.sample.R;
import com.androidhuman.sectionadapter.sample.model.ListItem;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class HorizontalListSection extends Section<ListItem> {

    private static final int ITEM_TYPE_HORIZONTAL_LIST = 0;

    private int[] itemViewTypes = new int[]{ITEM_TYPE_HORIZONTAL_LIST};

    public HorizontalListSection() {
        List<ListItem> items = new ArrayList<>(7);
        items.add(new ListItem("https://lh3.ggpht.com/O0aW5qsyCkR2i7Bu-jUU1b5BWA_NygJ6ui4MgaAvL7gfqvVWqkOBscDaq4pn-vkwByUx=w300-rw", "Chrome"));
        items.add(new ListItem("https://lh6.ggpht.com/fR_IJDfD1becp10IEaG2ly07WO4WW0LdZGUaNSrscqpgr9PI53D3Cp0yd2dXOgyux8w=w300-rw", "Dropbox"));
        items.add(new ListItem("https://lh4.ggpht.com/bT2W_cLEBJ58KwL3F9N3FfecplkcC4RaB-OFpA120dp8MBfiHOo6W0yXhaY6I5yD7Ck=w300-rw", "Flipboard"));
        items.add(new ListItem("https://lh6.ggpht.com/8-N_qLXgV-eNDQINqTR-Pzu5Y8DuH0Xjz53zoWq_IcBNpcxDL_gK4uS_MvXH00yN6nd4=w300-rw", "Gmail"));
        items.add(new ListItem("https://lh3.googleusercontent.com/DKoidc0T3T1KvYC2stChcX9zwmjKj1pgmg3hXzGBDQXM8RG_7JjgiuS0CLOh8DUa7as=w300-rw", "Google"));
        items.add(new ListItem("https://lh6.ggpht.com/k7Z4J1IIXXJnC2NRnFfJNlkn7kZge4Zx-Yv5uqYf4222tx74wXDzW24OvOxlcpw0KcQ=w300-rw", "Google Drive"));
        items.add(new ListItem("https://lh5.ggpht.com/4FGu-qxd6JYpjLJmBkJgz5xoxCpcnX9s3JJ5NevI8v1GuW9E4kOQMr57-Hg5V2HIHxQ=w300-rw", "Google Play Games"));
        setItems(items);
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE_HORIZONTAL_LIST;
    }

    @Override
    public int[] getItemViewTypes() {
        return itemViewTypes;
    }

    @Override
    public int getChildCount() {
        return 1;
    }

    @Override
    public int getItemSpan(int position) {
        return Section.MATCH_PARENT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView view = new RecyclerView(parent.getContext());
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, 700));
        view.setLayoutManager(new LinearLayoutManager(parent.getContext(), LinearLayoutManager.HORIZONTAL, false));
        view.addItemDecoration(new DefaultItemMarginDecoration());
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RecyclerViewHolder) {
            ItemAdapter adapter = new ItemAdapter(((RecyclerViewHolder) holder).rvRecyclerView.getContext());
            ((RecyclerViewHolder) holder).rvRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {

        RecyclerView rvRecyclerView;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            rvRecyclerView = (RecyclerView) itemView;
        }
    }

    class ItemAdapter extends RecyclerView.Adapter<ItemHolder> {

        private DrawableRequestBuilder<String> mImageRequest;

        public ItemAdapter(Context context) {
            mImageRequest = Glide.with(context).fromString();
        }

        @Override
        public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_horizontal_list, parent, false));
        }

        @Override
        public void onBindViewHolder(ItemHolder holder, int position) {
            final ListItem item = getItem(position);

            mImageRequest.load(item.imageUrl)
                    .into(holder.ivCover);
            holder.tvTitle.setText(item.title);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Clicked " + item.title, Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return HorizontalListSection.this.getItemCount();
        }
    }

    class ItemHolder extends RecyclerView.ViewHolder {

        ImageView ivCover;

        TextView tvTitle;

        public ItemHolder(View itemView) {
            super(itemView);
            ivCover = (ImageView) itemView.findViewById(R.id.iv_item_horizontal_list);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_item_horizontal_list);
        }
    }

    class DefaultItemMarginDecoration extends RecyclerView.ItemDecoration {

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);

            outRect.left = 40;
            outRect.top = 40;
            outRect.bottom = 40;
            if (position == getItemCount()-1) {
                outRect.right = 40;
            }
        }
    }

}
