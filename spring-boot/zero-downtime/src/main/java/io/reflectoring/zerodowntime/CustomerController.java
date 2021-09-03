package io.reflectoring.zerodowntime;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

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
    String createCustomer() {
        if (featureFlagService.writeToNewCustomerSchema()) {
            NewCustomer customer = new NewCustomer("Bob", "Builder", "Build Street", "21");
            newCustomerRepository.save(customer);
        } else {
            OldCustomer customer = new OldCustomer("Bob", "Builder", "21 Build Street");
            oldCustomerRepository.save(customer);
        }
        return "customer created";
    }

    @GetMapping("/customers/{id}}")
    String getCustomer(@PathVariable("id") Long id) {
        if (featureFlagService.readFromNewCustomerSchema()) {
            Optional<NewCustomer> customer = newCustomerRepository.findById(id);
            return customer.get().toString();
        } else {
            Optional<OldCustomer> customer = oldCustomerRepository.findById(id);
            return customer.get().toString();
        }
    }

    @GetMapping("/customers/list")
    String getCustomer() {
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
