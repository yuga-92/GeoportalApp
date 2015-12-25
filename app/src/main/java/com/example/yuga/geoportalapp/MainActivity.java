package com.example.yuga.geoportalapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnInfoWindowClickListener {


    private static final int ERROR_DIALOG_REQUEST = 1;
    //map object
    GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    //Location object used for getting latitude and longitude
    Location mLastLocation;
    String markersData;
    String markerID;
    int i = 0;
    final Context context = this;
    private String mark_name = null;
    private GoogleMap googleMap;


    ///// Setting infowindow adapter
    private class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private View view;

        public CustomInfoWindowAdapter() {
            view = getLayoutInflater().inflate(R.layout.custom_infowindow,
                    null);
        }

        @Override
        public View getInfoContents(Marker marker) {
            final String title = marker.getTitle();
            final TextView titleUi = ((TextView) view.findViewById(R.id.title));
            if (title != null) {
                titleUi.setText(title);
            } else {
                titleUi.setText("");
            }


            final TextView snippetUi = ((TextView) view
                    .findViewById(R.id.snippet));

            snippetUi.setText("Touch this to view additional info");


            return view;
        }

        @Override
        public View getInfoWindow(final Marker marker) {
        //Not used now. For custom view of infowindow

            final String title = marker.getTitle();
            final TextView titleUi = ((TextView) view.findViewById(R.id.title));
            if (title != null) {
                titleUi.setText(title);
            } else {
                titleUi.setText("");
            }


            final TextView snippetUi = ((TextView) view
                    .findViewById(R.id.snippet));

            snippetUi.setText("Touch this to view additional info");


            return null;
        }
    }

    //////end



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buildGoogleApiClient();
        googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                .getMap();
        if ( googleMap != null ) {

            googleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());
        }

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

    //Checking the google play services is available

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


    //Initializing map

    private boolean initMap() {
        if (mMap == null) {
            SupportMapFragment mapFragment= (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mMap=mapFragment.getMap();

        }
        return (mMap!=null);

    }


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
                    fileRead();
                    try {
                        vievMarkers();
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
    //Open file for reading
    //TODO: change values
    public void fileRead (){

        try {
            File myFile = new File(Environment.getExternalStorageDirectory().getPath()+"/mysdfile.txt");

            FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(
                    new InputStreamReader(fIn));
            String aDataRow = "";
            String aBuffer = "";
            while ((aDataRow = myReader.readLine()) != null) {
                aBuffer += aDataRow + "\n";
            }
            // txtData.setText(aBuffer);
            markersData = aBuffer;
            myReader.close();
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    public  void vievMarkers () throws JSONException {
        JSONArray jsonArray;
        jsonArray = new JSONArray(markersData);
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

        //  Toast.makeText(getApplicationContext(), "this is my Toast message!!! =)",
        //         Toast.LENGTH_LONG).show();
        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        //  Toast.makeText(getApplicationContext(), "this is a click on the map! =)",
        //         Toast.LENGTH_LONG).show();


    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        // getMarkerName(latLng, snippetOBJ);
        String marker_name = mark_name;
        String outputFile = "";
        JSONObject snippetOBJ= new JSONObject();
        try {
            snippetOBJ.put("clean","clean");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // viewMarkers(latLng, snippetOBJ,marker_name);
        MarkerOptions marker;

        marker = new MarkerOptions()
                .title("New marker at "+latLng.toString())
                .position(latLng)
                .snippet(String.valueOf(snippetOBJ))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        mMap.addMarker(marker);



        JSONArray jsARR = null;
        try {
            jsARR = new JSONArray(markersData);
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
        jsARR.put(jsObj1);

        outputFile += jsARR.toString();
        File myFile1 = new File(Environment.getExternalStorageDirectory().getPath()+"/mysdfile.txt");

        try {
            FileOutputStream fOut = new FileOutputStream(myFile1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            myFile1.delete();
            myFile1.createNewFile();
            FileWriter fw = new FileWriter(myFile1.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(outputFile);
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {return;}
        if (resultCode == 1) {
            String name = data.getStringExtra("message");

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