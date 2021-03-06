package com.example.yuga.geoportalapp;

import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonParser {
    static JSONArray jsArr;
    static JSONObject snippetOBJ= new JSONObject();

    public static JSONArray parseToOutput(JSONObject snippetOBJ, MarkerOptions marker) {


        try {
            if (MarkerFile.getMarkersData() !="") jsArr = new JSONArray(MarkerFile.getMarkersData());
            else jsArr = new JSONArray();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject jsObj1 = new JSONObject();
        try {
            jsObj1.put("name",marker.getTitle().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            jsObj1.put("content", snippetOBJ);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray latlngArr = new JSONArray();
        try {
            latlngArr.put(marker.getPosition().latitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            latlngArr.put(marker.getPosition().longitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            jsObj1.put("latlng",latlngArr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
       jsArr.put(jsObj1);

        return jsArr;
    }




}
