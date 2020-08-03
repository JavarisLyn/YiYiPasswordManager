package com.leeeyf.yiyipsdmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
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
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private String username_str;
    private String password_str;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initview();


        loginBtn.setOnClickListener(new View.OnClickListener() {
            LoginResult rst;
            @Override
            public void onClick(View v) {
                Thread newThread;

                newThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            rst = user_auth();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                newThread.start();
                try {
                    newThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                System.out.println("rst "+rst.getMsg());
                if(rst.isState()){
                    Toast  t = Toast.makeText(LoginActivity.this,rst.getMsg(),Toast.LENGTH_SHORT);
                    t.show();
                }

                else{
                    Toast  t = Toast.makeText(LoginActivity.this,rst.getMsg(),Toast.LENGTH_SHORT);
                    t.show();
                }


            }


        });



    }

    private void initview(){
        username = findViewById(R.id.username_login);
        password = findViewById(R.id.password_login);
        loginBtn = findViewById(R.id.login_btn);
    }

    private LoginResult user_auth() throws IOException {
        username_str = username.getText().toString();
        password_str = password.getText().toString();
        LoginUser user = new LoginUser(username_str,password_str);
        String userStr = JSON.toJSONString(user);

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//        JSONObject json = new JSONObject();
//        json.put("user",user);
        String json = new Gson().toJson(user);

        OkHttpClient mOkHttpClient = new OkHttpClient();
        //FormBody.Builder builder = new FormBody.Builder();
        RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));

        //builder.add(json);
        final Request request = new Request.Builder()
                .url("http://192.168.1.73:8081/user/login")
                //.url("http://www.baidu.com")
                .post(requestBody )
                .build();
        Call call = mOkHttpClient.newCall(request);
        final LoginResult result = new LoginResult();

        okhttp3.Response temp = call.execute();
        if(temp.isSuccessful()){
            System.out.println("call测试成功");
            String responseStr = temp.body().string();

            LoginResult loginResult = JSONObject.parseObject(responseStr, LoginResult.class);
            result.setState(loginResult.isState());
            result.setMsg(loginResult.getMsg());
        }
        else{
            System.out.println("call测试失败");
            result.setState(false);
            //System.out.println("call测试失败原因"+e);
        }

        //异步
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                System.out.println("call测试成功");
//                String responseStr = response.body().string();
//
//                 LoginResult loginResult = JSONObject.parseObject(responseStr, LoginResult.class);
//                 result.setState(loginResult.isState());
//                 result.setMsg(loginResult.getMsg());
//
////                if(result.isState()){
////                    Toast  t = Toast.makeText(LoginActivity.this,result.getMsg(),Toast.LENGTH_SHORT);
////                    t.show();
////                }
////
////                else{
////                    Toast  t = Toast.makeText(LoginActivity.this,result.getMsg(),Toast.LENGTH_SHORT);
////                    t.show();
////                }
//
//
//            }
//
//        });

        return result;
    }

}
