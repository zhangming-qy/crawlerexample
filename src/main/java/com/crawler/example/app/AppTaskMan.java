package com.crawler.example.app;

import com.crawler.example.entity.AppTask;
import com.crawler.example.map.AppTaskMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
@Scope("prototype")
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

    public void updateAppTasksStatus(AppTaskStatus appTaskStatus){
        appTask.setStatus(appTaskStatus.name());
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

    public AppTaskStatus getAppStatus(){
        return AppTaskStatus.valueOf(appTask.getStatus());
    }

    public String getAppStatusStr(){
        return appTask.getStatus();
    }

    @Override
    public void setTask(AppTask appTask) {
        this.appTask = appTask;
    }

    @Override
    public boolean isSupportConcurrent() {
        return false;
    }

    @Override
    public AppTask call() {
        //TODO
        return appTask;
    }
}
