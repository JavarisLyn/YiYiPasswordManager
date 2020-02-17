package com.leeeyf.yiyipsdmanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.codersun.fingerprintcompat.AonFingerChangeCallback;
import com.codersun.fingerprintcompat.FingerManager;
import com.codersun.fingerprintcompat.SimpleFingerCheckCallback;

import static com.leeeyf.yiyipsdmanager.MainActivity.navigationView;
import static com.leeeyf.yiyipsdmanager.MainActivity.sharedPreferences;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            switch (FingerManager.checkSupport(StartActivity.this)) {
                case DEVICE_UNSUPPORTED:
                    showToast("您的设备不支持指纹");
                    break;
                case SUPPORT_WITHOUT_DATA:
                    showToast("请在系统录入指纹后再验证");
                    break;
                case SUPPORT:
                    FingerManager.build().setApplication(getApplication())
                            .setTitle("指纹验证")
                            .setDes("请按下指纹")
                            .setNegativeText("取消")
                            .setFingerCheckCallback(new SimpleFingerCheckCallback() {
                                @Override
                                public void onSucceed() {
                                    showToast("验证成功");

                                    if(navigationView!=null)
                                    {
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putBoolean("fingerlock",true);
                                        editor.commit();
                                        //IfUseFingerLock=false;
                                        navigationView.getMenu().getItem(0).setIcon(R.drawable.ic_fingerprint_color_24dp);
                                        navigationView.setItemIconTintList(null);
                                    }



                                    Intent intent = new Intent(StartActivity.this,MainActivity.class);
                                    startActivity(intent);
                                }

                                @Override
                                public void onError(String error) {
                                    showToast("验证失败");
                                    if(!sharedPreferences.getBoolean("fingerlock",false))
                                    {
                                        Intent intent = new Intent(StartActivity.this,MainActivity.class);
                                        startActivity(intent);
                                    }
                                }

                                @Override
                                public void onCancel() {
                                    showToast("您取消了识别");
                                    if(!sharedPreferences.getBoolean("fingerlock",false))
                                    {
                                        Intent intent = new Intent(StartActivity.this,MainActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            })
                            .setFingerChangeCallback(new AonFingerChangeCallback() {
                                @Override
                                protected void onFingerDataChange() {
                                    showToast("指纹数据发生了变化");
                                }
                            })
                            .create()
                            .startListener(StartActivity.this);
                    break;
            }




        }
    }
    private void showToast(String content) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
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
