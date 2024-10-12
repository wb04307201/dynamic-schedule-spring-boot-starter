package cn.wubo.dynamic.schedule.config;

import cn.wubo.dynamic.schedule.core.DynamicScheduledTaskService;
import cn.wubo.dynamic.schedule.core.SpringContextUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({DynamicScheduleProperties.class})
public class DynamicScheduleConfig {

    /**
     * 定义一个动态定时任务服务
     * 该服务负责根据配置属性初始化和管理动态定时任务
     *
     * @param dynamicScheduleProperties 动态定时任务的配置属性，包含定时任务的调度策略等信息
     * @return 返回初始化后的动态定时任务服务实例
     */
    @Bean
    public DynamicScheduledTaskService dynamicScheduledTaskService(DynamicScheduleProperties dynamicScheduleProperties) {
        return new DynamicScheduledTaskService(dynamicScheduleProperties);
    }

    @Bean(name = "dynamicScheduleSpringContextUtils")
    public SpringContextUtils springContextUtils() {
        // 创建并返回一个SpringContextUtils的实例，用于处理动态调度相关的Spring上下文操作
        return new SpringContextUtils();
    }
}
