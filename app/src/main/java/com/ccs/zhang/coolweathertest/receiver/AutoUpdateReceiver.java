package com.ccs.zhang.coolweathertest.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ccs.zhang.coolweathertest.service.AutoUpdateService;

/**
 * Created by zhang on 2015/2/1.
 */
public class AutoUpdateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, AutoUpdateService.class);
        context.startService(i);
    }
}
