package com.sslcs.uniform.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.sslcs.uniform.R;
import com.sslcs.uniform.databinding.ActivityMainBinding;
import com.sslcs.uniform.net.Api;
import com.sslcs.uniform.net.NetCallback;
import com.sslcs.uniform.net.bean.response.Top250Response;
import com.sslcs.uniform.utils.DebugLog;

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
            public void onSuccess(Top250Response response) {
                DebugLog.e("onSuccess : " + response.title +" count: " + response.subjects.size());
                MovieAdapter adapter = new MovieAdapter(MainActivity.this, response.subjects);
                binding.lvMovies.setAdapter(adapter);
                binding.tvTitle.setText(response.title);
            }

            @Override
            public void onError(Throwable e) {
                DebugLog.e("onError : " + e.getMessage());
            }
        };
        addTask(Api.getTop250(0, 20, callback));
    }
}
