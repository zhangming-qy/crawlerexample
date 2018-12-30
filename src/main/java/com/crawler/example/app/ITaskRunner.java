package com.crawler.example.app;

import com.crawler.example.entity.AppTask;

public interface ITaskRunner extends Runnable {
    public void setTask(AppTask appTask);
}
