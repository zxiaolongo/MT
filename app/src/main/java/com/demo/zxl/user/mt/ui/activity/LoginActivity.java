package com.demo.zxl.user.mt.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.demo.zxl.user.mt.R;
import com.demo.zxl.user.mt.util.SMSUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by HASEE.
 */
public class LoginActivity extends Activity {
    //保持时间递减状态码
    private static final int KEEP_TIME_DES = 100;
    //已经经历过60秒倒计时了,重新开始一次60秒倒计时
    private static final int TIME_RESET = 101;

    @BindView(R.id.iv_user_back)
    ImageView ivUserBack;
    @BindView(R.id.iv_user_password_login)
    TextView ivUserPasswordLogin;
    @BindView(R.id.et_user_phone)
    EditText etUserPhone;
    @BindView(R.id.tv_user_code)
    TextView tvUserCode;
    @BindView(R.id.et_user_psd)
    EditText etUserPsd;
    @BindView(R.id.et_user_code)
    EditText etUserCode;
    @BindView(R.id.login)
    TextView login;
    private int time = 60;//倒计时时长
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case KEEP_TIME_DES:
                    //时间需要递减
                    time--;
                    //递减时间体现在页面上
                    tvUserCode.setText("请稍后("+time+")");
                    break;
                case TIME_RESET:
                    time = 60;
                    tvUserCode.setText("重新获取");
                    //倒计时结束,按钮才可用
                    tvUserCode.setEnabled(true);
                    break;
            }
            super.handleMessage(msg);
        }
    };

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
                    //校验成功,让用户注册进去
                    Log.i("", "检验验证码短信成功==============");
                    registerUser();
                }else{
                    //校验失败,验证码错误
                    Log.i("", "检验验证码短信失败==============");
                }
            }
            super.afterEvent(event, result, data);
        }
    };

    /**
     * 用户可以发送注册请求
     */
    private void registerUser() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        //注册一个事件处理器,用于监听下发验证码短信和校验验证码短信结果
        SMSSDK.registerEventHandler(eventHandler);
    }

    @OnClick({R.id.tv_user_code,R.id.login})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.tv_user_code:
                sendCode();
                break;
            case R.id.login:
                checkCode();
                break;
        }
    }

    private void checkCode() {
        String phone = etUserPhone.getText().toString().trim();
        String psd = etUserPsd.getText().toString().trim();
        String code = etUserCode.getText().toString().trim();

        if(SMSUtil.isMobileNO(phone) && !TextUtils.isEmpty(psd) && !TextUtils.isEmpty(code)){
            //短信验证码校验
            SMSSDK.submitVerificationCode("86",phone,code);
        }
    }

    private void sendCode() {
        //1.判断是否输入合法手机号(正则表达式)
        String phone = etUserPhone.getText().toString().trim();
        if(SMSUtil.isMobileNO(phone)){
            //2.通过SMSSDK下发验证码短信
            SMSSDK.getVerificationCode("86", phone, new OnSendMessageHandler() {
                @Override
                public boolean onSendMessage(String country, String phone) {
                    //返回true  不去发送   false 需要发送
                    return false;
                }
            });

            //让右侧按钮进行倒计时
            //按钮在下发验证码短信的时候不可用
            tvUserCode.setEnabled(false);
            new Thread(){
                @Override
                public void run() {
                    while(time>0){
                        //进行倒计时操作
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //在睡眠结束后,更新时间,页面中体现目前倒计时时间
                        handler.sendEmptyMessage(KEEP_TIME_DES);
                    }
                    handler.sendEmptyMessage(TIME_RESET);
                }
            }.start();
        }else{
            Toast.makeText(this, "手机号不合法", Toast.LENGTH_SHORT).show();
        }
    }
}
