package com.csci310.ParkHere;


        import android.content.Intent;
        import android.media.Rating;
        import android.support.v7.widget.RecyclerView;
        import android.view.View;
        import android.widget.ImageView;
        import android.widget.RatingBar;
        import android.widget.TextView;
        import android.widget.Toast;

public class FeedListRowHolder extends RecyclerView.ViewHolder {
    protected ImageView thumbnail;
    protected TextView house;
    protected TextView dates;
    protected TextView price;
    protected RatingBar rating;
    protected TextView activity;
    protected View mRootView;
    protected TextView booking;

    public FeedListRowHolder(View view) {
        super(view);
        mRootView = view;
        this.thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        this.house = (TextView) view.findViewById(R.id.house);
        this.dates = (TextView) view.findViewById(R.id.dates);
        this.price = (TextView) view.findViewById(R.id.price);
        this.rating = (RatingBar) view.findViewById(R.id.ratingBar);
        this.activity = (TextView) view.findViewById(R.id.activity);
        this.booking=(TextView) view.findViewById(R.id.bookings);
    }

}
