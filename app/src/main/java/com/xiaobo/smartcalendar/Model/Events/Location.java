package com.xiaobo.smartcalendar.Model.Events;

import android.content.Context;

import com.amap.api.services.core.LatLonPoint;

public class Location {

    
    LatLonPoint startPoint;      
    LatLonPoint endPoint;        

    
    public String startName;
    public String endName;

    
    public String cityCode;
    public String mCity;

    public void setStartPoint(LatLonPoint startPoint) {
        this.startPoint = startPoint;
    }
    public void setStartPoint(double longitude, double latitude) {
        this.startPoint = new LatLonPoint(longitude, latitude);
    }
    public void setStartName(String str) {
        this.startName = str;
    }

    public String getStartName() {
        return startName;
    }

    public void setEndPoint(LatLonPoint endPoint) {
        this.endPoint = endPoint;
    }
    public void setEndPoint(double latitude, double longitude) {
        this.endPoint = new LatLonPoint(latitude, longitude);
    }

    public LatLonPoint getEndPoint() {
        return endPoint;
    }

    public void setEndName(String str) {
        this.endName = str;
    }

    public String getEndName() {
        return endName;
    }

    public String getmCity() {
        return mCity;
    }

    public void setmCity(String mCity) {
        this.mCity = mCity;
    }

    public Location() {

        this.startPoint = new LatLonPoint(0, 0);
        this.endPoint = new LatLonPoint(0, 0);
        this.startName = "未设定起点";
        this.endName = "未设定终点";

    }
    public Location(Context context, LatLonPoint startPoint, LatLonPoint endPoint) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
//        calRoute(context, startPoint, endPoint);
    }

}
