package com.leeeyf.yiyipsdmanager;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.leeeyf.yiyipsdmanager.AppSelect.bitmap2Bytes;
import static com.leeeyf.yiyipsdmanager.AppSelect.drawableToBitamp;

public class addActivity extends AppCompatActivity {

    Context mContext;
    private Button addbt;
    private String account;
    private String username;
    private String password;

    private Button selectbtn;

    private Toolbar myToolbar;

    private TextInputEditText layout_account;
    private TextInputEditText layout_username;
    private TextInputEditText layout_password;
    private TextInputEditText layout_icon;
    byte[] appIcon;
    Bitmap icon;

    //是否使用特殊的标题栏背景颜色，android5.0以上可以设置状态栏背景色，如果不使用则使用透明色值
    protected boolean useThemestatusBarColor = false;
    //是否使用状态栏文字和图标为暗色，如果状态栏采用了白色系，则需要使状态栏和图标为暗色，android6.0以上可以设置
    protected boolean useStatusBarColor = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        setStatusBar();
        //顶部导航栏
        myToolbar = findViewById(R.id.add_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(addActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        selectbtn=findViewById(R.id.add_selectbtn);

        layout_account=findViewById(R.id.et_appname);
        layout_username=findViewById(R.id.et_username);
        layout_password=findViewById(R.id.et_password);

  //      Resources resources = mContext.getResources();
//        Drawable drawable = resources.getDrawable(R.drawable.ic_apps_black_24dp);
//        if(getDrableImage(mContext, "ic_apps_black_24dp")!=null)
//            icon=getDrableImage(mContext, "ic_apps_black_24dp");
//        icon=drawableToBitamp(drawable);
       //appIcon=bitmap2Bytes(icon);
        Resources resources = getBaseContext().getResources();//
        //不是该context，而是basecontext
      //  Log.d("resource", "resource");

        icon=drawableToBitamp(resources.getDrawable(R.drawable.ic_app_default));
        appIcon=bitmap2Bytes(icon);//不选择app时，图标使用默认。

        addbt=findViewById(R.id.addbtn);
        addbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account=layout_account.getText().toString();
                username=layout_username.getText().toString();
                password=layout_password.getText().toString();

               // byte[] appIcon = bitmap2Bytes(drawableToBitamp(layout_icon.getd));
                //icon=layout_icon;
                Account e = new Account(account,username,password,dateToStr(),appIcon);
                CRUD op = new CRUD(getApplicationContext());
                op.open();
                op.addNote(e);
                op.close();
                Intent intent = new Intent(addActivity.this,MainActivity.class);
                startActivity(intent);

            }
        });

        selectbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(addActivity.this,AppSelect.class);
                startActivityForResult(intent,3);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        Log.d("tracker", "onActivityResult: b");
        super.onActivityResult(requestCode,resultCode,data);
        Log.d("tracker", "resultCode: "+resultCode);
        String app_name=data.getStringExtra("app_name");
        appIcon = data.getByteArrayExtra("app_image");
        icon = BitmapFactory.decodeByteArray(appIcon,0,appIcon.length);
        Drawable drawable =new BitmapDrawable(icon);

        layout_account.setText(app_name);
        layout_account.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);

        Log.d("tracker", "onActivityResult: b");



    }

//    public static Bitmap getDrableImage(Context context, String name) {
//        ApplicationInfo info = context.getApplicationInfo();
//        Resources resources = context.getResources();
//        Log.d("flag", "getDrableImage: ");
//        int resId = resources.getIdentifier(name, "drawable", info.packageName);
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inSampleSize = 2;
//        return BitmapFactory.decodeResource(resources, resId, options);
//    }

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

    public String dateToStr(){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return simpleDateFormat.format(date);

    }






}
