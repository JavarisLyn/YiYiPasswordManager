package com.leeeyf.yiyipsdmanager;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.leeeyf.yiyipsdmanager.entity.AppInfo;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class AppSelect extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private List<AppInfo> applist = new ArrayList<>();
    private ListView listView;

    byte[] appIcon;
    Bitmap icon;
    private Toolbar myToolbar;

    MyAdapter arrayAdapter;

    //是否使用特殊的标题栏背景颜色，android5.0以上可以设置状态栏背景色，如果不使用则使用透明色值
    protected boolean useThemestatusBarColor = false;
    //是否使用状态栏文字和图标为暗色，如果状态栏采用了白色系，则需要使状态栏和图标为暗色，android6.0以上可以设置
    protected boolean useStatusBarColor = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_select);

        setStatusBar();
        //顶部导航栏
        myToolbar = findViewById(R.id.select_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //返回
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
//            intent.putExtra("content",et.getText().toString());
//            intent.putExtra("time",dateToStr());
                if(getIntent().getStringExtra("appname")!=null)
                {
                    intent.putExtra("app_name",getIntent().getStringExtra("appname"));
                    intent.putExtra("app_image",getIntent().getByteArrayExtra("icon"));
                }
                else
                {
                    intent.putExtra("app_name"," ");
                    intent.putExtra("app_image",appIcon);
                }
                setResult(3,intent);
                finish();
            }
        });




        listView=(ListView)findViewById(R.id.applist);  //找到列表
        listView.setOnItemClickListener(this);
        getAppProcessName(this);
        arrayAdapter=new MyAdapter(this,R.layout.appentry,applist,AppSelect.this);  //定义适配器
        listView.setAdapter(arrayAdapter);  //添加适配器

        Resources resources = getBaseContext().getResources();//
        //不是该context，而是basecontext
        //  Log.d("resource", "resource");

        icon=drawableToBitamp(resources.getDrawable(R.drawable.ic_apps_black_24dp));
        appIcon=bitmap2Bytes(icon);//不选择app时，图标使用默认。

    }

    //获取app信息
    public void getAppProcessName(Context context) {

        final PackageManager packageManager = getPackageManager();
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        final List<ResolveInfo> apps = packageManager.queryIntentActivities(mainIntent, 0);

        for (int i = 0; i < apps.size(); i++) {
            Drawable imageicon = apps.get(i).activityInfo.loadIcon(packageManager); //获得图标(此处得到的为Drawable型的，xml文件中的图片是Imageview型的，需要用setImageDrawable进行转换，见MyAdapter.class)
            String appname = apps.get(i).activityInfo.applicationInfo.loadLabel(packageManager).toString(); //获得名称
            AppInfo appinfo = new AppInfo(imageicon, appname);  //创建appinfo对象
            applist.add(appinfo);  //加入应用信息队列
            Log.i("NAME", "getAppProcessName: " + appname);//打印日志查看
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch ((parent.getId())){
            case R.id.applist:
                Log.d("click", "listClick: ");
                AppInfo appinfo = (AppInfo) parent.getItemAtPosition(position);
                Intent intent = new Intent();
                intent.putExtra("app_name",appinfo.getMappname());
                byte[] appIcon = bitmap2Bytes(drawableToBitamp(appinfo.getMappimage()));
                intent.putExtra("app_image",appIcon);

                setResult(3,intent);
                finish();
                break;
        }
    }

    //搜索
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.select_menu,menu);

        MenuItem mSearch = menu.findItem(R.id.search);
        SearchView mSearchView = (SearchView)mSearch.getActionView();

        mSearchView.setQueryHint("Search");
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                arrayAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            Intent intent = new Intent();
//            intent.putExtra("content",et.getText().toString());
//            intent.putExtra("time",dateToStr());
            if(getIntent().getStringExtra("appname")!=null)
            {
                intent.putExtra("app_name",getIntent().getStringExtra("appname"));
                intent.putExtra("app_image",getIntent().getByteArrayExtra("icon"));
            }
            else
            {
                intent.putExtra("app_name"," ");
                intent.putExtra("app_image",appIcon);
            }
            setResult(3,intent);
            finish();
            return true;
        }
        else if(keyCode==KeyEvent.KEYCODE_HOME) return true;

        return super.onKeyDown(keyCode,event);
    }

    /**
     * drawable转化成bitmap的方法
     * @param drawable 需要转换的Drawable
     */
    public static Bitmap drawableToBitamp(Drawable drawable) {
        Bitmap bitmap;
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        System.out.println("Drawable转Bitmap");
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        bitmap = Bitmap.createBitmap(w,h,config);
        //注意，下面三行代码要用到，否在在View或者surfaceview里的canvas.drawBitmap会看不到图
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * bitmap转化成byte数组
     * @param bm 需要转换的Bitmap
     * @return
     */
    public static byte[] bitmap2Bytes(Bitmap bm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
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
}
