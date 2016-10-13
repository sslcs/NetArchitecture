package com.sslcs.uniform.net;

import com.sslcs.uniform.net.bean.response.Top250Response;
import com.sslcs.uniform.net.retrofit.RetrofitSender;

import retrofit2.Call;

public class Api {
    public static  Task getTop250(int start, int count, NetCallback<Top250Response> callback) {
        Call<Top250Response> call = RetrofitSender.getInstance()
            .getTop250(start, count);
        call.enqueue(callback);
        return new NetTask(call);
    }
}
