package com.xiaobo.smartcalendar.Service.BackgroundService;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.xiaobo.smartcalendar.MainActivity;
import com.xiaobo.smartcalendar.Model.Contradiction.MyTemporalInconsistency;
import com.xiaobo.smartcalendar.Model.Contradiction.MyTemporalInconsistencyManager;
import com.xiaobo.smartcalendar.Model.Events.Location;
import com.xiaobo.smartcalendar.Model.Events.MyEvent;
import com.xiaobo.smartcalendar.Model.Events.MyEventManager;
import com.xiaobo.smartcalendar.Model.Events.TempEventManager;
import com.xiaobo.smartcalendar.Public.ProfileManager;
import com.xiaobo.smartcalendar.Public.PublicVaribale;
import com.xiaobo.smartcalendar.Public.VaribaleManager;
import com.xiaobo.smartcalendar.Service.HttpRequest.HttpRequestIntentService;
import com.xiaobo.smartcalendar.Service.MainService.MainService;
import com.xiaobo.smartcalendar.activity.SetingActivity.SetingActivity;

import java.util.List;
import java.util.UUID;

public class ReceivedBroadcastService extends Service {

    private static final String TAG = "ReceivedBroadcastService";

    LocationReceiver locationReceiver;
    CalRouteReceiver calRouteReceiver;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "接收服务创建");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "接收服务启动");
        initReceiver();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        
        unregisterReceiver(locationReceiver);
        unregisterReceiver(calRouteReceiver);

    }

    private void initReceiver() {
        
        locationReceiver = new LocationReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.xiaobo.locationservice");
        registerReceiver(locationReceiver, filter);

        
        calRouteReceiver = new CalRouteReceiver();
        IntentFilter filterRoute = new IntentFilter();
        filterRoute.addAction("com.calroute_message");
        registerReceiver(calRouteReceiver, filterRoute);
    }

    
    private class LocationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            String city = bundle.getString("city");
            String district = bundle.getString("district");
            String latitude = bundle.getString("latitude");
            String longitude = bundle.getString("longitude");
            Log.d("ReceivedService", "接收到定位广播:" + city + " " + district + " " + latitude + " " + longitude);
            Location location = new Location();
            location.setEndPoint(Double.parseDouble(latitude), Double.parseDouble(longitude));
            ProfileManager.get(getApplicationContext()).saveHomeLocation(location);
        }
    }

    
    private class CalRouteReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "路径计算接收到通知");
            Bundle bundle = intent.getExtras();
            UUID uuid = UUID.fromString(bundle.getString("uuid"));
            int commuting_time = 0;
            try {
                commuting_time = Integer.valueOf(bundle.getString("commuting_time"));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            Log.d(TAG,"事件ID为" + uuid);
            Log.d(TAG, "获取到路径计算结果:" + bundle.getString("message"));
            Log.d(TAG, "通勤时间为:" + commuting_time + "秒");

            Intent i = HttpRequestIntentService.newIntent(ReceivedBroadcastService.this);
            
            if (MyEventManager.getInstance().getMyEvent(uuid) != null) {
                Log.d(TAG, "修改事件");
                MyEvent e = MyEventManager.getInstance().getMyEvent(uuid);
                e.setCommutingTime(commuting_time);
                MyEventManager.getInstance().resetMyEvent(e.getmId(), e);
                Bundle httpBundle = new Bundle();
                httpBundle.putString("REQUEST_TYPE", String.valueOf(PublicVaribale.RequestType.REVISE_EVENT));
                httpBundle.putString("EVENT_ID", String.valueOf(uuid));
                i.putExtras(httpBundle);


            }
            else {
                
                Log.d(TAG, "新事件");
                TempEventManager.getInstance().setCommutingTime(uuid, commuting_time);
                
                if (TempEventManager.getInstance().getTempEvent(uuid).isCompleteEdit()) {
                    TempEventManager.getInstance().saveEvents();
                    Log.d(TAG, "上传事件");
                    Bundle httpBundle = new Bundle();
                    httpBundle.putString("REQUEST_TYPE", String.valueOf(PublicVaribale.RequestType.UP_EVENT));
                    httpBundle.putString("EVENT_ID", String.valueOf(uuid));
                    i.putExtras(httpBundle);
                }

            }


            if (MyEventManager.getInstance().getMyEvent(uuid) != null) {
                startService(i);
            }





















            


        }
    }

    private class LoginStateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            Log.d(TAG, "登陆返回信息->" + bundle.getString("message"));
            String state = bundle.getString("state");
            Log.d(TAG, "接收到登陆状态->" + state);

            if (state.equals(PublicVaribale.LOGIN_SUCCESS) && VaribaleManager.getInstance().getArrayForUploadList().size() > 0) {
                for (UUID uuid : VaribaleManager.getInstance().getArrayForUploadList()) {
                    Intent i = HttpRequestIntentService.newIntent(ReceivedBroadcastService.this);
                    Bundle httpBundle = new Bundle();
                    httpBundle.putString("REQUEST_TYPE", String.valueOf(PublicVaribale.RequestType.UP_EVENT));
                    httpBundle.putString("EVENT_ID", String.valueOf(uuid));
                    i.putExtras(httpBundle);
                    startService(i);
                }
            }
        }
    }

}
