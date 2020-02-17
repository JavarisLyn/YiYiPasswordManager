package com.leeeyf.yiyipsdmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {

    private TextView startbtn;
    private TextView agreement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        startbtn = findViewById(R.id.start);
        agreement = findViewById(R.id.agreement);
        Log.d("wacher", "handleMessage: welcome");

        startbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        agreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this,AgreementActivity.class);
                startActivity(intent);
            }
        });
    }
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }
        else if(keyCode==KeyEvent.KEYCODE_HOME) return true;

        return super.onKeyDown(keyCode,event);
    }
}
