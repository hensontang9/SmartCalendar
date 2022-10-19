package com.xiaobo.smartcalendar.activity.LoginActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xiaobo.smartcalendar.Public.ProfileManager;
import com.xiaobo.smartcalendar.R;
import com.xiaobo.smartcalendar.activity.RegisterActivity.RegisterActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.xiaobo.smartcalendar.Public.PublicVaribale.LOGIN_SUCCESS;
import static com.xiaobo.smartcalendar.Public.PublicVaribale.LOGIN_URL;

public class LoginActivity extends AppCompatActivity {

    EditText usernameEditText;
    EditText passwordEditText;
    Button loginButton;
    Button forgetPosswordButton;
    Button registerButton;

    String username;
    String password;

    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent i = getIntent();
        Bundle userInfo = i.getExtras();
        if (userInfo != null) {
            try {
                username = userInfo.getString("username");
                password = userInfo.getString("password");
            }
            catch (Exception e) {
                Log.e("LoginActivity", "获取用户信息错误");
            }
        }

        token = ProfileManager.get(getApplicationContext()).getToken();

        usernameEditText = findViewById(R.id.username_login);
        passwordEditText = findViewById(R.id.password_login);
        loginButton = findViewById(R.id.button_login);
        forgetPosswordButton = findViewById(R.id.button_forget_password);
        registerButton = findViewById(R.id.button_register_on_loginactivity);

        usernameEditText.setText("username001");
        passwordEditText.setText("123456");

        if (!(username == null)) {
            usernameEditText.setText(username);
        }
        if (!(password == null)) {
            passwordEditText.setText(password);
        }

        username = usernameEditText.getText().toString();
        password = passwordEditText.getText().toString();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("LoginActivity", "用户点击了登录按钮");
                Log.d("LoginActivity", "username is " + username + ", password is " + password);
                if (usernameEditText.toString().length() == 0 || passwordEditText.toString().length() == 0) {
                    Toast.makeText(LoginActivity.this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
                }
                else {
                    String login_username = usernameEditText.getText().toString();
                    String login_password = passwordEditText.getText().toString();
                    String request = UserLogin(LOGIN_URL, login_username, login_password);
                }
            }
        });

        forgetPosswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("LoginActivity", "用户点击了忘记密码按钮");
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("LoginActivity", "用户点击了注册按钮");
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                i.putExtra("username", usernameEditText.getText().toString());
                startActivityForResult(i, 1003);
            }
        });
    }

    private void AfterLogin(String request) {
        
        ProfileManager.get(getApplicationContext()).setToken(token);
        Looper.prepare();
        Toast.makeText(LoginActivity.this, request, Toast.LENGTH_SHORT).show();
        Looper.loop();
    }

    
    private String UserLogin(String url, String username, String password) {
        String action = "login";
        String login_mode = "login_verify_use_password";
        final StringBuffer state = new StringBuffer();

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("action", action)
                .add("login_mode", login_mode)
                .add("username", username)
                .add("password", password)
                .build();

        
        final LoginModel[] model = {null};

        final Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Call call = client.newCall(request);;
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("用户登陆失败", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result= response.body().string();
                Gson gson = new Gson();
                model[0] = gson.fromJson(result, LoginModel.class);
                if (response.isSuccessful()) {
                    Log.d("登陆成功", "response ----->" + result);
                    state.append("josn请求码 -> " + response.code() + "\n");
                    state.append("登陆返回结果 ->" + model[0].getMsg() + "\n");
                    token = model[0].getToken();
                    
                    ProfileManager.get(getApplicationContext()).setToken(token);

                    String state = LOGIN_SUCCESS;
                    
                    sendMessage(result, state);

                    finish();
                }
                else {
                    Log.e("登陆错误 -> ", result);
                    state.append(model[0].getMsg());
                }
                AfterLogin(model[0].getMsg());
            }
        });

        return state.toString();
    }

    
    private void sendMessage(String str, String state) {
        Intent i = new Intent();
        i.setAction("com.login_state");
        i.putExtra("massage", str);
        i.putExtra("state", state);

        this.sendBroadcast(i);
    }
}