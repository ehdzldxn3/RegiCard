package com.example.regicard.DATABASE;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;

public class DBHelper extends SQLiteOpenHelper {


    //데이터베이스 이름
    //private static final String Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + SQLITE_FILE;
    private static final String DATABASE_NAME= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+ File.separator+"test"+File.separator+"SMARTFRONT.db"; //


    //SingleTon Pattern(싱글톤 패턴)
    private static DBHelper dbInstance=null;


    public DBHelper(Context context) {
        super(context, DATABASE_NAME,null,1);
        String TAG = "TAG";

        Log.e(TAG, ""+context );
        Log.e(TAG, ""+DATABASE_NAME );
    }

    //SingleTon Pattern(싱글톤 패턴)
    public static DBHelper getInstance(Context context) {

        if(dbInstance==null)
            dbInstance = new DBHelper(context);
        return dbInstance;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE if not exists COMMON ("
                + "_id integer primary key autoincrement,"
                + "CODE text,"
                + "REMARK text);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE if exists SMARTFRONT";
        db.execSQL(sql);
    }





}