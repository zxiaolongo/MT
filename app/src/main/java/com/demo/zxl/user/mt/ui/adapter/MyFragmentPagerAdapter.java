package com.demo.zxl.user.mt.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.demo.zxl.user.mt.ui.fragment.GoodsFragment;
import com.demo.zxl.user.mt.ui.fragment.SellerFragment;
import com.demo.zxl.user.mt.ui.fragment.SuggestFragment;


/**
 * Created by HASEE.
 */

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private String[] str = new String[]{"商品","评价","商家"};
    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
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
}
