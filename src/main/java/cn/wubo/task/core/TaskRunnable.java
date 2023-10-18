package cn.wubo.task.core;

import cn.wubo.task.exception.TaskRuntimeException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskRunnable implements Runnable {

    private final String beanName;

    private final String methodName;

    private final List<Object> methodParams;

    public TaskRunnable(String beanName, String methodName) {
        this(beanName, methodName, new ArrayList<>());
    }

    public TaskRunnable(String beanName, String methodName, List<Object> methodParams) {
        this.beanName = beanName;
        this.methodName = methodName;
        this.methodParams = methodParams;
    }

    @Override
    public void run() {
        Object target = SpringContextUtils.getBean(beanName);
        try {
            Method method = null;
            if (methodParams.isEmpty()) {
                method = target.getClass().getMethod(methodName);
                method.invoke(target);
            } else {
                method = target.getClass().getMethod(methodName, methodParams.stream().map(Object::getClass).collect(Collectors.toList()).toArray(new Class<?>[0]));
                method.invoke(target, methodParams);
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new TaskRuntimeException(e.getMessage(), e);
        }
    }
}
