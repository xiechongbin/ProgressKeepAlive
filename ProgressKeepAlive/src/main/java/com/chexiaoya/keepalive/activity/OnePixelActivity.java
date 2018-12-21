package com.chexiaoya.keepalive.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.chexiaoya.keepalive.R;
import com.chexiaoya.keepalive.manager.KeepAliveManager;

/**
 * 1像素Activity进程保活  通过关闭和开启屏幕 启动一像素Activity 来降低oom_adj的值来提升进程的优先级
 * cat/proc/进程号/oom_adj
 * oom_adj 越低 进程的优先级越高
 * Created by xcb on 2018/12/20.
 */
public class OnePixelActivity extends AppCompatActivity {

    /**
     * 启动activity
     */
    public static void launchActivity(Context context) {
        Intent intent = new Intent(context, OnePixelActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initOnePixelActivity();
        setContentView(R.layout.activity_one_pixel);
        Log.d("onePixel", "开启一像素Activity");
        KeepAliveManager.getInstance().setOnePixelActivity(this);
    }

    /**
     * 初始化一个一像素的Activity
     */
    private void initOnePixelActivity() {
        //左上角显示一像素
        Window window = getWindow();
        window.setGravity(Gravity.START | Gravity.TOP);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.x = 0;
        layoutParams.y = 0;
        layoutParams.width = 1;
        layoutParams.height = 1;
        window.setAttributes(layoutParams);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("onePixel", "结束一像素Activity");
    }

}
