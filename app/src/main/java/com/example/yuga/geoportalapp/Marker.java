package com.example.yuga.geoportalapp;

import android.os.Parcel;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by yuga9 on 13.03.2016.
 */
public class Marker implements android.os.Parcelable {
    String markerTitle;
    String markerSnippet;
    LatLng markerLatLng;
    double latitude;
    double longitude;

    protected Marker(Parcel in) {
        this.markerTitle = in.readString();
        this.markerSnippet = in.readString();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();

    }

    public static final Creator<Marker> CREATOR = new Creator<Marker>() {
        @Override
        public Marker createFromParcel(Parcel in) {
            return new Marker(in);
        }

        @Override
        public Marker[] newArray(int size) {
            return new Marker[size];
        }
    };

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(markerTitle);
        dest.writeString(markerSnippet);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }

    public Marker(String markerTitle, String markerSnippet, LatLng markerLatLng) {
        this.markerTitle = markerTitle;
        this.markerSnippet = markerSnippet;
        this.markerLatLng = markerLatLng;
        longitude= markerLatLng.longitude;
        latitude = markerLatLng.latitude;
    }
}
