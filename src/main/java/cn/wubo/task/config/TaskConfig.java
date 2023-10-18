package cn.wubo.task.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({TaskProperties.class})
public class TaskConfig {
}
