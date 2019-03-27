package com.demo.zxl.user.mt.ui.activity;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.demo.zxl.user.mt.R;
import com.demo.zxl.user.mt.moudle.bean.Seller;
import com.demo.zxl.user.mt.ui.adapter.MyFragmentPagerAdapter;
import com.flipboard.bottomsheet.BottomSheetLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HASEE.
 */
public class BusinessActivity extends FragmentActivity {
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ib_menu)
    ImageButton ibMenu;

    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.vp)
    ViewPager vp;

    @BindView(R.id.bottomSheetLayout)
    BottomSheetLayout bottomSheetLayout;//用于放置购物车对话框容器

    @BindView(R.id.imgCart)
    ImageView imgCart;
    @BindView(R.id.tvSelectNum)
    TextView tvSelectNum;
    @BindView(R.id.tvCountPrice)
    TextView tvCountPrice;
    @BindView(R.id.tvDeliveryFee)
    TextView tvDeliveryFee;
    @BindView(R.id.tvSendPrice)
    TextView tvSendPrice;
    @BindView(R.id.tvSubmit)
    TextView tvSubmit;
    @BindView(R.id.bottom)
    LinearLayout bottom;
    @BindView(R.id.fl_Container)
    FrameLayout flContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bussiness);
        ButterKnife.bind(this);
        Seller seller = (Seller) getIntent().getSerializableExtra("seller");
        //viewpager设置上数据适配器

        MyFragmentPagerAdapter myFragmentPagerAdapter
                = new MyFragmentPagerAdapter(getSupportFragmentManager());
        vp.setAdapter(myFragmentPagerAdapter);
        //让tabLayout和viewpager进行绑定
        tabs.setupWithViewPager(vp);
    }
}
