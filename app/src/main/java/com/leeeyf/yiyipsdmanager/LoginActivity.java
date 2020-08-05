package com.leeeyf.yiyipsdmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.leeeyf.yiyipsdmanager.Utils.RSAUtils;
import com.leeeyf.yiyipsdmanager.entity.LoginResult;
import com.leeeyf.yiyipsdmanager.entity.LoginUser;

import java.io.IOException;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;

import okhttp3.Call;
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
    private TextView gotoRegister;

    public static SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private RSAPublicKey publickey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initview();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            LoginResult rst;
            @Override
            public void onClick(View v) {

                Thread newThread1;
                newThread1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            publickey = getPublicKey();
                            Log.d("gongyao", ""+publickey );
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                newThread1.start();
                try {
                    newThread1.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

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
                } else{
                    Toast  t = Toast.makeText(LoginActivity.this,rst.getMsg(),Toast.LENGTH_SHORT);
                    t.show();

                    //saveLoginState();
                }
            }
        });

        gotoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterAcyivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right2left,R.anim.open_close);//实现从右向左滑出
            }
        });
    }

    private void initview(){
        username = findViewById(R.id.username_login);
        password = findViewById(R.id.password_login);
        loginBtn = findViewById(R.id.login_btn);
        gotoRegister = findViewById(R.id.goRegister);
    }

    private LoginResult user_auth() throws IOException {
        username_str = username.getText().toString();
        password_str = password.getText().toString();
        String rsa_password = null;
        try {
            rsa_password = RSAUtils.encryptByPublicKey(password_str, publickey);
            //利用公钥加密
        } catch (Exception e) {
            e.printStackTrace();
        }

        LoginUser user = new LoginUser(username_str,rsa_password);
        Log.d("rsapassword", rsa_password);
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

    // 存储用户登录状态
    private void saveLoginState(){
        editor = sharedPreferences.edit();
        editor.putBoolean("loginState",true);
        editor.commit();
    }

    //得到公钥
    private RSAPublicKey getPublicKey() throws IOException {
        RSAPublicKey publicKey = null;

        String url = "http://192.168.1.73:8081/user/getkey";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()  //默认为GET请求，可以不写
                .build();
        Call call = client.newCall(request);


        Response response = call.execute();
        String responseStr = response.body().string();
        HashMap<String,String> map = JSONObject.parseObject(responseStr, HashMap.class);
        String modulus = map.get("modulus");
        //String publicExponent = new BigInteger(String.valueOf(map.get("publicExponent")));
        String publicExponent = map.get("publicExponent");
        //这个地方我也奇怪，后端传的明明是String类型，过来后却分别变成BigInteger和Integer了

        publicKey = RSAUtils.getPublicKey(modulus, publicExponent);


        return publicKey;

    }



}
