package com.ccs.zhang.coolweathertest.util;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * Created by zhang on 2015/1/29.
 */
public class HttpUtil {
    public static void sendHttpRequest(final String address,final HttpCallBackListener listener){
        Log.e("HttpUtil的sendHttpRequest方法","sendHttpRequest");
        new Thread(){
            @Override
            public void run() {
                String response = "";
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(address);
                HttpResponse httpResponse = null;
                try {
                    httpResponse = httpClient.execute(httpGet);
                    if (httpResponse == null){
                        Log.e("httpResponse is null","httpResponse is null");
                    }else{
                        Log.e("status code",String.valueOf(httpResponse.getStatusLine().getStatusCode()));
                    }
                    if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                        response = EntityUtils.toString(httpResponse.getEntity());
                    }
                    if(listener != null){
                        listener.onFinish(response.toString());
                    }
                }catch (Exception e){
                    if (listener != null){
                        Log.e("code","code");
                        listener.onError(e);
                    }
                }
            }
        }.start();
    }
}
