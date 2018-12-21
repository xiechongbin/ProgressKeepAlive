package com.chexiaoya.keepalive.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.chexiaoya.keepalive.manager.KeepAliveManager;

/**
 * 屏幕亮或者灭的广播接收者
 * Created by xcb on 2018/12/20.
 */
public class ScreenOnOffBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
            //屏幕灭掉 开启OnePixelActivity
            KeepAliveManager.getInstance().startOnePixelActivity(context);
        } else if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
            //屏幕点亮 关闭OnePixelActivity
            KeepAliveManager.getInstance().finishOnePixelActivity(context);
        }
    }
}
