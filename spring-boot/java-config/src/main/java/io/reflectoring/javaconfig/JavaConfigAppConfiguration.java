package io.reflectoring.javaconfig;

import io.reflectoring.javaconfig.model.Department;
import io.reflectoring.javaconfig.model.Employee;
import io.reflectoring.javaconfig.model.Organization;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@ComponentScan(basePackages = "io.reflectoring.javaconfig")
public class JavaConfigAppConfiguration {

    @Bean(name = "newEmployee", initMethod = "init", destroyMethod = "destroy")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @Cacheable(cacheNames = "employeesCache")
    public Employee newEmployee(final String firstName, final String lastName) {
        return Employee.builder().firstName(firstName).lastName(lastName).build();
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Department newDepartment(final String deptName) {
        final Department department = Department.builder().name(deptName).build();
        acme().addDepartment(department);
        return department;
    }

    @Bean(name = "founder")
    @Qualifier(value = "founder")
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public Employee founderEmployee() {
        final Employee founder = newEmployee("Scott", "Tiger");
        founder.setDesignation("Founder");
        founder.setDepartment(coreDepartment());
        return founder;
    }

    @Bean(name = "core")
    @Qualifier(value = "core")
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public Department coreDepartment() {
        return newDepartment("Core");
    }

    @Bean(name = "acme")
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Qualifier(value = "acme")
    public Organization acme() {
        final Organization acmeCo = new Organization();
        acmeCo.setName("Acme Inc");
        return acmeCo;
    }
}
