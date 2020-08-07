package com.leeeyf.yiyipsdmanager.UploadData;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.leeeyf.yiyipsdmanager.entity.LoginResult;
import com.leeeyf.yiyipsdmanager.entity.LoginUser;
import com.leeeyf.yiyipsdmanager.entity.UserwithAccounts;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static com.leeeyf.yiyipsdmanager.LoginActivity.username_str;
import static com.leeeyf.yiyipsdmanager.MainActivity.Accounts;

public class UploadUserData {
    public static void uploadData(){
        Thread newThread;
        newThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    LoginUser user = new LoginUser(username_str,"psd");
                    UserwithAccounts userwithAccounts = new UserwithAccounts(user, Accounts);
                    String userStr = JSON.toJSONString(userwithAccounts);
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    String json = new Gson().toJson(userwithAccounts);
                    OkHttpClient mOkHttpClient = new OkHttpClient();
                    RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
                    final Request request = new Request.Builder()
                            .url("http://192.168.1.73:8081/user/saveUserData")
                            .post(requestBody )
                            .build();
                    Call call = mOkHttpClient.newCall(request);
                    final LoginResult result = new LoginResult();
                    okhttp3.Response temp = call.execute();
                    if(temp.isSuccessful()){
                        Log.d("uploadtestlyf", "success");
                    }
                    else{
                        Log.d("uploadtestlyf", "failed");
                    }
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
    }
}
