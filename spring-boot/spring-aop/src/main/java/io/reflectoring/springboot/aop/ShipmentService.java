package io.reflectoring.springboot.aop;

import org.springframework.stereotype.Service;

@Service
public class ShipmentService {
    @Log
    // this here is what's called a join point
    public void shipStuff(){
        System.out.println("In Service");
    }
}