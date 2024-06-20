package cn.wubo.dynamic.schedule.config;

import cn.wubo.dynamic.schedule.core.DynamicScheduledTaskService;
import cn.wubo.dynamic.schedule.core.SpringContextUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({DynamicScheduleProperties.class})
public class DynamicScheduleConfig {

    DynamicScheduleProperties dynamicScheduleProperties;

    public DynamicScheduleConfig(DynamicScheduleProperties dynamicScheduleProperties) {
        this.dynamicScheduleProperties = dynamicScheduleProperties;
    }

    /**
     * 创建一个DynamicScheduledTaskService对象，并将其作为Bean返回
     *
     * @param dynamicScheduleProperties 动态调度属性对象
     * @return DynamicScheduledTaskService对象
     */
    @Bean
    public DynamicScheduledTaskService dynamicScheduledTaskService(DynamicScheduleProperties dynamicScheduleProperties) {
        return new DynamicScheduledTaskService(dynamicScheduleProperties);
    }

    @Bean(name = "dynamicScheduleSpringContextUtils")
    public SpringContextUtils springContextUtils() {
        return new SpringContextUtils();
    }
}
