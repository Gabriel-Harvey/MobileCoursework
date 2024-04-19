package com.example.coursework;

import android.util.Log;

import java.security.PublicKey;

public class Forecast {

    private String title;
    private String weatherType, day;
    private String description;
    private String min;
    private String max;
    private String windSpeed;
    private String humidity;
    private String uvRisk;
    private String sunset, sunrise;

    public void SetTitle(String T){

        title = T;

        try
        {
            String[] arrOfTitle = title.split(", ", 2);
            String[] arrOfWeather = arrOfTitle[0].split(": ", 2);
            weatherType = arrOfWeather[1];
            day = arrOfWeather[0];
        }
        catch (Exception e)
        {
            //e.printStackTrace();
            Log.e("Forecast","Error extracting metric value");
        }
    }



    public void SetDescription(String D){
        description = D;

        try
        {
            String[] arrOfDesc = description.split(", ", 12);
            String[] arrOfMax = arrOfDesc[0].split(": ", 2);

            //Max Temp
            if (arrOfMax[0].equals("Maximum Temperature"))
            {
                Log.i("Forecast", "is max");
                String[] arrOfMaxTemps = arrOfMax[1].split(" ", 2);
                max = arrOfMaxTemps[0];

                String[] arrOfMin = arrOfDesc[1].split(": ", 2);
                String[] arrOfMinTemps = arrOfMin[1].split(" ", 2);
                min = arrOfMinTemps[0];
            }
            else if (arrOfMax[0].equals("Minimum Temperature")) //Late check
            {
                Log.i("Forecast", "is late and min");

                max = "[Na]";

                String[] arrOfMin = arrOfDesc[0].split(": ", 2);
                String[] arrOfMinTemps = arrOfMin[1].split(" ", 2);
                min = arrOfMinTemps[0];

            }

            //Wind Speed
            String[] arrOfPosWindSpeed = arrOfDesc[3].split(": ", 2);
            if (arrOfPosWindSpeed[0].equals("Wind Speed"))
            {
                windSpeed = arrOfPosWindSpeed[1];
            }
            else if (arrOfPosWindSpeed[0].equals("Visibility"))
            {
                String[] arrOfWindSpeed = arrOfDesc[2].split(": ", 2);
                windSpeed = arrOfWindSpeed[1];
            }

            //Humidity
            String[] arrOfPosHumidity = arrOfDesc[6].split(": ", 2);
            if (arrOfPosHumidity[0].equals("Humidity"))
            {
                humidity = arrOfPosHumidity[1];
            }
            else if (arrOfPosHumidity[0].equals("UV Risk"))
            {
                String[] arrOfHumidity = arrOfDesc[5].split(": ", 2);
                humidity = arrOfHumidity[1];
            }

            //UV Risk
            String[] arrOfPosUVRisk = arrOfDesc[7].split(": ", 2);
            if (arrOfPosUVRisk[0].equals("UV Risk"))
            {
                uvRisk = arrOfPosUVRisk[1];
            }
            else if (arrOfPosUVRisk[0].equals("Pollution"))
            {
                String[] arrOfUVRisk = arrOfDesc[6].split(": ", 2);
                uvRisk = arrOfUVRisk[1];
            }

            //Sunrise/Sunset
            String[] arrOfPosSunset = arrOfDesc[8].split(": ", 2);
            if (arrOfPosSunset[0].equals("Sunset")) //Late
            {
                sunset = arrOfPosSunset[1];

                sunrise = "[Na]";

            }
            else if (arrOfPosSunset[0].equals("Pollution")) //Early
            {
                String[] arrOfSunrise = arrOfDesc[9].split(": ", 2);
                sunrise = arrOfSunrise[1];

                String[] arrOfSunset = arrOfDesc[10].split(": ", 2);
                sunset = arrOfSunset[1];

            }

//            for (String a : arrOfDesc){
//                Log.i("Forecast",a);
//            }
        }
        catch (Exception e)
        {
            //e.printStackTrace();
            Log.e("Forecast","Error extracting metric value");
        }


    }

    public String GetTitle(){return title;}
    public String GetDescription(){return description;}
    public String GetWeatherType(){return weatherType;}
    public String GetMin(){return min;}
    public String GetMax(){return max;}
    public String GetWindSpeed(){return windSpeed;}
    public String GetHumidity(){return humidity;}
    public String GetUVRisk(){return uvRisk;}
    public String GetSunrise(){return sunrise;}
    public String GetSunset(){return sunset;}
    public String GetDay(){return day;}






    public String toString(){
        return title + " " + description + '\n';
    }


}
