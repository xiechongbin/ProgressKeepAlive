package com.chexiaoya.keepalive.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * 远程服务 运行在remote进程中
 * Created by xcb on 2018/12/20.
 */
public class RemoteService extends Service {
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("LiveTest", "RemoteService:onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("LiveTest", "RemoteService:onServiceDisconnected");
            reBindLocalService();
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return new RemoteServiceBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        bindService(new Intent(this, LocalService.class), connection, BIND_AUTO_CREATE);
        Log.d("LiveTest", "RemoteService:onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 重新启动并绑定Local服务
     */
    public void reBindLocalService() {
        startService(new Intent(this, LocalService.class));
        bindService(new Intent(this, LocalService.class), connection, BIND_AUTO_CREATE);
    }

    private class RemoteServiceBinder extends Binder {
        public RemoteService getLocalService() {
            return RemoteService.this;
        }
    }
}
