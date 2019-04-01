package com.demo.zxl.user.mt.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
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
import butterknife.OnClick;
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
                商品3    typeId     100
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
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.setPosition(position);
        //给viewHolder中的控件赋值
        GoodsInfo goodsInfo = getItem(position);
        viewHolder.tvName.setText(goodsInfo.getName());
        //当前售价
        viewHolder.tvNewprice.setText(goodsInfo.getNewPrice() + "");
        //历史售价
        viewHolder.tvOldprice.setText(goodsInfo.getOldPrice() + "");
        //展示图片
        Picasso.with(parent.getContext()).load(goodsInfo.getIcon()).into(viewHolder.ivIcon);

        //显示购买数量逻辑
        if (goodsInfo.getCount() > 0){
            //显示减号和数量
            viewHolder.ibMinus.setVisibility(View.VISIBLE);
            viewHolder.tvCount.setVisibility(View.VISIBLE);

            viewHolder.tvCount.setText(goodsInfo.getCount()+"");
        }else{
            viewHolder.ibMinus.setVisibility(View.GONE);
            viewHolder.tvCount.setVisibility(View.GONE);
        }
        return convertView;
    }

    public void setData(ArrayList<GoodsInfo> data) {
        this.data = data;
        notifyDataSetChanged();
    }
    public ArrayList<GoodsInfo> getData(){
        return data;
    }

    class ViewHolder {
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
        private int position;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
        @OnClick({R.id.ib_add,R.id.ib_minus})
        public void onClick(View view){
            switch (view.getId()){
                case R.id.ib_add:
                    addGoodsInfo();
                    break;
                case R.id.ib_minus:
                    deleteGoods();
                    break;
            }
        }

        private void deleteGoods() {
            //获取选中商品对象
            final GoodsInfo goodsInfo = data.get(position);
            if (goodsInfo.getCount() == 1){
                //执行减号滚回动画
                //减号滚出动画(旋转,透明度变化,平移)
                RotateAnimation rotateAnimation = new RotateAnimation(720f,0f,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
                AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f,0.5f);
                TranslateAnimation translateAnimation = new TranslateAnimation(
                        Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 2f,
                        Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);

                AnimationSet animationSet = new AnimationSet(false);
                animationSet.addAnimation(rotateAnimation);
                animationSet.addAnimation(alphaAnimation);
                animationSet.addAnimation(translateAnimation);
                animationSet.setDuration(200);
                //让控件执行动画
                ibMinus.startAnimation(animationSet);
                //减号执行完动画后,再次隐藏加号和数量
                animationSet.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        //动画结束,需要把减号和数量再次隐藏
                        ibMinus.setVisibility(View.GONE);
                        tvCount.setVisibility(View.GONE);
                        //商品数量要从1件变成0件
                        goodsInfo.setCount(0);
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
            }else{
                int deleteCount = goodsInfo.getCount()-1;
                goodsInfo.setCount(deleteCount);
                notifyDataSetChanged();
            }
        }

        private void addGoodsInfo() {
            //获取选中商品对象
            GoodsInfo goodsInfo = data.get(position);
            if (goodsInfo.getCount() == 0){
                //减号滚出动画(旋转,透明度变化,平移)
                RotateAnimation rotateAnimation = new RotateAnimation(0f, 720f,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
                AlphaAnimation alphaAnimation = new AlphaAnimation(0.5f, 1.0f);
                TranslateAnimation translateAnimation = new TranslateAnimation(
                        Animation.RELATIVE_TO_SELF, 2f, Animation.RELATIVE_TO_SELF, 0,
                        Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);

                AnimationSet animationSet = new AnimationSet(false);
                animationSet.addAnimation(rotateAnimation);
                animationSet.addAnimation(alphaAnimation);
                animationSet.addAnimation(translateAnimation);
                animationSet.setDuration(200);
                //让控件执行动画
                ibMinus.startAnimation(animationSet);

                //减号显示
                ibMinus.setVisibility(View.VISIBLE);
                //数量显示
                tvCount.setVisibility(View.VISIBLE);
            }
            //商品数量加1
            int addCount = goodsInfo.getCount()+1;
            goodsInfo.setCount(addCount);
            //将最新的数据更新在商品数据适配器中
            notifyDataSetChanged();

            //小球抛物线运动
            addFlyImage();
        }

        private void addFlyImage() {
            //小球在x轴上的运动是匀速运动
            //小球在y轴上的运动是加速运动
            //将上诉2个运动进行合并,则会形成一个抛物线的效果


            //创建一个用于飞行的小球ImageView对象(图片大小和图片资源和加号图片保持一致)



            //获取小球飞行开始位置,飞行结束位置
            //将飞行的小球放置在开始位置上后在自行飞行动画
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }
}
