package com.androidhuman.sectionadapter.sample;

import com.androidhuman.sectionadapter.SectionAdapter;
import com.androidhuman.sectionadapter.sample.model.ListItem;
import com.androidhuman.sectionadapter.sample.section.HorizontalListSection;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class GooglePlayListActivity extends AppCompatActivity {

    RecyclerView rvRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_play_list);

        rvRecyclerView = (RecyclerView) findViewById(R.id.rv_activity_google_play);

        SectionAdapter adapter = new SectionAdapter(this);
        adapter.setupWithRecyclerView(rvRecyclerView);

        adapter.add(newSampleSection());
        adapter.add(new HorizontalListSection());
        adapter.add(new HorizontalListSection());
        adapter.add(new HorizontalListSection());

        adapter.notifyDataSetChanged();
    }

    private HorizontalListSection newSampleSection() {
        List<ListItem> items = new ArrayList<>(7);
        items.add(new ListItem("https://lh3.ggpht.com/O0aW5qsyCkR2i7Bu-jUU1b5BWA_NygJ6ui4MgaAvL7gfqvVWqkOBscDaq4pn-vkwByUx=w300-rw", "Chrome"));
        items.add(new ListItem("https://lh6.ggpht.com/fR_IJDfD1becp10IEaG2ly07WO4WW0LdZGUaNSrscqpgr9PI53D3Cp0yd2dXOgyux8w=w300-rw", "Dropbox"));
        items.add(new ListItem("https://lh4.ggpht.com/bT2W_cLEBJ58KwL3F9N3FfecplkcC4RaB-OFpA120dp8MBfiHOo6W0yXhaY6I5yD7Ck=w300-rw", "Flipboard"));
        items.add(new ListItem("https://lh6.ggpht.com/8-N_qLXgV-eNDQINqTR-Pzu5Y8DuH0Xjz53zoWq_IcBNpcxDL_gK4uS_MvXH00yN6nd4=w300-rw", "Gmail"));
        items.add(new ListItem("https://lh3.googleusercontent.com/DKoidc0T3T1KvYC2stChcX9zwmjKj1pgmg3hXzGBDQXM8RG_7JjgiuS0CLOh8DUa7as=w300-rw", "Google"));
        items.add(new ListItem("https://lh6.ggpht.com/k7Z4J1IIXXJnC2NRnFfJNlkn7kZge4Zx-Yv5uqYf4222tx74wXDzW24OvOxlcpw0KcQ=w300-rw", "Google Drive"));
        items.add(new ListItem("https://lh5.ggpht.com/4FGu-qxd6JYpjLJmBkJgz5xoxCpcnX9s3JJ5NevI8v1GuW9E4kOQMr57-Hg5V2HIHxQ=w300-rw", "Google Play Games"));

        HorizontalListSection section = new HorizontalListSection();
        section.setItems(items);

        return section;
    }
}
