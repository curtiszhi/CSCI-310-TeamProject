package com.csci310.ParkHere;

import android.graphics.Bitmap;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

public class publicActivity extends AppCompatActivity {
    private String name;
    private Float rating;
    private Bitmap profiel_pic;
    private String[] review;
    private TextView name_text;
    private RatingBar ratingBar;
    private ImageView pic_image;
    private LinearLayout review_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public);

        name_text=(TextView) findViewById(R.id.public_name);
        ratingBar=(RatingBar) findViewById(R.id.ratingBar);
        pic_image=(ImageView) findViewById(R.id.Public_image);
        review_layout=(LinearLayout) findViewById(R.id.review);

        name_text.setText("name: "+name);
        ratingBar.setRating(rating);
        pic_image.setImageBitmap(profiel_pic);
        for(int i=0;i<review.length;i++){
            TextView review_text = new TextView(this);
            review_text.setText(review[i]);
            review_text.setLayoutParams(new AppBarLayout.LayoutParams(AppBarLayout.LayoutParams.MATCH_PARENT, AppBarLayout.LayoutParams.WRAP_CONTENT));
            ((LinearLayout) review_layout).addView(review_text);
        }




    }
}
