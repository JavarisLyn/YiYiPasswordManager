package com.leeeyf.yiyipsdmanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Base64;

import static com.leeeyf.yiyipsdmanager.MainActivity.sharedPreferences;

public class AccountDatabase extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "accounts";
    public static final String ID="id";
    public static final String ACCOUNTNAME="AccountName";
    public static final String USERNAME="username";
    public static final String PASSWORD="password";
    public static final String TIME="time";
    public static final String ICON="icon";

    public AccountDatabase(Context context){
        super(context,"accounts",null,1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_NAME
                +"("
                +ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                +ACCOUNTNAME +" TEXT NOT NULL,"
                +USERNAME +" TEXT NOT NULL,"
                +PASSWORD +" TEXT NOT NULL,"
                +TIME +" TEXT NOT NULL,"
                +ICON +" BLOB)"

        );
        byte[] icon=Base64.decode(sharedPreferences.getString("default_icon",null), Base64.DEFAULT);

//        if(icon!=null)
//            Log.d("wacher2", "onCreate: !=null"+sharedPreferences.getString("default_icon",null));
        db.execSQL("INSERT INTO accounts(id,AccountName,username,password,time,icon) values(?,?,?,?,?,?) ",new Object[]{1,"密码管家","lee","yiyipasswordmanager","2020-1-1 12:12",icon});
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
