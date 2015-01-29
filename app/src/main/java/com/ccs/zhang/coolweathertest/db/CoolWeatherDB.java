package com.ccs.zhang.coolweathertest.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ccs.zhang.coolweathertest.model.City;
import com.ccs.zhang.coolweathertest.model.County;
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

    /**
     * 将city实例保存到数据库中
     * @param city
     */
    public void saveCity(City city){
        if(city != null){
            ContentValues contentValues = new ContentValues();
            contentValues.put("",city.getCityName());
            contentValues.put("",city.getCityCode());
            contentValues.put("",city.getProvinceId());
            sqLiteDatabase.insert("City",null,contentValues);
        }
    }

    /**
     * 读取某省份下的所有城市信息
     * @param provinceId
     * @return
     */
    public List<City> loadCities(int provinceId){
        List<City> cityList = new ArrayList<City>();
        Cursor cursor = sqLiteDatabase.query("City",null,"province_id = ?",new String[]{String.valueOf(provinceId)},null,null,null);
        while (cursor.moveToNext()){
            City city = new City();
            city.setId(cursor.getInt(cursor.getColumnIndex("id")));
            city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
            city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
            city.setProvinceId(provinceId);
            cityList.add(city);
        }
        if (cursor != null){
            cursor.close();
        }
        return cityList;
    }

    /**
     * 将County实例保存到数据库中
     * @param county
     */
    public void saveCounty(County county){
        if (county != null){
            ContentValues contentValues = new ContentValues();
            contentValues.put("county_name",county.getCountyName());
            contentValues.put("county_code",county.getCountyCode());
            contentValues.put("city_id",county.getCityId());
            sqLiteDatabase.insert("County",null,contentValues);
        }
    }

    /**
     * 从数据库读取某城市下所有的县信息
     * @param cityId
     * @return
     */
    public List<County> loadCounties(int cityId){
        List<County> countyList = new ArrayList<County>();
        Cursor cursor = sqLiteDatabase.query("County",null,"city_id = ?",new String[]{String.valueOf(cityId)},null,null,null);
        while (cursor.moveToNext()){
            County county = new County();
            county.setId(cursor.getInt(cursor.getColumnIndex("id")));
            county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
            county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
            county.setCityId(cityId);
            countyList.add(county);
        }
        if (cursor != null){
            cursor.close();
        }
        return countyList;
    }
}
