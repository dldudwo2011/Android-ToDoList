package com.example.dmit2504oa01lab2youngjaelee;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.UUID;

public class DBManager extends SQLiteOpenHelper {

    static final String TAG = "DBManager";
    static final String DB_NAME = "toDo.db";
    static final int DB_VERSION = 1;
    static final String DB_TABLE = "Current_Lists";
    static final String DB_TABLE2 = "List_Items";
    static final String C_ID = BaseColumns._ID;
    static final String C_NAME = "listName";
    static final String C_ID2 = BaseColumns._ID;
    static final String C_ASSOCIATED_LIST = "associatedList";
    static final String C_DESCRIPTION = "description";
    static final String C_FLAG = "completionFlag";
    static final String C_CREATED_DATE = "createdDate";


    public DBManager(Context context)
    {
        super(context,DB_NAME,null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String sql = "create table " + DB_TABLE + " (" + C_ID + " int primary key, " + C_NAME + " text)";
        String sql2 = "create table " + DB_TABLE2 + " (" + C_ID2 + " int primary key, " + C_ASSOCIATED_LIST + " text, " + C_DESCRIPTION + " text, " + C_FLAG + " text, " + C_CREATED_DATE + " text)";
        Log.d(TAG, sql);
        db.execSQL(sql);
        db.execSQL(sql2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("drop table if exists " + DB_TABLE);
        db.execSQL("drop table if exists " + DB_TABLE2);
        Log.d(TAG, "in onUpgrade()");
        onCreate(db);
    }
}
