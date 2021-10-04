package com.example.earthquakereport;

public class Quake {

    private long date;
    private double mag;
    private String place,country,url;

    public Quake(double mag,String place,String country,long date,String url){
        this.place=place;
        this.date=date;
        this.mag=mag;
        this.country=country;
        this.url=url;
    }

    public double getMag(){
        return mag;
    }
    public String getPlace(){
        return place;
    }
    public String getCountry(){
        return country;
    }
    public long getDate(){
        return date;
    }
    public String getUrl(){
        return url;
    }
}
