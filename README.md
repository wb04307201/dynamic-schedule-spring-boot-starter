# dynamic-schedule-spring-boot-starter

[![](https://jitpack.io/v/com.gitee.wb04307201/dynamic-schedule-spring-boot-starter.svg)](https://jitpack.io/#com.gitee.wb04307201/dynamic-schedule-spring-boot-starter)
[![star](https://gitee.com/wb04307201/dynamic-schedule-spring-boot-starter/badge/star.svg?theme=dark)](https://gitee.com/wb04307201/dynamic-schedule-spring-boot-starter)
[![fork](https://gitee.com/wb04307201/dynamic-schedule-spring-boot-starter/badge/fork.svg?theme=dark)](https://gitee.com/wb04307201/dynamic-schedule-spring-boot-starter)
[![star](https://img.shields.io/github/stars/wb04307201/dynamic-schedule-spring-boot-starter)](https://github.com/wb04307201/dynamic-schedule-spring-boot-starter)
[![fork](https://img.shields.io/github/forks/wb04307201/dynamic-schedule-spring-boot-starter)](https://github.com/wb04307201/dynamic-schedule-spring-boot-starter)  
![MIT](https://img.shields.io/badge/License-Apache2.0-blue.svg) ![JDK](https://img.shields.io/badge/JDK-17+-green.svg) ![SpringBoot](https://img.shields.io/badge/Srping%20Boot-3+-green.svg)

> 更轻量化的任务调度功能，完全基于Spring Scheduling Tasks开发，  
> 可以动态添加、删除任务调度，  
> 可以将Bean转化为任务调度执行。

## 代码示例
1. 使用[动态调度](https://gitee.com/wb04307201/dynamic-schedule-spring-boot-starter)、[消息中间件](https://gitee.com/wb04307201/message-spring-boot-starter)、[动态编译工具](https://gitee.com/wb04307201/loader-util)、[实体SQL工具](https://gitee.com/wb04307201/sql-util)实现的[在线编码、动态调度、发送钉钉群消息、快速构造web页面Demo](https://gitee.com/wb04307201/dynamic-schedule-demo)

## 第一步 增加 JitPack 仓库
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

## 第二步 引入jar
1.1.0版本后升级到jdk17 SpringBoot3+
```xml
<dependency>
    <groupId>com.gitee.wb04307201</groupId>
    <artifactId>dynamic-schedule-spring-boot-starter</artifactId>
    <version>1.1.1</version>
</dependency>
```

## 第三步 在启动类上加上`@EnableDynamicSchedule`注解
```java
@EnableDynamicSchedule
@SpringBootApplication
public class FilePreviewDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(FilePreviewDemoApplication.class, args);
    }

}
```

### 第四步 注入service
```java
    @Autowired
    DynamicScheduledTaskService taskService;
```

### 动态增加删除任务
```java
taskService.add(任务名称, cron, Runable);
// 添加Cron表达式定时任务
taskService.add(任务名称, cron, () -> {// TODO 业务});
// 可以通过集成Runnable接口封装执行
taskService.add(任务名称, cron, new BeanMethodRunnable(bean名称, method名称, 方法入参));
// 添加固定延迟定时任务
taskService.addFixedDelayTask(任务名称, 延迟时间, 初始延迟时间, () -> {// TODO 业务});
// 添加固定频率定时任务
taskService.addFixedRateTask(任务名称, 定时时间间隔, 初始延迟时间, () -> {// TODO 业务});
taskService.cancel(任务名称);
```
