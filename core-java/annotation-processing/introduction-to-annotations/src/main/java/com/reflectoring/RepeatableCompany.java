package com.reflectoring;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Repeatable(RepeatableCompanies.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface RepeatableCompany {
    String name() default "Name_1";
    String city() default "City_1";
}
