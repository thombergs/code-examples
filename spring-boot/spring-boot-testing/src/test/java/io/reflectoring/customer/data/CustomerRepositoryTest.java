package io.reflectoring.customer.data;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class CustomerRepositoryTest {

  @Autowired
  private CustomerRepository repository;

  @Autowired
  private ApplicationContext applicationContext;

  @BeforeEach
  void printApplicationContext() {
    Arrays.stream(applicationContext.getBeanDefinitionNames())
            .map(name -> applicationContext.getBean(name).getClass().getName())
            .sorted()
            .forEach(System.out::println);
  }

  @Test
  void findsByName() {
    Customer customer = Customer.builder()
            .name("Hurley")
            .build();
    repository.save(customer);

    List<Customer> foundCustomers = repository.findByName("Hurley");
    assertThat(foundCustomers).hasSize(1);
  }

}