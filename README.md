# dynamic-schedule-spring-boot-starter

[![](https://jitpack.io/v/com.gitee.wb04307201/dynamic-schedule-spring-boot-starter.svg)](https://jitpack.io/#com.gitee.wb04307201/dynamic-schedule-spring-boot-starter)

> 动态添加、删除调度任务  
> 注入DynamicScheduledTaskService后通过add和cancel添加、删除调度任务  
> 可使用BeanMethodRunnable将Bean中的方法变成调度

### 添加注解
```java
@EnableDynamicSchedule
```

### 注入service
```java
    @Autowired
    DynamicScheduledTaskService taskService;
```

### 动态增加删除任务
```java
taskService.add(任务名称, cron, Runable);
taskService.add(任务名称, cron, () -> {// TODO 业务});
taskService.add(任务名称, cron, new BeanMethodRunnable(bean名称, method名称，方法入参));
taskService.cancel(任务名称);
```
