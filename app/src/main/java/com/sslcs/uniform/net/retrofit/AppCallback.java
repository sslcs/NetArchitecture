package com.sslcs.uniform.net.retrofit;

import rx.Subscriber;

public abstract class AppCallback<T> extends Subscriber<T> {
    public abstract void onSuccess(T response);

    @Override
    public abstract void onError(Throwable e);

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    @Override
    public void onCompleted() { }
}
