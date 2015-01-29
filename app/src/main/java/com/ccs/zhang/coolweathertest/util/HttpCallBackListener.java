package com.ccs.zhang.coolweathertest.util;

/**
 * Created by zhang on 2015/1/29.
 */
public interface HttpCallBackListener {
    void onFinish(String response);
    void onError(Exception e);
}
