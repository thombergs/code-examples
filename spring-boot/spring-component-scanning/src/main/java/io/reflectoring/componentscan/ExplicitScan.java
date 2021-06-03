package io.reflectoring.componentscan;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

import io.reflectoring.vehicles.Car;

@Configuration
@ComponentScan(basePackages= "io.reflectoring.vehicles", 
includeFilters=@ComponentScan.Filter(type=FilterType.ASSIGNABLE_TYPE, classes=Car.class)
, useDefaultFilters=false
// Uncomment next line and comment out include filter to checkout exclude filter
//excludeFilters=@ComponentScan.Filter(type=FilterType.ASSIGNABLE_TYPE, classes=Car.class)
)
public class ExplicitScan {

}