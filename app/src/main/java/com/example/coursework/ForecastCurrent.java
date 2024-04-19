package com.example.coursework;

import android.util.Log;
public class ForecastCurrent {

    private String title, description, temparature;


    public void SetDescription(String D) {
        description = D;

        String[] arrOfDesc = description.split(", ", 6);
        String[] arrOfTempLg = arrOfDesc[0].split(": ", 2);
        String[] arrOfTemp = arrOfTempLg[1].split(" ", 2);

        temparature = arrOfTemp[0];
    }

    public void SetTitle(String T) {
        title = T;
        //Log.i("Forecast current", "in Set title" );
    }
    public String GetTitle(){return title;}
    public String GetDescription(){return description;}
    public String GetTemp(){return temparature;}

    public String toString(){
        return title + " " + description + '\n';
    }

}
