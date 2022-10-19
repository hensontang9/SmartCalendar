package com.xiaobo.smartcalendar.activity.TestActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xiaobo.smartcalendar.R;
import com.xiaobo.smartcalendar.Service.HttpRequest.HttpRequestIntentService;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        loginTest();
    }
    private void loginTest() {
        Button button = findViewById(R.id.button_logintest);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = HttpRequestIntentService.newIntent(TestActivity.this);
                i.putExtra("REQUEST_TYPE", "LOGIN");
                startService(i);
            }
        });
    }
}