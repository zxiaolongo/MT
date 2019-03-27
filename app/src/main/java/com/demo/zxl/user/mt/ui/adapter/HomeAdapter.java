package com.demo.zxl.user.mt.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.demo.zxl.user.mt.R;
import com.demo.zxl.user.mt.moudle.bean.HomeInfo;
import com.demo.zxl.user.mt.moudle.bean.HomeItem;
import com.demo.zxl.user.mt.moudle.bean.Promotion;
import com.demo.zxl.user.mt.moudle.bean.Seller;
import com.demo.zxl.user.mt.ui.activity.BusinessActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * Created by HASEE.
 */
public class HomeAdapter extends RecyclerView.Adapter {
    private static final int ITEM_HEAD = 0;//头条目状态码
    private static final int ITEM_SELLER = 1;//商家条目状态码
    private static final int ITEM_DIV = 2;//分割线条目状态码
    private Activity activity;

    private HomeInfo data;

    public HomeAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public int getItemViewType(int position) {
        //判断每一个position指向条目类型
        if (position == 0) {
            //头条目
            return ITEM_HEAD;
        } else if (data.getBody().get(position - 1).getType() == 1) {
            //分割线条目
            return ITEM_DIV;
        } else {
            //商家条目
            return ITEM_SELLER;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_HEAD) {
            //创建头viewHolder
            View view = View.inflate(parent.getContext(), R.layout.item_title, null);
            HeaderViewHolder headerViewHolder = new HeaderViewHolder(view);
            return headerViewHolder;
        } else if (viewType == ITEM_DIV) {
            //创建分割线viewHolder
            View view = View.inflate(parent.getContext(), R.layout.item_division, null);
            DivViewHolder divViewHolder = new DivViewHolder(view);
            return divViewHolder;
        } else {
            //创建商家viewHolder
            View view = View.inflate(parent.getContext(), R.layout.item_seller, null);
            SellerViewHolder sellerViewHolder = new SellerViewHolder(view);
            return sellerViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        if (itemViewType == ITEM_HEAD) {
            //头条目
        } else if (itemViewType == ITEM_DIV) {
            //分割线条目数据填充
            setDivData(holder, position);
        } else {
            //商家条目数据填充
            setSellerData(holder, position);
            ((SellerViewHolder) holder).setPosition(position);
        }
    }

    /**
     * @param holder    商家条目viewHolder对象
     * @param position  需要用到数据的索引位置
     */
    private void setSellerData(RecyclerView.ViewHolder holder, int position) {
        HomeItem homeItem = data.getBody().get(position - 1);
        //设置商家名称
        ((SellerViewHolder)holder).tvTitle.setText(homeItem.getSeller().getName());
        //设置商家评分
        String score = homeItem.getSeller().getScore();
        if (!TextUtils.isEmpty(score)){
            float floatScore = Float.parseFloat(score);
            ((SellerViewHolder)holder).ratingBar.setRating(floatScore);
        }
    }

    /**
     * @param holder    分割线viewHolder对象
     * @param position  需要用到数据的索引位置
     */
    private void setDivData(RecyclerView.ViewHolder holder, int position) {
        HomeItem homeItem = data.getBody().get(position - 1);
        ((DivViewHolder)holder).tv1.setText(homeItem.getRecommendInfos().get(0));
        ((DivViewHolder)holder).tv2.setText(homeItem.getRecommendInfos().get(1));
        ((DivViewHolder)holder).tv3.setText(homeItem.getRecommendInfos().get(2));
        ((DivViewHolder)holder).tv4.setText(homeItem.getRecommendInfos().get(3));
        ((DivViewHolder)holder).tv5.setText(homeItem.getRecommendInfos().get(4));
        ((DivViewHolder)holder).tv6.setText(homeItem.getRecommendInfos().get(5));
    }

    @Override
    public int getItemCount() {
        if (data != null && data.getHead() != null && data.getBody() != null) {
            return data.getBody().size() + 1;
        }
        return 0;
    }

    public void setData(HomeInfo data) {
        this.data = data;
        //根据此方法传递进来的data对列表进行更新
        notifyDataSetChanged();
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.slider)
        SliderLayout slider;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            //给SliderLayout添加图片,数据来源于data对象
            for (int i = 0; i < data.getHead().getPromotionList().size(); i++) {
                Promotion promotion = data.getHead().getPromotionList().get(i);
                //TextSliderView 等同于ImageView+TextView 即能显示文本又能显示图片
                TextSliderView textSliderView = new TextSliderView(activity);
                textSliderView
                        .description(promotion.getInfo())//指定描述文本内容
                        .image(promotion.getPic())//指定需要加载图片
                        .setScaleType(BaseSliderView.ScaleType.Fit);//前景对背景填充方式  FitXY  centercrop

                //将textSliderView控件添加在SliderLayout内部
                slider.addSlider(textSliderView);
            }
            //动画效果指定
            slider.setPresetTransformer(SliderLayout.Transformer.DepthPage);//指定SliderLayout图片切换动画效果
            slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);//指定点位置
            slider.setCustomAnimation(new DescriptionAnimation());//描述文字出现动画效果
            slider.setDuration(4000);//图片翻页时长
        }
    }

    class DivViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_division_title)
        TextView tvDivisionTitle;
        @BindView(R.id.tv1)
        TextView tv1;
        @BindView(R.id.tv2)
        TextView tv2;
        @BindView(R.id.tv3)
        TextView tv3;
        @BindView(R.id.tv4)
        TextView tv4;
        @BindView(R.id.tv5)
        TextView tv5;
        @BindView(R.id.tv6)
        TextView tv6;

        DivViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    class SellerViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tvCount)
        TextView tvCount;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.ratingBar)
        RatingBar ratingBar;
        private int position;//就是点中条目的索引(和用到集合的索引错1)

        SellerViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, BusinessActivity.class);
                    List<HomeItem> homeItemList = data.getBody();
                    HomeItem homeItem = homeItemList.get(position - 1);
                    Seller seller = homeItem.getSeller();
                    intent.putExtra("seller",seller);
                    activity.startActivity(intent);
                }
            });
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }
}
