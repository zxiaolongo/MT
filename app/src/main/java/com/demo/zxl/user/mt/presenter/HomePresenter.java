package com.demo.zxl.user.mt.presenter;

import android.util.Log;

import com.demo.zxl.user.mt.moudle.bean.HomeInfo;
import com.demo.zxl.user.mt.moudle.bean.ResponseInfo;
import com.demo.zxl.user.mt.ui.adapter.HomeAdapter;
import com.google.gson.Gson;

import retrofit2.Call;


/**
 * Created by HASEE.
 */
public class HomePresenter extends BasePresenter{
    private HomeAdapter homeAdapter;

    public HomePresenter(HomeAdapter homeAdapter) {
        this.homeAdapter = homeAdapter;
    }

    @Override
    protected void sendErrorMessage(String errorMessage) {

    }

    @Override
    protected void parseJson(String json) {
        Log.i("","json = "+json);
        Gson gson = new Gson();
        HomeInfo homeInfo = gson.fromJson(json, HomeInfo.class);

        homeAdapter.setData(homeInfo);
    }
    //网络请求触发方法
    public void getHomeData(){
        Call<ResponseInfo> call = responseInfoApi.getHomeInfo("home");
        call.enqueue(new CallBackAdapter());
    }
}
