package cn.wubo.dynamic.schedule;


import cn.wubo.dynamic.schedule.core.DynamicScheduleConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({DynamicScheduleConfig.class})
public @interface EnableDynamicSchedule {

}
