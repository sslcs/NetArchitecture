package com.sslcs.uniform.net.retrofit;

import com.sslcs.uniform.net.bean.response.Top250Response;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AppServer {
    @GET("top250")
     Call<Top250Response> getTop250(@Query("start") int start, @Query("count") int count);
}
