package com.csci310.ParkHere;


import java.util.List;

/**
 * Created by seanyuan on 10/7/16.
 */

public class FeedItem {
    private String spotID;
    private String address;
    private double latitude;
    private double longitude;
    private String startdates;
    private String enddates;
    private String starttime;
    private String endtime;
    private double price;
    private String cancelpolicy;
    private String description;
    private String rating;
    private Boolean activity;
    private List<String> filters;
    private String Host;

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

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
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