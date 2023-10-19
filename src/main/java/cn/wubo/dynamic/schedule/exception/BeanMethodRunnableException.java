package cn.wubo.dynamic.schedule.exception;

public class BeanMethodRunnableException extends RuntimeException {

    public BeanMethodRunnableException(String message) {
        super(message);
    }

    public BeanMethodRunnableException(String message, Throwable cause) {
        super(message, cause);
    }
}
