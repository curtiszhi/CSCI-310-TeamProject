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
    private DatabaseReference mDatabase;

    SplitParkingSpot(FeedItem fd, String userStart, String userEnd)
    {
        //Initialize properties:
        originalSpot = fd;
        if (userStart.contains("M"))
            this.userStart = userStart.substring(0, userStart.length() - 2);
        else
            this.userStart = userStart;
        if (userEnd.contains("M"))
            this.userEnd = userEnd.substring(0, userEnd.length() - 2);
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
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Split the spot and add the new spots:
        for (FeedItem spot : getUpdatedSpots())
            addSpot(spot);

        //Delete the old spot:
        deleteSpot(originalSpot);
    }

    private ArrayList<FeedItem> getUpdatedSpots()
    {
        ArrayList<FeedItem> spots = new ArrayList<>();

        spots.add(setSpotWithTimeActivity(userStart, userEnd, false, 0));
        if (timeElapsedBefore)
            spots.add(setSpotWithTimeActivity(originalStart, userStart, true, 1));
        if (timeElapsedAfter)
            spots.add(setSpotWithTimeActivity(userEnd, originalEnd, true, 2));

        return spots;
    }

    private FeedItem setSpotWithTimeActivity(String start, String end, boolean activity, int id)
    {
        FeedItem fd = (FeedItem)SplitParkingSpot.deepClone(originalSpot);
        if (fd != null)
        {
            fd.setIdentifier(originalSpot.getHost() + Long.toString(System.currentTimeMillis()) + id);
            fd.setStartDates(start.split(" ")[0]);
            fd.setStartTime(start.split(" ")[1] + "PM");
            fd.setEndDates(end.split(" ")[0]);
            fd.setEndTime(end.split(" ")[1] + "PM");
            fd.setActivity(activity);
        }

        return fd;
    }

    private void addSpot(FeedItem spot)
    {
        mDatabase.child("users").child(spot.getHost()).child("hosting").child(spot.getIdentifier()).setValue(spot.getIdentifier());
        mDatabase.child("parking-spots-hosting").child(spot.getIdentifier()).setValue(spot);
    }

    private void deleteSpot(FeedItem spot)
    {
        mDatabase.child("users").child(spot.getHost()).child("hosting").child(spot.getIdentifier()).removeValue();
        mDatabase.child("parking-spots-hosting").child(spot.getIdentifier()).removeValue();
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
