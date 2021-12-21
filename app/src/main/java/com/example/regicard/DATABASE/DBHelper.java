package com.example.regicard.DATABASE;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    //데이터베이스 버전
    private static final int DATABASE_VERSION=1;
    //데이터베이스 이름
    private static final String DATABASE_NAME="SMARTFRONT.db";
    //테이블 이름
    private static final String TABLE_NAME="COMMON";

    //Table Columns
    private static final String DATA_ID="CODE";
    private static final String DATA_NAME="REMARK";


    //SingleTon Pattern(싱글톤 패턴)
    private static DBHelper dbInstance=null;


    public DBHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
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