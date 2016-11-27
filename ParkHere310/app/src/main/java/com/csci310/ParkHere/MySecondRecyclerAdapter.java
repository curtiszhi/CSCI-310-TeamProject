
package com.csci310.ParkHere;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import static com.csci310.ParkHere.ActionActivity.user_all;


public class MySecondRecyclerAdapter extends RecyclerView.Adapter<FeedListRowHolder> {


    public static ArrayList<FeedItem> feedItemList;
    private ArrayList<Integer> booking_code=new ArrayList<Integer>();
    private int max_booking;
    private int min_booking;
    //public static FeedListRowHolder feedListRowHolder;
    private String start;
    private String end;

    private Context mContext;
    Boolean pay = false;
    Boolean past = false;
    boolean wish=false;

    public MySecondRecyclerAdapter(Context context, String hi) {
        if(hi.equals("rent")){
            this.feedItemList = ListingActivity.rentList;
        }
        if(hi.equals("host")){
            System.out.println("host is true");
            this.feedItemList = ListingActivity.hostList;
        }
        if(hi.equals("wish")){
            this.feedItemList = ListingActivity.wishList;
            wish=true;
        }
        if(hi.equals("past")){
            System.out.println("past is true");
            this.feedItemList = AddPastActivity.hostList;
            past = true;
        }
        if(hi.equals("results")){
            pay = true;
            this.feedItemList = ListingResultActivity.resultList;
            for(int i=0;i<feedItemList.size();i++){
                booking_code.add(feedItemList.get(i).getBookings());
            }
            max_booking= Collections.max(booking_code);
            min_booking=Collections.min(booking_code);

        }
        System.out.println("got list" + this.feedItemList.size());
        this.mContext = context;
        this.start =start;
        this.end=end;
    }

    public void setTime(String start, String end){
        this.start=start;
        this.end=end;
        System.out.println(this.start+"recycle");
        System.out.println(this.end+"recycle");
    }

    @Override
    public FeedListRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.past_list_item, null);
        FeedListRowHolder mh = new FeedListRowHolder(v);

        return mh;
    }

    @Override
    public void onBindViewHolder(FeedListRowHolder feedListRowHolder, int i) {
        FeedItem feedItem = feedItemList.get(i);
        String color_code=find_code(feedItem.getBookings());

        TextView address=(TextView) feedListRowHolder.house;

        feedListRowHolder.house.setText(feedItem.getAddress());
        feedListRowHolder.house.setTextColor(Color.parseColor(color_code));
        if (!feedItem.getPhotos().isEmpty()) {
            if (feedItem.getPhotos().get(0) != null) {
                byte[] decodedString = Base64.decode(feedItem.getPhotos().get(0), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                feedListRowHolder.thumbnail.setImageBitmap(decodedByte);
            }
        }


        feedListRowHolder.rating.setRating(feedItem.calculateRate());
        if(feedItem.getActivity()){
            feedListRowHolder.activity.setText(String.valueOf("Availabe"));
            feedListRowHolder.activity.setTextColor(Color.parseColor(color_code));
        }else{
            feedListRowHolder.activity.setText(String.valueOf("Archive"));
            feedListRowHolder.activity.setTextColor(Color.parseColor(color_code));
        }
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
                intent.putExtra("start", start);
                intent.putExtra("end", end);
                current_view.getContext().startActivity(intent);
            }else if(past){
                System.out.println("else if past");
                Intent intent = new Intent(current_view.getContext(), AddActivity.class);
                intent.putExtra("ItemPosition", position);
                intent.putExtra("isPast", "true");
                current_view.getContext().startActivity(intent);
            }else if(wish){
                Intent intent = new Intent(current_view.getContext(), RentActivity.class);
                intent.putExtra("ItemPosition", position);
                intent.putExtra("wish", 1);
                current_view.getContext().startActivity(intent);
            }
            else {
                Intent intent = new Intent(current_view.getContext(), DetailedViewActivity.class);
                intent.putExtra("ItemPosition", position);
                current_view.getContext().startActivity(intent);
            }

        }
    }


    private String find_code(int booking){
        String color="";
        int code=0;
        int range=max_booking-min_booking;
        if(range==0){
            color="#000000";
            return color;
        }
        else{
            double split_range=range/5.0;
            int double_min=booking-min_booking;
            double temp=double_min/split_range;
            code=(int)Math.round(temp);
            if(code==0){
                color="#A0A0A0";
            }
            if(code==1){
                color="#808080";
            }
            if(code==2){
                color="#606060";
            }
            if(code==3){
                color="#404040";
            }
            if(code==4){
                color="#202020";
            }
            if(code==5){
                color="#000000";
            }

            return color;
        }
    }
}
