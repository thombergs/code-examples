package io.reflectoring.springboot.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ValidationAspect {
    @Pointcut("within(io.reflectoring.springboot.aop.ValidationService)")
    public void validationPointcut(){}

    @Around("validationPointcut()")
    public void aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("In Around Aspect");
        int arg = (int) joinPoint.getArgs()[0];
        if (arg < 0)
            throw new RuntimeException("Argument should not be negative");
        else
            joinPoint.proceed();
    }
}
