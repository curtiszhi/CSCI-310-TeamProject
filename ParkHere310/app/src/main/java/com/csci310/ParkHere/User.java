package com.csci310.ParkHere;

import java.util.List;

/**
 * Created by seanyuan on 9/30/16.
 */

public class User {
    String userName;
    String email;
    String phone;
    private String[] review;

    public String[] getReview() {
        return review;
    }

    public void setReview(String[] review) {
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

    Boolean isHost;
    List<String> renting;
    List<String> hosting;

    public User() {

    }

    public User(String userName, String email, String phone, Boolean isHost, List<String> renting, List<String> hosting) {
        this.userName = userName;
        this.email = email;
        this.phone = phone;
        this.isHost = isHost;
        this.renting = renting;
        this.hosting = hosting;
    }

}
