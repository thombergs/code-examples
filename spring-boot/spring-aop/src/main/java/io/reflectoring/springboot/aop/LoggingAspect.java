package io.reflectoring.springboot.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoggingAspect {
    @Pointcut("@annotation(Log)")
    public void logPointcut(){
    }
    @Before("logPointcut()")
    public void logAllMethodCallsAdvice(){
        System.out.println("In Aspect");
    }
}