package com.csci310.ParkHere;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by curtiszhi on 10/15/16.
 */

public class AddressOperation
{
    private static final String API_KEY = "AIzaSyBxTXSEer2OE4jdVeG6AUa2UWF8QzZJhlo";

    //Return the comprehensive information of the address in JSON:
    public static String parseAddress(String address)
    {
        address = address.trim().replaceAll("\\s+", "+");
        String result = "";
        try
        {
            URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?address=" + address + "&key=" + API_KEY);
            URLConnection urlConnection = url.openConnection();
            String line = null;
            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            while ((line = br.readLine()) != null)
                result += line;
        }
        catch (IOException e){e.printStackTrace();}
        return result;
    }

    //Return the place_id for the JSON address info
    public static String parseJSON(String result)
    {
        String place_id = "";
        try
        {
            JSONObject jsonObject = new JSONObject(result);

            double lng = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lng");

            double lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lat");

            place_id = ((JSONArray) jsonObject.get("results")).getJSONObject(0).getString("place_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return place_id;
    }

    public static void search(String sTime, String eTime, String sDate, String eDate, boolean isCompact, boolean isCovered, boolean isHandicapped, String address)
    {

    }
}
