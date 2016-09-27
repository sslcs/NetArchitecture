**Android网络架构，RxJava和Retrofit实践**

谈起网络框架，大家基本上都很熟悉。从Google推出的Volley，到大名鼎鼎的OkHttp、Retrofit，还有比较基础的HttpUrlConnection、android-async-http，以及已经被废弃掉的HttpClient。应该也都经历过每次更换框架时，牵一发而动全身的那种痛苦，明明只是想换个网络框架而已，结果大部分文件都被改动了。这就是本文想解决的一个问题：搭建一个网络架构，通过修改有限的几个文件，达成更换网络框架的目的。

我们从最基础的网络请求开始思考，其主要元素包括：请求参数(params)、结果回调(callback)和请求任务(task)。不管用什么框架，请求参数基本上不用改动，但结果回调和请求任务会随着框架的更改变得大不一样，所以我们主要考虑对这两个对象做下封装，使其能够适应不同的框架。

首先我们定义一个结果回调类NetCallback:
```
public abstract class NetCallback {
    /**
     * 请求成功时回调接口
     * @paramt返回结果
     */
    public abstract void onSuccess(T t);

    /**
     * 请求失败时回调接口
     * @parame错误信息
     */
    public abstract void onError(Throwable e);
}
```
再定义一个请求任务的统一接口Task:
```
public interface Task {
    /**
     * 取消网络请求
     */
    void cancel();
}
```
请求任务接口实现类NetTask，这是一个空的实现了，以后可以根据框架添加RetrofitTask、RxJavaTask等等。
```
public class NetTask implements Task {
    @Override
    public void cancel(){
    }
}
```
这样基本架构就算完成，可以进行接口调用了。不过调用接口之前，要先定义接口及其实现类。我们用一个登录接口简单模拟下。
接口类Api：
```
public interface Api {
    /**
     * 登录接口
     * @param params 请求参数
     * @param callback 回调接口
     * @return 请求任务
     */
    Task login(BaseParams params, NetCallback callback);
}
```
接口实现类ApiImpl：
```
public enum  ApiImpl implements Api {
    INSTANCE;

    @Override
    public Task login(BaseParams params, NetCallback callback) {
        return new NetTask();
   }
}
```
OK，现在可以进行调用了。为了避免重复添加代码，我们先定义一个基类TaskActivity，进行请求任务的管理：
```
public class TaskActivity extends BaseActivity {
    private ArrayList<Task> tasks;

    /**
     * 添加请求任务
     * @param task 请求任务
     */
    public void addTask(Task task) {
        if (tasks == null) {
            tasks = new ArrayList<>();
        }
        tasks.add(task);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tasks != null) {
            for (Task task : tasks) {
                if (task != null) task.cancel();
            }
        }
    }
}
```
TaskActivity的子类MainActivity:
```
public class MainActivity extends TaskActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        login();
    }

    public void login() {
        LoginParams params = new LoginParams();
        NetCallback<BaseResponse> callback = new NetCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                DebugLog.e("onSuccess");
            }

            @Override
            public void onError(Throwable e) {
                DebugLog.e("onError : " + e.getMessage());
            }
        };
        addTask(ApiImpl.INSTANCE.login(params, callback));
    }
}
```
一个登录接口就这样调用完成了。请求参数(params)、结果回调(callback)和请求任务(task)，网络请求是不是既清晰又简单方便？！