package com.leeeyf.yiyipsdmanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditActivity extends AppCompatActivity {
    String old_account;
    String old_username;
    String old_password;
    byte[] old_icon;

    private Button savebtn;
    private Button dltbtn;
    private Button selectbtn;
    //private Button addBtn;
    private TextInputEditText old_account_layout;
    private TextInputEditText old_username_layout;
    private TextInputEditText old_password_layout;

    private Toolbar myToolbar;

    public Intent intent = new Intent();



    String account;
    String username;
    String password;

    Long id;
    //选择应用的图标
    byte[] appIcon;
    Bitmap icon;

    //是否使用特殊的标题栏背景颜色，android5.0以上可以设置状态栏背景色，如果不使用则使用透明色值
    protected boolean useThemestatusBarColor = false;
    //是否使用状态栏文字和图标为暗色，如果状态栏采用了白色系，则需要使状态栏和图标为暗色，android6.0以上可以设置
    protected boolean useStatusBarColor = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        setStatusBar();

        //顶部导航栏
        myToolbar = findViewById(R.id.edit_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //add
        //addBtn = findViewById(R.id.editfloatingActionButton);

        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("AccountName",old_account);

                intent.putExtra("UserName",old_username);
                intent.putExtra("PassWord",old_password);
                intent.putExtra("id",id);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

//        addBtn.setOnClickListener(new View.OnClickListener() {//"添加"按钮事件
//            @Override
//            public void onClick(View v) {
////                adapater = new NoteAdapter(getApplicationContext(),noteList);
////                lstvw.setAdapter(adapater);
////                refreshListView();
//
//            }
//        });




        selectbtn= findViewById(R.id.selectbtn);

        old_account_layout=findViewById(R.id.edit_appname);
        old_username_layout=findViewById(R.id.edit_username);
        old_password_layout=findViewById(R.id.edit_password);
        savebtn=findViewById(R.id.savebtn);
        dltbtn=findViewById(R.id.dltbtn);



        Intent getIntent = getIntent();
        id=getIntent.getLongExtra("id",0);
        old_account = getIntent.getStringExtra("account");
        old_username = getIntent.getStringExtra("username");
        old_password = getIntent.getStringExtra("password");
        old_icon=getIntent.getByteArrayExtra("icon");
        Log.d("icon", "onCreate: "+old_icon);
        appIcon=old_icon;

        old_account_layout.setText(old_account);
        Bitmap biticon = BitmapFactory.decodeByteArray(appIcon,0,appIcon.length);
        Drawable drawable =new BitmapDrawable(biticon);
        old_account_layout.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);
        old_account_layout.setSelection(old_account.length());//光标放在文本最后
        old_username_layout.setText(old_username);
        old_username_layout.setSelection(old_username.length());//光标放在文本最后
        old_password_layout.setText(old_password);
        old_password_layout.setSelection(old_password.length());//光标放在文本最后

        //修改


        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("AccountName",old_account_layout.getText().toString());

                intent.putExtra("UserName",old_username_layout.getText().toString());
                intent.putExtra("PassWord",old_password_layout.getText().toString());
                intent.putExtra("id",id);
                intent.putExtra("icon",appIcon);
                Log.d("old_account_layout", "onClick: "+old_account_layout.getText()+id);
                setResult(1,intent);
                finish();

            }
        });
        //删除
        dltbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                builder.setTitle("");
                builder.setMessage("是否删除？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(EditActivity.this, "嘻嘻嘻",Toast.LENGTH_SHORT).show();
                        intent.putExtra("id",id);
                        setResult(2,intent);
                        finish();
                    }
                });
                builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(EditActivity.this,"那你再瞅瞅~",Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
//
            }
        });
        //选择应用
        selectbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditActivity.this,AppSelect.class);
                intent.putExtra("appname",old_account_layout.getText().toString());
                intent.putExtra("icon",appIcon);
                startActivityForResult(intent,3);
            }
        });

    }






    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
//            Intent intent = new Intent();
//            intent.putExtra("content",et.getText().toString());
//            intent.putExtra("time",dateToStr());
            intent.putExtra("AccountName",old_account);

            intent.putExtra("UserName",old_username);
            intent.putExtra("PassWord",old_password);
            intent.putExtra("id",id);
            setResult(RESULT_OK,intent);
            finish();
            return true;
        }
        else if(keyCode==KeyEvent.KEYCODE_HOME) return true;

        return super.onKeyDown(keyCode,event);
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

        old_account_layout.setText(app_name);
        old_account_layout.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);

        Log.d("tracker", "onActivityResult: b");



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

    public String dateToStr(){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return simpleDateFormat.format(date);

    }
}
