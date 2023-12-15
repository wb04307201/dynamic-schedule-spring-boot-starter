package cn.wubo.dynamic.schedule.core;

import cn.wubo.dynamic.schedule.config.DynamicScheduleProperties;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DynamicScheduledTaskService {

    private static final String DEBUG_MSG = "定时任务添加结果：{}";

    private final DynamicScheduledTaskRegistrar dynamicScheduledTaskRegistrar;

    public DynamicScheduledTaskService(DynamicScheduleProperties dynamicScheduleProperties) {
        this.dynamicScheduledTaskRegistrar = new DynamicScheduledTaskRegistrar(dynamicScheduleProperties.getPoolSize(), dynamicScheduleProperties.getThreadNamePrefix(), dynamicScheduleProperties.getRemoveOnCancel());
    }


    /**
     * 添加Cron表达式定时任务
     *
     * @param taskName 任务名称
     * @param cron     定时表达式
     * @param runnable 定时任务的实现
     */
    public void add(String taskName, String cron, Runnable runnable) {
        Boolean result = dynamicScheduledTaskRegistrar.addCronTask(taskName, cron, runnable);
        log.debug(DEBUG_MSG, result);
    }


    /**
     * 添加固定延迟定时任务
     *
     * @param taskName     任务名称
     * @param interval     延迟时间
     * @param initialDelay 初始延迟时间
     * @param runnable     定时任务的实现
     */
    public void addFixedDelayTask(String taskName, Long interval, Long initialDelay, Runnable runnable) {
        Boolean result = dynamicScheduledTaskRegistrar.addFixedDelayTask(taskName, interval, initialDelay, runnable);
        log.debug(DEBUG_MSG, result);
    }


    /**
     * 添加固定频率定时任务
     *
     * @param taskName     任务名称
     * @param interval     定时时间间隔
     * @param initialDelay 初始延迟时间
     * @param runnable     定时任务的实现
     */
    public void addFixedRateTask(String taskName, Long interval, Long initialDelay, Runnable runnable) {
        Boolean result = dynamicScheduledTaskRegistrar.addFixedRateTask(taskName, interval, initialDelay, runnable);
        log.debug(DEBUG_MSG, result);
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
