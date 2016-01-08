package com.example.yuga.geoportalapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    //TODO extract in other class file
    private Context myContext;
    public CustomInfoWindowAdapter(Context context) {
        myContext = context;
    }

    @Override
    public View getInfoContents(Marker marker) {

        LayoutInflater inflater = (LayoutInflater) myContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        // Getting view from the layout file info_window_layout
        View view = inflater.inflate(R.layout.custom_infowindow, null);
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
       /* final String title = marker.getTitle();
        final TextView titleUi = ((TextView) view.findViewById(R.id.title));
        if (title != null) {
            titleUi.setText(title);
        } else {
            titleUi.setText("");
        }
        final TextView snippetUi = ((TextView) view
                .findViewById(R.id.snippet));

        snippetUi.setText("Touch this to view additional info");

*/
        return null;
    }
}