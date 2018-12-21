package com.chexiaoya.keepalive.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class LocalJobTestService extends Service {

    public LocalJobTestService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("LiveTest", "LocalJobTestService:onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("LiveTest", "LocalJobTestService:onStartCommand");
        Intent ret = new Intent(this, LocalJobService.class);
        Bundle bundle = new Bundle();
        bundle.putString("serviceName", this.getClass().getName());
        ret.putExtra("bundle", bundle);
        startService(ret);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d("LiveTest", "LocalJobTestService:onDestroy");
        super.onDestroy();
    }
}
