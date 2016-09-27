    package com.reven.netarchitecture.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.reven.netarchitecture.R;
import com.reven.netarchitecture.databinding.ActivityMainBinding;
import com.reven.netarchitecture.net.ApiImpl;
import com.reven.netarchitecture.net.NetCallback;
import com.reven.netarchitecture.net.bean.params.BaseParams;
import com.reven.netarchitecture.net.bean.params.LoginParams;
import com.reven.netarchitecture.net.bean.response.BaseResponse;
import com.reven.netarchitecture.utils.DebugLog;

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
        addTask(ApiImpl.INSTANCE.login(params, callback));
    }
}
