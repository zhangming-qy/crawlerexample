package com.crawler.example.app;

public class TaskRunnerMon implements Runnable {

    @Override
    public void run() {
        TaskRunner.checkSubmittedAppTasks();
    }
}
