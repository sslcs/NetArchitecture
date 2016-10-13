**Android网络架构，RxJava和Retrofit实践**

谈起网络框架，大家基本上都很熟悉。从Google推出的Volley，到大名鼎鼎的OkHttp、Retrofit，还有比较基础的HttpUrlConnection、android-async-http，以及已经被废弃掉的HttpClient。应该也都经历过每次更换框架时，牵一发而动全身的那种痛苦，明明只是想换个网络框架而已，结果大部分文件都被改动了。这就是本文想解决的一个问题：搭建一个网络架构，通过修改有限的几个文件，达成更换网络框架的目的。

我们从最基础的网络请求开始思考，其实网络请求的过程和买包子的过程差不多。`你向老板要两个菜包，然后老板去拿包子你耐心等待。拿到包子之后开吃。`这其中包含三个主要元素：请求参数(params)`两个菜包`、结果回调(callback)`拿到包子之后开吃`和请求任务(task)`老板去拿包子`。不管用什么框架，请求参数基本上不用改动，就像去不同的包子店同样说`要两个菜包`就行了。但结果回调会有所改变，`有的用袋子装好给你、有的用餐盒装好给你`。请求任务也会大不一样，`有的多个人分别去拿、有的一个人挨个拿、有的拿到快、有的拿的慢`。综上我们考虑对结果回调和请求任务做下封装，使不同的框架最终都要调用这两个封装对象，这样更换框架时只要修改这两个对象和接口实现的类，具体调用的地方不用做任何修改。

#### 基础版本 ####
首先我们定义一个结果回调**抽象类**NetCallback，这样根据不同的框架可以继承不同的实现，调用的地方就不用变动了。
```
public abstract class NetCallback {
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
```
再定义一个请求任务类NetTask，所有任务都要能够取消，这样界面退出时能够避免一些网络流量的浪费。
```
public class NetTask {
    /**
     * 取消网络请求
     */
    public void cancel();
}
```
这样基本架构就算完成，可以进行接口调用了。不过调用接口之前，要先添加一个接口接口类，这样可以在不同的地方调用同一个接口，减少复制粘贴的操作。接口实现类Api：
```
public class Api {
    /**
     * 豆瓣电影Top250
     */
    public static NetTask getTop250(int start, int count, NetCallback<Top250Response> callback{
        return new NetTask();
    }
}
```
OK，现在可以进行调用了。为了避免重复添加代码，我们先定义一个基类TaskActivity，进行请求任务的管理：
```
public class TaskActivity extends BaseActivity {
    private ArrayList<NetTask> tasks;

    /**
     * 添加请求任务
     * @param task 请求任务
     */
    public void addTask(NetTask task) {
        if (tasks == null) {
            tasks = new ArrayList<>();
        }
        tasks.add(task);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tasks != null) {
            for (NetTask task : tasks) {
                if (task != null) task.cancel();
            }
        }
    }
}
```
接口调用类，TaskActivity的子类MainActivity:
```
public class MainActivity extends TaskActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        getTop250();
    }

    public void getTop250() {
        NetCallback<Top250Response> callback = new NetCallback<Top250Response>() {
            @Override
            public void onSuccess(Top250Responseresponse) {
                DebugLog.e("onSuccess");
            }

            @Override
            public void onError(Throwable e) {
                DebugLog.e("onError : " + e.getMessage());
            }
        };
        addTask(Api.getTop250(0, 20, callback));
    }
}
```
一个登录接口就这样调用完成了。请求参数(params)、结果回调(callback)和请求任务(task)，网络请求是不是既清晰又简单方便？！

#### Retrofit实现 ####
我们用Retrofit实验一下，看看该架构的实用性（Retrofit的相关依赖这里不做介绍，直接使用）。首先添加Retrofit相关代码，Retrofit接口类AppSever：
```
public interface AppServer {
    @GET("top250")
     Call<Top250Response> getTop250(@Query("start") int start, @Query("count") int count);
}
```
Retrofit对象单例封装RetrofitSender：
```
public class RetrofitSender {
    private AppServer server;

    //构造方法私有
    private RetrofitSender() {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();
        okBuilder.connectTimeout(30, TimeUnit.SECONDS);
        okBuilder.readTimeout(31, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder().client(okBuilder.build())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.douban.com/v2/movie/")
            .build();
        server = retrofit.create(AppServer.class);
    }

    //获取单例
    public static AppServer getInstance() {
        return SingletonHolder.INSTANCE.server;
    }

    private static class SingletonHolder {
        private static final RetrofitSender INSTANCE = new RetrofitSender();
    }
}
```
Retrofit接口回调类AppCallback：
```
public abstract class AppCallback<T> implements Callback<T> {
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        onSuccess(response.body());
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        onError(t);
    }

    //请求成功回调
    public abstract void onSuccess(T response);

    //请求失败回调
    public abstract void onError(Throwable e);
}
```
添加完毕，调用Retrofit需要做哪些修改呢？首先NetCallback继承AppCallback，仅仅一行改动：
```
public abstract class NetCallback<T> extends AppCallback<T> {
    //其他不用改动
}
```
然后包装Retrofit的Call，修改NetTask：
```
public class NetTask {
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
```
最后修改接口实验类Api：
```
public static NetTask getTop250(int start, int count, NetCallback<Top250Response> callback) {
    Call<Top250Response> call = RetrofitSender.getInstance()
        .getTop250(start, count);
    call.enqueue(callback);
    return new NetTask(call);
}
```
OK，具体调用的地方根本没有做任何改动，网络框架已经切换到Retrofit。以后是不是再也不担心更换网络框架了？！

#### Retrofit & RxJava ####
我们再来实验一下Retrofit结合RxJava的网络框架，这次需要改动多少呢？首先修改Retrofit接口类AppSever，函数返回类型修改为Observable：
```
public interface AppServer {
    @GET("top250")
    Observable<Top250Response> getTop250(@Query("start") int start, @Query("count") int count);
}
```
Retrofit对象单例封装RetrofitSender增加一行：
```
Retrofit retrofit = new Retrofit.Builder().client(okBuilder.build())
    .addConverterFactory(GsonConverterFactory.create())
    //下面是新增加的一行
    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
    .baseUrl("https://api.douban.com/v2/movie/")
    .build();
```
修改Retrofit接口回调类AppCallback：
```
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
```
然后包装RxJava的Subscription，修改NetTask：
```
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
```
最后修改接口实验类Api：
```
public class Api {
    public static NetTask getTop250(int start, int count, NetCallback<Top250Response> callback) {
        Subscription task = RetrofitSender.getInstance()
            .getTop250(start, count)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(callback);
        return new NetTask(task);
    }
}
```
OK，运行，一切正常！完全没有动业务逻辑相关代码，网络框架的更换轻松顺畅。再也不用担心更换网络框架时伤筋动骨肉相连绵不断的改动了！！！