package com.xiaobo.smartcalendar.Model.Location;

import com.amap.api.location.AMapLocation;

public class LocationModel {

    int locationType;
    double longitude;
    double latitude;
    float accuracy;
    String provider;
    float speed;
    float bearing;
    int satellites;
    String country;
    String province;
    String city;
    String cityCode;
    String district;
    String adCode;
    String address;
    String poiName;
    long time;

    public LocationModel(AMapLocation aMapLocation) {
        locationType = aMapLocation.getLocationType();
        longitude = aMapLocation.getLongitude();
        latitude = aMapLocation.getLatitude();
        accuracy = aMapLocation.getAccuracy();
        provider = aMapLocation.getProvider();
        speed = aMapLocation.getSpeed();
        bearing = aMapLocation.getBearing();
        satellites = aMapLocation.getSatellites();
        country = aMapLocation.getCountry();
        province = aMapLocation.getProvince();
        city = aMapLocation.getCity();
        cityCode = aMapLocation.getCityCode();
        district = aMapLocation.getDistrict();
        adCode = aMapLocation.getAdCode();
        address = aMapLocation.getAddress();
        poiName = aMapLocation.getPoiName();
        time = aMapLocation.getTime();
    }

    public int getLocationType() {
        return locationType;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public String getProvider() {
        return provider;
    }

    public float getSpeed() {
        return speed;
    }

    public float getBearing() {
        return bearing;
    }

    public int getSatellites() {
        return satellites;
    }

    public String getCountry() {
        return country;
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getCityCode() {
        return cityCode;
    }

    public String getDistrict() {
        return district;
    }

    public String getAdCode() {
        return adCode;
    }

    public String getAddress() {
        return address;
    }

    public String getPoiName() {
        return poiName;
    }

    public long getTime() {
        return time;
    }
}
