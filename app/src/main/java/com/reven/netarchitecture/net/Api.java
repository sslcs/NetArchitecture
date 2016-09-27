package com.reven.netarchitecture.net;

import com.reven.netarchitecture.net.bean.params.BaseParams;

/**
 * Created by LiuCongshan on 2016/9/25.
 */
public interface Api {
    /**
     * 登录接口
     *
     * @param params   请求参数
     * @param callback 回调接口
     * @return 请求任务
     */
    Task login(BaseParams params, NetCallback callback);
}
