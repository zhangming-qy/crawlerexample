package com.crawler.example.app;

import com.crawler.example.entity.AppTask;
import com.crawler.example.entity.ComInfo;
import com.crawler.example.map.ComInfoMap;
import com.crawler.example.web.MsgPageLookup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Component
@Scope("prototype")
public class MsgPageLookupTask implements ITaskRunner {
    private final Logger log = LoggerFactory.getLogger(MsgPageLookupTask.class);

    @Autowired
    private AppTaskMan appTaskMan;

    @Autowired
    private ComInfoMap comInfoMap;

    public AppTask getAppTask() {
        return appTaskMan.getAppTask();
    }

    @Override
    public void setTask(AppTask appTask) {
        appTaskMan.setTask(appTask);
    }

    @Override
    public boolean isSupportConcurrent() {
        return true;
    }

    @Override
    public AppTask call() {
        AppTask appTask = getAppTask();
        List<ComInfo> comInfoList = comInfoMap.getMsgUnRequestedListByCategory(appTask.getGroup_name());

        //Status PENDING to RUNNING
        if(appTask.getStatus().equals(AppTaskStatus.PENDING.name()))
            appTaskMan.updateAppTasksStatus(AppTaskStatus.RUNNING);
        else if(comInfoList.size() == 0 && appTask.getStatus().equals(AppTaskStatus.RUNNING.name())){
            appTaskMan.updateAppTasksStatus(AppTaskStatus.WAITING);
        }

        MsgPageLookup.setComInfoDeque(comInfoList);

        TaskScheduler taskScheduler = new TaskScheduler();
        Queue<Future<?>> futureList = new LinkedList<>();
        for(int i=0;i<comInfoList.size();i++){
            futureList.add(taskScheduler.submit(ApplicationContextProvider.getBean(MsgPageLookup.class)));
        }

        log.info("Added message page lookup schedule tasks {}.", comInfoList.size());

        //waiting all tasks finish
        while(futureList.peek()!=null){
            try{
                Future<?> fs = futureList.poll();
                fs.get();
            }
            catch (InterruptedException e){
                log.error(e.getLocalizedMessage());
                e.printStackTrace();
            }
            catch (ExecutionException e){
                log.error(e.getLocalizedMessage());
                e.printStackTrace();
            }
        }

        log.info("All message page lookup schedule tasks {} are finished.", futureList.size());

        return appTask;
    }
}
