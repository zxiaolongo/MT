package com.itheima105.request;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_post_key_value)
    Button btnPostKeyValue;
    @BindView(R.id.btn_post_map_key_value)
    Button btnPostMapKeyValue;
    @BindView(R.id.btn_post_json)
    Button btnPostJson;
    @BindView(R.id.btn_post_file)
    Button btnPostFile;
    @BindView(R.id.btn_post_files)
    Button btnPostFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_post_key_value, R.id.btn_post_map_key_value, R.id.btn_post_json, R.id.btn_post_file, R.id.btn_post_files})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_post_key_value:
                postKeyValue();//上传键值对
                break;
            case R.id.btn_post_map_key_value:
                postKeyValueMap();//上传Map形式键值对
                break;
            case R.id.btn_post_json:
                postJson();//上传json
                break;
            case R.id.btn_post_file:
                postFile();//上传文件
                break;
            case R.id.btn_post_files:
                postFiles();//上传多个文件
                break;
        }
    }

    private void postFiles() {

        try {
            //准备好需要上传给服务器的文件
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.a);
            File imgFile = new File(getCacheDir(), "imgFile");
            FileOutputStream fileOutputStream = null;
            fileOutputStream = new FileOutputStream(imgFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
            Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.mipmap.b);
            File imgFile1 = new File(getCacheDir(), "imgFile");
            FileOutputStream fileOutputStream1 = null;
            fileOutputStream1 = new FileOutputStream(imgFile1);
            bitmap1.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream1);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://httpbin.org/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ResponseInfoApi responseInfoApi = retrofit.create(ResponseInfoApi.class);
            // 创建 RequestBody，用于封装构建RequestBody
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"),imgFile);
            RequestBody requestFile1 =
                    RequestBody.create(MediaType.parse("multipart/form-data"),imgFile1);

            HashMap<String, RequestBody> map = new HashMap<>();
            map.put("actimg",requestFile);
            map.put("listImg",requestFile1);
            Call<ResponseBody> call = responseInfoApi.uploadFiles(map);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    ResponseBody body = response.body();
                    Log.i("","请求成功");
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.i("","请求失败");
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void postFile() {
        try {
            //准备好需要上传给服务器的文件
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            File imgFile = new File(getCacheDir(), "imgFile");
            FileOutputStream fileOutputStream = new FileOutputStream(imgFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://httpbin.org/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ResponseInfoApi responseInfoApi = retrofit.create(ResponseInfoApi.class);
            // 创建 RequestBody，用于封装构建RequestBody
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"),imgFile);
            // MultipartBody.Part和后端约定好Key，这里的partName是用actimg
            // 上传文件名称
            //  需要上传文件所在的RequestBody对象
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("actimg", imgFile.getName(), requestFile);

            Call<ResponseBody> call = responseInfoApi.upload(body);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    ResponseBody responseBody = response.body();
                    Log.i("","上传成功");
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.i("","上传失败");

                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void postJson() {
        Student student = new Student("张小龙", "20");
        Gson gson = new Gson();
        String json = gson.toJson(student);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://httpbin.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ResponseInfoApi responseInfoApi = retrofit.create(ResponseInfoApi.class);

        //postJson方法用于传递json给服务器,需要指定一个请求体RequestBody
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), json);

        Call<ResponseBody> call = responseInfoApi.postJson(requestBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    ResponseBody body = response.body();
                    Log.i("","body.string() = "+body.string());
                    Log.i("","请求成功");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("","请求失败");
            }
        });
    }

    private void postKeyValueMap() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("name","张小龙");
        hashMap.put("age","19");

        //发送一个post请求给指定链接地址  baseurl  :  http://httpbin.org/    后半部分用于测试post请求链接地址:post
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://httpbin.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ResponseInfoApi responseInfoApi = retrofit.create(ResponseInfoApi.class);
        //将请求的数据封装在map集合中上传给服务器
        Call<ResponseBody> call = responseInfoApi.postKeyValueMap(hashMap);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    //获取服务器返回的响应体
                    ResponseBody body = response.body();
                    //服务器返回的字符串获取下来
                    Log.i("","body.string() = "+body.string());
                    Log.i("","请求成功");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("","请求失败");

            }
        });
    }

    private void postKeyValue() {
        //发送一个post请求给指定链接地址  baseurl  :  http://httpbin.org/    后半部分用于测试post请求链接地址:post
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://httpbin.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ResponseInfoApi responseInfoApi = retrofit.create(ResponseInfoApi.class);
        Call<ResponseBody> call = responseInfoApi.postKeyValue("张小龙", "18");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    //获取服务器返回的响应体
                    ResponseBody body = response.body();
                    //服务器返回的字符串获取下来
                    Log.i("","body.string() = "+body.string());
                    Log.i("","请求成功");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("","请求失败");

            }
        });
    }

    public interface ResponseInfoApi{
        //响应体  http://httpbin.org/post?name=""&age=""
        //表单发送数据给服务器
        @FormUrlEncoded()
        @POST("post")
        Call<ResponseBody> postKeyValue(@Field("name") String name,@Field("age") String age);

        @FormUrlEncoded()
        @POST("post")
        Call<ResponseBody> postKeyValueMap(@FieldMap() Map<String,String> map);
        //发送请求的时候,将键值对封装在一个map集中进行上传

        //post请求上传json
        //Content-Type 代表数据类型
        //accept 告知服务器接收数据类型
        //RequestBody 代表请求体
        //ResponseBody 代表响应体
        @Headers({"Content-Type: application/json","Accept: application/json"})
        @POST("post")
        Call<ResponseBody> postJson(@Body RequestBody body);


        //post请求上传文件
        //@Multipart  多部件,代表需要上传文件
        //MultipartBody.Part 包含文件的对象
        @Multipart
        @POST("post")
        Call<ResponseBody> upload(@Part MultipartBody.Part file);

        //post请求上传多张图片
        @Multipart
        @POST("post")
        Call<ResponseBody> uploadFiles(
                @PartMap() Map<String, RequestBody> maps);
    }
}
