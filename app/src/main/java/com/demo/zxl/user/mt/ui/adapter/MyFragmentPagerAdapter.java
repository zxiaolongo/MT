package com.demo.zxl.user.mt.ui.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import com.demo.zxl.user.mt.moudle.bean.Seller;
import com.demo.zxl.user.mt.ui.fragment.GoodsFragment;
import com.demo.zxl.user.mt.ui.fragment.SellerFragment;
import com.demo.zxl.user.mt.ui.fragment.SuggestFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HASEE.
 */

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private Seller seller;
    private String[] str = new String[]{"商品","评价","商家"};
    private List<Fragment> fragmentList = new ArrayList<>();
    public MyFragmentPagerAdapter(FragmentManager fm, Seller seller) {
        super(fm);
        this.seller = seller;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new GoodsFragment();
                break;
            case 1:
                fragment = new SuggestFragment();
                break;
            case 2:
                fragment = new SellerFragment();
                break;
        }
        fragmentList.add(fragment);
        //activity如何给fragment传递数据

        //创建一个邮包
        Bundle bundle = new Bundle();
        //向邮包中放置一个实现了序列化接口的对象
        bundle.putSerializable("seller",seller);
        //将对象设置给fragment
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return str.length;
    }

    //指定tab中文本方法
    @Override
    public CharSequence getPageTitle(int position) {
        return str[position];
    }

    /**
     * @return  通过Fragment数据适配器获取索引位置0上的GoodsFragment对象
     */
    public GoodsFragment getGoodsFragment(){
        GoodsFragment goodsFragment = (GoodsFragment) fragmentList.get(0);
        return goodsFragment;
    }
}
