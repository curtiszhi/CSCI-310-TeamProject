package com.teamproject.csci310.parkhere310;

/**
 * Created by seanyuan on 9/29/16.
 */

public class User {

    public String userName;
    public String email;
    public String userPhone;
    public Boolean isOwner;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String userName, String email, String userPhone, boolean isOwner) {
        this.userName = userName;
        this.email = email;
        this.userPhone = userPhone;
        this.isOwner = isOwner;
    }

}