package com.demo.zxl.user.mt.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.demo.zxl.user.mt.R;
import com.demo.zxl.user.mt.moudle.bean.GoodsInfo;
import com.demo.zxl.user.mt.util.CountPriceFormater;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HASEE.
 */
public class ConfirmOrderActivity extends Activity {
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.iv_location)
    ImageView ivLocation;
    @BindView(R.id.tv_hint_select_receipt_address)
    TextView tvHintSelectReceiptAddress;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_label)
    TextView tvLabel;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.ll_receipt_address)
    LinearLayout llReceiptAddress;
    @BindView(R.id.iv_arrow)
    ImageView ivArrow;
    @BindView(R.id.rl_location)
    RelativeLayout rlLocation;
    @BindView(R.id.iv_icon)
    ImageView ivIcon;
    @BindView(R.id.tv_seller_name)
    TextView tvSellerName;
    @BindView(R.id.ll_select_goods)
    LinearLayout llSelectGoods;
    @BindView(R.id.tv_deliveryFee)
    TextView tvDeliveryFee;
    @BindView(R.id.tv_CountPrice)
    TextView tvCountPrice;
    @BindView(R.id.tvSubmit)
    TextView tvSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        ButterKnife.bind(this);
        //购买商品的数据集合
        List<GoodsInfo> shopCartList
                = (List<GoodsInfo>) getIntent().getSerializableExtra("shopCartList");
        //配送费
        float floatDeliveryFee
                = getIntent().getFloatExtra("deliveryFee", 0.0f);

        //设置配送费
        tvDeliveryFee.setText(CountPriceFormater.format(floatDeliveryFee));
        //llSelectGoods放置购买商品列表的线性布局
        llSelectGoods.removeAllViews();
        float totalPrice  = 0.0f;
        for (int i = 0; i < shopCartList.size(); i++) {
            View view = View.inflate(this,R.layout.item_confirm_order_goods,null);
            TextView tvName = (TextView) view.findViewById(R.id.tv_name);
            TextView tvCount = (TextView) view.findViewById(R.id.tv_count);
            TextView tvPrice = (TextView) view.findViewById(R.id.tv_price);
            GoodsInfo goodsInfo = shopCartList.get(i);

            tvName.setText(goodsInfo.getName());
            tvCount.setText(goodsInfo.getCount()+"");

            float price = goodsInfo.getCount() * goodsInfo.getNewPrice();
            tvPrice.setText(CountPriceFormater.format(price));

            totalPrice += price;

            llSelectGoods.addView(view);
        }

        totalPrice += floatDeliveryFee;
        tvCountPrice.setText(CountPriceFormater.format(totalPrice));
    }
}
