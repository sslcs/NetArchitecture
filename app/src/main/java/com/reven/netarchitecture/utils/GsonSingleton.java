package com.reven.netarchitecture.utils;

import com.google.gson.Gson;

public class GsonSingleton {
    public static Gson get() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final Gson INSTANCE = new Gson();
    }
}
