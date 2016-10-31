package com.csci310.ParkHere;


import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Created by seanyuan on 10/7/16.
 */

public class FeedItem {
    private String spotID;
    private String address;

    public FeedItem(String spotID, String address, double latitude, double longitude, String startdates, String enddates, String starttime, String endtime, double price, String cancelpolicy, String description, Vector<Integer> rating, Boolean activity, List<String> filters, String host, Vector<String> photos, Map<String, Vector<String>> rentedTime, String identifier, Vector<String> review, String currentRenter) {
        this.spotID = spotID;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.startdates = startdates;
        this.enddates = enddates;
        this.starttime = starttime;
        this.endtime = endtime;
        this.price = price;
        this.cancelpolicy = cancelpolicy;
        this.description = description;
        this.rating = rating;
        this.activity = activity;
        this.filters = filters;
        Host = host;
        this.photos = photos;
        this.rentedTime = rentedTime;
        this.identifier = identifier;
        this.review = review;
        this.currentRenter = currentRenter;
    }

    private double latitude;
    private double longitude;
    private String startdates;
    private String enddates;
    private String starttime;
    private String endtime;
    private double price;
    private String cancelpolicy;
    private String description;
    private Vector<Integer> rating;
    private Boolean activity;
    private List<String> filters;
    private String Host;

    public Vector<String> getPhotos() {
        return photos;
    }

    public void setPhotos(Vector<String> photos) {
        this.photos = photos;
    }

    public Vector<String> photos;
    public Map<String,Vector<String>> rentedTime;
    private String identifier;
    private Vector<String> review;
    private String currentRenter;
    public Map<String,Vector<String>> getRentedTime() {
        return rentedTime;
    }
    public void setRentedTime(Map<String,Vector<String>>rentedTime) {
        this.rentedTime = rentedTime;
    }


    FeedItem(){
        photos=new Vector<String>();
        rentedTime= new HashMap<String,Vector<String>>();
        rating=new Vector<Integer>();
        review = new Vector<String>();
    }



    public Vector<String> getReview() {
        return review;
    }

    public void setReview(Vector<String> review) {
        this.review = review;
    }




    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }


    public String getCurrentRenter() {
        return currentRenter;
    }

    public void setCurrentRenter(String currentRenter) {
        this.currentRenter = currentRenter;
    }


    public String getSpotID() { return spotID; }

    public void setSpotID(String spotID) { this.spotID = spotID; }

    public String getHost() {
        return Host;
    }

    public void setHost(String host) {
        Host = host;
    }

    public String getCancel() {
        return cancelpolicy;
    }

    public void setCancel(String cancelpolicy) {
        this.cancelpolicy = cancelpolicy;
    }

    public int getRating() {
        int sum = 0;
        for(int i = 0; i < rating.size(); i++){
            sum = sum + rating.get(i);
        }
        if(rating.size() == 0){
            return 0;
        }else{
            return sum/rating.size();
        }
    }

    public void setRating(Vector<Integer> rating) {
        this.rating = rating;
    }

    public void addRating(Integer rating){
        this.rating.add(rating);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLongitude() { return longitude; }

    public void setLongitude(double longitude) { this.longitude = longitude; }

    public double getLatitude() { return latitude; }

    public void setLatitude(double latitude) { this.latitude = latitude; }

    public String getStartDates() {
        return startdates;
    }

    public void setStartDates(String startdates) {
        this.startdates = startdates;
    }

    public String getEndDates() {
        return enddates;
    }

    public void setEndDates(String enddates) {
        this.enddates = enddates;
    }

    public String getStartTime() {
        return starttime;
    }

    public void setStartTime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndTime() {
        return endtime;
    }

    public void setEndTime(String endtime) {
        this.endtime = endtime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Boolean getActivity() {
        return activity;
    }

    public void setActivity(Boolean activity) {
        this.activity = activity;
    }

    public List<String> getFilter(){
        return filters;
    }

    public void setFilter(List<String> filters) {
        this.filters = filters;
    }
}