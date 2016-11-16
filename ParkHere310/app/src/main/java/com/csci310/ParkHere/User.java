package com.csci310.ParkHere;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by seanyuan on 9/30/16.
 */

public class User {
    private String userName;
    private String email;
    private String phone;
    private Boolean isHost;
    private ArrayList<String> review;
    private ArrayList<Double> rating;
    private List<String> renting;
    private List<String> hosting;
    private String photo=null;
    public User() {
        rating=new ArrayList<Double>();
        renting=new ArrayList<String>();
        hosting=new ArrayList<String>();
        review=new ArrayList<String>();
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }




    public void setRating(ArrayList<Double> rating) {
        this.rating = rating;
    }

    public ArrayList<Double> getRating(){
        return rating;
    }

    public Float calculate_Rating(){
        Float rate;
        if(rating.size()==0){
            rate = (float)0;
            return rate;
        }
        else{
            float total=0;
            for(int i=0;i<rating.size();i++){
                total+=rating.get(i);
            }
            rate=total/(float)rating.size();
        }
        return rate;
    }



    public ArrayList<String> getReview() {
        return review;
    }

    public void setReview(ArrayList<String> review) {
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







}
