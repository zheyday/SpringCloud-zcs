package zcs.seckill.utils;

import com.google.common.util.concurrent.RateLimiter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Aspect
@SuppressWarnings("UnstableApiUsage")
public class RateLimiterAop {
    private final RateLimiter rateLimiter = RateLimiter.create(1000);

    @Around("@annotation(zcs.seckill.utils.ServiceLimit)")
    public Object around(ProceedingJoinPoint joinPoint) {
        boolean flag = rateLimiter.tryAcquire(0, TimeUnit.MICROSECONDS);
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
