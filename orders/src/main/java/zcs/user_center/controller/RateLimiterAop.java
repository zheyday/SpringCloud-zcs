package zcs.user_center.controller;

import com.google.common.util.concurrent.RateLimiter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
@SuppressWarnings("UnstableApiUsage")
public class RateLimiterAop {
    private final RateLimiter rateLimiter = RateLimiter.create(1);

    @Around("@annotation(zcs.user_center.controller.ServiceLimit)")
    public Object around(ProceedingJoinPoint joinPoint) {
        boolean flag = rateLimiter.tryAcquire();
        Object obj = null;
        try {
            if (flag)
                obj = joinPoint.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return obj;
    }
}
