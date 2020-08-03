package com.leeeyf.yiyipsdmanager;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;


public class AboutUsActivity extends AppCompatActivity {

    private Toolbar myToolbar;
    private Switch mSwitch;
    //是否使用特殊的标题栏背景颜色，android5.0以上可以设置状态栏背景色，如果不使用则使用透明色值
    protected boolean useThemestatusBarColor = false;
    //是否使用状态栏文字和图标为暗色，如果状态栏采用了白色系，则需要使状态栏和图标为暗色，android6.0以上可以设置
    protected boolean useStatusBarColor = true;

    private RelativeLayout relativeLayout;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
        //顶部导航栏
        initViews();
        setStatusBar();

//        mSwitch=findViewById(R.id.mySwitch);
//        mSwitch.setChecked(IfUseFingerLock);//和该变量绑定
//        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked){
//                    IfUseFingerLock=true;
//                    Log.d("switch", IfUseFingerLock+"");
//                    Intent intent = new Intent(AboutUsActivity.this,StartActivity.class);
//                    startActivity(intent);
//                }else {
//                    IfUseFingerLock=false;
//                    Log.d("switch", IfUseFingerLock+"");
//                }
//            }
//        });

//        Element store = new Element();
//        store.setTitle("去应用商店为我们评分");
//        store.setValue("market://details?id=");
//        Uri uri = Uri.parse("market://details?id="+"com.leeeyf.yiyipsdmanager");
//        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
//        store.setIntent(goToMarket);


        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.ic_laucher)//图片
                .setDescription("密码管家")//介绍
                .addItem(new Element().setTitle("Version 1.0"))
                .addItem(new Element().setTitle("By LeeeYF_"))
                .addItem(new Element().setTitle("感谢@WZ提出的建议"))
                .addGroup("与我联系")
                .addEmail("2956705997@qq.com")//邮箱
                .addWebsite("www.leeyangfan.com")//网站
                //.addItem(store)
                .addPlayStore("com.leeeyf.yiyipsdmanager")//应用商店
                .addGitHub("JavarisLyn")//github
                .create();
        relativeLayout.addView(aboutPage);

//        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(SettingsActivity.this,MainActivity.class);
//                startActivity(intent);
//            }
//        });
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case android.R.id.home:
//                finish();
//                break;
//            default:
//
//        }
//        return true;
//    }

    private void initViews(){
        relativeLayout= (RelativeLayout) findViewById(R.id.relative);
        myToolbar = findViewById(R.id.setting_Toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AboutUsActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
//        setSupportActionBar(toolbar);
//        ActionBar actionBar=getSupportActionBar();
//        if(actionBar!=null){
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }
    }

    //顶部状态栏----------
    public void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            //根据上面设置是否对状态栏单独设置颜色
            if (useThemestatusBarColor) {
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));//设置状态栏背景色
            } else {
                getWindow().setStatusBarColor(getResources().getColor(R.color.toolbar));//透明
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        } else {
            Toast.makeText(this, "低于4.4的android系统版本不存在沉浸式状态栏", Toast.LENGTH_SHORT).show();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && useStatusBarColor) {//android6.0以后可以对状态栏文字颜色和图标进行修改
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            Intent intent = new Intent(AboutUsActivity.this,MainActivity.class);
            startActivity(intent);

            return true;
        }
        else if(keyCode==KeyEvent.KEYCODE_HOME) return true;

        return super.onKeyDown(keyCode,event);
    }
}
