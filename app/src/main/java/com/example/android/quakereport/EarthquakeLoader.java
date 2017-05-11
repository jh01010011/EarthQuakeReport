package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;


public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {

    private static String LOG_TAG=EarthquakeLoader.class.getName();

    private String mURL;
    public EarthquakeLoader(Context context,String url){

        super(context);
        mURL=url;


    }

    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG,"_____On StartLoading______");

        forceLoad();
    }

    @Override
    public List<Earthquake> loadInBackground() {
        Log.i(LOG_TAG,"____Loading IN Background_____");


        if(mURL==null){
            return null;

        }

        List<Earthquake> earthquakes=QueryUtils.extractEarthquakes(mURL);
        return earthquakes;
    }
}
