package cn.wubo.dynamic.schedule.core;

import cn.wubo.dynamic.schedule.config.DynamicScheduleProperties;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DynamicScheduledTaskService {

    private final DynamicScheduledTaskRegistrar dynamicScheduledTaskRegistrar;

    public DynamicScheduledTaskService(DynamicScheduleProperties dynamicScheduleProperties) {
        this.dynamicScheduledTaskRegistrar = new DynamicScheduledTaskRegistrar(dynamicScheduleProperties.getPoolSize(), dynamicScheduleProperties.getThreadNamePrefix(), dynamicScheduleProperties.getRemoveOnCancel());
    }

    /**
     * 新增任务
     *
     * @param taskName
     * @param cron
     */
    public void add(String taskName, String cron, Runnable runnable) {
        Boolean result = dynamicScheduledTaskRegistrar.addCronTask(taskName, cron, runnable);
        log.info("定时任务添加结果：" + result);
    }

    /**
     * 取消任务
     *
     * @param taskName
     */
    public void cancel(String taskName) {
        dynamicScheduledTaskRegistrar.cancelCronTask(taskName);
    }
}
