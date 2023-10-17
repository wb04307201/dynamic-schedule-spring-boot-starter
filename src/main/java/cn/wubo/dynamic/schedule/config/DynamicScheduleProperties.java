package cn.wubo.dynamic.schedule.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "dynamic.schedule")
public class DynamicScheduleProperties {
}
