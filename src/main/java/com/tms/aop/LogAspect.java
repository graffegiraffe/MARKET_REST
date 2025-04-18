package com.tms.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Date;

@Aspect
@Component
public class LogAspect {

    @Before("@annotation(com.tms.annotation.LogAnnotation)")
    public void logBefore(JoinPoint joinPoint) {
        System.out.println(new Date() + " INFO IN:" + joinPoint.getSignature().getDeclaringType() + ":"
                + joinPoint.getSignature().getName());
    }

    @AfterThrowing(value = "@annotation(com.tms.annotation.LogAnnotation)", throwing = "err")
    public void logAfterThrowing(JoinPoint joinPoint, ArithmeticException err) {
        System.out.println(new Date() + " ERROR " + joinPoint.getSignature().getDeclaringType() + ":"
                + joinPoint.getSignature().getName() + ":" + err.getMessage());
    }
    
    @After("@annotation(com.tms.annotation.LogAnnotation)")
    public void logAfter(JoinPoint joinPoint) {
        System.out.println(new Date() + " INFO OUT:" + joinPoint.getSignature().getDeclaringType() + ":"
                + joinPoint.getSignature().getName());
    }
    
    @AfterReturning(value = "@annotation(com.tms.annotation.LogAnnotation)", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        System.out.println(new Date() + " INFO " + joinPoint.getSignature().getDeclaringType() + ":"
                + joinPoint.getSignature().getName() + ":" + result);
    }

    @Around(value = "@annotation(com.tms.annotation.LogAnnotation)")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println(new Date() + " INFO AROUND IN:" + joinPoint.getSignature().getDeclaringType() + ":"
                + joinPoint.getSignature().getName());
        Object result = joinPoint.proceed();
        System.out.println(new Date() + " INFO AROUND OUT:" + joinPoint.getSignature().getDeclaringType() + ":"
                + joinPoint.getSignature().getName());
        return result;
    }
}
