package com.reven.netarchitecture.utils.rxbus;

/**
 * Created by LiuCongshan on 2016/9/29.
 */

import com.reven.netarchitecture.utils.DebugLog;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import rx.Subscription;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

public class RxBus {
    private final Subject<Object, Object> bus;
    private HashMap<Object, ArrayList<Subscription>> subscriptions;

    public RxBus() {
        bus = new SerializedSubject<>(PublishSubject.create());
    }

    public static RxBus getDefault() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 发送事件
     *
     * @param event 事件对象
     */
    public void post(Object event) {
        bus.onNext(event);
    }

    /**
     * 订阅事件
     *
     * @param subscriber 订阅者
     * @param callback   事件回调
     * @param <T>        事件类型
     */
    public <T> void subscribe(Object subscriber, Callback<T> callback) {
        Class<T> class1 = getRawType(callback);
        add(subscriber, bus.ofType(class1).subscribe(callback));
    }

    private <T> Class<T> getRawType(Callback<T> callback) {
        Type genericSuperclass = callback.getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
        Type type = params[0];
        return (Class<T>) getRawType(type);
    }

    private Class<?> getRawType(Type type) {
        if (type == null) throw new NullPointerException("type == null");

        if (type instanceof Class<?>) {
            // Type is a normal class.
            return (Class<?>) type;
        }
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;

            // I'm not exactly sure why getRawType() returns Type instead of Class. Neal isn't either but
            // suspects some pathological case related to nested classes exists.
            Type rawType = parameterizedType.getRawType();
            if (!(rawType instanceof Class)) throw new IllegalArgumentException();
            return (Class<?>) rawType;
        }
        if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            return Array.newInstance(getRawType(componentType), 0).getClass();
        }
        if (type instanceof TypeVariable) {
            // We could use the variable's bounds, but that won't work if there are multiple. Having a raw
            // type that's more general than necessary is okay.
            return Object.class;
        }
        if (type instanceof WildcardType) {
            return getRawType(((WildcardType) type).getUpperBounds()[0]);
        }

        throw new IllegalArgumentException("Expected a Class, ParameterizedType, or " + "GenericArrayType, but <" +
            type + "> is of type " + type.getClass().getName());
    }

    /**
     * 添加订阅协议
     *
     * @param subscriber   订阅者
     * @param subscription 订阅协议
     */
    private void add(Object subscriber, Subscription subscription) {
        if (subscriptions == null) {
            subscriptions = new HashMap<>();
        }

        ArrayList<Subscription> list = subscriptions.get(subscriber);
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(subscription);
        subscriptions.put(subscriber, list);
    }

    /**
     * 取消事件订阅
     *
     * @param subscriber 订阅者
     */
    public void unsubscribe(Object subscriber) {
        DebugLog.e("unsubscribe");
        if (subscriptions == null) {
            return;
        }

        ArrayList<Subscription> list = subscriptions.get(subscriber);
        if (list == null) {
            return;
        }

        for (Subscription subscription : list) {
            subscription.unsubscribe();
        }
        subscriptions.remove(subscriber);
    }

    /**
     * 取消所有订阅
     */
    public void unsubscribeAll() {
        Set<Map.Entry<Object, ArrayList<Subscription>>> set = subscriptions.entrySet();
        for (Map.Entry<Object, ArrayList<Subscription>> entry : set) {
            ArrayList<Subscription> subscriptions = entry.getValue();
            for (Subscription subscription : subscriptions) {
                subscription.unsubscribe();
            }
        }
        subscriptions = null;
    }

    private static class SingletonHolder {
        public static RxBus INSTANCE = new RxBus();
    }
}
