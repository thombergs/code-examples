package com.reflectoring;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SingleMemberCompany {
    String value() default "ABC";
}
