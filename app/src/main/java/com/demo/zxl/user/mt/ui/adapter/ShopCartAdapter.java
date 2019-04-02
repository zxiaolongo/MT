package com.demo.zxl.user.mt.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.zxl.user.mt.R;
import com.demo.zxl.user.mt.moudle.bean.GoodsInfo;
import com.demo.zxl.user.mt.moudle.bean.GoodsTypeInfo;
import com.demo.zxl.user.mt.ui.activity.BusinessActivity;
import com.demo.zxl.user.mt.ui.fragment.GoodsFragment;
import com.demo.zxl.user.mt.util.Constant;
import com.demo.zxl.user.mt.util.CountPriceFormater;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by HASEE.
 */
public class ShopCartAdapter extends RecyclerView.Adapter {
    private BusinessActivity businessActivity;
    private List<GoodsInfo> data;

    public ShopCartAdapter(BusinessActivity businessActivity, List<GoodsInfo> shopCartDataList) {
        this.businessActivity = businessActivity;
        this.data = shopCartDataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(businessActivity, R.layout.item_cart, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        GoodsInfo goodsInfo = data.get(position);
        //设置商品名称
        ((ViewHolder) holder).tvName.setText(goodsInfo.getName());
        ((ViewHolder) holder).tvCount.setText(goodsInfo.getCount() + "");
        float goodsTotalPrice = goodsInfo.getCount() * goodsInfo.getNewPrice();
        ((ViewHolder) holder).tvTypeAllPrice.setText(CountPriceFormater.format(goodsTotalPrice));

        ((ViewHolder) holder).setPosition(position);
    }

    @Override
    public int getItemCount() {
        if (data != null && data.size() > 0) {
            return data.size();
        }
        return 0;
    }

    public void setData(List<GoodsInfo> data) {
        this.data = data;
        //根据目前传递进来的data对页面进行刷新
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_type_all_price)
        TextView tvTypeAllPrice;
        @BindView(R.id.ib_minus)
        ImageButton ibMinus;
        @BindView(R.id.tv_count)
        TextView tvCount;
        @BindView(R.id.ib_add)
        ImageButton ibAdd;
        @BindView(R.id.ll)
        LinearLayout ll;
        private int operation = Constant.ADD;
        private int position;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick({R.id.ib_minus,R.id.ib_add})
        public void onClick(View view){
            GoodsInfo goodsInfo = data.get(position);
            switch (view.getId()){
                case R.id.ib_add:
                    operation = Constant.ADD;
                    //商品列表数据发生变化
                    refreshGoodsAdapter(operation,goodsInfo.getId());
                    //商品分类数据发生变化
                    refreshGoodsTypeAdapter(operation,goodsInfo.getTypeId());
                    //购物车列表数据发生变化
                    notifyDataSetChanged();
                    //购物车总金额和总数量发生变化
                    businessActivity.businessPresenter.refreshShopCartData();
                    break;
                case R.id.ib_minus:
                    operation = Constant.DELETE;
                    refreshGoodsAdapter(operation,goodsInfo.getId());
                    refreshGoodsTypeAdapter(operation,goodsInfo.getTypeId());
                    //如果现在操作的商品对象数量已经为0了,则此商品对象需要从购物车数据集合中清除
                    if (data.get(position).getCount() == 0){
                        data.remove(data.get(position));
                    }
                    notifyDataSetChanged();
                    //如果现在购物车中一件商品都没有,则需要即将对话框隐藏掉
                    if (data.size() == 0){
                        businessActivity.hiddenSheetView();
                    }
                    businessActivity.businessPresenter.refreshShopCartData();
                    break;
            }
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }

    /**
     * @param operation     操作符
     * @param typeId        需要修改分类数量的分类id
     */
    private void refreshGoodsTypeAdapter(int operation, int typeId) {
        //1.找到商品分类的数据适配器,按照操作符要求对数据进行更新
        GoodsFragment goodsFragment = businessActivity.myFragmentPagerAdapter.getGoodsFragment();
        if (goodsFragment!=null){
            //2.获取商品分类的数据集合
            List<GoodsTypeInfo> goodsTypeInfoList = goodsFragment.goodsTypeAdapter.getData();
            for (int i = 0; i < goodsTypeInfoList.size(); i++) {
                GoodsTypeInfo goodsTypeInfo = goodsTypeInfoList.get(i);
                if (goodsTypeInfo.getId() == typeId){
                    //3.通过分类typeId找到所属的分类
                    switch (operation){
                        case Constant.ADD:
                            int addCount = goodsTypeInfo.getCount() + 1;
                            goodsTypeInfo.setCount(addCount);
                            break;
                        case Constant.DELETE:
                            if(goodsTypeInfo.getCount()>0){
                                int deleteCount = goodsTypeInfo.getCount() - 1;
                                goodsTypeInfo.setCount(deleteCount);
                            }
                            break;
                    }
                }
            }
            goodsFragment.goodsTypeAdapter.notifyDataSetChanged();
        }
    }

    /**
     * @param operation 增加还是减少操作符
     * @param id         需要变更数量的商品的唯一性id
     */
    private void refreshGoodsAdapter(int operation, int id) {
        //1.找到商品列表数据适配器,让其按照操作符对GoodsInfo对象进行数量变化
        GoodsFragment goodsFragment = businessActivity.myFragmentPagerAdapter.getGoodsFragment();
        //2.获取商品列表数据适配器
        if(goodsFragment !=null ){
            ArrayList<GoodsInfo> goodsInfoList = goodsFragment.goodsAdapter.getData();
            for (int i = 0; i < goodsInfoList.size(); i++) {
                GoodsInfo goodsInfo = goodsInfoList.get(i);
                if (goodsInfo.getId() == id){
                    //目前遍历到的商品id和点击+或者-商品id一致,则需要按照操作符更新商品数量
                    switch (operation){
                        case Constant.ADD:
                            int addCount = goodsInfo.getCount() + 1;
                            goodsInfo.setCount(addCount);
                            break;
                        case Constant.DELETE:
                            if (goodsInfo.getCount()>0){
                                int deleteCount = goodsInfo.getCount() - 1;
                                goodsInfo.setCount(deleteCount);
                            }
                            break;
                    }
                }
            }
            //通知商品列表数据适配器更新
            goodsFragment.goodsAdapter.notifyDataSetChanged();
        }
    }
}
