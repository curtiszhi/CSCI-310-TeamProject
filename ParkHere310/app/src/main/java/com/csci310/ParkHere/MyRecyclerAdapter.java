package com.csci310.ParkHere;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class MyRecyclerAdapter extends RecyclerView.Adapter<FeedListRowHolder> {


    public static List<FeedItem> feedItemList;

    private Context mContext;

    public MyRecyclerAdapter(Context context, List<FeedItem> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;
    }

    @Override
    public FeedListRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, null);
        FeedListRowHolder mh = new FeedListRowHolder(v);

        return mh;
    }

    @Override
    public void onBindViewHolder(FeedListRowHolder feedListRowHolder, int i) {
        FeedItem feedItem = feedItemList.get(i);
        feedListRowHolder.house.setText(feedItem.getAddress());
       // feedListRowHolder.thumbnail.setImageResource(feedItem.getThumbnail());
        feedListRowHolder.dates.setText(feedItem.getEndDates() + feedItem.getEndTime());
        String stringdouble= Double.toString(feedItem.getPrice());
        feedListRowHolder.price.setText(stringdouble);
        feedListRowHolder.rating.setRating(feedItem.getRating());
        feedListRowHolder.activity.setText(String.valueOf(feedItem.getActivity()));
        feedListRowHolder.mRootView.setOnClickListener(new ItemOnClickListener(feedListRowHolder.mRootView, i));
    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }

    private class ItemOnClickListener implements View.OnClickListener{
        private View current_view;
        private String position;
        public ItemOnClickListener(View v, int i) {
            current_view = v;
            position = Integer.toString(i);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(current_view.getContext(), DetailedViewActivity.class);
            intent.putExtra("ItemPosition", position);
            current_view.getContext().startActivity(intent);
        }
    }
}
