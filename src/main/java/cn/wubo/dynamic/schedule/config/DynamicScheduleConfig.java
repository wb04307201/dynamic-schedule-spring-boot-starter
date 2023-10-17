package cn.wubo.dynamic.schedule.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({DynamicScheduleProperties.class})
public class DynamicScheduleConfig {
}
