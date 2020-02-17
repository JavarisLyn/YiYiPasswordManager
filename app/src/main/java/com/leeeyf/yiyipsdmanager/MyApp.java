package com.leeeyf.yiyipsdmanager;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.codersun.fingerprintcompat.FingerManager;


import static com.leeeyf.yiyipsdmanager.AppSelect.bitmap2Bytes;
import static com.leeeyf.yiyipsdmanager.AppSelect.drawableToBitamp;
import static com.leeeyf.yiyipsdmanager.MainActivity.sharedPreferences;

public class MyApp extends Application {

        /**
         * 当前Acitity个数
         */
        private int activityAount = 0;
        public boolean isForeground = false;
        public int appCount=0;
        public boolean isRunInBackground=false;

        //public static Resources mresources = getBaseContext().getResources();//
//    //不是该context，而是basecontext
//    //  Log.d("resource", "resource");
//
       // public  byte[] icon=bitmap2Bytes(drawableToBitamp(mresources.getDrawable(R.drawable.ic_app_default)));



        @Override
        public void onCreate() {

            super.onCreate();
            //Toast.makeText(this,"ss",Toast.LENGTH_LONG);
            initialPrefs();
            Log.d("front", "onCreate: ");
            initBackgroundCallBack();

        }

        private void initBackgroundCallBack() {
            registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                }

                @RequiresApi(api = Build.VERSION_CODES.Q)
                @Override
                public void onActivityStarted(Activity activity) {
                    appCount++;
                    if (appCount==1) {
                        //应用从后台回到前台 需要做的操作
                        back2App(activity);
                        Log.d("front", "前台 "+appCount);
                        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
                        Log.e("current", "pkg:"+cn.getPackageName());
                        Log.e("currentclass", "cls:"+cn.getClassName());
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M&&sharedPreferences.getBoolean("fingerlock",false)) {
                            switch (FingerManager.checkSupport(MyApp.this)) {
                                case DEVICE_UNSUPPORTED:
                                    break;
                                case SUPPORT_WITHOUT_DATA:
                                    break;
                                case SUPPORT:
                                    if(cn.getClassName().equals("com.leeeyf.yiyipsdmanager.MainActivity")||cn.getClassName().equals("com.leeeyf.yiyipsdmanager.StartActivity")){
                                        Intent intent = new Intent(MyApp.this,StartActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                    break;
                            }
                        }
                    }
                }
                @Override
                public void onActivityResumed(Activity activity) {
                }

                @Override
                public void onActivityPaused(Activity activity) {
                }

                @Override
                public void onActivityStopped(Activity activity) {
                    appCount--;
                    if (appCount == 0) {
                        //应用进入后台 需要做的操作
                        Log.d("front", "后台 "+appCount);
                        leaveApp(activity);
                    }
                }

                @Override
                public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                }

                @Override
                public void onActivityDestroyed(Activity activity) {
                }
            });
        }

        /**
         * 从后台回到前台需要执行的逻辑
         *
         * @param activity
         */
        private void back2App(Activity activity) {
            isRunInBackground = false;
        }

        /**
         * 离开应用 压入后台或者退出应用
         *
         * @param activity
         */
        private void leaveApp(Activity activity) {
            isRunInBackground = true;
        }

    public void initialPrefs()
    {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (!sharedPreferences.contains("fingerlock")) {
            editor.putBoolean("fingerlock", false);
            editor.commit();
        }
        if(getBaseContext().getResources().getDrawable(R.drawable.ic_app_default)!=null)
        {
            byte[] icon=bitmap2Bytes(drawableToBitamp(getBaseContext().getResources().getDrawable(R.drawable.ic_app_default)));
//        Log.d("wacher1", "initialPrefs: "+icon);
//        if(icon==null)
//            Log.d("wacher", "onCreate:null");
            String s= Base64.encodeToString(icon,Base64.DEFAULT);
//        Log.d("wacher2", "initialPrefs: ");
            editor.putString("default_icon",s);
            editor.commit();
        }
        else {
            editor.putString("default_icon","");
            editor.commit();
        }


    }



    }

