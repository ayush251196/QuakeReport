package com.example.android.quakereport;

import java.net.URL;

public class Earthquake {
    private  String magnitude;
    private String place ;
    private String time;
    private String url;
    Earthquake(String magnitude,String place ,String time,String url){
        this.magnitude=magnitude;
        this.place=place;
        this.time=time;
        this.url=url;
    }

    public String getMagnitude() {
        return magnitude;
    }

    public String getPlace() {
        return place;
    }

    public String getTime() {
        return time;
    }

    public String getUrl() {
        return url;
    }
}
