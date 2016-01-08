package com.example.yuga.geoportalapp;
import android.os.Environment;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class MarkerFile {
    public static String markersData;

    public static void fileRead (){
        try {
            //todo change this
            java.io.File myFile = new java.io.File(Environment.getExternalStorageDirectory().getPath()+"/mysdfile.txt");
            FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(
                    new InputStreamReader(fIn));
            String aDataRow;
            String aBuffer = "";
            while ((aDataRow = myReader.readLine()) != null) {
                aBuffer += aDataRow + "\n";
            }
            markersData = aBuffer;
            myReader.close();
        } catch (Exception e) {
           e.printStackTrace();
        }
    }

    public static void fileWrite(String outputString){
        File myFile1 = new File(Environment.getExternalStorageDirectory().getPath()+"/mysdfile.txt");
        try {
            myFile1.delete();
            myFile1.createNewFile();
            FileWriter fw = new FileWriter(myFile1.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(outputString);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
