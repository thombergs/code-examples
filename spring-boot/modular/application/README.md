# Modularizing a Spring Boot Application

This module is a Spring Boot application that includes the following modules

* [security-module](../security-module)
* [booking-module](../booking-module)

## Points of Interest
Have a look at [ModularApplication.java](src/main/java/io/reflectoring/application/ModularApplication.java)
to see how the booking module is imported with `@Import`. The security module is included
via Spring Boot's [auto configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-developing-auto-configuration.html)
mechanism.
 
## Companion Blog Article
This repository is accompanied by a [companion blog article](https://reflectoring.io/modularizing-spring-boot).