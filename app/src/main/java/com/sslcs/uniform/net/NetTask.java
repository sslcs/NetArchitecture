package com.sslcs.uniform.net;

import retrofit2.Call;

public class NetTask implements Task {
    private Call call;

    public NetTask(Call call) {
        this.call = call;
    }

    @Override
    public void cancel() {
        if (call != null && !call.isCanceled()) {
            call.cancel();
        }
    }
}
