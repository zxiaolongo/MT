package com.demo.zxl.user.mt.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.zxl.user.mt.R;
import com.demo.zxl.user.mt.db.DBHelper;
import com.demo.zxl.user.mt.global.MyApplication;
import com.demo.zxl.user.mt.moudle.bean.UserInfo;
import com.demo.zxl.user.mt.ui.activity.LoginActivity;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by HASEE.
 */
public class UserFragment extends BaseFragment {
    @BindView(R.id.tv_user_setting)
    ImageView tvUserSetting;
    @BindView(R.id.iv_user_notice)
    ImageView ivUserNotice;
    @BindView(R.id.login)
    ImageView login;
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.ll_userinfo)
    LinearLayout llUserinfo;
    @BindView(R.id.iv_address)
    ImageView ivAddress;
    Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        //将登录用户的信息展示一下
        if (MyApplication.userId != -1){
            try {
                //有登录进来的用户,显示登录用户信息
                login.setVisibility(View.GONE);
                llUserinfo.setVisibility(View.VISIBLE);

                DBHelper dbHelper = new DBHelper(getActivity());
                Dao<UserInfo,Integer> dao = dbHelper.getDao(UserInfo.class);
                UserInfo userInfo = dao.queryForId(MyApplication.userId);
                //显示登录用户名称和手机号
                username.setText(userInfo.getName());
                phone.setText(userInfo.getPhone());

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            //没有登录用户,提示用户登录
            login.setVisibility(View.VISIBLE);
            llUserinfo.setVisibility(View.GONE);
        }
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_user, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.login)
    public void onViewClicked() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }
}
