package com.example.yuga.geoportalapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class MarkerInfo extends AppCompatActivity {

    String markerSnippetReceived;
    String markerReceivedGoogleID;
    int idReceived;
    String namereceived;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_info);
        idReceived = getIntent().getIntExtra("id", 0);
        namereceived = getIntent().getStringExtra("markername");
        markerReceivedGoogleID = getIntent().getStringExtra("MarkerGoogleID");
        markerSnippetReceived = getIntent().getStringExtra("snippet");

        viewMarkerInfo();

    }

    public void edit_click(View v) {
        //TODO: тут мені треба передати те ж ІД клікнутого маркера в наступне актівіт щоб дальше з ним працювати
        Intent intent3 = new Intent(this, EditActivity.class);
        intent3.putExtra("snippet",markerSnippetReceived);
        intent3.putExtra("id", idReceived);
        intent3.putExtra("MarkerGoogleID", markerReceivedGoogleID);
        intent3.putExtra("markername",namereceived);
        startActivity(intent3);

    }

    public void close_click(View v) {
        //тут просто закриваю дане актівіті.
        Intent intent = new Intent();
        intent.putExtra("message", "Activity closed");
        setResult(1, intent);
        finish();
    }


    // відкриваю файл на читання - зчитую в стрінг. Потім іфом в циклі шукаю в стрічці з файлу інформацію по даному ІД маркера
    //коли найду потрібне ІД то циклом витягую інфу в іншу стрічку поки не побачу нове ІД
    //І виводжу в окремі текст в'ю

    public void viewMarkerInfo (){
        //TextView txtData;
        // txtData = (TextView) findViewById(R.id.txtData);
        // txtData.setText(markerSnippetReceived);

        JSONObject jsObj = null;
        try {
            jsObj = new JSONObject(markerSnippetReceived);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Виводжу текст в'ю, присвоюю їм значення з об'єкту
        LinearLayout layout = (LinearLayout) findViewById(R.id.lin_layout);
        for (Iterator<String> iter = jsObj.keys(); iter.hasNext(); ) {
            String key = iter.next();

            if (!key.equalsIgnoreCase("null") && !key.equalsIgnoreCase("id")) { //тут

                TextView txt_view1 = new TextView(this);
                TextView txt_view = new TextView(this);
                txt_view.setPadding(0, 0, 0, 25);
                txt_view1.setTextColor(Color.parseColor("#000000"));
                txt_view1.setTextSize(20);
                txt_view.setTextColor(Color.parseColor("#333131"));
                txt_view.setTextSize(18);
                txt_view1.setText(key+":");
                try {
                    txt_view.setText(jsObj.getString(key));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                layout.addView(txt_view1);
                layout.addView(txt_view);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //TODO: зробити резултом перевірку зберегло едіт актівіті зміни в файл чи ні
        if (data == null) {
            return;
        }
        if (resultCode == 1) {
            String name = data.getStringExtra("name");
            //  Toast.makeText(getApplicationContext(), name,
            //          Toast.LENGTH_LONG).show();
        }
    }
}