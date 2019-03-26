package com.demo.zxl.user.mt.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


import com.demo.zxl.user.mt.R;
import com.demo.zxl.user.mt.ui.fragment.BaseFragment;
import com.demo.zxl.user.mt.ui.fragment.HomeFragment;
import com.demo.zxl.user.mt.ui.fragment.MoreFragment;
import com.demo.zxl.user.mt.ui.fragment.OrderFragment;
import com.demo.zxl.user.mt.ui.fragment.UserFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends FragmentActivity implements View.OnClickListener {
    @BindView(R.id.main_fragment_container)
    FrameLayout mainFragmentContainer;

    @BindView(R.id.main_bottome_switcher_container)
    LinearLayout mainBottomeSwitcherContainer;

    private ArrayList<BaseFragment> fragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //提供用于切换的多个fragment
        initFragment();
        initClick();
        //默认选中首页模块
        View view = mainBottomeSwitcherContainer.getChildAt(0);
        onClick(view);
    }

    private void initFragment() {
        fragmentList = new ArrayList<>();

        fragmentList.add(new HomeFragment());
        fragmentList.add(new OrderFragment());
        fragmentList.add(new UserFragment());
        fragmentList.add(new MoreFragment());
    }

    private void initClick() {
        //获取所有的孩子节点总数
        int childCount = mainBottomeSwitcherContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            //根据索引i获取孩子节点对象
            View childFrameLayout = mainBottomeSwitcherContainer.getChildAt(i);
            childFrameLayout.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        //根据孩子view对象,判断此view对象所在线性布局索引位置
        int indexOfChild = mainBottomeSwitcherContainer.indexOfChild(v);
        //根据索引切换选中的fragment
        changeFragment(indexOfChild);
        //根据索引切换选中按钮状态(颜色)
        changeUI(indexOfChild);
    }

    private void changeUI(int indexOfChild) {
        int childCount = mainBottomeSwitcherContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = mainBottomeSwitcherContainer.getChildAt(i);
            if (i == indexOfChild){
                //遍历到了选中的索引位置帧布局对象
                setEnable(childView,false);
            }else{
                setEnable(childView,true);
            }
        }
    }

    /**
     * @param childView     放置多个控件的容器
     * @param isEnable      容器以及内部的孩子控件是否可用
     */
    private void setEnable(View childView, boolean isEnable) {
        childView.setEnabled(isEnable);
        if (childView instanceof ViewGroup){
            int childCount = ((ViewGroup) childView).getChildCount();
            for (int i = 0; i < childCount; i++) {
                View view = ((ViewGroup) childView).getChildAt(i);
                view.setEnabled(isEnable);
            }
        }
    }

    /**
     * @param index  需要切换到索引位置
     */
    private void changeFragment(int index) {
        BaseFragment baseFragment = fragmentList.get(index);
        //1.获取fragment管理者对象
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container,baseFragment).commit();
    }
}
