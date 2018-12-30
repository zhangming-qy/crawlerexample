package com.crawler.example.app;

import com.crawler.example.app.AppTasksStatus;
import com.crawler.example.entity.AppTask;
import com.crawler.example.map.AppTaskMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class AppTaskMan implements ITaskRunner {

    @Autowired
    private AppTaskMap appTaskMap;

    private AppTask appTask;

    public AppTaskMan(){}

    public AppTask getAppTask(){
        return this.appTask;
    }

    public void setAppTaskMap(AppTaskMap appTaskMap) {
        this.appTaskMap = appTaskMap;
    }

    public void updateAppTasksStatus(AppTasksStatus appTasksStatus){
        appTask.setStatus(appTasksStatus.name());
        appTask.setModified_time(new Timestamp(System.currentTimeMillis()));
        updateAppTasks();
    }

    public void updateAppTasksCurrUrl(String url){
        appTask.setCurr_url(url);
        appTask.setModified_time(new Timestamp(System.currentTimeMillis()));
        updateAppTasks();
    }

    public void updateAppTasks(){
        appTaskMap.update(appTask);
    }

    @Override
    public void setTask(AppTask appTask) {
        this.appTask = appTask;
    }

    @Override
    public void run() {
        //TODO
    }
}
