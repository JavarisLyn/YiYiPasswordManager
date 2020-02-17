package com.leeeyf.yiyipsdmanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.codersun.fingerprintcompat.FingerManager;


import static com.leeeyf.yiyipsdmanager.MainActivity.sharedPreferences;

public class Splash extends AppCompatActivity {
    private boolean ifFirst=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //隐藏顶部标题栏
        // getSupportActionBar().hide();
        handler.sendEmptyMessageDelayed(0, 2000);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //实现页面的跳转
            ifFirst=sharedPreferences.getBoolean("iffirst",true);
            Log.d("wacher", "handleMessage: "+ifFirst);
            if(ifFirst==true)
            {
                Log.d("wacher", "handleMessage: "+ifFirst);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("iffirst",false);
                editor.commit();
                Intent intent = new Intent(Splash.this,WelcomeActivity.class);
                startActivity(intent);
            }
            else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M&&sharedPreferences.getBoolean("fingerlock",false)) {

                    switch (FingerManager.checkSupport(Splash.this)) {
                        case DEVICE_UNSUPPORTED:
                            Intent intent = new Intent(Splash.this, MainActivity.class);
                            startActivity(intent);
                            break;
                        case SUPPORT_WITHOUT_DATA:
                            intent = new Intent(Splash.this, MainActivity.class);
                            startActivity(intent);
                            break;
                        case SUPPORT:
                            intent = new Intent(Splash.this, StartActivity.class);
                            startActivity(intent);
                            break;
                    }
                    finish();
                    super.handleMessage(msg);
                }
                else{
                    Intent intent = new Intent(Splash.this, MainActivity.class);
                    startActivity(intent);
                }
            }





        }
    };
}
