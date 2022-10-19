package com.xiaobo.smartcalendar.Model.Events;

import com.amap.api.services.core.LatLonPoint;

public class TargetLocation {

    
    LatLonPoint targetPoint;
    
    String targetName;
    
    String cityCode;
    String mCity;

    
    public LatLonPoint getTargetPoint() {
        return targetPoint;
    }

    public void setTargetPoint(LatLonPoint targetPoint) {
        this.targetPoint = targetPoint;
    }
    public void setTargetPoint(double latitude, double longitude) {
        this.targetPoint = new LatLonPoint(latitude, longitude);
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getmCity() {
        return mCity;
    }

    public void setmCity(String mCity) {
        this.mCity = mCity;
    }

    
    public TargetLocation() {
        this.targetPoint = new LatLonPoint(0, 0);
        this.targetName = "未设定目标地点";
    }
    public TargetLocation(LatLonPoint endPoint, String targetName) {
        this.targetPoint = endPoint;
        this.targetName = targetName;
    }

    public String describeLocation() {
        StringBuilder str = new StringBuilder();
        str.append("经纬度:" + this.targetPoint.toString() + "\n");
        str.append("地名:" + this.targetName + "\n");
        str.append("城市编码:" + this.cityCode + "\n");
        str.append("城市:" + this.mCity + "\n");

        return str.toString();
    }
}
