package com.example.SMARTFRONTDESK.DATABASE;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;

public class DBHelper extends SQLiteOpenHelper {



    //데이터베이스
    //DB 경로
    //private static final String DB_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + File.separator + "test";
    private static final String DB_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)+ File.separator+"DataBase";
    //DB 이름
    private static final String DB_NAME = "REGICARD.db";
    //DB 경로 + 이름
    private static final String DB_P_N = DB_PATH + File.separator + DB_NAME;


    //SingleTon Pattern(싱글톤 패턴)
    private static DBHelper dbInstance=null;


    public DBHelper(Context context) {
        super(context, DB_P_N,null,1);
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
                + "CODE text primary key,"
                + "REMARK text);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE if exists SMARTFRONT";
        db.execSQL(sql);
    }





}