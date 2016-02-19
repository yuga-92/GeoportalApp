package com.example.yuga.geoportalapp;

import android.app.Dialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnInfoWindowClickListener {
//TODO extract Google Play Services in other class file...
    private static final int ERROR_DIALOG_REQUEST = 1;
    //map object
    GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    //Location object used for getting latitude and longitude
    Location mLastLocation;
    String markerID;
    String outputDataToFile;
    private GoogleMap googleMap;

    Map <Marker, Long> markers = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            MarkerFile.fileRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
        buildGoogleApiClient();
        googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                .getMap();
       if ( googleMap != null ) googleMap.setInfoWindowAdapter( new MyInfoWindowAdapter(this));

    }

     protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void gotoLocation(double lat,double lng,float zoom) {
        LatLng latLng=new LatLng(lat,lng);
        CameraUpdate update= CameraUpdateFactory.newLatLngZoom(latLng,zoom);
        mMap.moveCamera(update);
    }

    private boolean checkServices() {

        int isAvailable= GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (isAvailable== ConnectionResult.SUCCESS){
            return true;
        }else if (GooglePlayServicesUtil.isUserRecoverableError(isAvailable)){
            Dialog dialog=GooglePlayServicesUtil.getErrorDialog(isAvailable,this,ERROR_DIALOG_REQUEST);
            dialog.show();
        }else {
            Toast.makeText(MainActivity.this, "Cannot connnect to mapping Service", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    //Checking the google play services is available

    private boolean initMap() {
        if (mMap == null) {
            SupportMapFragment mapFragment= (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mMap=mapFragment.getMap();

        }
        return (mMap!=null);
    }

    //Initializing map

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            //getting the latitude value
            double latitudeValue=mLastLocation.getLatitude();
            //getting the longitude value
            double longitudeValue=mLastLocation.getLongitude();
            if(checkServices()){
                if(initMap()){
                    //update the map with the current location
                    gotoLocation(latitudeValue, longitudeValue, 15);
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    mMap.setOnMarkerClickListener(this);
                    mMap.setOnMapClickListener(this);
                    mMap.setOnMapLongClickListener(this);
                    mMap.setOnInfoWindowClickListener(this);
                    try {
                        viewMarkers();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("GettingLocation", "onConnectionFailed");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("GettingLocation", "onConnectionFailed");
    }

    public  void viewMarkers() throws JSONException {
        JSONArray jsonArray;
        jsonArray = new JSONArray(MarkerFile.getMarkersData());
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObj = jsonArray.getJSONObject(i);
            mMap.addMarker(new MarkerOptions()
                            .title(jsonObj.getString("name"))
                            .snippet(jsonObj.getJSONObject("content").toString())
                            .position(new LatLng(
                                    jsonObj.getJSONArray("latlng").getDouble(0),
                                    jsonObj.getJSONArray("latlng").getDouble(1)
                            ))
            );
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        try {
            JsonParser.snippetOBJ.put("clean", "clean");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MarkerOptions marker;
        marker = new MarkerOptions()
                .title("New marker at "+latLng.toString())
                .position(latLng)
                .snippet(String.valueOf(JsonParser.snippetOBJ))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mMap.addMarker(marker);

            outputDataToFile = JsonParser.parseToOutput(JsonParser.snippetOBJ, marker).toString();
            MarkerFile.fileWrite(outputDataToFile);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {return;}
        if (resultCode == 1) {
            String name = data.getStringExtra("message"); //not used yet
        }

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent1 = new Intent(this, MarkerInfo.class);
        String markerSnippet = marker.getSnippet();
        intent1.putExtra("id", markerID);
        intent1.putExtra("markername",marker.getTitle().toString());
        intent1.putExtra("MarkerGoogleID",marker.getId());
        intent1.putExtra("snippet", markerSnippet); //тут буде стрінг, який містить джейсон дані з ключами дескріпшин
        startActivityForResult(intent1, 1);
    }
}