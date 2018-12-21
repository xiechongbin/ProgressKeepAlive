package com.chexiaoya.keepalive.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.chexiaoya.keepalive.R;
import com.chexiaoya.keepalive.manager.KeepAliveManager;
import com.chexiaoya.keepalive.service.LocalJobTestService;

/**
 * 主Activity
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //方法1：一像素进程保活
        //KeepAliveManager.getInstance().registerBroadcastReceiver(this);

        // 方法2：启动前台服务保活
        //Intent intent = new Intent(this, ForegroundService.class);
        //startService(intent);

        // 方法3：bindService相互拉活
        //Intent intent = new Intent(this, LocalService.class);
        //startService(intent);

        //通过JobScheduler来进程保活
        Intent intent = new Intent(this, LocalJobTestService.class);
        startService(intent);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KeepAliveManager.getInstance().unRegisterBroadcastReceiver(this);
    }
}
