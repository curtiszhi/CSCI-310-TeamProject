package com.csci310.ParkHere;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by curtiszhi on 10/15/16.
 */

public class SearchOperation
{
    private ProgressDialog dialog;
    private static final String API_KEY = "AIzaSyBxTXSEer2OE4jdVeG6AUa2UWF8QzZJhlo";

    public static void parseJSON(String[] result) {
        try {
            JSONObject jsonObject = new JSONObject(result[0]);

            double lng = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lng");

            double lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lat");

            Log.d("latitude", "" + lat);
            Log.d("longitude", "" + lng);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


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

    public static void search(String sTime, String eTime, String sDate, String eDate, boolean isCompact, boolean isCovered, boolean isHandicapped, String address) {

    }
}
