package com.demo.zxl.user.mt.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


import com.demo.zxl.user.mt.moudle.bean.ReceiptAddressBean;
import com.demo.zxl.user.mt.moudle.bean.UserInfo;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by HASEE.
 */

public class DBHelper extends OrmLiteSqliteOpenHelper {
    private HashMap<String,Dao> daoMap = new HashMap<>();
    public DBHelper(Context context) {
        super(context, "takeout.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        //将数据库中所有的表给创建好
        try {
            //1.存储用户信息的表
            TableUtils.createTable(connectionSource, UserInfo.class);
            //2.存储用户送货地址表
            TableUtils.createTable(connectionSource, ReceiptAddressBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }

    //获取dao对象方法
    public Dao getDao(Class clazz){
        Dao dao = daoMap.get(clazz.getSimpleName());
        try {
            if (dao == null){
                dao = super.getDao(clazz);
                daoMap.put(clazz.getSimpleName(),dao);
            }
            return dao;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
