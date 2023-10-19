package cn.wubo.dynamic.schedule.config;

import cn.wubo.dynamic.schedule.core.DynamicScheduledTaskService;
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

    @Bean
    public DynamicScheduledTaskService dynamicScheduledTaskService(DynamicScheduleProperties dynamicScheduleProperties) {
        return new DynamicScheduledTaskService(dynamicScheduleProperties);
    }
}
