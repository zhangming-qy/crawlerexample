package com.crawler.example.app;

import com.crawler.example.entity.AppTask;

import java.util.concurrent.Callable;

public interface ITaskRunner extends Callable<AppTask> {
    void setTask(AppTask appTask);
    boolean isSupportConcurrent();
}
