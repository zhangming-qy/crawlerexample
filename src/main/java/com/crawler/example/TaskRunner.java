package com.crawler.example;

import com.crawler.example.entity.AppTasks;
import com.crawler.example.map.AppTasksMap;
import com.crawler.example.web.LvseSites;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TaskRunner implements Runnable {

    private final Logger log = LoggerFactory.getLogger(TaskRunner.class);

    private static List<AppTasks> appTasks;

    @Autowired
    private AppTasksMap appTasksMap;

    @Override
    @Scheduled(fixedRate=30000)
    public void run() {
        log.info("Application is already started...................");

        //Get pending tasks
        appTasks = appTasksMap.getAppTasksByStatus(AppTasksStatus.PENDING);

        for(AppTasks appTask : appTasks){
            try {
                if (appTask.getJClass() != null) {
                    Class<?> clazz = Class.forName(appTask.getJClass());
                    clazz.getMethod("run").invoke(null);
                }
            }
            catch (ClassNotFoundException ex){
                log.error(ex.getLocalizedMessage());
            }
            catch (NoSuchMethodException ex){
                ex.getLocalizedMessage();
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
        }

        LvseSites lvseSites = new LvseSites();
        lvseSites.run();
    }

    public static List<AppTasks> getAppTasks() {
        return appTasks;
    }

    public AppTasksStatus getCurrentTaskStatus(){

    }
}
