package com.example.android.quakereport;

/**
 * Created by blueb on 3/31/2017.
 */

public class Earthquake {
    private float magnitude;
    private String location;
    private long time_in_mili;
    private  String url;

    public Earthquake(float mag, String loc, long time, String url){
        this.magnitude=mag;
        this.location=loc;
        this.time_in_mili=time;
        this.url=url;
    }

    public float getMagnitude(){
        return this.magnitude;
    }

    public String getLocation(){
        return this.location;
    }

    public long getTime(){
        return this.time_in_mili;
    }

    public String getUrl(){return  this.url;}
}
