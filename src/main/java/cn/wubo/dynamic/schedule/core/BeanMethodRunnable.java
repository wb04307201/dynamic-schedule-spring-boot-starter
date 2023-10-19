package cn.wubo.dynamic.schedule.core;

import cn.wubo.dynamic.schedule.exception.BeanMethodRunnableException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BeanMethodRunnable implements Runnable {

    private final String beanName;

    private final String methodName;

    private final List<Object> methodParams;

    public BeanMethodRunnable(String beanName, String methodName) {
        this(beanName, methodName, new ArrayList<>());
    }

    public BeanMethodRunnable(String beanName, String methodName, List<Object> methodParams) {
        this.beanName = beanName;
        this.methodName = methodName;
        this.methodParams = methodParams;
    }

    @Override
    public void run() {
        Object target = SpringContextUtils.getBean(beanName);
        try {
            Method method;
            if (methodParams.isEmpty()) {
                method = target.getClass().getMethod(methodName);
                method.invoke(target);
            } else {
                method = target.getClass().getMethod(methodName, methodParams.stream().map(Object::getClass).collect(Collectors.toList()).toArray(new Class<?>[0]));
                method.invoke(target, methodParams);
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new BeanMethodRunnableException(e.getMessage(), e);
        }
    }
}
