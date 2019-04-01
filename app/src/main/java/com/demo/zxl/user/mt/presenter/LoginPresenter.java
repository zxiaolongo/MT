package com.demo.zxl.user.mt.presenter;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;

import com.demo.zxl.user.mt.db.DBHelper;
import com.demo.zxl.user.mt.global.MyApplication;
import com.demo.zxl.user.mt.moudle.bean.ResponseInfo;
import com.demo.zxl.user.mt.moudle.bean.UserInfo;
import com.demo.zxl.user.mt.ui.activity.LoginActivity;
import com.google.gson.Gson;

import com.j256.ormlite.android.AndroidDatabaseConnection;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.List;

import retrofit2.Call;


/**
 * Created by HASEE.
 */
public class LoginPresenter extends BasePresenter{
    private LoginActivity activity;

    public LoginPresenter(LoginActivity activity){
        this.activity = activity;
    }
    @Override
    protected void sendErrorMessage(String errorMessage) {

    }

    @Override
    protected void parseJson(String json) {
        registerOrLogin(json);
    }

    private void registerOrLogin(String json) {
        Gson gson = new Gson();
        //获取登录用户信息的对象
        UserInfo userInfo = gson.fromJson(json, UserInfo.class);
        //将登录用户的id记录在application中
        MyApplication.userId = userInfo.get_id();
        //将登录用户的信息维护到数据库中,
        // (1,注册使用,目前这个用户还没有注册过,现在将其注册登录进来,数据需要存储在数据库中
        // 2,切换登录用户使用,  A目前处于登录状态,A的登录退出,B的登录完成
        // 3,目前没有登录用户,将某一个用户A登录进来)

        //1.将数据库表中的所有isLogin的值都修改为0,代表将所有的用户退出登录
        DBHelper dbHelper = new DBHelper(activity);
        Dao<UserInfo,Integer> dao = dbHelper.getDao(UserInfo.class);
        //2.查询用户信息表中的所有数据
        AndroidDatabaseConnection connection = null;
        Savepoint savepoint = null;
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            connection = new AndroidDatabaseConnection(db, true);
            //设置事物的回滚点
            savepoint = connection.setSavePoint("start");
            //告知ormlite不需要由框架管理事物,由自己管理事物
            connection.setAutoCommit(false);
            List<UserInfo> userInfoList = dao.queryForAll();
            for (int i = 0; i < userInfoList.size(); i++) {
                UserInfo info = userInfoList.get(i);
                info.setLogin(0);
                //将变化的数据,更新到数据库中去
                dao.update(info);
            }
            //3.判断目前登录进来的这个用户是老用户(数据库中有存储他的数据)还是新用户(数据库里面没有存储过他的数据)
            int id = userInfo.get_id();
            //根据目前登录用户的id在数据库中进行查询
            UserInfo userBean = dao.queryForId(id);
            if (userBean == null){
                //新用户,插入数据到数据库中
                userInfo.setLogin(1);
                dao.create(userInfo);
            }else{
                //老用户,更新已有数据
                userBean.setLogin(1);
                userBean.setDiscount(userInfo.getDiscount());
                userBean.setBalance(userInfo.getDiscount());
                userBean.setName(userInfo.getName());
                userBean.setIntegral(userInfo.getIntegral());
                userBean.setPhone(userInfo.getPhone());
                userBean.set_id(userInfo.get_id());
                dao.update(userBean);
            }
            //提交事物
            connection.commit(savepoint);
            //用户等没有问题,结束登录界面
            activity.finish();
        } catch (SQLException e) {
            e.printStackTrace();
            //如果出现异常,回滚事物,在savePoint后执行的所有的sql语句都不生效
            try {
                connection.rollback(savepoint);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void getLoginUserInfo(String username,String psd,String phone,int type){
        Call<ResponseInfo> call = responseInfoApi.getLoginUserInfo(username, psd, phone, type);
        call.enqueue(new CallBackAdapter());
    }
}
