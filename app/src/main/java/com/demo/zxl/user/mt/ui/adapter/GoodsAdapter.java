package com.demo.zxl.user.mt.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.zxl.user.mt.R;
import com.demo.zxl.user.mt.moudle.bean.GoodsInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by HASEE.
 */
public class GoodsAdapter extends BaseAdapter implements StickyListHeadersAdapter {
    private ArrayList<GoodsInfo> data;
    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = View.inflate(parent.getContext(), R.layout.item_type_header,null);
        }
        GoodsInfo goodsInfo = getItem(position);
        ((TextView)convertView).setText(goodsInfo.getTypeName());
        return convertView;
    }
    @Override
    public long getHeaderId(int position) {
        //约定头有几个,分类有几种,头就有几个
        //因为多件商品拥有同一个分类id,分类id的不同情况就是分类总数量,即头个数
        /*分类1   id   100
                商品1    typeId     100
                商品2    typeId     100
        分类2   id   101
                商品4    typeId     101
                商品5    typeId     101
                商品6    typeId     101
                .......*/
        return getItem(position).getTypeId();
    }
    @Override
    public int getCount() {
        if (data != null && data.size() > 0) {
            return data.size();
        }
        return 0;
    }

    @Override
    public GoodsInfo getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        //复用convertView
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.item_goods, null);
            //通过viewHolder存储找过控件
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //给viewHolder中的控件赋值
        GoodsInfo goodsInfo = getItem(position);
        viewHolder.tvName.setText(goodsInfo.getName());
        //当前售价
        viewHolder.tvNewprice.setText(goodsInfo.getNewPrice()+"");
        //历史售价
        viewHolder.tvOldprice.setText(goodsInfo.getOldPrice()+"");
        //展示图片
        Picasso.with(parent.getContext()).load(goodsInfo.getIcon()).into(viewHolder.ivIcon);
        return convertView;
    }

    public void setData(ArrayList<GoodsInfo> data) {
        this.data = data;
        notifyDataSetChanged();
    }
    public ArrayList<GoodsInfo> getData(){
        return data;
    }

    static class ViewHolder {
        @BindView(R.id.iv_icon)
        ImageView ivIcon;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_zucheng)
        TextView tvZucheng;
        @BindView(R.id.tv_yueshaoshounum)
        TextView tvYueshaoshounum;
        @BindView(R.id.tv_newprice)
        TextView tvNewprice;
        @BindView(R.id.tv_oldprice)
        TextView tvOldprice;
        @BindView(R.id.ib_minus)
        ImageButton ibMinus;
        @BindView(R.id.tv_count)
        TextView tvCount;
        @BindView(R.id.ib_add)
        ImageButton ibAdd;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
