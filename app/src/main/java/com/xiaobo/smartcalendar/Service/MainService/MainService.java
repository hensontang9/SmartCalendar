package com.xiaobo.smartcalendar.Service.MainService;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.xiaobo.smartcalendar.Model.Contradiction.MyTemporalInconsistency;
import com.xiaobo.smartcalendar.Model.Contradiction.MyTemporalInconsistencyManager;
import com.xiaobo.smartcalendar.Model.Events.TempEventManager;
import com.xiaobo.smartcalendar.Public.PublicVaribale;
import com.xiaobo.smartcalendar.Service.HttpRequest.HttpRequestIntentService;

import java.util.ArrayList;
import java.util.List;

public class MainService extends Service {

    private static final String TAG = "MainService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "主服务创建");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "主服务启动");

        


        saveEvents();

        return super.onStartCommand(intent, flags, startId);
    }

    
    private void sortConflict() {
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000*5);     
                Log.d(TAG, "检测了一次所有事件的冲突");
                MyTemporalInconsistencyManager.getInstance().allConflictDetection();
                List<MyTemporalInconsistency> inconList = MyTemporalInconsistencyManager.getInstance().getAllMyIncons();
                if (inconList.size() >= 1) {
                    for (MyTemporalInconsistency myIncon : inconList) {
                        Log.d(TAG, "准备上传冲突:" + myIncon.getmID());
                        Intent i = HttpRequestIntentService.newIntent(MainService.this);
                        Bundle httpBundle = new Bundle();
                        httpBundle.putString("REQUEST_TYPE", String.valueOf(PublicVaribale.RequestType.UP_INCON));
                        httpBundle.putString("INCON_ID", myIncon.getmID());
                        i.putExtras(httpBundle);
                        startService(i);
                    }
                }
            }
        };
        Thread sortConflictThread = new Thread(runnable, "MainService");

        sortConflictThread.start();
    }

    
    private void saveEvents() {
        Handler handler = new Handler();
        Runnable runnable = new Runnable(){
            @Override
            public void run() {
                handler.postDelayed(this, 1000*60);     
                Log.d(TAG, "将没有计算路径的事件保存到数据库中(或者是计算失败的)" + "\n思考: 是否应该这么处理?");

                Log.d(TAG, "不再定时保存事件");
            }
        };
        Thread saveEventThread = new Thread(runnable, "MainService");
        saveEventThread.start();
    }

    
    private class CalRouteReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "接受到通知");
            Bundle bundle = intent.getExtras();
            Log.d(TAG, "获取到路径计算结果" + bundle.getString("message"));
        }
    }
    private void registerCalRouteReceiver() {
        CalRouteReceiver calRouteReceiver = new CalRouteReceiver();
        IntentFilter filter = new IntentFilter("com.calroute_message");
        registerReceiver(calRouteReceiver, filter);
    }

   @Override
    public void onDestroy() {
        Log.d(TAG, "主服务停止");
        super.onDestroy();
    }
}
