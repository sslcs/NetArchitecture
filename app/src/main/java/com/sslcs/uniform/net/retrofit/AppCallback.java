package com.sslcs.uniform.net.retrofit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class AppCallback<T> implements Callback<T> {
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        onSuccess(response.body());
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        onError(t);
    }

    //请求成功回调
    public abstract void onSuccess(T response);

    //请求失败回调
    public abstract void onError(Throwable e);
}
