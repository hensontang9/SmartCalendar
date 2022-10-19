package com.xiaobo.smartcalendar.activity.RegisterActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xiaobo.smartcalendar.Public.VerifyCodeButton;
import com.xiaobo.smartcalendar.R;
import com.xiaobo.smartcalendar.activity.LoginActivity.LoginActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.xiaobo.smartcalendar.Public.PublicVaribale.REGISTER_URL;

public class RegisterActivity extends AppCompatActivity {

    private Button mButtonRegister;
    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private EditText mConfirmPasswordEditText;
    private EditText mUserPhoneNumberEditText;
    private EditText mVerificationCodeEditText;
    private VerifyCodeButton mButtonSendVerificationCode;

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Intent i = getIntent();
        Bundle userInfo = i.getExtras();
        if (userInfo != null) {
            try {
                username = userInfo.getString("username");
            }
            catch (Exception e) {
                Log.e("LoginActivity", "获取用户信息错误");
            }
        }

        mButtonRegister = findViewById(R.id.button_register);
        mUsernameEditText = findViewById(R.id.username_register);
        mPasswordEditText = findViewById(R.id.password_register);
        mConfirmPasswordEditText = findViewById(R.id.confirmpassword_register);
        mUserPhoneNumberEditText = findViewById(R.id.userphonenumber_register);
        mVerificationCodeEditText = findViewById(R.id.vcode_register);
        mButtonSendVerificationCode = findViewById(R.id.button_vcode);

        
        mUsernameEditText.setText("username001");
        mUserPhoneNumberEditText.setText("13711112222");
        mPasswordEditText.setText("123456");

        if (!(username == null)) {
            mUsernameEditText.setText(username);
        }

        mButtonSendVerificationCode.setOnClickListener(new VerifyCodeButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                String phone = mUserPhoneNumberEditText.getText().toString();
                if(!TextUtils.isEmpty(phone)){
                    Toast.makeText(RegisterActivity.this, "执行发送验证码操作", Toast.LENGTH_SHORT).show();
                    mButtonSendVerificationCode.start();
                }else{
                    Toast.makeText(RegisterActivity.this, "请输入手机号", Toast.LENGTH_SHORT).show();

                }
            }
        });

        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("RegisterActivity", "开始注册操作");
                String username = mUsernameEditText.getText().toString();
                String phonenumber = mUserPhoneNumberEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();
                String verify_code = mVerificationCodeEditText.getText().toString();






                String request = UserRegister(REGISTER_URL, username, phonenumber, password, "350452");

            }
        });
    }

    private void AfterRegister(String request) {
        Looper.prepare();
        Toast.makeText(RegisterActivity.this, request, Toast.LENGTH_SHORT).show();









        Intent i = new Intent();
        i.setClass(RegisterActivity.this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        
        i.putExtra("username", mUsernameEditText.getText().toString());
        i.putExtra("password", mPasswordEditText.getText().toString());

        setResult(1004, i);
        startActivity(i);

        this.finish();
        Looper.loop();



    }

    
    private String UserRegister(String url, String username, String phonenumber, String password, String verify_code) {
        String action = "register";
        final StringBuffer state = new StringBuffer();

        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("action", action)
                .add("phone", phonenumber)
                .add("username", username)
                .add("password", password)
                .add("verify_code", verify_code)
                .build();

        
        final RegisterModel[] model = {null};

        final Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        Call call = client.newCall(request);;
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("用户注册失败", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Gson gson = new Gson();
                model[0] = gson.fromJson(result, RegisterModel.class);
                if (response.isSuccessful()) {
                    Log.d("注册成功", "response ----->" + result);
                    state.append("josn请求码 -> " + response.code() + "\n");
                    state.append("注册返回结果 ->" + model[0].getMsg() + "\n");
                }
                else {
                    Log.e("发生错误", result);
                    state.append(result);
                }
                AfterRegister(model[0].getMsg());
            }
        });

        return state.toString();
    }
}