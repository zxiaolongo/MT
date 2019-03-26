package com.demo.zxl.user.mt.presenter;


import com.demo.zxl.user.mt.moudle.bean.ResponseInfo;
import com.demo.zxl.user.mt.presenter.net.ResponseInfoApi;
import com.demo.zxl.user.mt.util.Constant;

import java.io.IOException;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by HASEE.
 * 封装项目中所有网络请求基类
 */
public abstract class BasePresenter {
    public ResponseInfoApi responseInfoApi;
    private HashMap<String,String> errorMap = new HashMap<>();
    public BasePresenter(){
        errorMap.put("1","错误类型一");
        errorMap.put("2","错误类型二");
        errorMap.put("3","错误类型三");
        errorMap.put("4","错误类型四");
        errorMap.put("5","错误类型五");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASEURL)//请求链接地址
                .addConverterFactory(GsonConverterFactory.create())//请求返回数据,以及需要用到解析的工具
                .build();//构建retrofit对象
        //如何发送请求方法
        //1.完整链接地址
        //2.请求方式
        //3.请求参数
        //4.请求结果数据用什么进行封装
        responseInfoApi = retrofit.create(ResponseInfoApi.class);
        //将请求发送出去
        /*Call<ResponseInfo> call = responseInfoApi.getHomeInfo("home");
        call.enqueue(callBack);*/
    }

    class CallBackAdapter implements Callback<ResponseInfo> {
        @Override
        public void onResponse(Call<ResponseInfo> call, Response<ResponseInfo> response) {
            //如果请求完成后服务器有响应
            ResponseInfo responseInfo = response.body();
            if (responseInfo.code.equals("0")){
                //data字段中的数据才是有意义的
                String json = responseInfo.data;
                //解析展示,无法在父类中具体实现,提供一个抽象方法,让子类实现
                parseJson(json);
            }else{
                //本次请求响应有异常
                String errorMessage = errorMap.get(responseInfo.code);
                onFailure(call,new RuntimeException(errorMessage));
            }
        }
        @Override
        public void onFailure(Call<ResponseInfo> call, Throwable t) {
            if (t instanceof RuntimeException){
                //t.getMessage()方法获取的就是RuntimeException中提供的参数
                sendErrorMessage(t.getMessage());
            }
            //请求失败方法,无服务没有给响应方法
            sendErrorMessage("请求异常");
        }
    }
    //留给子类处理错误方法
    protected abstract void sendErrorMessage(String errorMessage);
    //留给子类需要进行实现解析方法
    protected abstract void parseJson(String json) ;

    private void syncGetRequest() {
        new Thread(){
            @Override
            public void run() {
                try {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(Constant.BASEURL)//请求链接地址
                            .addConverterFactory(GsonConverterFactory.create())//请求返回数据,以及需要用到解析的工具
                            .build();//构建retrofit对象
                    //如何发送请求方法
                    //1.完整链接地址
                    //2.请求方式
                    //3.请求参数
                    //4.请求结果数据用什么进行封装
                    ResponseInfoApi responseInfoApi = retrofit.create(ResponseInfoApi.class);
                    //将请求发送出去
                    Call<ResponseInfo> call = responseInfoApi.getHomeInfo("home");
                    //请求发出去后,得到一个响应体
                    Response<ResponseInfo> response = call.execute();//同步请求
                    //通过响应体获取数据
                    ResponseInfo responseInfo = response.body();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /*public interface GitHub {
        //@GET指定的是请求方式
        //@GET括号中()包含就是链接地址(方法传递参数形式指定)
        //@Path("owner") 指定的就是可变链接地址内容
        //请求返回值结果,由Call中的泛型指定
        @GET("/repos/{owner}/{repo}/contributors")
        Call<List<Contributor>> contributors(@Path("owner") String owner, @Path("repo") String repo);
    }*/
}
