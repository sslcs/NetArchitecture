package com.sslcs.uniform.ui;

import com.sslcs.uniform.net.Task;

import java.util.ArrayList;

public class TaskActivity extends BaseActivity {
    private ArrayList<Task> tasks;

    /**
     * 添加请求任务
     *
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
