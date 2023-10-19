package cn.wubo.dynamic.schedule.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class DynamicScheduledTaskRegistrar extends ScheduledTaskRegistrar {

    private final Map<String, ScheduledTask> scheduledTaskMap = new LinkedHashMap<>(16);

    public DynamicScheduledTaskRegistrar(Integer poolSize, String threadNamePrefix, Boolean removeOnCancel) {
        super();
        // 两种实现方案
        // ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);
        //TaskScheduler taskScheduler = new ConcurrentTaskScheduler(scheduledExecutorService);
        // 第二种实现方案
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(poolSize);
        taskScheduler.setThreadNamePrefix(threadNamePrefix);
        taskScheduler.setRemoveOnCancelPolicy(removeOnCancel);
        taskScheduler.initialize();
        this.setScheduler(taskScheduler);
    }


    /**
     * 新增任务
     *
     * @param taskName
     * @param cron
     * @param runnable
     */
    public Boolean addCronTask(String taskName, String cron, Runnable runnable) {
        if (scheduledTaskMap.containsKey(taskName)) {
            log.error("定时任务[{}}]已存在，添加失败", taskName);
            return Boolean.FALSE;
        }
        CronTask cronTask = new CronTask(runnable, cron);
        ScheduledTask scheduledTask = this.scheduleCronTask(cronTask);
        scheduledTaskMap.put(taskName, scheduledTask);
        log.info("定时任务[{}]新增成功", taskName);
        return Boolean.TRUE;
    }

    /**
     * 删除任务
     *
     * @param taskName
     */
    public void cancelCronTask(String taskName) {
        ScheduledTask scheduledTask = scheduledTaskMap.get(taskName);
        if (null != scheduledTask) {
            scheduledTask.cancel();
            scheduledTaskMap.remove(taskName);
        }
        log.info("定时任务[{}}]删除成功", taskName);
    }

    @Override
    public void destroy() {
        super.destroy();
        scheduledTaskMap.values().forEach(ScheduledTask::cancel);
    }
}
