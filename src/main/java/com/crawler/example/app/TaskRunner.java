package com.crawler.example.app;

import com.crawler.example.entity.AppTask;
import com.crawler.example.map.AppTaskMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
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

    private static ThreadPoolExecutor executor  = new ThreadPoolExecutor(corePoolSize, corePoolSize*2, 10l, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(1000));

    @Scheduled(fixedDelay=120000) //delay 2mins
    public void start() {
        log.info("Application is running...................");

        refreshAppTasks();

        List<Future<AppTask>> resultList = new ArrayList<>();

        for(AppTask appTask : appTasks){

            try {
                Class clazz = Class.forName(appTask.getJclass());
                ITaskRunner task = (ITaskRunner)ApplicationContextProvider.getBean(clazz);

                if(task==null)continue;

                task.setTask(appTask);
                resultList.add(executor.submit(task));
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
        for (Future<AppTask> fs : resultList) {
            try {
                //打印各个线任务执行的结果，调用future.get() 阻塞主线程，获取异步任务的返回结果
                AppTask appTask = fs.get(10, TimeUnit.MINUTES);
                log.info("Task [root url=" +  appTask.getRoot_url() +
                        "; group name=" + appTask.getGroup_name() +
                        "; java class=" + appTask.getJclass() +
                        "; status=" + appTask.getStatus() + "] execute finish.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e){
                log.warn("Task execute 10mins timeout!");
            }
            /*finally {
                //启动一次顺序关闭，执行以前提交的任务，但不接受新任务。如果已经关闭，则调用没有其他作用。
                executor.shutdown();
            }*/
        }

        log.info("Application is done!");
    }

    public void refreshAppTasks(){
        //Get tasks order by status
        appTasks = appTaskMap.getAppTasksByStatus(AppTaskStatus.PENDING.name());

        //if no pending tasks, resume running tasks
        if(appTasks.size() == 0 && executor.getActiveCount()==0){
            List<String> listStatus = new ArrayList(Arrays.asList(AppTaskStatus.PENDING.name(),
                    AppTaskStatus.RUNNING.name(),
                    AppTaskStatus.HOLDING.name(),
                    AppTaskStatus.WAITING.name()));

            appTasks = appTaskMap.getAppTasksInAndOrderByStatus(listStatus);
        }
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
