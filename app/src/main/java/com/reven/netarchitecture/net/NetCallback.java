package com.reven.netarchitecture.net;

/**
 * Created by Reven on 2016/9/25.
 */
public abstract class NetCallback<T> {
    /**
     * 请求成功时回调接口
     * @param t 返回结果
     */
    public abstract void onSuccess(T t);

    /**
     * 请求失败时回调接口
     * @param e 错误信息
     */
    public abstract void onError(Throwable e);
}
