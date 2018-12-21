package com.chexiaoya.keepalive.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * 本地服务
 * Created by xcb on 2018/12/21.
 */
public class LocalService extends Service {

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("LiveTest", "LocalService:onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("LiveTest", "LocalService:onServiceDisconnected");
            reBindRemoteService();
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return new LocalServiceBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // LocalService绑定远程服务RemoteService
        Log.d("LiveTest", "LocalService:onStartCommand");
        bindService(new Intent(this, RemoteService.class), connection, BIND_AUTO_CREATE);
        return super.onStartCommand(intent, flags, startId);
    }


    /**
     * 重新启动并绑定远程服务
     */
    public void reBindRemoteService() {
        startService(new Intent(this, RemoteService.class));
        bindService(new Intent(this, RemoteService.class), connection, BIND_AUTO_CREATE);
    }

    private class LocalServiceBinder extends Binder {
        public LocalService getLocalService() {
            return LocalService.this;
        }
    }
}
