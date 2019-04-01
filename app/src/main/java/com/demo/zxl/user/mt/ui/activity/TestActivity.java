package com.demo.zxl.user.mt.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.demo.zxl.user.mt.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.smssdk.EventHandler;
import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by HASEE.
 */

public class TestActivity extends Activity {
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.btn_click)
    Button btnClick;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    private EventHandler eventHandler = new EventHandler() {
        @Override
        public void afterEvent(int event, int result, Object data) {
            //event 具体做的事件类型
            //result 某个事件结果
            if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                Log.i("", "afterEvent threadId = " + Thread.currentThread().getId());
                //目前的事件就是下发验证码短信
                if (result == SMSSDK.RESULT_COMPLETE) {
                    //发送验证码短信成功  7928
                    Log.i("", "验证码短信发送成功==============");
                } else {
                    //发送验证码短信失败
                    Log.i("", "验证码短信发送失败==============");
                }
            }else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){
                //如果事件类型是校验验证码
                if (result == SMSSDK.RESULT_COMPLETE){
                    //校验成功
                    Log.i("", "检验验证码短信成功==============");
                }else{
                    //校验失败
                    Log.i("", "检验验证码短信失败==============");
                }
            }
            super.afterEvent(event, result, data);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);

        //主线程线程id是1
        Log.i("", "onCreate threadId = " + Thread.currentThread().getId());

        //发送验证码短信这件事情是否成功
        SMSSDK.registerEventHandler(eventHandler);

        //在点击按钮后,可以发送验证码短信给指定手机号
        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = etPhone.getText().toString().trim();
                SMSSDK.getVerificationCode("86", phone, new OnSendMessageHandler() {
                    @Override
                    public boolean onSendMessage(String country, String phone) {
                        return false;
                    }
                });
            }
        });

        //短信验证码和手机号匹配校验过程
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //验证码短信3分钟有效
                SMSSDK.submitVerificationCode("86","18612699412","5320");
            }
        });
    }
}
