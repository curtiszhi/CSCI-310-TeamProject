package com.csci310.ParkHere;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import static com.csci310.ParkHere.ActionActivity.user_all;


public class MyRecyclerAdapter extends RecyclerView.Adapter<FeedListRowHolder> {


    public static ArrayList<FeedItem> feedItemList;

    private Context mContext;
    Boolean pay = false;

    public MyRecyclerAdapter(Context context, String hi) {
        if(hi.equals("rent")){
            this.feedItemList = ListingActivity.rentList;
        }
        if(hi.equals("host")){
            this.feedItemList = ListingActivity.hostList;
        }
        if(hi.equals("results")){
            pay = true;
            this.feedItemList = ListingResultActivity.resultList;
        }
        System.out.println("got list" + this.feedItemList.size());
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
        if (!feedItem.getPhotos().isEmpty()) {
            if (feedItem.getPhotos().get(0) != null) {
                byte[] decodedString = Base64.decode(feedItem.getPhotos().get(0), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                feedListRowHolder.thumbnail.setImageBitmap(decodedByte);
            }
        }
       // feedListRowHolder.thumbnail.setImageResource(feedItem.getThumbnail());
        feedListRowHolder.dates.setText("Start: " + feedItem.getStartDates()+ " End: " + feedItem.getEndDates());
        String stringdouble= Double.toString(feedItem.getPrice());
        feedListRowHolder.price.setText("$" + stringdouble);
        feedListRowHolder.rating.setRating(feedItem.calculateRate());
        feedListRowHolder.activity.setText(String.valueOf("Available: " + feedItem.getActivity()));
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
            if(pay){
                Intent intent = new Intent(current_view.getContext(), RentActivity.class);
                intent.putExtra("ItemPosition", position);
                current_view.getContext().startActivity(intent);
            }else{
                Intent intent = new Intent(current_view.getContext(), DetailedViewActivity.class);
                intent.putExtra("ItemPosition", position);
                current_view.getContext().startActivity(intent);
            }

        }
    }
}
