package io.reflectoring.springboot.aop;

import org.springframework.stereotype.Service;

@Service
public class BillingService {
    public void createBill() {
        System.out.println("Bill created");
    }

    public void createBill(Long price) {
        System.out.println("Bill Created: " + price);
    }
}
