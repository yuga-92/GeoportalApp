package com.example.yuga.geoportalapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EditActivity extends AppCompatActivity {
    String markerSnippetReceived1;
    List<EditText> paramsValues = new ArrayList<EditText>();
    List<EditText> paramsNames = new ArrayList<EditText>();
    int MarkerID;
    String markerReceivedGoogleID;
    int i=0;
    String nameReceived;
    EditText markerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        markerSnippetReceived1 = getIntent().getStringExtra("snippet");
        MarkerID = getIntent().getIntExtra("id", 0);
        markerReceivedGoogleID = getIntent().getStringExtra("MarkerGoogleID");
        nameReceived = getIntent().getStringExtra("markername");
        markerEdit();
    }

    public void close1_click(View v){
        Intent intent2 = new Intent();
        intent2.putExtra("name", "From Edit");
        setResult(1, intent2);
        finish();
    }

    public void markerEdit (){
        JSONObject jsObj = null;
        try {
            jsObj = new JSONObject(markerSnippetReceived1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LinearLayout layout = (LinearLayout) findViewById(R.id.linlayout2);
        TextView tvMarkerName =new TextView(this);
        markerName = new EditText(this);
        tvMarkerName.setText(R.string.markerNameMSG);
        TextView tvHint =new TextView(this);
        TextView tvHint2 =new TextView(this);
        TextView tvHint3 =new TextView(this);
        tvHint.setText(R.string.edtActFirstFieldHint);
        tvHint2.setText(R.string.edtActSecondFieldHint);
        tvHint3.setText(R.string.edtActCleanDescriptionHint);
        layout.addView(tvMarkerName);
        layout.addView(markerName);
        layout.addView(tvHint);
        layout.addView(tvHint2);
        layout.addView(tvHint3);
        //Виводжу текст в'ю, присвоюю їм значення з об'єкту
        int num;
        String numberFieldLabel;
        for (Iterator<String> iter = jsObj.keys(); iter.hasNext(); ) {
            String key = iter.next();
            num = i+1;
            TextView paramsNumber = new TextView(this);
            numberFieldLabel = num + getString(R.string.edtActParamHint);
            paramsNumber.setText(numberFieldLabel);
            EditText paramName = new EditText(this);
            EditText paramValue = new EditText(this);
            paramName.setText(key);
            paramsValues.add(paramValue);
            paramsNames.add(paramName);
            i++;
            try {
                paramValue.setText(jsObj.getString(key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            layout.addView(paramsNumber);
            layout.addView(paramName);
            layout.addView(paramValue);
        }
    }

    public void save_click(View v) throws JSONException {
        String inputFile = null;
        String outputFile = "";
        try {
            inputFile = MarkerFile.fileRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
        outputFile = replaceMarkerData(inputFile, outputFile);
        MarkerFile.fileWrite(outputFile);
    }

    private String replaceMarkerData(String inputFile, String outputFile) throws JSONException {
        JSONArray jsARR = new JSONArray(inputFile);
        for (int i = 0; i < jsARR.length(); i++) {
            JSONObject jsonObj = jsARR.getJSONObject(i);
            if (jsonObj.getString("name").equalsIgnoreCase(nameReceived)){
                JSONObject jsObj2 = new JSONObject();
                jsonObj.remove("content");
                for (int k = 0; k< paramsValues.size(); k++) {
                    if (!(paramsValues.get(k).getText().toString().isEmpty()) || !(paramsValues.get(k).getText().toString().isEmpty())) {
                        jsObj2.put(paramsNames.get(k).getText().toString(), paramsValues.get(k).getText().toString());
                    }
                }
                jsonObj.put("content", jsObj2);
                if (!(String.valueOf(markerName).equalsIgnoreCase(""))) {
                    jsonObj.remove("name");
                    jsonObj.put("name", markerName.getText().toString());
                }
            }
            outputFile += jsARR.getJSONObject(i).toString()+",";
        }
        outputFile = "["+outputFile.substring(0,outputFile.length()-1)+"]";
        return outputFile;
    }

    public  void addNewOption(View v){
        LinearLayout layout = (LinearLayout) findViewById(R.id.linlayout2);
        EditText etNewParamName = new EditText(this);
        EditText etNewParamValue = new EditText(this);
        TextView tvNewParamNumber = new TextView(this);
        int num = i+1;
        String numberFieldLabel = num + getString(R.string.edtActParamHint);
        tvNewParamNumber.setText(numberFieldLabel);
        paramsValues.add(etNewParamValue);
        paramsNames.add(etNewParamName);
        layout.addView(tvNewParamNumber);
        layout.addView(etNewParamName);
        layout.addView(etNewParamValue);
        i++;
    }
}



