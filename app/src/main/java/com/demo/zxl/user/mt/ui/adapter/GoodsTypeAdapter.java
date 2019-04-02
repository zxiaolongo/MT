package com.demo.zxl.user.mt.ui.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.demo.zxl.user.mt.R;
import com.demo.zxl.user.mt.moudle.bean.GoodsTypeInfo;
import com.demo.zxl.user.mt.ui.fragment.GoodsFragment;
import com.demo.zxl.user.mt.util.Constant;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HASEE.
 */
public class GoodsTypeAdapter extends RecyclerView.Adapter {
    private GoodsFragment goodsFragment;
    private List<GoodsTypeInfo> data;
    //记录目前选中分类条目的索引值
    public int currentPosition = 0;
    public GoodsTypeAdapter(GoodsFragment goodsFragment){
        this.goodsFragment = goodsFragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_type, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        GoodsTypeInfo goodsTypeInfo = data.get(position);
        ((ViewHolder) holder).type.setText(goodsTypeInfo.getName());
        //指定商品分类数量
        if (goodsTypeInfo.getCount() > 0) {
            ((ViewHolder) holder).tvCount.setVisibility(View.VISIBLE);
            ((ViewHolder) holder).tvCount.setText(goodsTypeInfo.getCount() + "");
        } else {
            ((ViewHolder) holder).tvCount.setVisibility(View.INVISIBLE);
        }
        //itemView就是某一个条目布局文件转换成的view对象,选中条目的背景需要变白,文字需要变红
        if(position == currentPosition){
            ((ViewHolder) holder).itemView.setBackgroundColor(Color.WHITE);
            ((ViewHolder) holder).type.setTextColor(Color.RED);
        }else{
            ((ViewHolder) holder).itemView.setBackgroundColor(Color.LTGRAY);
            ((ViewHolder) holder).type.setTextColor(Color.BLACK);
        }
        ((ViewHolder) holder).setPosition(position);
    }

    @Override
    public int getItemCount() {
        if (data != null && data.size() > 0) {
            return data.size();
        }
        return 0;
    }

    public void setData(List<GoodsTypeInfo> data) {
        this.data = data;
        notifyDataSetChanged();
    }
    public List<GoodsTypeInfo> getData(){
        return data;
    }

    /**
     * @param operation     操作符  用于区分增加还是减少
     * @param typeId        修改修改数量的分类id
     */
    public void refreshGoodsTypeAdapterCount(int operation, int typeId) {
        //通过typeId在商品分类集合中进行遍历,找到需要修改数量的分类对象
        for (int i = 0; i < data.size(); i++) {
            GoodsTypeInfo goodsTypeInfo = data.get(i);
            if (typeId == goodsTypeInfo.getId()){
                //找到需要修改数量的分类对象了,根据操作符修改分类数量
                switch (operation){
                    case Constant.ADD:
                        int addCount = goodsTypeInfo.getCount()+1;
                        goodsTypeInfo.setCount(addCount);
                        break;
                    case Constant.DELETE:
                        if (goodsTypeInfo.getCount()>0){
                            int deleteCount = goodsTypeInfo.getCount()-1;
                            goodsTypeInfo.setCount(deleteCount);
                        }
                        break;
                }
            }
        }
        //将+或者-操作变更的商品数量,更新到列表页面中
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tvCount)
        TextView tvCount;
        @BindView(R.id.type)
        TextView type;
        private int position;

        //view就是由一个条目布局文件转换成的view对象
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //1.将选中的条目颜色变化
                    currentPosition = position;
                    notifyDataSetChanged();
                    //2.在点中某一个分类后,需要切换右侧显示的商品列表指向索引位置
                    //获取选中分类id,此id用于决定右侧listView滚动到哪个索引位置
                    int id = data.get(position).getId();
                    //将更新右侧列表位置的操作,放置在GoodsFragment中进行处理
                    goodsFragment.refreshGoodsAdapterUI(id);
                }
            });
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }
}
