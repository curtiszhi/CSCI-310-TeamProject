package com.csci310.ParkHere;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by curtiszhi on 11/15/16.
 */

public class SplitParkingSpot
{
    private FeedItem originalSpot;
    private String userStart;
    private String userEnd;
    private String originalStart;
    private String originalEnd;
    private boolean timeElapsedBefore;
    private boolean timeElapsedAfter;
    private java.text.SimpleDateFormat sdf;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;

    SplitParkingSpot(FeedItem fd, String userStart, String userEnd)
    {
        //Initialize properties:
        originalSpot = fd;
        if (userStart.contains("M"))
            this.userStart = userStart.substring(0, userStart.length() - 2);
        else
            this.userStart = userStart;
        if (userStart.contains("M"))
            this.userEnd = userStart.substring(0, userEnd.length() - 2);
        else
            this.userEnd = userEnd;
        originalStart = originalSpot.getStartDates() + " " + originalSpot.getStartTime().substring(0, originalSpot.getStartTime().length() - 2);
        originalEnd = originalSpot.getEndDates() + " " + originalSpot.getEndTime().substring(0, originalSpot.getEndTime().length() - 2);
        sdf = new java.text.SimpleDateFormat("MM-dd-yyyy HH:mm");
        try {
            timeElapsedBefore = sdf.parse(this.userStart).getTime() > sdf.parse(originalStart).getTime();
            timeElapsedAfter = sdf.parse(originalEnd).getTime() > sdf.parse(this.userEnd).getTime();
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Split the spot and update the database:
        for (FeedItem spot : getUpdatedSpots())
            addSpot(spot);
    }

    private ArrayList<FeedItem> getUpdatedSpots()
    {
        ArrayList<FeedItem> spots = new ArrayList<>();

        spots.add(setSpotWithTimeActivity(userStart, userEnd, false));
        if (timeElapsedBefore)
            spots.add(setSpotWithTimeActivity(originalStart, userStart, true));
        if (timeElapsedAfter)
            spots.add(setSpotWithTimeActivity(userEnd, originalEnd, true));

        return spots;
    }

    private FeedItem setSpotWithTimeActivity(String start, String end, boolean activity)
    {
        FeedItem fd = (FeedItem)SplitParkingSpot.deepClone(originalSpot);
        if (fd != null)
        {
            fd.setStartDates(start.split(" ")[0]);
            fd.setStartTime(start.split(" ")[1]);
            fd.setEndDates(end.split(" ")[0]);
            fd.setEndTime(end.split(" ")[1]);
            fd.setActivity(activity);
        }

        return fd;
    }

    private void addSpot(FeedItem spot)
    {
        mDatabase.child("users").child(mFirebaseUser.getUid()).child("hosting").child(spot.getIdentifier()).setValue(spot.getIdentifier());
        mDatabase.child("parking-spots-hosting").child(spot.getIdentifier()).setValue(spot);
    }

    public static Object deepClone(Object object)
    {
        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
