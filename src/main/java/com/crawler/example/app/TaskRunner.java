package com.crawler.example.app;

import com.crawler.example.entity.AppTask;
import com.crawler.example.map.AppTaskMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

@Component
@Profile("!test")
public class TaskRunner implements CommandLineRunner,Runnable {

    private final static Logger log = LoggerFactory.getLogger(TaskRunner.class);

    private static List<AppTask> appTasks;

    @Autowired
    private AppTaskMap appTaskMap;

    public TaskRunner(){}

    @Async
    @Override
    public void run(String[] args) {
        log.info("Application is starting schedule tasks...................");

        //add refresh and monitor running status task
        //TaskScheduler.setMaximumPoolSize(TaskScheduler.CORE_POOL_SIZE*2);
        TaskScheduler.scheduleWithFixedDelay(this, 0,30, TimeUnit.SECONDS);
        TaskScheduler.scheduleWithFixedDelay(new Thread( () -> TaskScheduler.removeFinishedAppTasks()), 30, 30, TimeUnit.SECONDS);

        log.info("Application is scheduled done, tasks are running.");
    }

    @Override
    public void run() {
        refreshAppTasks();
        submitAppTasks();
    }

    public void refreshAppTasks(){
        //Get and add tasks order by status
        List<String> listStatus = new ArrayList(Arrays.asList(AppTaskStatus.PENDING.name(),
                AppTaskStatus.RUNNING.name(),
                AppTaskStatus.HOLDING.name(),
                AppTaskStatus.WAITING.name()));

        appTasks = appTaskMap.getAppTasksInAndOrderByStatus(listStatus);

        log.info("Added Pending, Running, Holding, Waiting tasks {}.", appTasks.size());

        //appTasks = appTaskMap.getAppTasksByStatus(AppTaskStatus.PENDING.name());
        //log.info("Added Pending tasks.");

        //if no pending tasks, resume running tasks
        //if(appTasks.size() == 0 && TaskScheduler.getScheduledActiveCount() <= 2){}
    }

    public void submitAppTasks(){

        for(AppTask appTask : appTasks){
            TaskScheduler.submitAppTasks(appTask);
        }
    }

}
