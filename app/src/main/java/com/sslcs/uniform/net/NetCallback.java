package com.sslcs.uniform.net;

import com.sslcs.uniform.net.retrofit.AppCallback;

public abstract class NetCallback<T> extends AppCallback<T> {
    /**
     * 请求成功时回调接口
     *
     * @param response 返回结果
     */
    public abstract void onSuccess(T response);

    /**
     * 请求失败时回调接口
     *
     * @param e 错误信息
     */
    public abstract void onError(Throwable e);
}
