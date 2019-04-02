package com.demo.zxl.user.mt.presenter;



import com.demo.zxl.user.mt.moudle.bean.GoodsInfo;
import com.demo.zxl.user.mt.moudle.bean.GoodsTypeInfo;
import com.demo.zxl.user.mt.ui.activity.BusinessActivity;
import com.demo.zxl.user.mt.ui.adapter.GoodsAdapter;
import com.demo.zxl.user.mt.ui.fragment.GoodsFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HASEE.
 */
public class BusinessPresenter extends BasePresenter{
    private BusinessActivity businessActivity;

    public BusinessPresenter(BusinessActivity activity){
        this.businessActivity = activity;
    }
    @Override
    protected void sendErrorMessage(String errorMessage) {

    }

    @Override
    protected void parseJson(String json) {

    }

    //计算购物车中商品的总数量和总金额
    public void refreshShopCartData() {
        int totalCount = 0;
        float totalPrice = 0.0f;
        //购物车中气泡数量 == 商品列表中选中商品数量的总和
        //选中商品列表集合计算总数量和总金额
        //businessActivity---(MyFragmentPagerAdapter)-->GoodsFragment------->(商品分类数据适配(分类集合),商品数据适配器(商品集合))

        GoodsFragment goodsFragment = businessActivity.myFragmentPagerAdapter.getGoodsFragment();
        if (goodsFragment!=null){
            //商品数据适配器
            GoodsAdapter goodsAdapter = goodsFragment.goodsAdapter;
            //商品数据适配器中的商品列表集合
            ArrayList<GoodsInfo> goodsInfoList = goodsAdapter.getData();
            //根据商品列表集合,获取购买商品的总数量和总金额
            for (int i = 0; i < goodsInfoList.size(); i++) {
                GoodsInfo goodsInfo = goodsInfoList.get(i);
                if (goodsInfo.getCount()>0){
                    totalCount += goodsInfo.getCount();
                    totalPrice += goodsInfo.getCount()*goodsInfo.getNewPrice();
                }
            }
//            Log.i("","====================totalCount = "+totalCount);
//            Log.i("","====================totalPrice = "+totalPrice);
            //告知businessActivity显示计算出来的总金额和中数量
            businessActivity.refreshShopCartUI(totalCount,totalPrice);
        }
    }

    //获取购物车中购买商品集合方法
    public List<GoodsInfo> getShopCartDataList(){
        ArrayList<GoodsInfo> shopCartList = new ArrayList<>();
        //获取包含了商品数据适配器的fragment对象
        GoodsFragment goodsFragment = businessActivity.myFragmentPagerAdapter.getGoodsFragment();
        //通过fragment对象获取右侧商品数据适配器
        if (goodsFragment!=null){
            //商品列表集合
            ArrayList<GoodsInfo> goodsInfoList = goodsFragment.goodsAdapter.getData();
            for (int i = 0; i < goodsInfoList.size(); i++) {
                GoodsInfo goodsInfo = goodsInfoList.get(i);
                if (goodsInfo.getCount()>0){
                    //goodsInfo对象有选中商品,将选中商品的对象添加到一个集合中
                    shopCartList.add(goodsInfo);
                }
            }
            return shopCartList;
        }
        return  null;
    }

    public void clearAllData() {
        //1.商品列表没有商品被选中
        clearGoodsAdapterData();
        //2.商品分类没有任何气泡显示
        clearGoodsTypeAdapterData();
        //3.购物车列表中没有任何数据,并且购物车对话框隐藏
        clearShopCartData();
        //4.购物车总数和总金额为0
        businessActivity.refreshShopCartUI(0,0.0f);
    }

    private void clearShopCartData() {
        List<GoodsInfo> shopCartDataList = getShopCartDataList();
        shopCartDataList.clear();
        businessActivity.hiddenSheetView();
    }

    private void clearGoodsAdapterData() {
        //获取包含了商品数据适配器的fragment对象
        GoodsFragment goodsFragment = businessActivity.myFragmentPagerAdapter.getGoodsFragment();
        //通过fragment对象获取右侧商品数据适配器
        if (goodsFragment!=null){
            //商品列表集合
            ArrayList<GoodsInfo> goodsInfoList = goodsFragment.goodsAdapter.getData();
            for (int i = 0; i < goodsInfoList.size(); i++) {
                GoodsInfo goodsInfo = goodsInfoList.get(i);
                if (goodsInfo.getCount()>0){
                    //goodsInfo对象有选中商品,将选中商品的对象添加到一个集合中
                    goodsInfo.setCount(0);
                }
            }
            goodsFragment.goodsAdapter.notifyDataSetChanged();
        }
    }

    private void clearGoodsTypeAdapterData() {
        GoodsFragment goodsFragment = businessActivity.myFragmentPagerAdapter.getGoodsFragment();
        if (goodsFragment!=null){
            List<GoodsTypeInfo> goodsTypeInfoList = goodsFragment.goodsTypeAdapter.getData();
            for (int i = 0; i < goodsTypeInfoList.size(); i++) {
                GoodsTypeInfo goodsTypeInfo = goodsTypeInfoList.get(i);
                if(goodsTypeInfo.getCount()>0){
                    goodsTypeInfo.setCount(0);
                }
            }
            goodsFragment.goodsTypeAdapter.notifyDataSetChanged();
        }
    }
}
