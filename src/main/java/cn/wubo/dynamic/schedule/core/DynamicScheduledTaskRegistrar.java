package cn.wubo.dynamic.schedule.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.*;
import org.springframework.util.Assert;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class DynamicScheduledTaskRegistrar extends ScheduledTaskRegistrar {

    private static final String ERROR_MSG = "定时任务[%s]已存在，添加失败";
    private final Map<String, ScheduledTask> scheduledTaskMap = new LinkedHashMap<>(16);

    public DynamicScheduledTaskRegistrar(Integer poolSize, String threadNamePrefix, Boolean removeOnCancel) {
        super();
        // 两种实现方案
        // ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);
        // TaskScheduler taskScheduler = new ConcurrentTaskScheduler(scheduledExecutorService);
        // 第二种实现方案
        // 创建并配置线程池任务调度器
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(poolSize); // 设置线程池大小
        taskScheduler.setThreadNamePrefix(threadNamePrefix); // 设置线程名称前缀
        taskScheduler.setRemoveOnCancelPolicy(removeOnCancel); // 设置取消策略
        taskScheduler.setErrorHandler(t -> log.error("Dynamic scheduled task throw an exception: {}", t.getMessage(), t)); // 设置错误处理器
        taskScheduler.initialize(); // 初始化调度器
        this.setScheduler(taskScheduler); // 设置当前调度器
    }

    /**
     * 添加定时任务
     *
     * @param taskName 任务名称，用作唯一标识
     * @param cron     定时规则，遵循cron表达式
     * @param runnable 需要定时执行的Runnable任务
     * @return 总是返回Boolean.TRUE，表示任务添加成功
     * @throws IllegalArgumentException 如果任务已存在，将抛出异常
     */
    public void addCronTask(String taskName, String cron, Runnable runnable) {
        // 确保任务名称的唯一性，如果已存在则抛出异常
        Assert.isTrue(scheduledTaskMap.containsKey(taskName), String.format(ERROR_MSG, taskName));

        // 创建CronTask对象，封装了Runnable任务和cron定时规则
        CronTask cronTask = new CronTask(runnable, cron);

        // 调度中心安排 cron 任务
        ScheduledTask scheduledTask = this.scheduleCronTask(cronTask);

        // 将新创建的定时任务存储到任务映射表中，以便后续管理和查询
        scheduledTaskMap.put(taskName, scheduledTask);
    }

    /**
     * 添加一个具有固定延迟的任务
     * <p>
     * 该方法用于创建一个在给定初始延迟后首次执行，然后在每次执行后都等待固定间隔时间的任务
     * 它检查任务名称是否已存在于任务映射中，如果不存在，则创建一个新的定时任务并将其添加到映射中
     *
     * @param taskName     任务名称，用于在任务映射中唯一标识任务
     * @param interval     任务执行之间的固定间隔时间（秒）
     * @param initialDelay 任务首次执行前的延迟时间（秒）
     * @param runnable     包含任务具体执行逻辑的Runnable对象
     * @return 总是返回Boolean.TRUE，表示任务已经成功添加
     * @throws IllegalArgumentException 如果任务名称不存在于scheduledTaskMap中，则抛出异常
     */
    public void addFixedDelayTask(String taskName, Long interval, Long initialDelay, Runnable runnable) {
        // 确认任务名称已存在于任务映射中，如果不存在则抛出异常
        Assert.isTrue(scheduledTaskMap.containsKey(taskName), String.format(ERROR_MSG, taskName));

        // 创建一个固定延迟任务对象，设置任务的执行间隔和初始延迟
        FixedDelayTask fixedDelayTask = new FixedDelayTask(runnable, Duration.ofSeconds(interval), Duration.ofSeconds(initialDelay));

        // 调度创建的固定延迟任务，获取ScheduledTask对象
        ScheduledTask scheduledTask = this.scheduleFixedDelayTask(fixedDelayTask);

        // 将任务名称和对应的ScheduledTask对象存入映射中，以便后续管理和查找
        scheduledTaskMap.put(taskName, scheduledTask);
    }

    /**
     * 添加一个定时任务
     *
     * @param taskName     定时任务的名称
     * @param interval     任务执行的时间间隔（秒）
     * @param initialDelay 任务首次执行的延迟时间（秒）
     * @param runnable     任务的执行逻辑
     * @return 总是返回true，表示任务添加成功
     * <p>
     * 此方法用于向系统中添加一个定时任务，该任务将以固定的时间间隔重复执行
     * 任务的首次执行会有一个延迟时间，之后将严格按照指定的时间间隔执行
     * 如果尝试添加一个已存在的任务，将会抛出异常，确保任务的唯一性
     */
    public void addFixedRateTask(String taskName, Long interval, Long initialDelay, Runnable runnable) {
        // 确保任务的唯一性，如果任务已存在则抛出异常
        Assert.isTrue(scheduledTaskMap.containsKey(taskName), String.format(ERROR_MSG, taskName));

        // 创建一个定时任务对象，包含任务的执行逻辑、时间间隔和首次执行的延迟时间
        FixedRateTask fixedRateTask = new FixedRateTask(runnable, Duration.ofSeconds(interval), Duration.ofSeconds(initialDelay));

        // 调度定时任务，获取ScheduledTask对象
        ScheduledTask scheduledTask = this.scheduleFixedRateTask(fixedRateTask);

        // 将任务名称和对应的ScheduledTask对象存入映射，以便后续管理和查询
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
