package com.xiaobo.smartcalendar.Public;

import com.amap.api.services.core.LatLonPoint;
import com.xiaobo.smartcalendar.Model.Events.MyEvent;
import com.xiaobo.smartcalendar.Model.Events.MyEventManager;
import com.xiaobo.smartcalendar.Model.Location.LocationModel;

import java.util.UUID;

public class CurrentLocation {
    private static CurrentLocation sCurrentLocation;
    CurrentLocation() {
        this.city = "北京";
        this.district = "朝阳区";
    }
    public static CurrentLocation getInstance() {
        if (sCurrentLocation == null) {
            sCurrentLocation = new CurrentLocation();
        }
        return sCurrentLocation;
    }

    double latitude;
    double longitude;
    String city;
    String district;
    LocationModel locationModel;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public LocationModel getLocationModel() {
        return locationModel;
    }

    public void setLocationModel(LocationModel locationModel) {
        this.locationModel = locationModel;
    }

    public void setPoint(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    
    public LatLonPoint existStartPoint(MyEvent e) {
        MyEvent myEvent = MyEventManager.getInstance().getRecentEvent(e);
        if (myEvent != null) {
            LatLonPoint perPoint = myEvent.getmTargetLocation().getTargetPoint();
            return perPoint;
        }
        else {
            return null;
        }
    }
}
