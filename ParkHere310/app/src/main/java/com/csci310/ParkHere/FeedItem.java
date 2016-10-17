package com.csci310.ParkHere;


import java.util.List;

/**
 * Created by seanyuan on 10/7/16.
 */

public class FeedItem {
    protected String house;
    protected String startdates;
    protected String enddates;
    protected String starttime;
    protected String endtime;
    protected int price;
    protected int cancelpolicy;
    protected String spotID;
    protected String description;
    protected String rating;
    protected String activity;
    protected List<Integer> filters;


    public int getCancel() {
        return cancelpolicy;
    }

    public void setCancel(int cancelpolicy) {
        this.cancelpolicy = cancelpolicy;
    }

    public String getSpotID() {
        return spotID;
    }

    public void setSpotID(String spotID) {
        this.spotID = spotID;
    }


    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public List<Integer> getFilter(){
        return filters;
    }

    public void setFilter(List<Integer> filters) {
        this.filters = filters;
    }
}