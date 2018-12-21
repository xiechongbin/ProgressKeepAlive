package com.chexiaoya.keepalive.manager;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.chexiaoya.keepalive.activity.OnePixelActivity;
import com.chexiaoya.keepalive.broadcast.ScreenOnOffBroadcastReceiver;

import java.lang.ref.WeakReference;

/**
 * 1像素进程守护管理
 * Created by xcb on 2018/12/20.
 */
public class KeepAliveManager {
    private static KeepAliveManager ourInstance;

    private ScreenOnOffBroadcastReceiver registerReceiver;

    private WeakReference<OnePixelActivity> weakReference;

    private KeepAliveManager() {

    }

    /**
     * 单例
     */
    public static KeepAliveManager getInstance() {
        if (ourInstance == null) {
            synchronized (KeepAliveManager.class) {
                if (ourInstance == null) {
                    ourInstance = new KeepAliveManager();
                }
            }
        }
        return ourInstance;
    }

    /**
     * 注册广播
     */
    public void registerBroadcastReceiver(Context context) {
        if (context != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Intent.ACTION_SCREEN_ON);
            intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
            registerReceiver = new ScreenOnOffBroadcastReceiver();
            context.registerReceiver(registerReceiver, intentFilter);
        }
    }

    /**
     * 反注册广播
     */
    public void unRegisterBroadcastReceiver(Context context) {
        if (context != null) {
            context.unregisterReceiver(registerReceiver);
        }
    }

    /**
     * 保存activity
     */
    public void setOnePixelActivity(OnePixelActivity activity) {
        weakReference = new WeakReference<>(activity);
    }

    /**
     * 启动一像素activity
     */
    public void startOnePixelActivity(Context context) {
        OnePixelActivity.launchActivity(context);
    }

    /**
     * 结束一像素activity
     */
    public void finishOnePixelActivity(Context context) {
        if (weakReference != null && weakReference.get() != null) {
            weakReference.get().finish();
        }
    }
}
