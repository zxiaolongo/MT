package com.demo.zxl.user.mt.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.demo.zxl.user.mt.moudle.bean.Seller;
import com.demo.zxl.user.mt.ui.fragment.GoodsFragment;

public class MyFragmentPagerAdapter2 extends PagerAdapter {
    private Seller seller;
    private String[] str = new String[]{"商品","评价","商家"};
    private Fragment[] fragments;
    private FragmentManager manager;
 
    public MyFragmentPagerAdapter2(FragmentManager fm, Fragment[] fragments,Seller seller) {
        super();
        manager=fm;
        this.seller = seller;
        this.fragments = fragments;
    }
    //指定tab中文本方法
    @Override
    public CharSequence getPageTitle(int position) {
        return str[position];
    }
 
    @Override
    public int getCount() {
        return fragments.length;
    }
 
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = fragments[position];
        //判断当前的fragment是否已经被添加进入Fragmentanager管理器中
        if (!fragment.isAdded()) {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(fragment, fragment.getClass().getSimpleName());
            //不保存系统参数，自己控制加载的参数
            transaction.commitAllowingStateLoss();
            //手动调用,立刻加载Fragment片段
            manager.executePendingTransactions();
        }
        if (fragment.getView().getParent() == null) {
            //添加布局
            container.addView(fragment.getView());
        }
        return fragment.getView();
    }
 
 
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //移除布局
        container.removeView(fragments[position].getView());
    }
 
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /**
     * @return  通过Fragment数据适配器获取索引位置0上的GoodsFragment对象
     */
    public GoodsFragment getGoodsFragment(){
        GoodsFragment goodsFragment = (GoodsFragment) fragments[0];
        return goodsFragment;
    }
}

