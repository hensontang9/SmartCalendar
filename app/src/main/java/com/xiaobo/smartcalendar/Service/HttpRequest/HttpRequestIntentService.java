package com.xiaobo.smartcalendar.Service.HttpRequest;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.IntentService;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.xiaobo.smartcalendar.Public.PublicVaribale.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xiaobo.smartcalendar.MainActivity;
import com.xiaobo.smartcalendar.Model.Contradiction.MyTemporalInconsistency;
import com.xiaobo.smartcalendar.Model.Contradiction.MyTemporalInconsistencyManager;
import com.xiaobo.smartcalendar.Model.Events.MyEvent;
import com.xiaobo.smartcalendar.Model.Events.MyEventManager;
import com.xiaobo.smartcalendar.Model.Response.MyEventResponse;
import com.xiaobo.smartcalendar.Model.Response.MyTemporalInconsistencyResponse;
import com.xiaobo.smartcalendar.MyDialog.MyDialogManager;
import com.xiaobo.smartcalendar.MyDialog.MyDialogOfInconResponse;
import com.xiaobo.smartcalendar.Public.ProfileManager;
import com.xiaobo.smartcalendar.Public.PublicVaribale;
import com.xiaobo.smartcalendar.Public.VaribaleManager;
import com.xiaobo.smartcalendar.R;
import com.xiaobo.smartcalendar.activity.AddEventActivity.AddEventActivity;
import com.xiaobo.smartcalendar.activity.AdjustActivity.AdjustActivity;
import com.xiaobo.smartcalendar.activity.LoginActivity.LoginActivity;
import com.xiaobo.smartcalendar.activity.SetingActivity.SetingActivity;


public class HttpRequestIntentService extends IntentService {

    private static final String TAG = "HttpRequestService";
    private String requestMessage;
    private String token = new String("token=" + ProfileManager.get(this).getToken());
    
    private RequestType mRequestType;
    private OkHttpClient client = new OkHttpClient();

    public static Intent newIntent(Context context) {

        return new Intent(context, HttpRequestIntentService.class);
    }
    public HttpRequestIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.i(TAG, "Received an intent: " + intent);
        try {
            mRequestType = RequestType.valueOf(intent.getStringExtra("REQUEST_TYPE"));
        }catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
        
        if (mRequestType != null) {
            switch (mRequestType) {
                case LOGIN:
                    Log.d(TAG, "发送登陆请求");
                    break;
                case REGISTER:
                    Log.d(TAG, "发送注册请求");
                    break;
                case UP_EVENT:
                    Log.d(TAG, "上传事件请求");
                    try {
                        UUID eventId = UUID.fromString(intent.getStringExtra("EVENT_ID"));
                        uploadEvent(eventId);
                    }
                    catch (Exception e) {

                    }
                    break;
                case REVISE_EVENT:
                    Log.d(TAG, "修改事件请求");
                    try {
                        UUID eventId = UUID.fromString(intent.getStringExtra("EVENT_ID"));
                        Log.d(TAG, "修改一个事件");
                        Log.d(TAG, "要修改的事件ID" + eventId.toString());
                        reviseEvent(eventId);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        UUID event1Id = UUID.fromString(intent.getStringExtra("EVENT1_ID"));
                        UUID event2Id = UUID.fromString(intent.getStringExtra("EVENT2_ID"));
                        Log.d(TAG, "修改两个个事件");
                        Log.d(TAG, "要修改的事件1ID" + event1Id.toString());
                        Log.d(TAG, "要修改的事件2ID" + event2Id.toString());
                        reviseEvent(event1Id);
                        reviseEvent(event2Id);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case DELETE_EVENT:
                    Log.d(TAG, "删除事件请求");
                    try {
                        UUID eventID = UUID.fromString(intent.getStringExtra("EVENT_ID"));
                        Log.d(TAG, "删除事件:" + eventID.toString());
                        deleteEvent(eventID);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case UP_INCON:
                    Log.d(TAG, "上传冲突请求");
                    try {
                        String inconID = String.valueOf(intent.getStringExtra("INCON_ID"));
                        uploadIncon(inconID);
                    }
                    catch (Exception e) {

                    }
                    break;
                case REVISE_TI:
                    Log.d(TAG, "修改冲突请求");
                    try {
                        String inconID = String.valueOf(intent.getStringExtra("INCON_ID"));
                        String e1_sT = String.valueOf(intent.getStringExtra("e1_sT"));
                        String e1_eT = String.valueOf(intent.getStringExtra("e1_eT"));
                        String e2_sT = String.valueOf(intent.getStringExtra("e2_sT"));
                        String e2_eT = String.valueOf(intent.getStringExtra("e2_eT"));
                        String handled = String.valueOf(intent.getStringExtra("handled"));

                        reviseIncon(inconID, e1_sT, e1_eT, e2_sT, e2_eT, handled);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case UP_CSV:
                    Log.d(TAG, "上传事件列表请求");
                    break;
                default:
                    Log.e(TAG, "未传入上传参数 或 上传参数错误");
                    break;
            }
        }
        else {
            Log.e(TAG,"请求类型为null");
        }

        

    }

    
    private void uploadEvent(UUID eventId) {
        String token = ProfileManager.get(getApplicationContext()).getToken();
        if (token.length() == 0) {
            Toast.makeText(getApplicationContext(), "您还没有登陆", Toast.LENGTH_SHORT).show();

            VaribaleManager.getInstance().addEventUUID(eventId);

            Intent i = new Intent(this, LoginActivity.class);
            i.addFlags(FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);

            return;
        }
        String url = upload_new_jsonURL + "token=" + ProfileManager.get(getApplicationContext()).getToken();
        Log.d(TAG, "上传事件的URL为:" + url);
        String json = String.valueOf(MyEventManager.getInstance().getJSONWithID(eventId));
        Log.d(TAG, "将上传的事件json->" + json);
        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), json);

        httpRequest(url, requestBody);
    }

    private void reviseEvent(UUID eventId) {
        String token = ProfileManager.get(getApplicationContext()).getToken();
        if (token.length() == 0) {
            Toast.makeText(getApplicationContext(), "您还没有登陆", Toast.LENGTH_SHORT).show();

            VaribaleManager.getInstance().addEventUUID(eventId);

            Intent i = new Intent(this, LoginActivity.class);
            i.addFlags(FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);

            return;
        }
        String url = revise_eventURL + "token=" + ProfileManager.get(getApplicationContext()).getToken();
        Log.d(TAG, "修改事件的URL为:" + url);
        String json = String.valueOf(MyEventManager.getInstance().getJSONWithID(eventId));
        Log.d(TAG, "将修改的事件json->" + json);
        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        patchRequest(url, requestBody);
    }

    
    private void deleteEvent(UUID eventID) {
        String token = ProfileManager.get(getApplicationContext()).getToken();
        if (token.length() == 0) {
            Toast.makeText(getApplicationContext(), "您还没有登陆", Toast.LENGTH_SHORT).show();

            VaribaleManager.getInstance().addEventUUID(eventID);

            Intent i = new Intent(this, LoginActivity.class);
            i.addFlags(FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);

            return;
        }
        String url = delete_eventURL + "token=" + ProfileManager.get(getApplicationContext()).getToken();
        Log.d(TAG, "修改事件的URL为:" + url);
        String json = String.valueOf(eventID);
        Log.d(TAG, "将修改的事件json->" + json);
        FormBody body = new FormBody.Builder().add("uuid", json).build();
        deleteRequest(url, body);
    }

    
    private void uploadIncon(String inconID) {
        String token = ProfileManager.get(getApplicationContext()).getToken();
        if (token.length() == 0) {
            Toast.makeText(getApplicationContext(), "您还没有登陆", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, LoginActivity.class);
            i.addFlags(FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);

            return;
        }
        String url = upload_new_TI + "token=" + ProfileManager.get(getApplicationContext()).getToken();
        Log.d(TAG, "上传冲突的URL为:" + url);
        String json = String.valueOf(MyTemporalInconsistencyManager.getInstance().getJSONWithID(inconID));
        Log.d(TAG, "将上传的冲突json->" + json);
        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), json);

        httpRequest(url, requestBody);
    }

    
    private void reviseIncon(String inconID, String e1_sT, String e1_eT, String e2_sT, String e2_eT, String handled) {

        String token = ProfileManager.get(getApplicationContext()).getToken();
        if (token.length() == 0) {
            Toast.makeText(getApplicationContext(), "您还没有登陆", Toast.LENGTH_SHORT).show();

            Intent i = new Intent(this, LoginActivity.class);
            i.addFlags(FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);

            return;
        }

        String url = revise_inconURL + "token=" + ProfileManager.get(getApplicationContext()).getToken();
        Log.d(TAG, "修改冲突的URL为:" + url);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uuid", inconID);
            jsonObject.put("input_e1_time_s", e1_sT);
            jsonObject.put("input_e1_time_e", e1_eT);
            jsonObject.put("input_e2_time_s", e2_sT);
            jsonObject.put("input_e2_time_e", e2_eT);
            jsonObject.put("handled", handled);
        }
        catch (Exception e) {
            Log.e(TAG, "冲突json编码失败");
            e.printStackTrace();
        }
        String json = String.valueOf(jsonObject);
        Log.d(TAG, "将修改的冲突json->" + json);
        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        patchRequest(url, requestBody);
    }
    private void reviseInconWithAcceptSysAdvice(String inconID) {
        String token = ProfileManager.get(getApplicationContext()).getToken();
        if (token.length() == 0) {
            Toast.makeText(getApplicationContext(), "您还没有登陆", Toast.LENGTH_SHORT).show();

            Intent i = new Intent(this, LoginActivity.class);
            i.addFlags(FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);

            return;
        }

        String url = revise_inconURL + "token=" + ProfileManager.get(getApplicationContext()).getToken();
        Log.d(TAG, "修改冲突的URL为:" + url);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uuid", inconID);
            jsonObject.put("handled", "true");
        }
        catch (Exception e) {
            Log.e(TAG, "冲突json编码失败");
            e.printStackTrace();
        }
        String json = String.valueOf(jsonObject);
        Log.d(TAG, "将修改的冲突json->" + json);
        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        patchRequest(url, requestBody);
    }

    
    
    

    
    private void httpRequest(String url, RequestBody requestBody) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)      
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)            
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)          
                .build();
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
                        Log.e(TAG, "错误详情->" + e.toString());
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
                            Log.d("HttpIntentService", "返回结果 -> " + result);
                            

                            popupTips(result);


                            sendMessage(result);
                        }
                    });
                }
                else {
                    Log.e(TAG, "服务器返回错误");
                    Log.e(TAG, result);
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), R.string.service_error, Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }

            }
        });
    }

    
    private void patchRequest(String url, RequestBody requestBody) {
        OkHttpClient client = new OkHttpClient();
        if (requestBody == null) {
            return;
        }
        Request request = new Request.Builder()
                .url(url)
                .patch(requestBody)
                .build();



        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    String result = patchURL(url, requestBody);
                    Log.d(TAG, "pacth请求返回" + result);
                }catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.start();


    }
    String patchURL(String url, RequestBody mRequest) throws IOException {
        Request request = new Request.Builder().url(url).patch(mRequest).build();
        try (Response response = client.newCall(request).execute()){
            return response.body().string();
        }
    }

    
    private void deleteRequest(String url, FormBody body) {
        OkHttpClient client = new OkHttpClient();
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    String result = deleteURL(url, body);
                    Log.d("TAG","delete请求返回" + result);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    String deleteURL(String url, FormBody body) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .delete(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
    
    private void popupTips(String message) {
        try {

            if (mRequestType == RequestType.UP_INCON) {
                MyTemporalInconsistencyResponse myTemporalInconsistencyResponse = JSON.parseObject(message, MyTemporalInconsistencyResponse.class);
                
                Log.d(TAG, "弹出冲突Dialog");
                MyDialogOfInconResponse.Builder builder = new MyDialogOfInconResponse.Builder(this);
                MyDialogOfInconResponse myDialogOfInconResponse = builder.setData(myTemporalInconsistencyResponse)
                        .ignore(new MyDialogOfInconResponse.OnIgnoreClickListener() {
                            @Override
                            public void agree() {
                                Log.d("MyDialogOfInconResponse", "点击了同意按钮");
                                
                                MyTemporalInconsistency myTemporalInconsistency = MyTemporalInconsistencyManager.getInstance().getMyIncon(myTemporalInconsistencyResponse.getData().getUuid());
                                myTemporalInconsistency.set_system_advice(myTemporalInconsistencyResponse.getData().getSysActionType1(), myTemporalInconsistencyResponse.getData().getSysTimeAction1(),
                                                            myTemporalInconsistencyResponse.getData().getSysActionType2(), myTemporalInconsistencyResponse.getData().getSysTimeAction2(),
                                                            myTemporalInconsistencyResponse.getData().getSysActionType3(), myTemporalInconsistencyResponse.getData().getSysTimeAction3(),
                                                            myTemporalInconsistencyResponse.getData().getSysActionType4(), myTemporalInconsistencyResponse.getData().getSysTimeAction4());
                                myTemporalInconsistency.accept_sys_advice();
                                
                                reviseInconWithAcceptSysAdvice(myTemporalInconsistencyResponse.getData().getUuid());
                            }
                        })
                        .edit(new MyDialogOfInconResponse.OnEditClickListener() {
                            @Override
                            public void edit() {
                                Log.d("MyDialogOfInconResponse", "点击了修改按钮");
                                
                                Intent i = new Intent(HttpRequestIntentService.this, AdjustActivity.class);
                                i.addFlags(FLAG_ACTIVITY_NEW_TASK);
                                Bundle bundle = new Bundle();
                                bundle.putString("InconID", myTemporalInconsistencyResponse.getData().getUuid());
                                i.putExtras(bundle);
                                MyTemporalInconsistencyManager.getInstance().getMyIncon(myTemporalInconsistencyResponse.getData().getUuid()).setHandled(MyTemporalInconsistency.Handled.postponed);

                                HttpRequestIntentService.this.startActivity(i);

                            }
                        }).create();
                myDialogOfInconResponse.show();
            }
            if (mRequestType == RequestType.UP_EVENT) {
                
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                builder.setMessage(message);
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


                
                MyEventResponse myEventResponse = JSON.parseObject(message, MyEventResponse.class);
                MyEvent event = MyEventManager.getInstance().getMyEvent(UUID.fromString(myEventResponse.getData().getUuid()));
                List<MyTemporalInconsistency> myTemporalInconsistencyList = MyTemporalInconsistencyManager.getInstance().conflictDetectionWithEvent(event);
                if (myTemporalInconsistencyList.size() > 0) {
                    for (MyTemporalInconsistency myIncon : myTemporalInconsistencyList) {
                        Log.d(TAG, "当前事件" + event.getmId() + "有冲突 准备上传");
                        Log.d(TAG, myIncon.getmID());
                        mRequestType = RequestType.UP_INCON;
                        uploadIncon(myIncon.getmID());
                    }
                }
            }



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
