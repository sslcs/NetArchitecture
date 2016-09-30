package com.reven.netarchitecture.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.reven.netarchitecture.R;
import com.reven.netarchitecture.databinding.ActivityNewBinding;
import com.reven.netarchitecture.net.bean.params.LoginParams;
import com.reven.netarchitecture.net.bean.response.BaseResponse;
import com.reven.netarchitecture.utils.rxbus.RxBus;

import java.util.ArrayList;

public class ActivityTest extends BaseActivity {
    private ActivityNewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new);
    }

    public void onClickSendFirst(View view) {
        RxBus.getDefault().post(new ArrayList<String>());
    }

    public void onClickSendSecond(View view) {
        RxBus.getDefault().post(new BaseResponse());
    }
}
