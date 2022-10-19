package com.xiaobo.smartcalendar.activity.SetingActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.xiaobo.smartcalendar.MainActivity;
import com.xiaobo.smartcalendar.Model.Contradiction.MyTemporalInconsistency;
import com.xiaobo.smartcalendar.Model.Contradiction.MyTemporalInconsistencyManager;
import com.xiaobo.smartcalendar.Model.Events.MyEvent;
import com.xiaobo.smartcalendar.Model.Events.MyEventManager;
import com.xiaobo.smartcalendar.Public.ProfileManager;
import com.xiaobo.smartcalendar.Public.PublicVaribale;
import com.xiaobo.smartcalendar.R;
import com.xiaobo.smartcalendar.Service.HttpRequest.HttpRequestIntentService;
import com.xiaobo.smartcalendar.activity.LoginActivity.LoginActivity;
import com.xiaobo.smartcalendar.activity.TestActivity.TableActivity;
import com.xiaobo.smartcalendar.activity.TestActivity.TestActivity;

import java.util.List;
import java.util.Locale;

public class SetingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seting);

        toTestActivity();
        setLanguage();
        login();
        showAllEvents();
        showAllTemporals();
        uploadAEvent();
        uploadAllEvent();
        uploadAIncon();
    }

    private void toTestActivity() {
        Button button = findViewById(R.id.to_testactivity);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SetingActivity.this, TestActivity.class);
                startActivity(i);
            }
        });
    }

    
    private void login() {
        Button loginButton = findViewById(R.id.login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SetingActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }

    
    private void setLanguage() {
        final int language = ProfileManager.get(this).getLanguage();
        Button changeLanguageButton = findViewById(R.id.change_language);
        changeLanguageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] strings = {"跟随系统", "简体中文", "English"};
                
                int selected = getSharedPreferences("language", Context.MODE_PRIVATE).getInt("language", -1);
                if (selected == 1) {
                    String defaultLanguage = Locale.getDefault().toString();
                    if (defaultLanguage.equals(Locale.CHINESE.toString()) || defaultLanguage.equals(Locale.SIMPLIFIED_CHINESE.toString())
                            || defaultLanguage.equals(Locale.TRADITIONAL_CHINESE.toString())) {
                        selected = 1;
                    } else {
                        selected = 2;
                    }
                }
                new AlertDialog.Builder(SetingActivity.this)
                        .setSingleChoiceItems(strings, language,
                                new DialogInterface.OnClickListener() {
                                    
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {




                                        ProfileManager.get(SetingActivity.this).setLanguage(i);
                                        dialogInterface.dismiss();
                                        
                                        finish();
                                        Intent intent = new Intent(SetingActivity.this, MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);



                                    }
                                })
                        .create()
                        .show();
            }
        });
    }



    
    private void showAllEvents() {
        Button showAllEventsButton = findViewById(R.id.show_all_events);
        showAllEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<MyEvent> list = MyEventManager.getInstance().getAllEvents();
                int sum = 0;
                for (MyEvent e : list) {
                    Log.d("SetingActivity", ++sum + " " + e.getmId().toString());
                }

                Intent intent = new Intent(SetingActivity.this, TableActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("TABLE_TYPE", "MYEVENT");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    
    private void showAllTemporals() {
        Button showAllTemporalsButton = findViewById(R.id.show_all_temporals);
        showAllTemporalsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<MyTemporalInconsistency> list = MyTemporalInconsistencyManager.getInstance().getAllMyIncons();
                int sum = 0;
                for (MyTemporalInconsistency incon : list) {
                    Log.d("SetingActivity", ++sum + " " + incon.getmID().toString());
                }

                Intent intent = new Intent(SetingActivity.this, TableActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("TABLE_TYPE", "MYTEMPORALINCONSISTENCY");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    
    private void uploadAEvent() {
        Button upload = findViewById(R.id.upload_a_event);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = HttpRequestIntentService.newIntent(SetingActivity.this);
                Bundle bundle = new Bundle();
                bundle.putString("REQUEST_TYPE", String.valueOf(PublicVaribale.RequestType.UP_EVENT));
                bundle.putString("EVENT_ID", String.valueOf(MyEventManager.getInstance().getAllEvents().get(0).getmId()));
                i.putExtras(bundle);
                startService(i);
            }
        });
    }

    
    private void uploadAllEvent() {
        Button upload = findViewById(R.id.upload_all_events);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<MyEvent> myEvents = MyEventManager.getInstance().getAllEvents();
                for (int index = 0; index < myEvents.size(); index++) {
                    Intent i = HttpRequestIntentService.newIntent(SetingActivity.this);
                    Bundle bundle = new Bundle();
                    bundle.putString("REQUEST_TYPE", String.valueOf(PublicVaribale.RequestType.UP_EVENT));
                    bundle.putString("EVENT_ID", String.valueOf(myEvents.get(index).getmId()));
                    i.putExtras(bundle);
                    startService(i);
                }
            }
        });
    }

    
    private void uploadAIncon() {
        Button upload = findViewById(R.id.upload_a_incon);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = HttpRequestIntentService.newIntent(SetingActivity.this);
                Bundle bundle = new Bundle();
                bundle.putString("REQUEST_TYPE", String.valueOf(PublicVaribale.RequestType.UP_INCON));
                bundle.putString("INCON_ID", String.valueOf(MyTemporalInconsistencyManager.getInstance().getAllMyIncons().get(0).getmID()));
                i.putExtras(bundle);
                startService(i);
            }
        });
    }
}