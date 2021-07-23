package io.reflectoring.zerodowntime;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {

    private final CustomerRepository oldCustomerRepository;
    private final NewCustomerRepository newCustomerRepository;
    private final FeatureFlagService featureFlagService;

    public CustomerController(CustomerRepository oldCustomerRepository, NewCustomerRepository newCustomerRepository, FeatureFlagService featureFlagService) {
        this.oldCustomerRepository = oldCustomerRepository;
        this.newCustomerRepository = newCustomerRepository;
        this.featureFlagService = featureFlagService;
    }

    @GetMapping("/customers/create")
    String createUser() {
        if (featureFlagService.writeToNewCustomerSchema()) {
            NewCustomer customer = new NewCustomer("Bob", "Builder", "Build Street", "21");
            newCustomerRepository.save(customer);
        } else {
            OldCustomer customer = new OldCustomer("Bob", "Builder", "21 Build Street");
            oldCustomerRepository.save(customer);
        }
        return "customer created";
    }

    @GetMapping("/customers/list")
    String showUser() {
        StringBuffer buffer = new StringBuffer();
        if (featureFlagService.readFromNewCustomerSchema()) {
            Iterable<NewCustomer> customers = newCustomerRepository.findAll();
            for (NewCustomer customer : customers) {
                buffer.append("\n");
                buffer.append(customer.toString());
            }
        } else {
            Iterable<OldCustomer> customers = oldCustomerRepository.findAll();
            for (OldCustomer customer : customers) {
                buffer.append("\n");
                buffer.append(customer.toString());
            }
        }
        return buffer.toString();
    }

}
