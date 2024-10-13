package cn.wubo.dynamic.schedule.config;

import cn.wubo.dynamic.schedule.core.DynamicScheduledTaskRegistrar;
import cn.wubo.dynamic.schedule.core.DynamicScheduledTaskService;
import cn.wubo.dynamic.schedule.core.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Slf4j
@Configuration
@EnableConfigurationProperties({DynamicScheduleProperties.class})
public class DynamicScheduleConfig {

    /**
     * 配置动态调度线程池任务调度器
     *
     * @param properties 动态调度属性配置
     * @return 返回配置好的线程池任务调度器实例
     */
    @Bean(name = "dynamicScheduleThreadPoolTaskScheduler")
    public ThreadPoolTaskScheduler threadPoolTaskScheduler(DynamicScheduleProperties properties) {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(properties.getPoolSize()); // 设置线程池大小
        taskScheduler.setThreadNamePrefix(properties.getThreadNamePrefix()); // 设置线程名称前缀
        taskScheduler.setRemoveOnCancelPolicy(properties.getRemoveOnCancel()); // 设置取消策略
        taskScheduler.setErrorHandler(t -> log.error("Dynamic scheduled task throw an exception: {}", t.getMessage(), t)); // 设置错误处理器
        taskScheduler.initialize(); // 初始化调度器
        return taskScheduler;
    }

    /**
     * 配置动态定时任务注册器
     * 通过此方法定义一个名为"dynamicScheduledTaskRegistrar"的动态定时任务注册器 Bean
     * 该注册器将用于管理和注册可以动态修改的定时任务
     *
     * @param threadPoolTaskScheduler 注入的线程池任务调度器，用于执行定时任务
     *                                该参数是一个线程池任务调度器，通过Qualifier注解指定其名称为"dynamicScheduleThreadPoolTaskScheduler"
     * @return 返回一个使用指定线程池任务调度器实例化的 DynamicScheduledTaskRegistrar 对象
     */
    @Bean(name = "dynamicScheduledTaskRegistrar")
    public DynamicScheduledTaskRegistrar dynamicScheduledTaskRegistrar(@Qualifier(value = "dynamicScheduleThreadPoolTaskScheduler") ThreadPoolTaskScheduler threadPoolTaskScheduler) {
        return new DynamicScheduledTaskRegistrar(threadPoolTaskScheduler);
    }

    /**
     * 定义一个Bean并命名为"dynamicScheduledTaskService"
     * 该Bean负责处理动态调度任务的服务
     *
     * @param dynamicScheduledTaskRegistrar 注入一个名为"dynamicScheduledTaskRegistrar"的动态调度任务注册器
     * @return 返回一个使用动态调度任务注册器初始化的DynamicScheduledTaskService实例
     */
    @Bean(name = "dynamicScheduledTaskService")
    public DynamicScheduledTaskService dynamicScheduledTaskService(@Qualifier(value = "dynamicScheduledTaskRegistrar") DynamicScheduledTaskRegistrar dynamicScheduledTaskRegistrar) {
        return new DynamicScheduledTaskService(dynamicScheduledTaskRegistrar);
    }

    // 定义一个名为"dynamicScheduleSpringContextUtils"的Bean，用于管理Spring上下文实用工具类的实例
    @Bean(name = "dynamicScheduleSpringContextUtils")
    public SpringContextUtils springContextUtils() {
        // 创建并返回一个SpringContextUtils的实例，用于处理动态调度相关的Spring上下文操作
        return new SpringContextUtils();
    }
}
