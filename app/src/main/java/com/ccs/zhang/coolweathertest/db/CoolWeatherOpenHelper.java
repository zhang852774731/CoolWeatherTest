package com.ccs.zhang.coolweathertest.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by zhang on 2015/1/28.
 * 创建province（省），city（市），county（县）表
 */
public class CoolWeatherOpenHelper extends SQLiteOpenHelper{
    /**
     * 创建province表的建表语句，id为主键，自动增长
     * province_name：省份名字
     * province_code:省份代码
     */
    private static final String CREATE_PROVINCE = "create table Province("
            +"id integer primary key autoincrement,"
            +"province_name text,"
            +"province_code text)";
    /**
     * city的建表语句
     * province_id为外键，依赖于province表的主键
     */
    private static final String CREATE_CITY ="create table City("
            +"id integer primary key autoincrement,"
            +"city_name text,"
            +"city_code text,"
            +"province_id integer)";
    /**
     * county的建表语句
     * city_id为外键，依赖于city表的主键
     */
    private static final String CREATE_COUNTY = "create table County("
            +"id integer primary key autoincrement,"
            +"county_name text,"
            +"county_code text,"
            +"city_id integer)";

    public CoolWeatherOpenHelper(Context context,String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e("Helper","helper");
        db.execSQL(CREATE_PROVINCE);
        db.execSQL(CREATE_CITY);
        db.execSQL(CREATE_COUNTY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
