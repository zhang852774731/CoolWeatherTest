package com.ccs.zhang.coolweathertest.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ccs.zhang.coolweathertest.R;
import com.ccs.zhang.coolweathertest.service.AutoUpdateService;
import com.ccs.zhang.coolweathertest.util.HttpCallBackListener;
import com.ccs.zhang.coolweathertest.util.HttpUtil;
import com.ccs.zhang.coolweathertest.util.Utility;

/**
 * Created by zhang on 2015/2/1.
 */
public class WeatherActivity extends Activity {
    private LinearLayout weatherInfoLayout;
    /**
     * 显示城市名字
     */
    private TextView cityNameText;
    /**
     * 显示发布时间
     */
    private TextView publishText;
    /**
     * 显示天气描述
     */
    private TextView weatherDespText;
    /**
     * 显示最低气温
     */
    private TextView temp1Text;
    /**
     * 显示最高气温
     */
    private TextView temp2Text;
    /**
     * 显示当前日期
     */
    private TextView currentDateText;
    /**
     * 切换城市按钮
     */
    private Button switchButton;
    /**
     * 刷新按钮
     */
    private Button refreshButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weather_layout);
        weatherInfoLayout = (LinearLayout)findViewById(R.id.weather_info_layout);
        weatherDespText = (TextView)findViewById(R.id.weather_desp);
        cityNameText = (TextView)findViewById(R.id.city_name);
        publishText = (TextView)findViewById(R.id.publish_text);
        temp1Text = (TextView)findViewById(R.id.temp1);
        temp2Text = (TextView)findViewById(R.id.temp2);
        currentDateText = (TextView)findViewById(R.id.current_date);
        switchButton = (Button)findViewById(R.id.switch_city);
        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeatherActivity.this,ChooseAreaActivity.class);
                intent.putExtra("from_weather_activity",true);
                startActivity(intent);
                finish();
            }
        });
        refreshButton = (Button)findViewById(R.id.refresh);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
                String weatherCode = sharedPreferences.getString("weather_code","");
                if (!TextUtils.isEmpty(weatherCode)) {
                    queryWeatherCode(weatherCode);
                }
            }
        });
        String countyCode = getIntent().getStringExtra("county_code");
        if (!TextUtils.isEmpty(countyCode)){
            publishText.setText("同步中...");
            weatherInfoLayout.setVisibility(View.INVISIBLE);
            queryWeatherCode(countyCode);
        }else {
            showWeather();
        }
    }

    private void queryWeatherCode(String countyCode){
        String address = "http://www.weather.com.cn/data/list3/city"+countyCode+".xml";
        queryFromServer(address,"countyCode");
    }

    private void queryWeatherInfo(String weatherCode){
        String address = "http://www.weather.com.cn/data/cityinfo/"+weatherCode+".html";
        queryFromServer(address,"weatherCode");
    }

    private void queryFromServer(final String address, final String type){
        HttpUtil.sendHttpRequest(address ,new HttpCallBackListener() {
            @Override
            public void onFinish(String response) {
                if ("countyCode".equals(type)){
                    if (!TextUtils.isEmpty(response)){
                        String[] array = response.split("\\|");
                        if (array !=null && array.length ==2){
                            String weatherCode = array[1];
                            queryWeatherInfo(weatherCode);
                        }
                    }
                }else if ("weatherCode".equals(type)){
                    Utility.handleWeatherResponse(WeatherActivity.this,response);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showWeather();
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        publishText.setText("同步失败...");
                    }
                });
            }
        });
    }

    private void showWeather(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        cityNameText.setText(preferences.getString("city_name",""));
        temp1Text.setText(preferences.getString("temp1",""));
        temp2Text.setText(preferences.getString("temp2",""));
        weatherDespText.setText(preferences.getString("weather_desp",""));
        publishText.setText(preferences.getString("publish_time","")+"发布");
        currentDateText.setText(preferences.getString("current_date",""));
        weatherInfoLayout.setVisibility(View.VISIBLE);
        cityNameText.setVisibility(View.VISIBLE);
        Intent intent = new Intent(this, AutoUpdateService.class);
        startService(intent);
    }
}
