package com.reven.netarchitecture.net.bean.params;

import com.reven.netarchitecture.utils.GsonSingleton;

/**
 * Created by LiuCongshan on 2016/9/25.
 */
public class BaseParams {
    @Override
    public String toString() {
        return GsonSingleton.get().toJson(this);
    }
}
