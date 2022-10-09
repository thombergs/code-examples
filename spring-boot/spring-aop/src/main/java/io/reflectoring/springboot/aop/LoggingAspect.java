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

    @Pointcut("execution(public void io.reflectoring.springboot.aop.ShipmentService.shipStuffWithBill())")
    public void logPointcutWithExecution(){}

    @Before("logPointcutWithExecution()")
    public void logMethodCallsWithExecutionAdvice() {
        System.out.println("In Aspect from execution");
    }

    @Pointcut("within(io.reflectoring.springboot.aop.BillingService)")
    public void logPointcutWithin() {}

    @Before("logPointcutWithin()")
    public void logMethodCallsWithinAdvice() {
        System.out.println("In Aspect from within");
    }

}