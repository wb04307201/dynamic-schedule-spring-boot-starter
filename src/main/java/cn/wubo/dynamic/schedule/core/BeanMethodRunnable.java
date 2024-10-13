package cn.wubo.dynamic.schedule.core;

import cn.wubo.dynamic.schedule.exception.BeanMethodRunnableException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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
        // 通过bean名称获取Spring上下文中的bean实例
        Object target = SpringContextUtils.getBean(beanName);
        try {
            // 定义方法对象
            Method method;
            // 如果方法参数为空，则调用无参数的方法
            if (methodParams.isEmpty()) {
                // 获取无参数的方法
                method = target.getClass().getMethod(methodName);
                // 调用无参数的方法
                method.invoke(target);
            } else {
                // 如果有方法参数，则调用有参数的方法
                // 获取有参数的方法，参数类型由methodParams流转换而来
                method = target.getClass().getMethod(methodName, methodParams.stream().map(Object::getClass).toArray(Class<?>[]::new));
                // 调用有参数的方法，传入参数
                method.invoke(target, methodParams);
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            // 如果发生异常，抛出自定义的BeanMethodRunnableException
            throw new BeanMethodRunnableException(e.getMessage(), e);
        }
    }
}
