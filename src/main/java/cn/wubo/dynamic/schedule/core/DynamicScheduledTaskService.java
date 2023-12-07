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
     * 添加定时任务
     *
     * @param taskName 任务名称
     * @param cron 表达式
     * @param runnable 任务执行逻辑
     */
    public void add(String taskName, String cron, Runnable runnable) {
        Boolean result = dynamicScheduledTaskRegistrar.addCronTask(taskName, cron, runnable);
        log.debug("定时任务添加结果：" + result);
    }


    /**
     * 取消指定任务名称的任务。
     *
     * @param taskName 要取消的任务名称
     */
    public void cancel(String taskName) {
        dynamicScheduledTaskRegistrar.cancelCronTask(taskName);
    }

}
