package com.sslcs.uniform.net;

import com.sslcs.uniform.net.bean.response.Top250Response;
import com.sslcs.uniform.net.retrofit.RetrofitSender;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class Api {
    public static NetTask getTop250(int start, int count, NetCallback<Top250Response> callback) {
        Subscription task = RetrofitSender.getInstance()
            .getTop250(start, count)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(callback);
        return new NetTask(task);
    }
}
