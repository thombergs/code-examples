package io.reflectoring.springboot.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
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
    public void logAllMethodCallsAdvice(JoinPoint joinPoint){
        System.out.println("In Aspect at " + joinPoint.getSignature().getName());
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

    @Pointcut("execution(public void io.reflectoring.springboot.aop.BillingService.createBill(Long))")
    public void logPointcutWithArgs() {}

    @Before("logPointcutWithArgs()")
    public void logMethodCallsWithArgsAdvice() {
        System.out.println("In Aspect from Args");
    }

    @Pointcut("within(io.reflectoring.springboot.aop.OrderService) && execution(public String io.reflectoring.springboot.aop.OrderService.*(..))")
    public void logPointcutWithLogicalOperator(){}

    @Before("logPointcutWithLogicalOperator()")
    public void logPointcutWithLogicalOperatorAdvice(){
        System.out.println("In Aspect from logical operator");
    }


    @Pointcut("@annotation(AfterLog)")
    public void logAfterPointcut(){}

    @After("logAfterPointcut()")
    public void logMethodCallsAfterAdvice(JoinPoint joinPoint) {
        System.out.println("In After Aspect at " + joinPoint.getSignature().getName());
    }

}