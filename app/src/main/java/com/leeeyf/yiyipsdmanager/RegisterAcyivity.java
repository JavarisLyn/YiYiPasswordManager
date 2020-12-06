package com.leeeyf.yiyipsdmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.leeeyf.yiyipsdmanager.entity.LoginResult;
import com.leeeyf.yiyipsdmanager.entity.LoginUser;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class RegisterAcyivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private EditText password_confirm;
    private Button register;
    private LoginResult rst = new LoginResult();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();



        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(validation()){
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                rst = register();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    thread.start();
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(rst.isState()){
                        Toast t = Toast.makeText(RegisterAcyivity.this,"注册成功",Toast.LENGTH_SHORT);
                        t.show();
                        Intent intent = new Intent(RegisterAcyivity.this,LoginActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.left2right,R.anim.open_close);//实现从左向右滑出
                    }
                    else{
                        Toast t = Toast.makeText(RegisterAcyivity.this,rst.getMsg(),Toast.LENGTH_SHORT);
                        t.show();
                    }

                }
            }
        });
    }

    private LoginResult register() throws IOException {
        LoginUser registerUser = new LoginUser(username.getText().toString(),password.getText().toString());
        String userStr = JSON.toJSONString(registerUser);

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        String json = new Gson().toJson(registerUser);

        OkHttpClient mOkHttpClient = new OkHttpClient();
        //FormBody.Builder builder = new FormBody.Builder();
        RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));

        //builder.add(json);
        final Request request = new Request.Builder()
                .url("http://192.144.143.218:8082/user/register")
                //.url("http://www.baidu.com")
                .post(requestBody )
                .build();
        Call call = mOkHttpClient.newCall(request);


        okhttp3.Response temp = call.execute();
        if(temp.isSuccessful()){
            String responseStr = temp.body().string();

            LoginResult loginResult = JSONObject.parseObject(responseStr, LoginResult.class);
            rst.setState(loginResult.isState());
            rst.setMsg(loginResult.getMsg());
        }
        else{
            String responseStr = temp.body().string();

            LoginResult loginResult = JSONObject.parseObject(responseStr, LoginResult.class);
            rst.setState(false);
            rst.setMsg(loginResult.getMsg());
        }
        return rst;
    }

    private boolean validation(){
        if(!password.getText().toString().equals(password_confirm.getText().toString())){
            Toast t = Toast.makeText(RegisterAcyivity.this,"两次密码不一致",Toast.LENGTH_SHORT);
            t.show();
            return false;
        }
        else{
            return true;
        }
    }

    private void initView(){
        username = findViewById(R.id.username_register);
        password = findViewById(R.id.password_register);
        password_confirm = findViewById(R.id.password_confirm);
        register = findViewById(R.id.register_btn);
    }

}
