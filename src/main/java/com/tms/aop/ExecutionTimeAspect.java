package com.tms.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExecutionTimeAspect {

    @Around("within(com.tms.*)")
    public Object logExecutionTimeCheckMoreThanOneSecond(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;

        if (executionTime > 1000) {
            System.out.printf("Method %s worked %d ms.\n", joinPoint.getSignature(), executionTime);
        }
        return proceed;
    }

    @Around("@annotation(com.tms.annotation.ExecutionTime)")
    public Object logExecutionTimeAnnotation(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;
        System.out.printf("Method %s worked %d ms.\n", joinPoint.getSignature(), executionTime);
        return proceed;
    }
}
