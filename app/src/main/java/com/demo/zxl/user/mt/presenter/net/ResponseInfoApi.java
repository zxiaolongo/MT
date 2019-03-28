package com.demo.zxl.user.mt.presenter.net;


import com.demo.zxl.user.mt.moudle.bean.ResponseInfo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by HASEE.
 */

public interface ResponseInfoApi {
    //此方法是只指定了链接地址,没有带上参数网络请求方法
    @GET("{home}")
    Call<ResponseInfo> getHomeInfo(@Path("home") String url);

    //此方法是不仅需要指定请求地址,还需要带上请求参数
    //@Query 指定传递参数关键字  在括号内需要指定传递给服务器参数名称
    @GET("{home}")
    Call<ResponseInfo> getHomeInfo(
            @Path("home") String url, @Query("latitude") String lat, @Query("longitude") String lng);


    //其余模块请求方式,请求地址,请求参数各不相同,只需要在此处定义多个方法即可
    @GET("business")
    Call<ResponseInfo> getGoodsInfo(@Query("sellerId") long sellerId);
}
