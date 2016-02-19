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
    private static String markersData;
    private static File myFile = new File(Environment.getExternalStorageDirectory().getPath()+"/mysdfile.txt");
    public static String getMarkersData() {
        return markersData;
    }

    public static String fileRead () throws IOException {
            if (!myFile.exists()){
                myFile.createNewFile();
            }
            else {
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
            }
        return markersData;
    }

    public static void fileWrite(String outputString){
        try {
            myFile.delete();
            myFile.createNewFile();
            FileWriter fw = new FileWriter(myFile.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(outputString);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
