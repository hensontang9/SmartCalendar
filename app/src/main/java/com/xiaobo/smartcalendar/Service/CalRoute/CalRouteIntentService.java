package com.xiaobo.smartcalendar.Service.CalRoute;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.DistanceResult;
import com.amap.api.services.route.DistanceSearch;
import com.xiaobo.smartcalendar.Model.Events.MyEvent;
import com.xiaobo.smartcalendar.Model.Events.MyEventManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CalRouteIntentService extends IntentService implements DistanceSearch.OnDistanceSearchListener {

    private static final String TAG = "CalRouteService";
    private String start_lat;
    private String start_lng;
    private String lat;
    private String lng;
    private UUID uuid;

    public static Intent newIntent(Context context) {
        return new Intent(context, CalRouteIntentService.class);
    }
    public CalRouteIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.i(TAG, "Received an intent: " + intent);

        
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            
            try {
                start_lat = bundle.getString("start_lat");
                start_lng = bundle.getString("start_lng");
                lat = bundle.getString("lat");
                lng = bundle.getString("lng");
                Log.d("\n" + TAG, "接收到经纬度" + "\n" + start_lat + "\n" + start_lng + "\n" + lat + "\n" + lng);
            }
            catch (Exception e) {
                Log.e(TAG, "接收到的坐标信息不规范");
            }
            try {
                uuid = UUID.fromString(bundle.getString("UUID"));
                MyEvent myEvent = MyEventManager.getInstance().getMyEvent(uuid);


                Log.d(TAG, "接收到事件ID:" + uuid);
            }
            catch (Exception e) {
                Log.e(TAG, "接收到的事件信息不规范");
            }

        }

        
        init();

    }

    
    private void init() {
        
        DistanceSearch.DistanceQuery distanceQuery = new DistanceSearch.DistanceQuery();
        DistanceSearch distanceSearch = null;
        try {
            distanceSearch = new DistanceSearch(this);
        } catch (AMapException e) {
            e.printStackTrace();
        }
        
        LatLonPoint start = new LatLonPoint(Double.valueOf(start_lat), Double.valueOf(start_lng));
        LatLonPoint dest = new LatLonPoint(Double.valueOf(lat), Double.valueOf(lng));
        List<LatLonPoint> latLonPoints = new ArrayList<LatLonPoint>();
        latLonPoints.add(start);
        distanceQuery.setOrigins(latLonPoints);
        distanceQuery.setDestination(dest);
        
        distanceQuery.setType(DistanceSearch.TYPE_DRIVING_DISTANCE);
        distanceSearch.calculateRouteDistanceAsyn(distanceQuery);
        distanceSearch.setDistanceSearchListener(this);
    }

    @Override
    public void onDistanceSearched(DistanceResult distanceResult, int i) {

        String result;
        String commuting_time = " ";
        
        if (i == 1000) {
            String time_string;
            
            String distance = Integer.valueOf((int) distanceResult.getDistanceResults().get(0).getDistance()) / 1000 + "";
            
            

            long second = (long) distanceResult.getDistanceResults().get(0).getDuration();
            commuting_time = String.valueOf(second);
            long days = second / 86400;            
            second = second % 86400;            
            long hours = second / 3600;            
            second = second % 3600;                
            long minutes = second / 60;            
            second = second % 60;

            if (days > 0) {
                time_string = days + "天" + hours + "小时" + minutes + "分钟";
            } else if (hours > 0) {
                time_string = hours + "小时" + minutes + "分钟";
            } else {
                time_string = minutes + "分钟";
            }
            Log.d(TAG, "距您约" + distance + "公里，驾车约" + time_string);

            result = "距您约" + distance + "公里，驾车约" + time_string;

            
            commuting_time = Long.parseLong(commuting_time) < 60 ? "0" : commuting_time;
            Log.d("CalRoute", "通勤时间:" + commuting_time + "秒");

        } else {

            Log.e(TAG, "errorcode " + i);
            Log.e(TAG, "暂无定位信息");
            result = "暂无定位信息";
        }



        sendMessage(result, commuting_time);
    }

    
    private void sendMessage(String str, String commuting_time) {
        Intent i = new Intent();
        i.setAction("com.calroute_message");
        i.putExtra("uuid", String.valueOf(uuid));
        i.putExtra("message", str);
        i.putExtra("commuting_time", commuting_time);
        this.sendBroadcast(i);
    }

}
