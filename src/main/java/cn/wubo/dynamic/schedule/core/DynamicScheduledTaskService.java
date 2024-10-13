package cn.wubo.dynamic.schedule.core;

public class DynamicScheduledTaskService {

    private final DynamicScheduledTaskRegistrar dynamicScheduledTaskRegistrar;

    public DynamicScheduledTaskService(DynamicScheduledTaskRegistrar dynamicScheduledTaskRegistrar) {
        this.dynamicScheduledTaskRegistrar = dynamicScheduledTaskRegistrar;
    }

    /**
     * 添加Cron表达式定时任务
     *
     * @param taskName 任务名称
     * @param cron     定时表达式
     * @param runnable 定时任务的实现
     */
    public void add(String taskName, String cron, Runnable runnable) {
        dynamicScheduledTaskRegistrar.addCronTask(taskName, cron, runnable);
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
        dynamicScheduledTaskRegistrar.addFixedDelayTask(taskName, interval, initialDelay, runnable);
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
        dynamicScheduledTaskRegistrar.addFixedRateTask(taskName, interval, initialDelay, runnable);
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
