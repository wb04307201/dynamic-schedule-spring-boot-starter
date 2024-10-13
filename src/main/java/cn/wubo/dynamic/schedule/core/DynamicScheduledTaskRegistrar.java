package cn.wubo.dynamic.schedule.core;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.*;
import org.springframework.util.Assert;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

public class DynamicScheduledTaskRegistrar extends ScheduledTaskRegistrar {

    private static final String ERROR_MSG = "定时任务[%s]已存在，添加失败";
    private final Map<String, ScheduledTask> scheduledTaskMap = new LinkedHashMap<>(16);

    public DynamicScheduledTaskRegistrar(ThreadPoolTaskScheduler taskScheduler) {
        super();
        this.setScheduler(taskScheduler); // 设置当前调度器
    }

    /**
     * 添加定时任务
     *
     * 本方法用于根据任务名称和cron表达式添加一个新的定时任务如果任务名称已存在，则抛出异常
     *
     * @param taskName 任务名称，用于唯一标识一个定时任务
     * @param cron     cron表达式，定义了任务的执行时间规则
     * @param runnable 需要定时执行的任务代码
     * @throws IllegalArgumentException 如果任务名称已存在
     */
    public void addCronTask(String taskName, String cron, Runnable runnable) {
        // 检查任务名称是否已存在，如果存在则抛出异常
        Assert.isTrue(!scheduledTaskMap.containsKey(taskName), String.format(ERROR_MSG, taskName));

        // 创建一个包含任务执行体和cron表达式的CronTask对象
        CronTask cronTask = new CronTask(runnable, cron);

        // 调度CronTask，返回一个ScheduledTask对象
        ScheduledTask scheduledTask = this.scheduleCronTask(cronTask);

        // 将任务名称和对应的ScheduledTask对象存入任务映射表中
        scheduledTaskMap.put(taskName, scheduledTask);
    }

    /**
     * 添加一个具有固定延迟的任务
     *
     * 该方法用于创建并调度一个具有固定延迟的任务任务将在指定的初始延迟后首次执行，
     * 并在每次执行完毕后，按照指定的间隔时间重复执行
     *
     * @param taskName 任务的名称，用于在任务地图中唯一标识任务
     * @param interval 任务执行之间的间隔时间（以秒为单位）
     * @param initialDelay 任务首次执行前的延迟时间（以秒为单位）
     * @param runnable 包含任务执行逻辑的可运行对象
     *
     * @throws IllegalArgumentException 如果指定的taskName已经存在于任务地图中，抛出此异常以避免重复任务
     */
    public void addFixedDelayTask(String taskName, Long interval, Long initialDelay, Runnable runnable) {
        // 确保任务地图中不存在相同的任务名称，以避免重复任务
        Assert.isTrue(!scheduledTaskMap.containsKey(taskName), String.format(ERROR_MSG, taskName));

        // 创建一个固定延迟任务对象，定义任务的执行逻辑、执行间隔和初始延迟
        FixedDelayTask fixedDelayTask = new FixedDelayTask(runnable, Duration.ofSeconds(interval), Duration.ofSeconds(initialDelay));

        // 调度固定延迟任务，获取任务的调度信息
        ScheduledTask scheduledTask = this.scheduleFixedDelayTask(fixedDelayTask);

        // 将任务名称和对应的调度任务对象存入任务地图中，以便后续管理和查找
        scheduledTaskMap.put(taskName, scheduledTask);
    }

    /**
     * 添加一个定时任务，以固定的时间间隔重复执行任务
     *
     * @param taskName 任务名称，用于唯一标识任务
     * @param interval 任务执行的时间间隔，单位为秒
     * @param initialDelay 任务首次执行的延迟时间，单位为秒
     * @param runnable 代表需要定时执行的任务
     *
     * 此方法首先会检查任务名称是否已存在于任务映射中，如果存在，则抛出异常
     * 然后，它创建一个FixedRateTask对象，该对象封装了任务执行的细节
     * 接着，调用scheduleFixedRateTask方法来安排任务的执行
     * 最后，将任务名称和安排好的任务对象存入映射中，以便后续管理和查询
     */
    public void addFixedRateTask(String taskName, Long interval, Long initialDelay, Runnable runnable) {
        // 确保任务名称的唯一性，如果已存在，则抛出异常
        Assert.isTrue(!scheduledTaskMap.containsKey(taskName), String.format(ERROR_MSG, taskName));

        // 创建FixedRateTask对象，定义任务的执行逻辑和时间间隔
        FixedRateTask fixedRateTask = new FixedRateTask(runnable, Duration.ofSeconds(interval), Duration.ofSeconds(initialDelay));

        // 安排任务的定时执行，并获取任务对象
        ScheduledTask scheduledTask = this.scheduleFixedRateTask(fixedRateTask);

        // 将任务名称和任务对象存入映射，以便后续管理和查询
        scheduledTaskMap.put(taskName, scheduledTask);
    }

    /**
     * 取消指定名称的定时任务
     *
     * @param taskName 定时任务的名称
     */
    public void cancelCronTask(String taskName) {
        // 获取指定任务对象
        ScheduledTask scheduledTask = scheduledTaskMap.get(taskName);
        if (null != scheduledTask) {
            // 如果任务存在，则取消任务
            scheduledTask.cancel();
            // 从任务映射中移除任务对象
            scheduledTaskMap.remove(taskName);
        }
    }

    /**
     * 销毁方法，用于释放资源和取消所有计划中的任务
     * 在子类中覆盖此方法时，应首先调用super.destroy()以确保基类的资源得到正确释放
     * 此方法专注于资源清理，特别是通过遍历并取消所有已计划的任务来避免资源泄露
     */
    @Override
    public void destroy() {
        // 调用基类的销毁方法进行必要的资源释放
        super.destroy();
        // 遍历计划任务映射，并调用每个任务的cancel方法进行取消，以避免资源泄露
        scheduledTaskMap.values().forEach(ScheduledTask::cancel);
    }
}
