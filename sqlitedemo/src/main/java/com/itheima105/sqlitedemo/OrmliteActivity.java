package com.itheima105.sqlitedemo;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.j256.ormlite.android.AndroidDatabaseConnection;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.query.In;

import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by HASEE.
 */

public class OrmliteActivity extends Activity {
    @BindView(R.id.btn_insert)
    Button btnInsert;
    @BindView(R.id.btn_delete)
    Button btnDelete;
    @BindView(R.id.btn_query)
    Button btnQuery;
    @BindView(R.id.btn_insert_t)
    Button btnInsertT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ormlite);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_insert, R.id.btn_delete, R.id.btn_query, R.id.btn_insert_t})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_insert:
                insert();
                break;
            case R.id.btn_delete:
                delete();
                break;
            case R.id.btn_query:
                query();
                break;
            case R.id.btn_insert_t:
                insertTransaction();
                break;
        }
    }

    private void insertTransaction() {
        AndroidDatabaseConnection connection = null;
        Savepoint savepoint = null;
        try {
//        dao对象操作t_user表中数据
            OrmLiteDBHelper ormLiteDBHelper = new OrmLiteDBHelper(this);
            Dao<UserInfo,Integer> dao = ormLiteDBHelper.getDao(UserInfo.class);

            //获取数据库对象
            SQLiteDatabase db = ormLiteDBHelper.getWritableDatabase();
            //创建一个android下链接数据库的对象
            connection = new AndroidDatabaseConnection(db, true);
            //事物回滚点(保存点)
            savepoint = connection.setSavePoint("start");
            //告知ormlite不要管理事物,事物交由自己进行处理
            connection.setAutoCommit(false);
            for (int i = 0; i < 10; i++) {
                UserInfo userInfo = new UserInfo();
                userInfo.setUsername("aaa"+i);
                //如果代码中有异常则插入操作全部失败
                if (i == 5){
                    int a = 1/0;
                }
                dao.create(userInfo);
            }
            //事物的提交位置
            connection.commit(savepoint);
        } catch (Exception e) {
            e.printStackTrace();
            //在捕获异常之前的数据的插入都是无效的,数据库回滚rollback
            try {
                connection.rollback(savepoint);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void query() {
        try {
            OrmLiteDBHelper ormLiteDBHelper = new OrmLiteDBHelper(this);
            Dao<UserInfo,Integer> dao = ormLiteDBHelper.getDao(UserInfo.class);
            //查询插入的所有数据
            List<UserInfo> userInfoList = dao.queryForAll();
            for (int i = 0; i < userInfoList.size(); i++) {
                UserInfo userInfo = userInfoList.get(i);
                Log.i("",userInfo.getUsername());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void delete() {
        //delete 告知根据什么做删除
        OrmLiteDBHelper ormLiteDBHelper = new OrmLiteDBHelper(this);
        Dao<UserInfo,Integer> dao = ormLiteDBHelper.getDao(UserInfo.class);
        for (int i = 0; i < 10; i++) {
            //以下代码是根据userInfo的id进行删除的,因为默认情况下每一个对象的id都是0,和数据库中自增长的id不一致,所以删除失败
            /*UserInfo userInfo = new UserInfo();
            userInfo.setUsername("aaa"+i);
            try {
                //删除失败了???
                dao.delete(userInfo);
            } catch (SQLException e) {
                e.printStackTrace();
            }*/
            try {
                //将创建的每一个对象的username维护进去,尝试让ormlite框架根据username的值进行数据的删除
                //1.获取删除构建sql语句对象
                DeleteBuilder<UserInfo, Integer> deleteBuilder = dao.deleteBuilder();
                //2.告知目前的删除语句,按照那个字段进行删除
                deleteBuilder.where().eq("username","aaa"+i);
                //3.让deleteBuilder直行删除操作
                deleteBuilder.delete();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void insert() {
        try {
//        dao对象操作t_user表中数据
            OrmLiteDBHelper ormLiteDBHelper = new OrmLiteDBHelper(this);
            Dao<UserInfo,Integer> dao = ormLiteDBHelper.getDao(UserInfo.class);

            for (int i = 0; i < 10; i++) {
                UserInfo userInfo = new UserInfo();
                userInfo.setUsername("aaa"+i);
                dao.create(userInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
