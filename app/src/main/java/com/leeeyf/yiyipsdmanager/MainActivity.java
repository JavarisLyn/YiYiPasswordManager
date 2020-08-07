package com.leeeyf.yiyipsdmanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.codersun.fingerprintcompat.FingerManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.leeeyf.yiyipsdmanager.entity.Account;
import com.leeeyf.yiyipsdmanager.entity.LoginResult;
import com.leeeyf.yiyipsdmanager.entity.LoginUser;
import com.leeeyf.yiyipsdmanager.entity.UserwithAccounts;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static com.leeeyf.yiyipsdmanager.LoginActivity.username_str;
import static com.leeeyf.yiyipsdmanager.UploadData.UploadUserData.uploadData;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener  {

    Context mcontext;
    public  static List<Account> Accounts = new ArrayList<>();
    private RecyclerView recycler_cardview;
    private InfoAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    FloatingActionButton addBtn;

    private DrawerLayout mDrawerLayout;
    public static NavigationView navigationView;


    private Toolbar myToolbar;

    private Switch mSwitch;
    public static SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    //是否使用特殊的标题栏背景颜色，android5.0以上可以设置状态栏背景色，如果不使用则使用透明色值
    protected boolean useThemestatusBarColor = false;
    //是否使用状态栏文字和图标为暗色，如果状态栏采用了白色系，则需要使状态栏和图标为暗色，android6.0以上可以设置
    protected boolean useStatusBarColor = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setStatusBar();
        //initialPrefs();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawelayout);



        editor = sharedPreferences.edit();  //获取输入能力
        //sliding

         navigationView = (NavigationView) findViewById(R.id.navigation_view);
         navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.fingerprint:
                        Log.i("fingerlock", "指纹"+sharedPreferences.getBoolean("fingerlock",false));
                        if(sharedPreferences.getBoolean("fingerlock",false))
                        {
                            //开->关
                            editor.putBoolean("fingerlock",false);
                            editor.commit();
                            //IfUseFingerLock=false;
                            item.setIcon(R.drawable.ic_fingerprint_black_24dp);
                            mDrawerLayout.openDrawer(navigationView);//设置点击toolbar出现侧滑菜单
                            Log.i("fingerlock", "指纹"+sharedPreferences.getBoolean("fingerlock",false));
                        }
                        else{
                            //关->开
//                            IfUseFingerLock=false;
//                            item.setIcon(R.drawable.ic_fingerprint);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                                switch (FingerManager.checkSupport(MainActivity.this)) {
                                    case DEVICE_UNSUPPORTED:
                                        showToast("您的设备不支持指纹");
                                        break;
                                    case SUPPORT_WITHOUT_DATA:
                                        showToast("请在系统录入指纹后再验证");
                                        break;
                                    case SUPPORT:
//                                        editor.putBoolean("fingerlock",true);
//                                        editor.commit();
//                                        //IfUseFingerLock=false;
//                                        item.setIcon(R.drawable.ic_fingerprint_color_24dp);
//                                        navigationView.setItemIconTintList(null);
                                        Intent intent = new Intent(MainActivity.this,StartActivity.class);
                                        startActivity(intent);
                                        Log.i("fingerlock", "指纹"+sharedPreferences.getBoolean("fingerlock",false));
                                        break;
                                }
                            }

                        }


                        break;
                    case R.id.aboutus:
                        Intent intent=new Intent(MainActivity.this, AboutUsActivity.class);
                        startActivity(intent);
                        Log.i("mNavigationView", "关于 is clicked!");
                        break;
                    case R.id.loginItem:
                        if(sharedPreferences.getBoolean("loginState",true)){
                            Toast  t = Toast.makeText(MainActivity.this,"已登录",Toast.LENGTH_SHORT);
                            t.show();
                        }
                        else{
                            Intent intent2=new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent2);
                        }

                        break;

                }
                return true;
            }
        });


//        //引入header和menu
//        navigationView.inflateHeaderView(R.layout.header_layout);
//        navigationView.inflateMenu(R.menu.sliding_menu);
//
//        //获取头部布局
//        View navHeaderView = navigationView.getHeaderView(0);
//        final MenuItem switch_item = menu.findItem(R.id.switch_item);
//        mSwitch = (Switch) switch_item.getActionView();
//        mSwitch=findViewById(R.id.switch_item);
//        mSwitch.setChecked(IfUseFingerLock);//和该变量绑定
//        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked){
//                    IfUseFingerLock=true;
//                    Log.d("switch", IfUseFingerLock+"");
//                    //Intent intent = new Intent(SettingsActivity.this,StartActivity.class);
//                    //startActivity(intent);
//                }else {
//                    IfUseFingerLock=false;
//                    Log.d("switch", IfUseFingerLock+"");
//                }
//            }
//        });

        mAdapter = new InfoAdapter(this,Accounts);

        //顶部导航栏
        myToolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myToolbar.setNavigationIcon(R.drawable.ic_lock_outline_black_24dp);
        //菜单栏
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                RelativePopupWindow popup = new RelativePopupWindow();
//                popup.showOnAnchor(MainActivity.this, VerticalPosition.ABOVE, HorizontalPosition.CENTER);
               // Log.i("fingerlock", "指纹"+sharedPreferences.getBoolean("fingerlock",false));
                navigationView.setItemIconTintList(null);
                if(sharedPreferences.getBoolean("fingerlock",false))
                    navigationView.getMenu().getItem(0).setIcon(R.drawable.ic_fingerprint_color_24dp);
                else
                    navigationView.getMenu().getItem(0).setIcon(R.drawable.ic_fingerprint_black_24dp);
                mDrawerLayout.openDrawer(navigationView);//设置点击toolbar出现侧滑菜单

//                Intent intent=new Intent(MainActivity.this,SettingsActivity.class);
//                startActivity(intent);
            }
        });

        initial();

        addBtn = findViewById(R.id.floatingActionButton);
        recycler_cardview=(RecyclerView)this.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recycler_cardview.setLayoutManager(layoutManager);



        mAdapter.setOnItemClickListener(new InfoAdapter.OnItemClickListener() {
            @Override
            public void onClick(List<Account> Accounts,int position) {
//                for(int i=0;i<Accounts.size();i++)
//                    Log.d("wacher", "publishResults: "+Accounts.get(i).getAccountName());
                Account account = Accounts.get(position);

                Intent intent = new Intent(MainActivity.this,EditActivity.class);
                intent.putExtra("account",account.getAccountName());
                intent.putExtra("id",account.getId());
                intent.putExtra("username",account.getUserName());
                intent.putExtra("password",account.getPassWord());
                intent.putExtra("time",account.getTime());
                Log.d("icon", "onClick: "+account.getIcon());
                intent.putExtra("icon",account.getIcon());
                startActivityForResult(intent,1);

                mAdapter.setmPosition(position);
                mAdapter.notifyDataSetChanged();
            }
        });
        recycler_cardview.setAdapter(mAdapter);

        addBtn.setOnClickListener(new View.OnClickListener() {//"新建"按钮事件
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,addActivity.class);
                startActivity(intent);
//                adapater = new NoteAdapter(getApplicationContext(),noteList);
//                lstvw.setAdapter(adapater);
//                refreshListView();

            }
        });

        uploadData();


    }






    //搜索
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
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
















    public void initial()
    {
        refresh();
    }


    public void refresh()
    {
        //ListSort(Accounts);
        //if(Accounts.size()>=2)
//        for(int i=0;i<Accounts.size();i++)
//            Log.d("tag", "refresh: "+Accounts.get(i).getAccountName());
        //mAdapter.setAccounts(Accounts);

        //Log.d("refresh", "refresh: ");
        CRUD op = new CRUD(getApplicationContext());
        op.open();
        if(Accounts.size()>0) Accounts.clear();
        Accounts.addAll(op.getAllAccounts());
        op.close();

        mAdapter.notifyDataSetChanged();
       // Log.d("refresh", "refresh: ");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
     //   Log.d("wacher", "onItemClick: ");
          // switch ((parent.getId())){
              //  case R.id.recyclerView:
////                Log.d("click", "onItemClick: ");
//                Account account = (Account)parent.getItemAtPosition(position);
//
//                Intent intent = new Intent(MainActivity.this,EditActivity.class);
//                intent.putExtra("account",account.getAccountName());
//                intent.putExtra("id",account.getId());
//                intent.putExtra("username",account.getUserName());
//                intent.putExtra("password",account.getPassWord());
//                intent.putExtra("time",account.getTime());
//                Log.d("icon", "onClick: "+account.getIcon());
//                intent.putExtra("icon",account.getIcon());
//                startActivityForResult(intent,1);
//
//                mAdapter.setmPosition(position);
//                mAdapter.notifyDataSetChanged();
      //  }
//
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent intent)
    {
        super.onActivityResult(requestCode,resultCode,intent);
        Log.d("tracker", "onActivityResult: a");
        if(resultCode==1)//修改
        {
            String accountname = intent.getStringExtra("AccountName");

            String username = intent.getStringExtra("UserName");
            String password = intent.getStringExtra("PassWord");

            byte[] appIcon = intent.getByteArrayExtra("icon");


            int id = intent.getExtras().getInt("id");
            Log.d("name", "onActivityResult:"+accountname+username+password+id);


            Account account = new Account(accountname,username,password,dateToStr(),appIcon);
            account.setId(id);
            CRUD op = new CRUD(getApplicationContext());
            op.open();
            op.updateNote(account);
            op.close();
        }
        else if(resultCode==2)//删除
        {
            int id=intent.getExtras().getInt("id");
            Account account = new Account("1","1","1","1",null);
            account.setId(id);
            CRUD op = new CRUD(this.getApplicationContext());
            op.open();
            op.removeNote(account);
            op.close();
        }
        refresh();

    }


    public boolean onKeyDown(int keyCode, KeyEvent event)
    {

        if(keyCode==KeyEvent.KEYCODE_BACK&&mDrawerLayout.isDrawerOpen(GravityCompat.START))
        {
            Intent intent=new Intent(this,MainActivity.class);
            startActivity(intent);
            //侧滑菜单返回到主界面
        }
        else
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
    private void showToast(String content) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }

//    private static void ListSort(List<Account> list) {
//        {    //排序方法
//            Collections.sort(list, new Comparator<Account>() {
//                @Override
//                public int compare(Account o1, Account o2) {
//                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    try {
//                        // format.format(o1.getTime()) 表示 date转string类型 如果是string类型就不要转换了
//                        Date dt1 = format.parse(o1.getTime());
//                        Log.d("tag", "compare: "+dt1+o1.getAccountName());
//                        Date dt2 = format.parse(o2.getTime());
//                        Log.d("tag", "compare: "+dt2+o2.getAccountName());
//                        // 这是由大向小排序   如果要由小向大转换比较符号就可以
//                        if (dt1.getTime() < dt2.getTime()) {
//                            return 1;
//                        } else if (dt1.getTime() > dt2.getTime()) {
//                            return -1;
//                        } else {
//                            return 0;
//                        }
//                    } catch (Exception e) {
//                        Log.d("tag", "compare: failed");
//                        e.printStackTrace();
//                    }
//                    return 0;
//                }
//
//            });
//        }
//    }

}
