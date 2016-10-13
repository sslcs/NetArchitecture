package com.sslcs.uniform.net;

import rx.Subscription;

public class NetTask {
    private Subscription task;

    public NetTask(Subscription task) {
        this.task = task;
    }

    /**
     * 取消网络请求
     */
    public void cancel() {
        if (task != null && !task.isUnsubscribed()) {
            task.unsubscribe();
        }
    }
}
