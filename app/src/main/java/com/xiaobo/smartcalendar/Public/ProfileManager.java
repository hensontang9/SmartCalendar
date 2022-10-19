package com.xiaobo.smartcalendar.Public;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.util.Log;

import com.amap.api.services.geocoder.GeocodeSearch;
import com.xiaobo.smartcalendar.Model.Events.Location;

import java.util.Locale;

public class ProfileManager {

    private static ProfileManager sProfileManager;
    private final Context mContext;

    public static ProfileManager get(Context mContext) {
        if (sProfileManager == null) {
            sProfileManager = new ProfileManager(mContext);
        }
        return sProfileManager;
    }

    private ProfileManager(Context mContext) {
        this.mContext = mContext;
    }

    
    public boolean setToken(String token) {
        SharedPreferences sPreferences = this.mContext.getSharedPreferences("config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sPreferences.edit();
        editor.putString("token", token);

        Log.d("ProfileManager", "存储了token : " + token);

        return editor.commit();
    }
    public String getToken() {

        SharedPreferences sPreferences = this.mContext.getSharedPreferences("config", Context.MODE_PRIVATE);
        String token = sPreferences.getString("token", "");

        return token;
    }

    public boolean setLanguage(int language) {
        SharedPreferences sPreferences = this.mContext.getSharedPreferences("config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sPreferences.edit();
        editor.putInt("language", language);
        Log.e("profilemanager", "设置语言为" + language);
        return editor.commit();
    }

    public int getLanguage() {
        SharedPreferences sPreferences = this.mContext.getSharedPreferences("config", Context.MODE_PRIVATE);
        int language = sPreferences.getInt("language", 0);

        Configuration configuration = this.mContext.getResources().getConfiguration();
        switch (language) {
            case 0:
                configuration.setLocale(Locale.getDefault());
                break;
            case 1:
                configuration.setLocale(Locale.SIMPLIFIED_CHINESE);
                break;
            case 2:
                configuration.setLocale(Locale.ENGLISH);
        }
        Log.d("ProfileManager", "获取当前语言设置" + language + " " + configuration.toString());
        this.mContext.getResources().updateConfiguration(configuration, this.mContext.getResources().getDisplayMetrics());

        return language;
    }

    
    public boolean saveHomeLocation(Location homeLocation) {
        SharedPreferences sPreferences = this.mContext.getSharedPreferences("config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sPreferences.edit();

        editor.putString("longitude", String.valueOf(homeLocation.getEndPoint().getLongitude()));
        editor.putString("latitude", String.valueOf(homeLocation.getEndPoint().getLatitude()));

        return editor.commit();
    }
    
    public Location getHomeLocation() {
        SharedPreferences sPreferences = this.mContext.getSharedPreferences("config", Context.MODE_PRIVATE);
        String longitude = sPreferences.getString("longitude", "");
        String latitude = sPreferences.getString("latitude", "");

        Location mHomeLocation = new Location();
        if (longitude == null || longitude == "" || latitude == null || latitude == "") {
            return null;
        }
        else {
            mHomeLocation.setEndPoint(Double.parseDouble(longitude), Double.parseDouble(latitude));
            return mHomeLocation;
        }

    }

}
