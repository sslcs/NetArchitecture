package com.reven.netarchitecture.net;

import com.reven.netarchitecture.net.bean.params.BaseParams;

/**
 * Created by LiuCongshan on 2016/9/25.
 */
public class Api {

    public static Task login(BaseParams params, NetCallback callback) {
        return new NetTask();
    }
}
