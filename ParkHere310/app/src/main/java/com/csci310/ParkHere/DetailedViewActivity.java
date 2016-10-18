package com.csci310.ParkHere;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by seanyuan on 10/17/16.
 */
public class DetailedViewActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        String value;
        Bundle bundle = getIntent().getExtras();
        value = bundle.getString("ItemPosition");
        int position = Integer.parseInt(value);
        FeedItem feedItem = MyRecyclerAdapter.feedItemList.get(position);

    }
}
