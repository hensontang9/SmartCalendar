package com.xiaobo.smartcalendar.Service.UploadQueue;

import static com.xiaobo.smartcalendar.Public.PublicVaribale.upload_new_jsonURL;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.xiaobo.smartcalendar.Model.Events.MyEvent;
import com.xiaobo.smartcalendar.Model.Events.MyEventManager;
import com.xiaobo.smartcalendar.Model.Events.TempEventManager;

import java.io.IOException;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class UploadQueue extends JobIntentService {
    static final String TAG = "UploadQueueJobIntentService";
    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.d(TAG, Thread.currentThread().getId() + "");

        Log.d(TAG, "Received an intent: " + intent);
        
        try {
            Bundle bundle = intent.getExtras();
            UUID uuid = UUID.fromString(bundle.getString("eventID"));
            TempEventManager.TempEvent event = (TempEventManager.TempEvent)MyEventManager.getInstance().getMyEvent(uuid);
            while (event.isCompleteEdit() && event.isCompleteCalDriveRoute()) {
                
                String json = event.toString();
                RequestBody body = FormBody.create(MediaType.parse("application/json; charset=utf-8"), json);
                httpRequest(upload_new_jsonURL, body);

            }

        }
        catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    
    private void httpRequest(String url, RequestBody requestBody) {
        OkHttpClient client = new OkHttpClient();
        if (requestBody == null) {
            return;
        }
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if (response.isSuccessful()) {
                    Log.i(TAG, "请求成功");
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "请求成功", Toast.LENGTH_SHORT).show();
                            

                            popupTips();

                            Log.d("HttpIntentService", "返回结果 -> " + result);
                            sendMessage(result);
                        }
                    });
                }
                else {
                    Log.e(TAG, "服务器返回错误");
                    Log.e(TAG, result);
                }

            }
        });
    }

    
    private void popupTips() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
            builder.setMessage("test");
            builder.setNegativeButton("取消", null);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            Log.i("HttpIntentService", "准备弹出Dialog");
            final Dialog dialog = builder.create();
            
            Window window = dialog.getWindow();
            if (window != null) {
                WindowManager.LayoutParams attributes = window.getAttributes();
                if (attributes != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        attributes.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
                    }
                    else {
                        attributes.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
                    }
                }
                window.setAttributes(attributes);
            }
            dialog.show();

        }
        catch (Exception e) {
            Log.e("HttpIntentService", "抛出了异常" + e);
        }
    }

    
    private void sendMessage(String str) {
        Intent i = new Intent();
        i.setAction("com.http_request_message");
        i.putExtra("message", str);
        this.sendBroadcast(i);
    }
}
