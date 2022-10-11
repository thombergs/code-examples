package io.reflectoring.springboot.aop;

import org.springframework.stereotype.Service;

@Service
public class OrderService {

    public String orderStuff() {
        System.out.println("Ordering stuff");
        return "Order";
    }

    public void cancelStuff() {
        System.out.println("Cancelling stuff");
    }

    @AfterLog
    public void checkStuff() {
        System.out.println("Checking stuff");
    }
}
