package com.demo.zxl.user.mt.ui.fragment;

import android.animation.ArgbEvaluator;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.demo.zxl.user.mt.R;
import com.demo.zxl.user.mt.presenter.HomePresenter;
import com.demo.zxl.user.mt.ui.adapter.HomeAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by HASEE.
 */
public class HomeFragment extends BaseFragment {
    @BindView(R.id.rv_home)
    RecyclerView rvHome;

    @BindView(R.id.home_tv_address)
    TextView homeTvAddress;
    @BindView(R.id.ll_title_search)
    LinearLayout llTitleSearch;
    @BindView(R.id.ll_title_container)
    LinearLayout llTitleContainer;
    Unbinder unbinder;
    //argbEvaluator指定一个插值器修改2个色值变化
    private ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    private int sumY = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //再次加重fragment的时候,让记录滚动距离变量设置为0
        sumY = 0;
        View view = View.inflate(getActivity(), R.layout.fragment_home, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        HomeAdapter homeAdapter = new HomeAdapter(getActivity());
        //默认情况下使用的线性布局竖直排列
        rvHome.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvHome.setAdapter(homeAdapter);

        rvHome.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                sumY += dy;
                int bgColor = 0X553190E8;
                if (sumY == 0){
                    //轮播图位于最顶部,呈现默认透明效果
                    bgColor = 0X553190E8;
                }else if(sumY>=300){
                    //如果recycleView滚动达到300个像素以后,则认为颜色不在发生变更
                    bgColor = 0XFF3190E8;
                }else{
                    //0到300之间颜色变化  白色 变成  黑色     ffffff    000000

                    //参数一:变化规则(伴随手指移动,渐变)
                    //参数二:起点色值
                    //参数三:终点设置
                    bgColor = (int) argbEvaluator.evaluate(
                            (sumY+0.0f)/300,0X553190E8,0XFF3190E8);
                }
                llTitleContainer.setBackgroundColor(bgColor);

                super.onScrolled(recyclerView, dx, dy);
            }
        });
        //获取首页放置在服务器端数据
        HomePresenter homePresenter = new HomePresenter(homeAdapter);
        homePresenter.getHomeData();
        super.onActivityCreated(savedInstanceState);
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
