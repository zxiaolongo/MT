package com.demo.zxl.user.mt.ui.adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.BaseInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.demo.zxl.user.mt.R;
import com.demo.zxl.user.mt.global.MyApplication;
import com.demo.zxl.user.mt.moudle.bean.GoodsInfo;
import com.demo.zxl.user.mt.ui.activity.BusinessActivity;
import com.demo.zxl.user.mt.ui.fragment.GoodsFragment;
import com.demo.zxl.user.mt.util.Constant;
import com.demo.zxl.user.mt.util.CountPriceFormater;
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
    private GoodsFragment goodsFragment;
    private BusinessActivity businessActivity;
    private ArrayList<GoodsInfo> data;
    public GoodsAdapter(BusinessActivity businessActivity, GoodsFragment goodsFragment){
        this.businessActivity = businessActivity;
        this.goodsFragment = goodsFragment;
    }

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
        viewHolder.tvNewprice.setText(CountPriceFormater.format(goodsInfo.getNewPrice()));
        //历史售价
        viewHolder.tvOldprice.setText(CountPriceFormater.format(goodsInfo.getOldPrice()));
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

        private int operation = Constant.ADD;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
        @OnClick({R.id.ib_add,R.id.ib_minus})
        public void onClick(View view){
            switch (view.getId()){
                case R.id.ib_add:
                    //点击完成后,+号按钮就不能再次触发点击事件
                    view.setEnabled(false);
                    addGoodsInfo(view);
                    operation = Constant.ADD;
                    break;
                case R.id.ib_minus:
                    deleteGoods();//耗时200毫秒
                    operation = Constant.DELETE;
                    break;
            }
            //将修改数量的商品对象获取出来,并且获取其typeId
            int typeId = data.get(position).getTypeId();
            //商品分类数量进行变更,商品分类数量变化必须交由商品分类数据适配器提供
            goodsFragment.goodsTypeAdapter.refreshGoodsTypeAdapterCount(operation,typeId);
            //获取businessActivity的业务逻辑封装类的对象businessPresenter,用于计算购物车中的商品总量和总金额
            businessActivity.businessPresenter.refreshShopCartData();
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
                        //确保商品由1件变为了0件后,再去计算购物车中商品数量
                        businessActivity.businessPresenter.refreshShopCartData();
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

        /**
         * @param view 点中+号的小球
         */
        private void addGoodsInfo(View view) {
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
            addFlyImage(view);
        }

        /**
         * @param view 点中的+号的对象
         */
        private void addFlyImage(View view) {
           //1.创建一个ImageView,这个ImageView大小和+号保证一致,并且让其能够添加在和+号重叠的位置
            ImageView imageView = new ImageView(businessActivity);
            imageView.setBackgroundResource(R.drawable.button_add);
            //2.将imageView添加在指定位置,即view的所在位置,将view的x和y的坐标获取出来
            int[] viewLocation = new int[2];
            view.getLocationInWindow(viewLocation);

            imageView.setX(viewLocation[0]);
            imageView.setY(viewLocation[1]- MyApplication.statusBarHeight);

            //3.将imageView按照指定大小添加在屏幕上
            businessActivity.addFlyImage(imageView,view.getWidth(),view.getHeight());

            //4.获取目前imageView的开始的坐标位置,从此坐标位置进行飞行
            int[] sourceLocation = new int[2];
            imageView.getLocationInWindow(sourceLocation);

            //5.获取飞行终点坐标
            int[] desLocation = businessActivity.getShopCartLocation();

            //6.飞行方法
            fly(imageView,sourceLocation,desLocation,view);
        }

        /**
         * @param imageView         飞行的imageView即小球
         * @param sourceLocation    飞行起点
         * @param desLocation       飞行终点
         * @param view               点中的+号
         */
        private void fly(final ImageView imageView, int[] sourceLocation, int[] desLocation, final View view) {
            //获取起点x和y的坐标
            int startX = sourceLocation[0];
            int startY = sourceLocation[1];
            //获取终点x和y的坐标
            int endX = desLocation[0];
            int endY = desLocation[1];
            //x轴移动(匀速)
            TranslateAnimation translateAnimationX = new TranslateAnimation(
                    Animation.ABSOLUTE, 0, Animation.ABSOLUTE, endX - startX,
                    Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0);
            //给x轴的移动添加插值器
            translateAnimationX.setInterpolator(new LinearInterpolator());
            //y轴移动(加速)
            TranslateAnimation translateAnimationY = new TranslateAnimation(
                    Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0,
                    Animation.ABSOLUTE, 0, Animation.ABSOLUTE, endY-startY);
            translateAnimationY.setInterpolator(new AccelerateInterpolator());
            //将x轴的匀速和y轴的加速进行合并,构成抛物线运动
            AnimationSet animationSet = new AnimationSet(false);
            animationSet.addAnimation(translateAnimationX);
            animationSet.addAnimation(translateAnimationY);
            animationSet.setDuration(300);

            imageView.startAnimation(animationSet);

            animationSet.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }
                @Override
                public void onAnimationEnd(Animation animation) {
                    //飞行动画结束后,才可以购买下一件上
                    view.setEnabled(true);
                    imageView.setVisibility(View.GONE);
                }
                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }
}
