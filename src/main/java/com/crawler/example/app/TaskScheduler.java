package com.crawler.example.app;

import com.crawler.example.entity.AppTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.*;

public class TaskScheduler {
    private final static Logger log = LoggerFactory.getLogger(TaskScheduler.class);
    private final static ConcurrentHashMap<String, Future<AppTask>> futureHashMap = new ConcurrentHashMap<>(32);

    public final static int CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors();

    private static ScheduledThreadPoolExecutor scheduledExecutor = new ScheduledThreadPoolExecutor(CORE_POOL_SIZE);

    public static int getActiveCount(){
        return scheduledExecutor.getActiveCount();
    }

    public static void setMaximumPoolSize(int maximumPoolSize){
        scheduledExecutor.setMaximumPoolSize(maximumPoolSize);
    }

    public static ScheduledFuture<?> scheduleWithFixedDelay(Runnable command,
                                                     long initialDelay,
                                                     long delay,
                                                     TimeUnit unit){
        return scheduledExecutor.scheduleWithFixedDelay(command, initialDelay, delay, unit);
    }

    public static ScheduledFuture<?> schedule(Runnable command){
        return scheduledExecutor.schedule(command, 0, TimeUnit.NANOSECONDS);
    }

    public static Future<AppTask> submitAppTasks(AppTask appTask){
        try {
            Class clazz = Class.forName(appTask.getJclass());
            ITaskRunner task = (ITaskRunner)ApplicationContextProvider.getBean(clazz);

            if(task==null)
                throw new ClassNotFoundException(clazz.getName());

            //if does not support concurrent class, one class one thread, if exists, then skip.
            String futureKey = task.isSupportConcurrent() ? String.valueOf(appTask.getId()) : appTask.getJclass();

            if(futureHashMap.containsKey(futureKey))
                return futureHashMap.get(futureKey);

            task.setTask(appTask);
            Future<AppTask> appTaskFuture = scheduledExecutor.submit(task);
            return futureHashMap.put(futureKey,appTaskFuture);
        }
        catch (ClassNotFoundException ex){
            log.error("Can't found class {}.", appTask.getJclass());
        }

        return null;
    }

    public static void removeFinishedAppTasks(){
        //遍历任务的结果
        for (Map.Entry<String,Future<AppTask>> fsEntry : futureHashMap.entrySet()) {
            try {
                Future<AppTask> fs = fsEntry.getValue();
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
        }
    }
}
