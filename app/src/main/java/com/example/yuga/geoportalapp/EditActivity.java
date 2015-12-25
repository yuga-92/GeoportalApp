package com.example.yuga.geoportalapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EditActivity extends AppCompatActivity {
    String markerSnippetReceived1;
    List<EditText> edits = new ArrayList<EditText>();
    List<EditText> edits_name = new ArrayList<EditText>();
    List<EditText> mark_n = new ArrayList<EditText>();
    int MarkerID;
    String markerReceivedGoogleID;
    int i=0;
    String namereceived;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        markerSnippetReceived1 = getIntent().getStringExtra("snippet");
        MarkerID = getIntent().getIntExtra("id", 0);
        markerReceivedGoogleID = getIntent().getStringExtra("MarkerGoogleID");
        namereceived = getIntent().getStringExtra("markername");
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
        TextView mark_name_label =new TextView(this);
        EditText marker_name = new EditText(this);
        mark_name_label.setText("Enter marker name (left empty to not change):");
        TextView tip =new TextView(this);
        TextView tip1 =new TextView(this);
        TextView tip2 =new TextView(this);
        tip.setText("The first field is the name of the parameter.");
        tip1.setText("The second field is the value of this parameter.");
        tip2.setText("To remove the description in the file - just delete the text from the field and save the file.");
        layout.addView(mark_name_label);
        layout.addView(marker_name);
        layout.addView(tip);
        layout.addView(tip1);
        layout.addView(tip2);
        //Виводжу текст в'ю, присвоюю їм значення з об'єкту
        int num;
        for (Iterator<String> iter = jsObj.keys(); iter.hasNext(); ) {
            String key = iter.next();
            num = i+1;
            TextView number_field = new TextView(this);
            number_field.setText(num+". Parameter:");
            EditText txt_view11 = new EditText(this);
            EditText txt_view1 = new EditText(this);
            txt_view11.setText(key);
            edits.add(txt_view1);
            edits_name.add(txt_view11);
            mark_n.add(marker_name);
            i++;
            try {
                txt_view1.setText(jsObj.getString(key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            layout.addView(number_field);
            layout.addView(txt_view11);
            layout.addView(txt_view1);
        }
    }

    public void save_click(View v) throws JSONException {
        File myFile = new File(Environment.getExternalStorageDirectory().getPath()+"/mysdfile.txt");
        String inputFile = null;
        String outputFile = "";
        //Replace JSON object
        try {
            FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(
                    new InputStreamReader(fIn));
            String aDataRow;
            String aBuffer = "";
            while ((aDataRow = myReader.readLine()) != null) {
                aBuffer += aDataRow + "\n";
            }
            inputFile = aBuffer;
            myReader.close();
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
        JSONArray jsARR = new JSONArray(inputFile);
        for (int i = 0; i < jsARR.length(); i++) {
            JSONObject jsonObj = jsARR.getJSONObject(i);

            if (jsonObj.getString("name").equalsIgnoreCase(namereceived)){
                JSONObject jsObj2 = new JSONObject();
                jsonObj.remove("content");
                for (int k = 0; k<edits.size(); k++) {
                    if (!(edits.get(k).getText().toString().isEmpty()) || !(edits.get(k).getText().toString().isEmpty())) {
                        jsObj2.put(edits_name.get(k).getText().toString(),edits.get(k).getText().toString());
                    }
                }
                jsonObj.put("content", jsObj2);
                if (!(String.valueOf(mark_n.get(0).getText()).equalsIgnoreCase(""))) {
                    jsonObj.remove("name");
                    jsonObj.put("name", mark_n.get(0).getText().toString());
                }
            }
            outputFile += jsARR.getJSONObject(i).toString()+",";
        }
        outputFile = "["+outputFile.substring(0,outputFile.length()-1)+"]";


        try {
            myFile.delete();
            myFile.createNewFile();
            FileWriter fw = new FileWriter(myFile.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(outputFile);
            bw.close();
            Toast.makeText(getApplicationContext(), "File saved successfully",
                    Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  void addNewOption(View v){
        LinearLayout layout = (LinearLayout) findViewById(R.id.linlayout2);
        EditText txt_view22 = new EditText(this);
        EditText txt_view2 = new EditText(this);
        TextView number_field1 = new TextView(this);
        int num = i+1;
        number_field1.setText(num+". Parameter:");
        edits.add(txt_view2);
        edits_name.add(txt_view22);
        layout.addView(number_field1);
        layout.addView(txt_view22);
        layout.addView(txt_view2);
        i++;
    }
}



