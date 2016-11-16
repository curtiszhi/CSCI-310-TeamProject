package com.csci310.ParkHere;


import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

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
    private ArrayList<String> rating;
    private Boolean activity;
    private List<String> filters;
    private String Host;
    public Vector<String> photos;
    public Map<String,ArrayList<String>> rentedTime;
    private String identifier;
    private ArrayList<String> review;
    private String currentRenter;

    public FeedItem(){
        photos=new Vector<String>();
        rentedTime= new HashMap<String,ArrayList<String>>();
        rating=new ArrayList<String>();
        review = new ArrayList<String>();
        filters=null;
    }





    public Vector<String> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<String> photos) {
        Vector v = new Vector(photos);

        this.photos = v;
    }


    public Map<String,ArrayList<String>> getRentedTime() {
        return rentedTime;
    }

    /*public Map<String,ArrayList<String>> getRentedTimeArray() {
        Map<String,Vector<String >> map = rentedTime;
        Map<String,ArrayList<String>> temp = new HashMap<String, ArrayList<String>>();
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            String key = (String) pair.getKey();
            ArrayList<String> thing =  new ArrayList<String>();
            for(int i=0;i<map.get(key).size();i++){
                thing.add(map.get(key).get(i));
            }

            it.remove();
            temp.put(key, thing);
        }
        return temp;
    }*/

    public void setRentedTime(Map<String,ArrayList<String>>rentedTime) {
        this.rentedTime = rentedTime;
    }


    public ArrayList<String> getReview() {
        return review;
    }

    public void setReview(ArrayList<String> review) {
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


    public void setRating(ArrayList<String> rating) {
        this.rating = rating;
    }

    public ArrayList<String> getRating(){
        return rating;
    }


    public Float calculateRate(){
        if(rating.size()==0){
            return (float)0;}
        else{
            double total=0;
            for(int i=0;i<rating.size();i++){

                total+=Double.parseDouble( (rating.get(i)).trim()+"");
            }
            Float rate=(float)total/(float)rating.size();
            return rate;
        }
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


      /* public FeedItem(String spotID, String address, double latitude, double longitude, String startdates, String enddates, String starttime, String endtime, double price, String cancelpolicy, String description, Vector<Integer> rating, Boolean activity, List<String> filters, String host, Vector<String> photos, Map<String, Vector<String>> rentedTime, String identifier, Vector<String> review, String currentRenter) {
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
    }*/
}