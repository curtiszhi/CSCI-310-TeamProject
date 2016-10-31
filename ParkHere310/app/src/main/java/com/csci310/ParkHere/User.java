package com.csci310.ParkHere;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Created by seanyuan on 9/30/16.
 */

public class User {
    private String userName;
    private String email;
    private String phone;
    private Vector<String> review;
    private Vector<String> rateList;
    private Vector<Integer> rating;
    private Boolean isHost;
    private List<String> renting;
    private List<String> hosting;
    private String photo;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }




    public void setRating(Vector<Integer> rating) {
        this.rating = rating;
    }

    public Vector<Integer> getRating(){
        return rating;
    }


    public Vector<String> getRateList() {
        return rateList;
    }

    public void setRateList(Vector<String> rateList) {
        this.rateList = rateList;
    }


    public Vector<String> getReview() {
        return review;
    }

    public void setReview(Vector<String> review) {
        this.review = review;
    }



    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getHost() {
        return isHost;
    }

    public void setHost(Boolean host) {
        isHost = host;
    }

    public List<String> getRenting() {
        return renting;
    }

    public void setRenting(List<String> renting) {
        this.renting = renting;
    }

    public List<String> getHosting() {
        return hosting;
    }

    public void setHosting(List<String> hosting) {
        this.hosting = hosting;
    }



    public User() {
        rateList=new Vector<String>();
        rating=new Vector<Integer>();
        renting=new ArrayList<String>();
        hosting=new ArrayList<String>();
        review=new Vector<String>();
    }



}
