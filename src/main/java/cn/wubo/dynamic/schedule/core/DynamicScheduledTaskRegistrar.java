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
     * 添加定时任务
     *
     * @param taskName  任务名称
     * @param cron      执行计划
     * @param runnable  任务执行逻辑
     * @return  添加成功返回true，已存在相同任务名称返回false
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
     * 取消cron任务
     *
     * @param taskName 任务名称
     */
    public void cancelCronTask(String taskName) {
        // 获取指定任务对象
        ScheduledTask scheduledTask = scheduledTaskMap.get(taskName);
        if (null != scheduledTask) {
            // 取消任务
            scheduledTask.cancel();
            // 从任务映射中移除任务
            scheduledTaskMap.remove(taskName);
        }
        // 记录日志，显示任务删除成功
        log.info("定时任务[{}]删除成功", taskName);
    }


    /**
     * 重写父类的destroy方法，用于销毁对象
     */
    @Override
    public void destroy() {
        super.destroy();
        /**
         * 遍历scheduledTaskMap的所有值，并调用每个值的cancel方法
         */
        scheduledTaskMap.values().forEach(ScheduledTask::cancel);
    }

}
