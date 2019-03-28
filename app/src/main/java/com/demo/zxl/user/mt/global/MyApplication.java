package com.demo.zxl.user.mt.global;

import android.app.Application;

import com.mob.MobSDK;


/**
 * Created by HASEE.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        //做sdk初始化
        MobSDK.init(this);
        super.onCreate();

        //9103
    }
}
