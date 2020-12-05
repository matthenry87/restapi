package com.matthenry87.restapi.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import static com.matthenry87.logging.LoggingConstants.CORRELATION_ID_MDC_KEY;

@Slf4j
@Aspect
@Component
@SuppressWarnings("java:S1186")
public class ControllerLoggingAdvice {

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controllers() {}

    @Pointcut("execution(* *(..))")
    public void methods() {}

    @Around("controllers() && methods()")
    public Object log(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        var stopWatch = new StopWatch();
        stopWatch.start();

        var returnObject = proceedingJoinPoint.proceed();

        stopWatch.stop();

        var methodName = proceedingJoinPoint.getSignature().getName();
        var correlationId = MDC.get(CORRELATION_ID_MDC_KEY);
        var duration = stopWatch.getLastTaskTimeMillis();

        log.info("Runtime for " + methodName + " for ID: " + correlationId + " : " + duration);

        return returnObject;
    }

}
