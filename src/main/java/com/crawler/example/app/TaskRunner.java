package com.crawler.example.app;

import com.crawler.example.entity.AppTask;
import com.crawler.example.map.AppTaskMap;
import com.crawler.example.web.LvseSites;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TaskRunner {

    private final Logger log = LoggerFactory.getLogger(TaskRunner.class);

    private static List<AppTask> appTasks;

    @Autowired
    private AppTaskMap appTaskMap;

    @Autowired
    private List<ITaskRunner> tasks;

    @Scheduled(fixedRate=30000)
    public void start() {
        log.info("Application is running...................");

        refreshAppTasks();

        for(AppTask appTask : appTasks){
            try {
                String clazz = appTask.getJClass();
                ITaskRunner task = getTask(clazz);

                if(task==null)continue;

                task.setTask(appTask);
                task.run();
                appTask.setStatus(AppTasksStatus.CLOSED.name());
                updateAppTask(appTask);
            }
            catch (Exception ex){
                log.error(ex.getLocalizedMessage());
                ex.printStackTrace();
            }
        }

        log.info("Application is done!");
    }

    public void refreshAppTasks(){
        //Get pending tasks
        appTasks = appTaskMap.getAppTasksByStatus(AppTasksStatus.PENDING.name());
    }

    public void updateAppTask(AppTask appTasks){
        appTaskMap.update(appTasks);
    }

    public static List<AppTask> getPendingAppTasks() {
        return appTasks;
    }

    public ITaskRunner getTask(String clazz){
        for(ITaskRunner task : tasks){
            if(task.getClass().getName().equals(clazz))
                return task;
        }

        return null;
    }
}
