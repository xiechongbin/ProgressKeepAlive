package com.chexiaoya.keepalive.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

/**
 * 启动前台服务
 * Created by xcb on 2018/12/20.
 */
public class ForegroundService extends Service {

    private static final int SERVICE_ID = 1;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //系统版本兼容
        if (Build.VERSION.SDK_INT < 18) {
            //设置成前台服务并且不显示通知栏信息
            startForeground(SERVICE_ID, new Notification());
        } else if (Build.VERSION.SDK_INT < 26) {//Android 4.3-Android 7.x
            startForeground(SERVICE_ID, new Notification());
            //移除消息栏通知
            startService(new Intent(this, InnerService.class));
        } else {//Android 8.0 以上,要设置channelID
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                NotificationChannel channel = new NotificationChannel("channel", "name", NotificationManager.IMPORTANCE_NONE);
                notificationManager.createNotificationChannel(channel);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel");
                //设置前台服务
                //8.0以上app退到后台，会立即弹出通知消息
                // 9.0以上需要添加权限 <uses-permission android:name="android.permission.FOREGROUND_SERVICE"/>s
                startForeground(SERVICE_ID, builder.build());
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 用来移除前台服务消息栏通知
     */
    private static class InnerService extends Service {

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            // 设置前台服务
            startForeground(SERVICE_ID, new Notification());
            // 移除消息栏通知
            stopForeground(true);
            // 结束自身
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }
    }

}
