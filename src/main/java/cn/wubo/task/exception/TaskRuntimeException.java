package cn.wubo.task.exception;

public class TaskRuntimeException extends RuntimeException{

    public TaskRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
