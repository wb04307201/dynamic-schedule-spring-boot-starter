package cn.wubo.task.core;

import cn.wubo.task.config.TaskProperties;
import cn.wubo.task.exception.TaskRuntimeException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

public class TaskService implements DisposableBean {

    private final Map<Runnable, ScheduledTask> scheduledTaskMap = new ConcurrentHashMap<>(16);
    private final Map<String, JobInfo> jobInfoMap = new HashMap<>();
    private final TaskScheduler taskScheduler;
    private final TaskProperties taskProperties;

    public TaskService(TaskProperties taskProperties) {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        // 定时任务执行线程池核心线程数
        threadPoolTaskScheduler.setPoolSize(taskProperties.getPoolSize());
        threadPoolTaskScheduler.setRemoveOnCancelPolicy(taskProperties.getRemoveOnCancel());
        threadPoolTaskScheduler.setThreadNamePrefix(taskProperties.getThreadNamePrefix());
        this.taskScheduler = threadPoolTaskScheduler;
        this.taskProperties = taskProperties;
    }

    public String add(JobInfo jobInfo) {
        if (!StringUtils.hasText(jobInfo.getJobId())) jobInfo.setJobId(UUID.randomUUID().toString());
        TaskRunnable task = new TaskRunnable(jobInfo.getBeanName(), jobInfo.getMethodName(), jobInfo.getMethodParams());
        CronTask cronTask = new CronTask(task, jobInfo.getCronExpression());
        // 如果当前包含这个任务，则移除
        if (this.scheduledTaskMap.containsKey(task)) {
            remove(jobInfo);
        }
        jobInfoMap.put(jobInfo.getJobId(), jobInfo);
        ScheduledTaskRegistrar scheduledTaskRegistrar = new ScheduledTaskRegistrar();
        ScheduledTask scheduledTask = scheduledTaskRegistrar.scheduleCronTask(cronTask);
        taskScheduler.schedule(cronTask.getRunnable(), cronTask.getTrigger());
        scheduledTaskMap.put(task, scheduledTask);
        return jobInfo.getJobId();
    }

    public void remove(String jobId) {
        if(!jobInfoMap.containsKey(jobId)) throw new TaskRuntimeException("");
        JobInfo jobInfo = jobInfoMap.get(jobId);
        TaskRunnable task = new TaskRunnable(jobInfo.getBeanName(), jobInfo.getMethodName(), jobInfo.getMethodParams());
        ScheduledTask scheduledTask = scheduledTaskMap.remove(task);
        if (scheduledTask != null) scheduledTask.cancel();
    }

    public void stop(String jobId) {
        if(!jobInfoMap.containsKey(jobId)) throw new TaskRuntimeException("");
        JobInfo jobInfo = jobInfoMap.get(jobId);
        TaskRunnable task = new TaskRunnable(jobInfo.getBeanName(), jobInfo.getMethodName(), jobInfo.getMethodParams());
        ScheduledTask scheduledTask = scheduledTaskMap.remove(task);
        if (scheduledTask != null) scheduledTask.cancel();
    }

    public void start(String jobId) {
        if(!jobInfoMap.containsKey(jobId)) throw new TaskRuntimeException("");
        JobInfo jobInfo = jobInfoMap.get(jobId);
        TaskRunnable task = new TaskRunnable(jobInfo.getBeanName(), jobInfo.getMethodName(), jobInfo.getMethodParams());
        ScheduledTask scheduledTask = scheduledTaskMap.remove(task);
        if (scheduledTask != null) scheduledTask.cancel();
    }

    @Override
    public void destroy() throws Exception {
        for (ScheduledTask task : scheduledTaskMap.values()) {
            task.cancel();
        }
        jobInfoMap.clear();
    }
}
