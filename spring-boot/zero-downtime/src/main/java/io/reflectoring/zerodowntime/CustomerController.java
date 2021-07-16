package io.reflectoring.zerodowntime;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {

    private final CustomerRepository customerRepository;

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping("/customers/create")
    String createUser() {
        Customer customer = new Customer("Bob", "Builder", "21 Build Street");
        customerRepository.save(customer);
        return "customer created";
    }

    @GetMapping("/customers/list")
    String showUser() {
        Iterable<Customer> customers = customerRepository.findAll();
        StringBuffer buffer = new StringBuffer();
        for (Customer customer : customers) {
            buffer.append("\n");
            buffer.append(customer.toString());
        }
        return buffer.toString();
    }

}
