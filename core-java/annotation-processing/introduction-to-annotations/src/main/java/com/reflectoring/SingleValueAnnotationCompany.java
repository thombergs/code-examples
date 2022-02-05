package com.reflectoring;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SingleValueAnnotationCompany {
    String value() default "ABC";
}
