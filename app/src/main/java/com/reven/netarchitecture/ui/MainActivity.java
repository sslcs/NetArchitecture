package com.reven.netarchitecture.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.reven.netarchitecture.R;
import com.reven.netarchitecture.databinding.ActivityMainBinding;
import com.reven.netarchitecture.net.Api;
import com.reven.netarchitecture.net.NetCallback;
import com.reven.netarchitecture.net.bean.params.LoginParams;
import com.reven.netarchitecture.net.bean.response.BaseResponse;
import com.reven.netarchitecture.utils.DebugLog;
import com.reven.netarchitecture.utils.rxbus.RxBus;
import com.reven.netarchitecture.utils.rxbus.Callback;

import java.util.ArrayList;

public class MainActivity extends TaskActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        login();
    }

    public void login() {
        LoginParams params = new LoginParams();
        NetCallback<BaseResponse> callback = new NetCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                DebugLog.e("onSuccess");
            }

            @Override
            public void onError(Throwable e) {
                DebugLog.e("onError : " + e.getMessage());
            }
        };
        addTask(Api.login(params, callback));
    }

    public void onClickNew(View view) {
        RxBus.getDefault().subscribe(this, new Callback<ArrayList<String>>() {
            @Override
            public void onEvent(ArrayList<String> params) {
                binding.vLeft.setBackgroundColor(0xff123456);
                DebugLog.e("call ArrayList<String");
            }
        });
        RxBus.getDefault().subscribe(this, new Callback<BaseResponse>() {
            @Override
            public void onEvent(BaseResponse response) {
                binding.vRight.setBackgroundColor(0xff456789);
                DebugLog.e("call BaseResponse");
            }
        });

        startActivity(new Intent(this, ActivityTest.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        RxBus.getDefault().unsubscribe(this);
    }
}
