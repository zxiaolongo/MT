package com.demo.zxl.user.mt.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.demo.zxl.user.mt.R;
import com.demo.zxl.user.mt.moudle.bean.GoodsInfo;
import com.demo.zxl.user.mt.moudle.bean.GoodsTypeInfo;
import com.demo.zxl.user.mt.moudle.bean.Seller;
import com.demo.zxl.user.mt.presenter.GoodsPresenter;
import com.demo.zxl.user.mt.ui.activity.BusinessActivity;
import com.demo.zxl.user.mt.ui.adapter.GoodsAdapter;
import com.demo.zxl.user.mt.ui.adapter.GoodsTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
/**
 * Created by HASEE.
 */
public class GoodsFragment extends BaseFragment {
    @BindView(R.id.rv_goods_type)
    RecyclerView rvGoodsType;

    @BindView(R.id.slhlv)
    se.emilsjolander.stickylistheaders.StickyListHeadersListView slhlv;

    Unbinder unbinder;
    private Seller seller;
    public GoodsAdapter goodsAdapter;//商品数据适配器
    public GoodsTypeAdapter goodsTypeAdapter;//商品分类数据适配器

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        seller = (Seller) bundle.getSerializable("seller");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_goods, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        goodsTypeAdapter = new GoodsTypeAdapter(this);
        rvGoodsType.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvGoodsType.setAdapter(goodsTypeAdapter);

        goodsAdapter = new GoodsAdapter((BusinessActivity) getActivity(),this);
        slhlv.setAdapter(goodsAdapter);

        GoodsPresenter goodsPresenter = new GoodsPresenter(goodsTypeAdapter, goodsAdapter,seller);
        goodsPresenter.getGoodsInfo(seller.getId());

        slhlv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //1.获取商品列表集合,根据firstVisibleItem获取目前在ListView顶部可见GoodsInfo
                ArrayList<GoodsInfo> goodsInfoList = goodsAdapter.getData();
                //获取商品分类集合
                List<GoodsTypeInfo> goodsTypeInfoList = goodsTypeAdapter.getData();
                if (goodsInfoList!=null && goodsTypeInfoList!=null){
                    //2.获取firstVisibleItem商品的分类typeId
                    GoodsInfo goodsInfo = goodsInfoList.get(firstVisibleItem);
                    int typeId = goodsInfo.getTypeId();
                    //3.拿typeId和左侧目前选中的商品分类对象id比对,如果一致,什么都不用做,如果不一致,则需要找到一个typeId和商品分类id一致的条目

                    GoodsTypeInfo goodsTypeInfo = goodsTypeInfoList.get(goodsTypeAdapter.currentPosition);
                    int id = goodsTypeInfo.getId();
                    if (typeId != id){
                        //4.循环遍历左侧商品分类,直到找到一个分类id和typeId一致的条目,循环中止,循环到的索引为i就是左侧分类需要选中的条目
                        for (int i = 0; i < goodsTypeInfoList.size(); i++) {
                            GoodsTypeInfo info = goodsTypeInfoList.get(i);
                            if (info.getId() == typeId){
                                //循环中止,i就是左侧条目需要选中的索引位置
                                goodsTypeAdapter.currentPosition = i;
                                //让商品分类数据适配刷新
                                goodsTypeAdapter.notifyDataSetChanged();
                                //让看不见的条目向上滚动至可见
                                rvGoodsType.smoothScrollToPosition(i);
                                break;
                            }
                        }
                    }
                }
            }
        });

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * @param id  目前左侧商品分类选中商品分类id
     *            根据此id修改右侧商品列表显示效果
     */
    public void refreshGoodsAdapterUI(int id) {
        //如果现在id和右侧列表第0个可见位置的typeId不一致,则需要找到一个id和typeId一致的索引位置,让ListView进行页面切换
        //1.获取右侧商品列表数据集合
        ArrayList<GoodsInfo> goodsInfoList = goodsAdapter.getData();
        //2.找到goodsInfo中typeId和目前选中条目id一致的那个索引位置
        for (int i = 0; i < goodsInfoList.size(); i++) {
            GoodsInfo goodsInfo = goodsInfoList.get(i);
            if (id == goodsInfo.getTypeId()){
                //i就是索引位置,只需要让右侧listView切换至i位置条目显示即可
                slhlv.setSelection(i);
                break;
            }
        }
    }
}
