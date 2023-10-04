package top.bujiaban.mqsub.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.lang.reflect.Method;

@Slf4j
public class CustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        if (ex instanceof ObjectOptimisticLockingFailureException) {
            log.info(ex.getMessage());
        } else {
            log.error(ex.getMessage(), ex);
        }
    }
}
