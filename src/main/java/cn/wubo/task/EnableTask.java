package cn.wubo.task;


import cn.wubo.task.config.TaskConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({TaskConfig.class})
public @interface EnableTask {

}
