package com.xiaobo.smartcalendar.Service.Location;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.ServiceSettings;
import com.xiaobo.smartcalendar.Model.Location.LocationModel;
import com.xiaobo.smartcalendar.Public.CurrentLocation;

public class LocationService extends Service {

    private static final String TAG = "LocationService";
    
    AMapLocationClient mLocationClient;
    AMapLocationClientOption mLocationOption;

    
    int mStartMode;

    
    IBinder mBinder;

    
    boolean mAllowRebind;

    
    @Override
    public void onCreate() {
        Log.i(TAG, "定位服务准备启动");

        
        initLocation();
    }

    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "定位服务启动");
        onCount();
        mLocationClient.startLocation();
        return mStartMode;
    }

    
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    
    @Override
    public boolean onUnbind(Intent intent) {
        return mAllowRebind;
    }

    
    @Override
    public void onRebind(Intent intent) {

    }

    
    @Override
    public void onDestroy() {
        Log.i(TAG, "定位服务停止");
        mLocationClient.stopLocation();
    }

    private void initLocation() {
        ServiceSettings.updatePrivacyShow(this,true,true);
        ServiceSettings.updatePrivacyAgree(this,true);
        try {
            mLocationClient = new AMapLocationClient(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mLocationOption = new AMapLocationClientOption();
        mLocationClient.setLocationListener(aMapLocationListener);
        
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        
        mLocationOption.setInterval(30000);
        mLocationClient.setLocationOption(mLocationOption);


    }

    
    AMapLocationListener aMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            StringBuffer str = new StringBuffer();
            if (aMapLocation != null) {
                
                if (aMapLocation.getErrorCode() == 0) {
                    str.append("定位成功\n");
                    str.append("市" + aMapLocation.getCity());
                    
                    CurrentLocation.getInstance().setPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                    LocationModel locationModel = new LocationModel(aMapLocation);
                    CurrentLocation.getInstance().setCity(locationModel.getCity());
                    CurrentLocation.getInstance().setDistrict(locationModel.getDistrict());
                    CurrentLocation.getInstance().setLocationModel(locationModel);
                    sendMessage(locationModel);
                }
                else {
                    str.append("定位失败\n");
                    Log.e(TAG, "ErrorCode = " + aMapLocation.getErrorCode());
                }
            }
            else {
                
                str.append("定位失败" + "\n");
                str.append("错误码:" + aMapLocation.getErrorCode() + "\n");
                str.append("错误信息:" + aMapLocation.getErrorInfo() + "\n");
                str.append("错误描述:" + aMapLocation.getLocationDetail() + "\n");

            }
            Log.d(TAG, str.toString());
        }
    };

    
    private void sendMessage(LocationModel locationModel) {
        Intent i  = new Intent();
        i.putExtra("city", locationModel.getCity());
        i.putExtra("district", locationModel.getDistrict());
        i.putExtra("latitude", String.valueOf(locationModel.getLatitude()));
        i.putExtra("longitude", String.valueOf(locationModel.getLongitude()));
        i.setAction("com.xiaobo.locationservice");
        Log.d(TAG, "发送位置广播");
        sendBroadcast(i);
    }

    
    private void onCount() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                
                int duration = 30;
                Log.d(TAG, "定位服务将在" + duration + "秒后停止");
                try {
                    Thread.sleep(duration * 1000);
                    stopSelf();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
