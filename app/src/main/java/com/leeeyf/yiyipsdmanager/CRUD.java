package com.leeeyf.yiyipsdmanager;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.leeeyf.yiyipsdmanager.entity.Account;

import java.util.ArrayList;
import java.util.List;

public class
CRUD {
    SQLiteOpenHelper dbHelper;
    SQLiteDatabase db;

    private static final String[] columns={
            AccountDatabase.ID,
            AccountDatabase.ACCOUNTNAME,
            AccountDatabase.USERNAME,
            AccountDatabase.PASSWORD,
            AccountDatabase.TIME,
            AccountDatabase.ICON
    };

    public CRUD(Context context)
    {
        dbHelper = new AccountDatabase(context);
    }
    public void open(){
        db = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    public Account addNote(Account account){
        ContentValues ContentValues = new ContentValues();
        ContentValues.put(AccountDatabase.ACCOUNTNAME,account.getAccountName());
        ContentValues.put(AccountDatabase.USERNAME,account.getUserName());
        ContentValues.put(AccountDatabase.PASSWORD,account.getPassWord());
        ContentValues.put(AccountDatabase.TIME,account.getTime());
        ContentValues.put(AccountDatabase.ICON,account.getIcon());
        
        int insrtId = (int)db.insert(AccountDatabase.TABLE_NAME,null,ContentValues);
        account.setId(insrtId);
        return account;
    }

    public Account getAccount(long id)
    {
        Cursor cursor = db.query(AccountDatabase.TABLE_NAME,columns,AccountDatabase.ID+ "=?",new String[]{String.valueOf(id)},null,null,null);
        if(cursor==null) cursor.moveToFirst();
        Account e = new Account(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getBlob(5));
        return e;
    }

    public List<Account> getAllAccounts()
    {
        Cursor cursor = db.query(AccountDatabase.TABLE_NAME, columns, null, null, null, null,AccountDatabase.TIME+" DESC");

        List<Account> accounts = new ArrayList<>();
        if(cursor.getCount()>0)
            while(cursor.moveToNext())
            {
                Account e = new Account(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getBlob(5));
                e.setId(cursor.getInt(cursor.getColumnIndex(AccountDatabase.ID)));
                accounts.add(e);
                //Log.d("datawacher", "getAllAccounts: "+e.getIcon());
            }
        return accounts;
    }

    public int updateNote(Account account){
        ContentValues values = new ContentValues();
        values.put(AccountDatabase.ACCOUNTNAME,account.getAccountName());
        values.put(AccountDatabase.USERNAME,account.getUserName());
        values.put(AccountDatabase.PASSWORD,account.getPassWord());
        values.put(AccountDatabase.TIME,account.getTime());
        values.put(AccountDatabase.ICON,account.getIcon());

        return db.update(AccountDatabase.TABLE_NAME,values,AccountDatabase.ID+"=?",new String[]{String.valueOf(account.getId())});

    }

    public void removeNote(Account account)
    {
        db.delete(AccountDatabase.TABLE_NAME,AccountDatabase.ID+"="+account.getId(),null);
    }






}
