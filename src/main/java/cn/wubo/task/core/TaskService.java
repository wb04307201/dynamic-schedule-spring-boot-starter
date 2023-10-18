package cn.wubo.task.core;

import cn.wubo.task.config.TaskProperties;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTask;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TaskService implements DisposableBean {

    private final Map<Runnable, ScheduledTask> scheduledTaskMap = new ConcurrentHashMap<>(16);
    private final Map<Integer, JobInfo> jobInfoMap = new HashMap<>();
    private final TaskScheduler taskScheduler;
    private final TaskProperties taskProperties;

    public TaskService(TaskProperties taskProperties) {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        // 定时任务执行线程池核心线程数
        taskScheduler.setPoolSize(taskProperties.getPoolSize());
        taskScheduler.setRemoveOnCancelPolicy(taskProperties.getRemoveOnCancel());
        taskScheduler.setThreadNamePrefix(taskProperties.getThreadNamePrefix());
        this.taskScheduler = taskScheduler;
        this.taskProperties = taskProperties;
    }

    @Override
    public void destroy() throws Exception {

    }
}
