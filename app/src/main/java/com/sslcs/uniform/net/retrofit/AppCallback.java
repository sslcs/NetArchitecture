package com.sslcs.uniform.net.retrofit;

import rx.Subscriber;

public abstract class AppCallback<T> extends Subscriber<T> {
    @Override
    public final void onError(Throwable e) {
        preprocessor();
        onFail(e);
    }

    @Override
    public final void onNext(T t) {
        preprocessor();
        onSuccess(t);
    }

    @Override
    public final void onCompleted() {}

    public abstract void preprocessor();

    public abstract void onSuccess(T response);

    public abstract void onFail(Throwable e);
}
