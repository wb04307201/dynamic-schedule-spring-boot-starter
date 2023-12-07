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
        // 获取目标对象
        Object target = SpringContextUtils.getBean(beanName);
        try {
            Method method;
            if (methodParams.isEmpty()) {
                // 获取目标对象的无参方法
                method = target.getClass().getMethod(methodName);
                // 调用目标对象的无参方法
                method.invoke(target);
            } else {
                // 获取目标对象的带参数方法
                method = target.getClass().getMethod(methodName, methodParams.stream().map(Object::getClass).collect(Collectors.toList()).toArray(new Class<?>[0]));
                // 调用目标对象的带参方法，并传入参数
                method.invoke(target, methodParams);
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            // 抛出自定义异常，包含错误信息
            throw new BeanMethodRunnableException(e.getMessage(), e);
        }
    }

}
