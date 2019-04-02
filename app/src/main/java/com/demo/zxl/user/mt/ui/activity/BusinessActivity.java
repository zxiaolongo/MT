package com.demo.zxl.user.mt.ui.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.zxl.user.mt.R;
import com.demo.zxl.user.mt.global.MyApplication;
import com.demo.zxl.user.mt.moudle.bean.GoodsInfo;
import com.demo.zxl.user.mt.moudle.bean.Seller;
import com.demo.zxl.user.mt.presenter.BusinessPresenter;
import com.demo.zxl.user.mt.ui.adapter.MyFragmentPagerAdapter;
import com.demo.zxl.user.mt.ui.adapter.MyFragmentPagerAdapter2;
import com.demo.zxl.user.mt.ui.adapter.ShopCartAdapter;
import com.demo.zxl.user.mt.ui.fragment.GoodsFragment;
import com.demo.zxl.user.mt.ui.fragment.SellerFragment;
import com.demo.zxl.user.mt.ui.fragment.SuggestFragment;
import com.demo.zxl.user.mt.util.CountPriceFormater;
import com.flipboard.bottomsheet.BottomSheetLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    public BusinessPresenter businessPresenter;
    public MyFragmentPagerAdapter2 myFragmentPagerAdapter;
    private View sheetView;//点击底部bottom需要显示购物车列表的view对象
    private ShopCartAdapter shopCartAdapter;
    private Seller seller;
    private float floatSendPrice;
    private float floatDeliveryFee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bussiness);
        ButterKnife.bind(this);
        seller = (Seller) getIntent().getSerializableExtra("seller");

        //创建一个BusinessActivity业务逻辑封装类
        businessPresenter = new BusinessPresenter(this);
        //viewpager设置上数据适配器

//        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), seller);
        Fragment[] fragments = new Fragment[3];
        //创建一个邮包
        Bundle bundle = new Bundle();
        //向邮包中放置一个实现了序列化接口的对象
        bundle.putSerializable("seller",seller);
        //将对象设置给fragment
        fragments[0]=new GoodsFragment();
        fragments[0].setArguments(bundle);
        fragments[1]=new SuggestFragment();
        fragments[1].setArguments(bundle);
        fragments[2]=new SellerFragment();
        fragments[2].setArguments(bundle);
         myFragmentPagerAdapter =
                new MyFragmentPagerAdapter2(getSupportFragmentManager(),fragments,seller);

        vp.setAdapter(myFragmentPagerAdapter);
        //让tabLayout和viewpager进行绑定
        tabs.setupWithViewPager(vp);

        //配送费
        String deliveryFee = seller.getDeliveryFee();
        floatDeliveryFee = Float.parseFloat(deliveryFee);
        tvDeliveryFee.setText(CountPriceFormater.format(floatDeliveryFee));

        //起送价
        String sendPrice = seller.getSendPrice();
        floatSendPrice = Float.parseFloat(sendPrice);
        tvSendPrice.setText(CountPriceFormater.format(floatSendPrice));
    }

    /**
     * @param imageView 添加在帧布局中的小球
     * @param width     小球宽度
     * @param height    小球高度
     */
    public void addFlyImage(ImageView imageView, int width, int height) {
        flContainer.addView(imageView, width, height);
    }

    //获取购物车所在的屏幕坐标位置,作为飞行终点
    public int[] getShopCartLocation() {
        int[] shopCartLocation = new int[2];
        imgCart.getLocationInWindow(shopCartLocation);
        return shopCartLocation;
    }

    /**
     * @param totalCount 购买商品总数量
     * @param totalPrice 购买商品中金额
     */
    public void refreshShopCartUI(int totalCount, float totalPrice) {
//        tvSelectNum
//        tvCountPrice
        if (totalCount == 0) {
            tvSelectNum.setVisibility(View.GONE);
            tvCountPrice.setText(CountPriceFormater.format(0.0f));
        } else {
            tvSelectNum.setVisibility(View.VISIBLE);
            tvSelectNum.setText(totalCount + "");
            tvCountPrice.setText(CountPriceFormater.format(totalPrice));
        }

        //起送价需要和购买商品的总金额比对
        if (totalPrice>=floatSendPrice){
            tvSendPrice.setVisibility(View.GONE);
            tvSubmit.setVisibility(View.VISIBLE);
        }else{
            tvSendPrice.setVisibility(View.VISIBLE);
            tvSubmit.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.bottom,R.id.tvSubmit})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.bottom:
                if (sheetView == null){
                  sheetView = onCreateSheetView();
                }
                if (bottomSheetLayout.isSheetShowing()){
                    //点击前对话框显示,点击后就需要隐藏
                    bottomSheetLayout.dismissSheet();
                }else{
                    //每次在显示购物车列表之前,都必须将购物车中最新的数据获取出来,用作显示
                    List<GoodsInfo> shopCartDataList = businessPresenter.getShopCartDataList();
                    if(shopCartDataList!=null && shopCartDataList.size()>0){
                        //最新获取的购物车中的商品集合,同步到数据适配器中显示
                        shopCartAdapter.setData(shopCartDataList);
                        //点击前对话框隐藏,点击后就需要显示
                        bottomSheetLayout.showWithSheetView(sheetView);
                    }
                }
                break;
            case R.id.tvSubmit:
                if(MyApplication.userId != -1){
                    //下单,怎么下单,发请求给服务器,服务器给计算总金额,并且告知每一个商品的最新单价
                    Intent intent = new Intent(BusinessActivity.this, ConfirmOrderActivity.class);
                    ArrayList<GoodsInfo> shopCartDataList = (ArrayList<GoodsInfo>) businessPresenter.getShopCartDataList();
                    //配送费传递给后一个界面
                    intent.putExtra("deliveryFee",floatDeliveryFee);
                    intent.putExtra("shopCartList",shopCartDataList);
                    startActivity(intent);
                }else{
                    //让用户登录
                    Intent intent = new Intent(BusinessActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

    private View onCreateSheetView() {
        View view = View.inflate(this,R.layout.cart_list,null);
        RecyclerView rvCart = (RecyclerView) view.findViewById(R.id.rvCart);

        List<GoodsInfo> shopCartDataList = businessPresenter.getShopCartDataList();
        //购物车中选中商品列表集合
        shopCartAdapter = new ShopCartAdapter(this,shopCartDataList);
        rvCart.setLayoutManager(new LinearLayoutManager(this));
        rvCart.setAdapter(shopCartAdapter);

        TextView tvClear = (TextView) view.findViewById(R.id.tvClear);
        tvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BusinessActivity.this);
                builder.setTitle("是否清空?");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //清空页面中的所有的数据
                        businessPresenter.clearAllData();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
        return view;
    }

    public void hiddenSheetView() {
        //如果目前对话框是显示状态,则需要进行隐藏
        if (bottomSheetLayout.isSheetShowing()){
            bottomSheetLayout.dismissSheet();
        }
    }
}
