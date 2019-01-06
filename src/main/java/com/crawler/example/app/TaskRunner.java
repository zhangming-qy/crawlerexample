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
    //private final static ConcurrentHashMap<String, Future<AppTask>> futureHashMap = new ConcurrentHashMap<>(32);

    @Autowired
    private AppTaskMap appTaskMap;
/*
    @Autowired
    private List<ITaskRunner> tasks;

    public final static int CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors();

    private static ThreadPoolExecutor executor  = new ThreadPoolExecutor(CORE_POOL_SIZE, CORE_POOL_SIZE*2, 10l, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(1000));

    private static ScheduledThreadPoolExecutor scheduledExecutor = new ScheduledThreadPoolExecutor(CORE_POOL_SIZE);
*/
    //@Scheduled(fixedDelay=120000) //delay 2mins
    @Async
    @Override
    public void run(String[] args) {
        log.info("Application is running...................");

        //add refresh and monitor running status task
        //scheduledExecutor.setMaximumPoolSize(2* CORE_POOL_SIZE);
        //scheduledExecutor.scheduleWithFixedDelay(this, 0, 30, TimeUnit.SECONDS);
        //scheduledExecutor.scheduleWithFixedDelay(new TaskRunnerMon(), 30,30, TimeUnit.SECONDS);
        TaskScheduler.setMaximumPoolSize(TaskScheduler.CORE_POOL_SIZE*2);
        TaskScheduler.scheduleWithFixedDelay(this, 0,30, TimeUnit.SECONDS);
        TaskScheduler.scheduleWithFixedDelay(new TaskRunnerMon(), 30,30, TimeUnit.SECONDS);

        //refreshAppTasks();
        //submitAppTasks();
        //checkSubmittedAppTasks();

        log.info("Application is done!");
    }

    @Override
    public void run() {
        refreshAppTasks();
        submitAppTasks();
    }

    public void refreshAppTasks(){
        //Get tasks order by status
        appTasks = appTaskMap.getAppTasksByStatus(AppTaskStatus.PENDING.name());

        //if no pending tasks, resume running tasks
        if(appTasks.size() == 0 && TaskScheduler.getActiveCount()==0){
            List<String> listStatus = new ArrayList(Arrays.asList(AppTaskStatus.PENDING.name(),
                    AppTaskStatus.RUNNING.name(),
                    AppTaskStatus.HOLDING.name(),
                    AppTaskStatus.WAITING.name()));

            appTasks = appTaskMap.getAppTasksInAndOrderByStatus(listStatus);
        }
    }

    public void submitAppTasks(){
       // List<Future<AppTask>> resultList = new ArrayList<>();

        for(AppTask appTask : appTasks){

            TaskScheduler.submitAppTasks(appTask);

            /*
            try {
                Class clazz = Class.forName(appTask.getJclass());
                ITaskRunner task = (ITaskRunner)ApplicationContextProvider.getBean(clazz);

                if(task==null)continue;

                //if does not support concurrent class, one class one thread, if exists, then skip.
                String futureKey = task.isSupportConcurrent() ? String.valueOf(appTask.getId()) : appTask.getJclass();

                if(futureHashMap.containsKey(futureKey)) continue;

                task.setTask(appTask);
                Future<AppTask> appTaskFuture = scheduledExecutor.submit(task);
                futureHashMap.put(futureKey,appTaskFuture);
                //resultList.add(scheduledExecutor.submit(task));
            }
            catch (ClassNotFoundException ex){
                log.error("Can't found class " + appTask.getJclass());
            }
            catch (Exception ex){
                log.error(ex.getLocalizedMessage());
                ex.printStackTrace();
            }*/
        }

    }

    /*
    public static void checkSubmittedAppTasks(){
        //遍历任务的结果
        for (Map.Entry<String,Future<AppTask>> fsEntry : futureHashMap.entrySet()) {
            try {
                Future<AppTask> fs = fsEntry.getValue();

                //打印各个线任务执行的结果，调用future.get() 阻塞主线程，获取异步任务的返回结果
                //AppTask appTask = fs.get(10, TimeUnit.MINUTES);
                if(fs.isDone()){
                    AppTask appTask = fs.get();
                    futureHashMap.remove(fsEntry.getKey());
                    log.info("Task [id={}, root_url={}, group_name={}, java_class={}, status={}] execute finish.",
                            appTask.getId(),
                            appTask.getRoot_url(),
                            appTask.getGroup_name(),
                            appTask.getJclass(),
                            appTask.getStatus());
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
                /*catch (TimeoutException e){
                    log.warn("Task execute 10mins timeout!");
                }*/
            /*
            finally {
                //启动一次顺序关闭，执行以前提交的任务，但不接受新任务。如果已经关闭，则调用没有其他作用。
                //executor.shutdown();
            }
        }
    }*/
}
