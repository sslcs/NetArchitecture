package com.sslcs.uniform.net.retrofit;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitSender {
    private AppServer server;

    //构造方法私有
    private RetrofitSender() {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();
        okBuilder.connectTimeout(30, TimeUnit.SECONDS);
        okBuilder.readTimeout(31, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder().client(okBuilder.build())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .baseUrl("https://api.douban.com/v2/movie/")
            .build();
        server = retrofit.create(AppServer.class);
    }

    //获取单例
    public static AppServer getInstance() {
        return SingletonHolder.INSTANCE.server;
    }

    private static class SingletonHolder {
        private static final RetrofitSender INSTANCE = new RetrofitSender();
    }
}
