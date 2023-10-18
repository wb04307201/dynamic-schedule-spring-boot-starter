package cn.wubo.task.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "task")
public class TaskProperties {
    private Integer poolSize = 16;
    private String threadNamePrefix = "TaskSchedulerThreadPool-";
    private Boolean removeOnCancel = Boolean.TRUE;
}
