package com.ccs.zhang.coolweathertest.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ccs.zhang.coolweathertest.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhang on 2015/1/28.
 * 封装数据的操作，保存数据，读取数据
 */
public class CoolWeatherDB {
    /**
     * 数据库名
     */
    private static final String DB_NAME = "cool_weather";
    /**
     * 数据库的版本号
     */
    private static final int VERSION = 1;

    private static CoolWeatherDB coolWeatherDB;
    private SQLiteDatabase sqLiteDatabase;

    private CoolWeatherDB(Context context){
        CoolWeatherOpenHelper coolWeatherOpenHelper = new CoolWeatherOpenHelper(context,DB_NAME,null,VERSION);
        sqLiteDatabase = coolWeatherOpenHelper.getWritableDatabase();
    }

    /**
     * 通过单例的模式返回CoolWeatherDB的实例
     * @param context
     * @return
     */
    public synchronized static CoolWeatherDB getInstance(Context context){
        if(coolWeatherDB == null){
            coolWeatherDB = new CoolWeatherDB(context);
        }
        return coolWeatherDB;
    }

    /**
     * 将province实例保存到数据库中
     * @param province
     */
    public void saveProvince(Province province) {
        if (province != null) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("province_name", province.getProvinceName());
            contentValues.put("province_code", province.getProvinceCode());
            sqLiteDatabase.insert("Province",null,contentValues);
        }
    }

    /**
     * 从数据库读取全国所有的省份信息
     * @return
     */
    public List<Province> loadProvinces(){
        List<Province> provinceList = new ArrayList<Province>();
        Cursor cursor = sqLiteDatabase.query("Province",null,null,null,null,null,null,null);
        while(cursor.moveToNext()){
            Province province = new Province();
            province.setId(cursor.getInt(cursor.getColumnIndex("id")));
            province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
            province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
            provinceList.add(province);
        }
        if(cursor != null){
            cursor.close();
        }
        return provinceList;
    }
}
