package com.xiaobo.smartcalendar.activity.PoiSearchWithoutMap;

public class PoiInfoModel {

    String lon;
    String lat;
    String title;
    String address;

    public String getLon() {
        return lon;
    }

    public String getLat() {
        return lat;
    }

    public String getTitle() {
        return title;
    }

    public String getAddress() {
        return address;
    }

    public PoiInfoModel (String lon, String lat, String title, String address) {
        this.lon = lon;
        this.lat = lat;
        this.title = title;
        this.address = address;
    }

}
