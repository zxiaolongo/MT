package com.demo.zxl.user.mt.presenter;

import android.util.Log;

import com.demo.zxl.user.mt.moudle.bean.BusinessInfo;
import com.demo.zxl.user.mt.moudle.bean.GoodsInfo;
import com.demo.zxl.user.mt.moudle.bean.GoodsTypeInfo;
import com.demo.zxl.user.mt.moudle.bean.ResponseInfo;
import com.demo.zxl.user.mt.moudle.bean.Seller;
import com.demo.zxl.user.mt.ui.adapter.GoodsAdapter;
import com.demo.zxl.user.mt.ui.adapter.GoodsTypeAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by HASEE.
 */
public class GoodsPresenter extends BasePresenter{
    private GoodsAdapter goodsAdapter;
    private Seller seller;
    private GoodsTypeAdapter goodsTypeAdapter;
    private BusinessInfo businessInfo;
    private ArrayList<GoodsInfo> allGoodsInfoList;

    public GoodsPresenter(GoodsTypeAdapter goodsTypeAdapter, GoodsAdapter goodsAdapter, Seller seller){
        this.goodsTypeAdapter = goodsTypeAdapter;
        this.goodsAdapter = goodsAdapter;
        this.seller = seller;
    }
    @Override
    protected void sendErrorMessage(String errorMessage) {

    }

    @Override
    protected void parseJson(String json) {
        Log.i("","json = "+json);
        Gson gson = new Gson();
        businessInfo = gson.fromJson(json, BusinessInfo.class);

        goodsTypeAdapter.setData(businessInfo.getList());
        initGoodsInfoList();
        //用商品列表集合填充商品数据适配器
        goodsAdapter.setData(allGoodsInfoList);
    }

    //获取所有商品所在的数据集合
    private void initGoodsInfoList() {
        //商品分类集合
        List<GoodsTypeInfo> goodsTypeInfoList = businessInfo.getList();
        //封装了所有商品的集合
        allGoodsInfoList = new ArrayList<>();
        for (int i = 0; i < goodsTypeInfoList.size(); i++) {
            GoodsTypeInfo goodsTypeInfo = goodsTypeInfoList.get(i);
            //每一个分类下商品集合
            List<GoodsInfo> goodsInfoList = goodsTypeInfo.getList();
            for (int j = 0; j < goodsInfoList.size(); j++) {
                GoodsInfo goodsInfo = goodsInfoList.get(j);
                //因为服务器返回的数据中,没有告知此商品所属分类id,所属分类名称,所属商家,所以需要在此处进行手动维护

                //指定商品的分类id
                goodsInfo.setTypeId(goodsTypeInfo.getId());
                //指定商品分类的名称
                goodsInfo.setTypeName(goodsTypeInfo.getName());
                //指定商品商家id
                goodsInfo.setSellerId((int) seller.getId());
                //商品数量,默认情况下数量为0
                goodsInfo.setCount(0);

                //将所有的商品封装在一个大的集合中
                allGoodsInfoList.add(goodsInfo);
            }
        }
    }

    public void getGoodsInfo(long sellerId){
        Call<ResponseInfo> call = responseInfoApi.getGoodsInfo(sellerId);
        call.enqueue(new CallBackAdapter());
    }
}
