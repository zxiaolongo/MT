package com.demo.zxl.user.mt.global;

import android.app.Application;


import com.demo.zxl.user.mt.db.DBHelper;
import com.demo.zxl.user.mt.moudle.bean.UserInfo;
import com.j256.ormlite.dao.Dao;
import com.mob.MobSDK;

import java.sql.SQLException;
import java.util.List;

import cn.smssdk.SMSSDK;

/**
 * Created by HASEE.
 */

public class MyApplication extends Application {
    //-1没有登录用户状态
    //如果现在有一个用户A ,在登录的状态下退出应用,退出后内存就释放了
    //当再次打开应用的时候,因为没有做登录请求的发送,所以userId的值,就是MyApplication记录的初始值-1
    public static int userId = -1;
    public static int statusBarHeight = 0;

    @Override
    public void onCreate() {
        //做sdk初始化
        MobSDK.init(this);
        super.onCreate();

        //获取status_bar_height资源的ID
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }

        //9103
        //需要判断目前手机是否有登录用户  token  isLogin 1
        //判读目前手机中是否有isLogin为1的数据
        DBHelper dbHelper = new DBHelper(this);
        Dao<UserInfo,Integer> dao = dbHelper.getDao(UserInfo.class);
        //select * from table where isLogin = 1;
        //query 方法就是告知此sql需要触发
        try {
            List<UserInfo> userInfoList = dao.queryBuilder().where().eq("isLogin", 1).query();
            if (userInfoList!=null && userInfoList.size()>0){
                UserInfo userInfo = userInfoList.get(0);
                MyApplication.userId = userInfo.get_id();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
