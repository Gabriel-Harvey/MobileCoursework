/*  Starter project for Mobile Platform Development in main diet 2023/2024
    You should use this project as the starting point for your assignment.
    This project simply reads the data from the required URL and displays the
    raw data in a TextField
*/

//
// Name                 _________________
// Student ID           _________________
// Programme of Study   _________________
//

// UPDATE THE PACKAGE NAME to include your Student Identifier
package com.example.coursework;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity  implements OnClickListener, OnMapReadyCallback
{
    private TextView glWType, glDayTemp, glWindSpeed, glHumidity, glUVRisk, glSunrise, glSunSet, glTomorrow, glOvermorrow, glTemp;
    private TextView lonWType, lonDayTemp, lonWindSpeed, lonHumidity, lonUVRisk, lonSunrise, lonSunSet, lonTomorrow, lonOvermorrow, lonTemp;
    private Button  nextBtn;
    private ImageButton startButton;
    private String result, result2;
    private Integer parseStage = 0;
    private ViewFlipper flipper;
    private LinkedList<Forecast> aList;
    private LinkedList<ForecastCurrent> aListCurrent;

    private GoogleMap myMap;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        aList = new LinkedList<>();
        aListCurrent = new LinkedList<>();

        //Glasgow forecast objects
        glWType = findViewById(R.id.glWeatherType);
        glDayTemp = findViewById(R.id.glDayTemp);
        glWindSpeed = findViewById(R.id.glWindSpeed);
        glHumidity = findViewById(R.id.glHumidity);
        glUVRisk = findViewById(R.id.glUVRisk);
        glSunrise = findViewById(R.id.glsunrise);
        glSunSet = findViewById(R.id.glsunset);
        glTomorrow = findViewById(R.id.glTomorrow);
        glOvermorrow = findViewById(R.id.glOvermorrow);
        glTemp = findViewById(R.id.glCurrentTemp);

        //London forecast objects
        lonWType = findViewById(R.id.lonWeatherType);
        lonDayTemp = findViewById(R.id.lonDayTemp);
        lonWindSpeed = findViewById(R.id.lonWindSpeed);
        lonHumidity = findViewById(R.id.lonHumidity);
        lonUVRisk = findViewById(R.id.lonUVRisk);
        lonSunrise = findViewById(R.id.lonsunrise);
        lonSunSet = findViewById(R.id.lonsunset);
        lonTomorrow = findViewById(R.id.lonTomorrow);
        lonOvermorrow = findViewById(R.id.lonOvermorrow);
        lonTemp = findViewById(R.id.lonCurrentTemp);

        startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(this);
        nextBtn = findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(this);

        flipper = findViewById(R.id.flipper);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        startProgress();
    }

    public void onClick(View aview)
    {
        if (aview == startButton )
        {
            startProgress();
            Log.i("Position", "In onClick()" );
        }
        else if (aview == nextBtn)
        {
            flipper.showNext();
            Log.i("Position", String.valueOf(flipper.getDisplayedChild()));
            if (flipper.getDisplayedChild() == 0) // On Glasgow
            {
                parseStage = 0;
                startProgress();
                updateMap(55.86267197826339F, -4.269548994187937F, "Glasgow");
            }
            else if (flipper.getDisplayedChild() == 1 ) //On London
            {
                parseStage = 1;
                startProgress();
                updateMap(51.50510613962853F, -0.12087533177249316F, "London");
            }
        }
    }

    public void startProgress()
    {
        result = null;
        result2 = null;
        aList = new LinkedList<>();
        aListCurrent = new LinkedList<>();

        if (parseStage == 0)
        {
            String urlSource = "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2648579"; //Glasgow
            new Thread(new Task(urlSource)).start();
        }
        else if (parseStage == 1)
        {
            String urlSource2 = "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2643743"; //London
            new Thread(new Task(urlSource2)).start();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap)
    {
        myMap = googleMap;

        LatLng glasgow = new LatLng(55.86267197826339, -4.269548994187937);
        myMap.addMarker(new MarkerOptions().position(glasgow).title("Glasgow"));
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(glasgow, 11.0f));

        myMap.getUiSettings().setZoomControlsEnabled(true);
        myMap.getUiSettings().setAllGesturesEnabled(false);

    }

    private void updateMap(Float lat, Float lng, String name)
    {
        LatLng location = new LatLng(lat, lng);
        myMap.clear();
        myMap.addMarker(new MarkerOptions().position(location).title(name));
        myMap.moveCamera(CameraUpdateFactory.newLatLng(location));
    }

    private void currentTest()
    {
        if (parseStage == 0)
        {
            String urlSourceCurrent = "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/2648579"; //Glasgow current
            new Thread(new Task2(urlSourceCurrent)).start();
        }
        else if (parseStage == 1)
        {
            String urlSourceCurrent = "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/2643743"; //Glasgow current
            new Thread(new Task2(urlSourceCurrent)).start();
        }

    }

    private class Task implements Runnable //Accessing URL
    {
        private final String url;
        public Task(String aUrl)
        {
            url = aUrl;
        }

        @Override
        public void run()
        {
            URL aurl;
            URLConnection yc;
            BufferedReader in;
            String inputLine;

            Log.i("Position","in run");

            try
            {
                Log.i("Position","in try");
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                while ((inputLine = in.readLine()) != null)
                {
                    result = result + inputLine;
                    //Log.e("MyTag",inputLine);

                }
                in.close();
            }
            catch (IOException ae)
            {
                Log.e("MyTag", "ioexception");
            }

            //Get rid of the first tag <?xml version="1.0" encoding="utf-8"?>
            int i = result.indexOf(">");
            result = result.substring(i+1);

            parseData(result);

            //Reads text to display
            MainActivity.this.runOnUiThread(() -> {
                Log.i("UI thread", "I am the UI thread");

                if (parseStage == 0)
                {
                    Log.i("UI thread", "in if");
                    glWType.setText(aList.get(0).GetWeatherType());
                    glDayTemp.setText(aList.get(0).GetMax() + " / " + aList.get(0).GetMin());
                    glWindSpeed.setText(aList.get(0).GetWindSpeed());
                    glHumidity.setText(aList.get(0).GetHumidity());
                    glUVRisk.setText(aList.get(0).GetUVRisk());
                    glSunrise.setText(aList.get(0).GetSunrise());
                    glSunSet.setText(aList.get(0).GetSunset());

                    glTomorrow.setText(aList.get(1).GetDay() + " - " + aList.get(1).GetMax() + " / " + aList.get(1).GetMin());
                    glOvermorrow.setText(aList.get(2).GetDay() + " - " + aList.get(2).GetMax() + " / " + aList.get(2).GetMin());
                }
                else if (parseStage == 1)
                {
                    Log.i("UI thread", "in if 2");

                    lonWType.setText(aList.get(0).GetWeatherType());
                    lonDayTemp.setText(aList.get(0).GetMax() + " / " + aList.get(0).GetMin());
                    lonWindSpeed.setText(aList.get(0).GetWindSpeed());
                    lonHumidity.setText(aList.get(0).GetHumidity());
                    lonUVRisk.setText(aList.get(0).GetUVRisk());
                    lonSunrise.setText(aList.get(0).GetSunrise());
                    lonSunSet.setText(aList.get(0).GetSunset());

                    lonTomorrow.setText(aList.get(1).GetDay() + " - " + aList.get(1).GetMax() + " / " + aList.get(1).GetMin());
                    lonOvermorrow.setText(aList.get(2).GetDay() + " - " + aList.get(2).GetMax() + " / " + aList.get(2).GetMin());
                }
            });

            currentTest();
        }

        public void parseData(String dataToParse)
        {
            Log.i("Parse Tag","in parseData()");
            Forecast aForecast = null;
            try
            {
                Log.i("Parse Tag","in try");
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(new StringReader(dataToParse));
                int eventType = xpp.getEventType();
                boolean useTags = false;
                while (eventType != XmlPullParser.END_DOCUMENT)
                {
                    if(eventType == XmlPullParser.START_TAG)
                    {
                        if(xpp.getName().equalsIgnoreCase("item"))
                        {
                            Log.d("Parse Tag","New Thing found!");
                            aForecast = new Forecast();
                            useTags = true;
                        }
                        else if(xpp.getName().equalsIgnoreCase("title") && useTags)
                        {
                            String temp = xpp.nextText();
                            aForecast.SetTitle(temp);
                            Log.d("Parse Tag","Title is " + temp);

                        }
                        else if(xpp.getName().equalsIgnoreCase("description") && useTags)
                        {
                            String temp = xpp.nextText();
                            aForecast.SetDescription(temp);
                            Log.d("Parse Tag","Description is " + temp);
                        }
                    }
                    else if(eventType == XmlPullParser.END_TAG) //Found a end tag
                    {
                        if(xpp.getName().equalsIgnoreCase("item"))
                        {
                            Log.d("Parse Tag","Forecast parsing completed!");
                            aList.add(aForecast);
                        }
                    }
                    eventType = xpp.next();
                } //End of while
            }
            catch (XmlPullParserException e)
            {
                Log.e("Parse Tag","Parsing error" + e);
                throw new RuntimeException(e);
            }
            catch (IOException ae1)
            {
                Log.e("Parse Tag","IO error during parsing");
            }
            Log.d("Parse Tag","End of document reached");
        } //End of parse
    }

    private class Task2 implements Runnable //Accessing URL
    {
        private final String url;
        public Task2(String aUrl)
        {
            url = aUrl;
            Log.i("Position",url);
        }

        @Override
        public void run()
        {
            URL aurl;
            URLConnection yc;
            BufferedReader in;
            String inputLine;

            Log.i("Position","in run");


            try
            {
                Log.i("Position","in try");
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                while ((inputLine = in.readLine()) != null)
                {
                    result2 = result2 + inputLine;
                    //Log.e("MyTag",inputLine);
                }
                in.close();
            }
            catch (IOException ae)
            {
                Log.e("MyTag", "ioexception");
            }

            //Get rid of the first tag <?xml version="1.0" encoding="utf-8"?>
            int i = result2.indexOf(">");
            result2 = result2.substring(i+1);

            parseData(result2);

            //Reads text to display
            MainActivity.this.runOnUiThread(() -> {
                Log.i("UI thread2", "I am the UI thread");

                if (parseStage == 0)
                {
                    Log.i("UI thread2", "in if");
                    glTemp.setText(aListCurrent.get(0).GetTemp());

                }
                else if (parseStage == 1)
                {
                    Log.i("UI thread2", "in if 2");
                    lonTemp.setText(aListCurrent.get(0).GetTemp());
                }

            });
        }

        public void parseData(String dataToParse)
        {
            Log.i("Parse Tag 2","in parseData()");
            ForecastCurrent aForecastCurrent = null;
            try
            {
                Log.i("Parse Tag 2","in try");
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(new StringReader(dataToParse));
                int eventType = xpp.getEventType();
                boolean useTags = false;
                Log.i("Parse Tag 2","in try 2");
                while (eventType != XmlPullParser.END_DOCUMENT)
                {
                    if(eventType == XmlPullParser.START_TAG)
                    {
                        Log.i("Parse Tag 2","here");
                        if(xpp.getName().equalsIgnoreCase("item"))
                        {
                            Log.d("Parse Tag 2","New Thing found!");
                            aForecastCurrent = new ForecastCurrent();
                            useTags = true;
                        }
                        else if(xpp.getName().equalsIgnoreCase("title") && useTags)
                        {
                            String temp = xpp.nextText();
                            aForecastCurrent.SetTitle(temp);
                            Log.d("Parse Tag 2","Title is " + temp);

                        }
                        else if(xpp.getName().equalsIgnoreCase("description") && useTags)
                        {
                            String temp = xpp.nextText();
                            aForecastCurrent.SetDescription(temp);
                            Log.d("Parse Tag 2","Description is " + temp);
                        }
                    }
                    else if(eventType == XmlPullParser.END_TAG) //Found a end tag
                    {
                        if(xpp.getName().equalsIgnoreCase("item"))
                        {
                            Log.d("Parse Tag 2","Forecast parsing completed!");
                            aListCurrent.add(aForecastCurrent);
                        }
                    }
                    eventType = xpp.next();
                } //End of while
            }
            catch (XmlPullParserException e)
            {
                Log.e("Parse Tag 2","Parsing error" + e);
                throw new RuntimeException(e);
            }
            catch (IOException ae1)
            {
                Log.e("Parse Tag 2","IO error during parsing");
            }
            Log.d("Parse Tag 2","End of document reached");
        } //End of parse
    }
}