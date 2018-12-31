package com.crawler.example.app;

import com.crawler.example.entity.AppTask;
import com.crawler.example.map.AppTaskMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@RestController
public class TaskRunner {

    private final Logger log = LoggerFactory.getLogger(TaskRunner.class);

    private static List<AppTask> appTasks;

    @Autowired
    private AppTaskMap appTaskMap;
/*
    @Autowired
    private List<ITaskRunner> tasks;
*/
    private final static int corePoolSize = Runtime.getRuntime().availableProcessors();

    private static ThreadPoolExecutor executor  = new ThreadPoolExecutor(corePoolSize, corePoolSize+1, 10l, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(1000));

    @Scheduled(fixedDelay=30000)
    public void start() {
        log.info("Application is running...................");

        refreshAppTasks();

        List<Future<String>> resultList = new ArrayList<Future<String>>();

        for(AppTask appTask : appTasks){

            try {
                Class clazz = Class.forName(appTask.getJclass());
                ITaskRunner task = (ITaskRunner)ApplicationContextProvider.getBean(clazz);

                if(task==null)continue;

                task.setTask(appTask);
                resultList.add(executor.submit(task));
                /*
                task.call();
                appTask.setStatus(AppTaskStatus.CLOSED.name());
                updateAppTask(appTask);
                */
            }
            catch (ClassNotFoundException ex){
                log.error("Can't found class " + appTask.getJclass());
            }
            catch (Exception ex){
                log.error(ex.getLocalizedMessage());
                ex.printStackTrace();
            }
        }

        //遍历任务的结果
        for (Future<String> fs : resultList) {
            try {
                log.info(fs.get());//打印各个线任务执行的结果，调用future.get() 阻塞主线程，获取异步任务的返回结果
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            /*finally {
                //启动一次顺序关闭，执行以前提交的任务，但不接受新任务。如果已经关闭，则调用没有其他作用。
                executor.shutdown();
            }*/
        }

        log.info("Application is done!");
    }

    public void refreshAppTasks(){
        //Get pending tasks
        appTasks = appTaskMap.getAppTasksByStatus(AppTaskStatus.PENDING.name());
    }

    public void updateAppTask(AppTask appTasks){
        appTaskMap.update(appTasks);
    }

    public static List<AppTask> getPendingAppTasks() {
        return appTasks;
    }

    /*
    public ITaskRunner getTask(String clazz){
        for(ITaskRunner task : tasks){
            if(task.getClass().getName().equals(clazz))
                return task;
        }

        return null;
    }
    */
}
