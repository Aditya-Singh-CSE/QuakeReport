package com.example.android.quakereport;

public class Earthquake {

    /** Magnitude of the earthquake*/
    private Double mMagnitude;

    /** Location of the earthquake*/
    private String mLocation;

    /** Url of earthquake*/
    private String mUrl;


    /** Date of the earthquake*/
    private String mDate;

    private long mTimeInMillisecond;

    /**
     *Construct a new (@Link Earthquake) object
     *  @param magnitude is the magnitude(size) of the earthquake
     * @param location is the city location of the earthquake
     * @param timeInMillisecond is the date earthquake happened
     */
    public Earthquake(Double magnitude, String location, long timeInMillisecond, String url){
        mMagnitude = magnitude;
        mLocation = location;
        mTimeInMillisecond = timeInMillisecond;
        mUrl = url;
    }

    /** Return the magnitude of the Earthquake
     * @return*/
    public Double getMagnitude(){
        return mMagnitude;
    }

    /** Return the location of the Earthquake */
    public String getLocation(){
        return mLocation;
    }

    /** Return the Date of the Earthquake */
    public String getDate(){
        return mDate;
    }

    public long getTimeInMillisecond(){
        return mTimeInMillisecond;
    }

    /** Return the url of earthquake */
    public String getUrl(){
        return mUrl;
    }

}
