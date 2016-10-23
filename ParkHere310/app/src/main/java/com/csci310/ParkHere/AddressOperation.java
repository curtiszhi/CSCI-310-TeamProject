package com.csci310.ParkHere;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

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

public class AddressOperation extends AsyncTask<String, Void, String>
{
    private static final String API_KEY = "AIzaSyBxTXSEer2OE4jdVeG6AUa2UWF8QzZJhlo";
    private ProgressDialog progressDialog;
    private Activity activity;

    //Return the address info in a JSON string:
    public static String getJSONfromAddress(String address)
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

    //Return the place_id from the JSON string:
    public static String getIDfromJSON(String jsonString)
    {
        String place_id = "";
        try
        {
            JSONObject jsonObject = new JSONObject(jsonString);
            place_id = ((JSONArray) jsonObject.get("results")).getJSONObject(0).getString("place_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return place_id;
    }

    //Return the [latitude, longitude] coordinates from the JSON string:
    public static double[] getCoordinatesFromJSON(String jsonString)
    {
        double lat = 0, lng = 0;
        try
        {
            JSONObject jsonObject = new JSONObject(jsonString);

            lng = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lng");

            lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lat");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new double[]{lat, lng};
    }

    //Return the formatted address from the JSON string:
    public static String getFormattedAddressFromJSON(String jsonString)
    {
        String formattedAddress = "";
        try
        {
            JSONObject jsonObject = new JSONObject(jsonString);
            formattedAddress = ((JSONArray) jsonObject.get("results")).getJSONObject(0).getString("formatted_address");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return formattedAddress;
    }

    public static void search(String sTime, String eTime, String sDate, String eDate, boolean isCompact, boolean isCovered, boolean isHandicapped, String address)
    {

    }

    public AddressOperation(Activity activity)
    {
        this.activity = activity;
        this.progressDialog = new ProgressDialog(activity);
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
        progressDialog.setMessage("Searching...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... params)
    {
        return getJSONfromAddress(params[0]);
    }

    @Override
    protected void onPostExecute(String result)
    {
        if (activity instanceof AddActivity)
        {
            ((AddActivity) activity).setFeedItem(result);
        }
    }

}
