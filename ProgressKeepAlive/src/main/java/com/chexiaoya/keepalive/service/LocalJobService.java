package com.chexiaoya.keepalive.service;

import android.app.ActivityManager;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

/**
 * jobService调度任务
 * Created by xcb on 2018/12/21.
 */
public class LocalJobService extends JobService {
    private int JOD_ID = 0;
    private String serviceClassName = "";

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d("LiveTest", "LocalJobService:onStartJob");
        try {
            Class service = getClassLoader().loadClass(serviceClassName);
            if (service != null) {
                if (isNeedKeepAliveServiceAlive(service)) {
                    Log.d("LiveTest", "LocalJobTestService正在运行中");
                } else {
                    Log.d("LiveTest", "LocalJobTestService已经死掉,重新拉活");
                    Intent intent = new Intent(this, LocalJobService.class);
                    startService(intent);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            //重新启动下一个任务
            startJobScheduler();
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d("LiveTest", "LocalJobService:onStopJob");
        return false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent.getBundleExtra("bundle");
        if (bundle != null) {
            serviceClassName = bundle.getString("serviceName");
        }
        startJobScheduler();
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 开启JobScheduler任务调度
     */
    private void startJobScheduler() {
        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.cancelAll();
        ComponentName componentName = new ComponentName(getPackageName(), LocalJobService.class.getName());
        JobInfo.Builder builder = new JobInfo.Builder(JOD_ID++, componentName);

        //表示间隔一定时间执行该任务，在version>=android N的系统版本中，这个值只能是15分钟以上
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            builder.setOverrideDeadline(5 * 1000);
        } else {
            builder.setPeriodic(5 * 1000);// 每隔一段时间执行 7.0以上的系统这个功能要大于15分钟以上
        }

        builder.setPersisted(true);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        int schedule = jobScheduler.schedule(builder.build());
        if (schedule <= 0) {
            Log.d("LiveTest", "schedule error！");
        }
    }

    /**
     * 判断服务是否还在运行
     */
    public boolean isNeedKeepAliveServiceAlive(Class<?> serviceClass) {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            for (ActivityManager.RunningServiceInfo runningServiceInfo : activityManager.getRunningServices(Integer.MAX_VALUE)) {
                if (runningServiceInfo.service.getClassName().equals(serviceClass.getName())) {
                    return true;
                }
            }
        }
        return false;
    }
}
