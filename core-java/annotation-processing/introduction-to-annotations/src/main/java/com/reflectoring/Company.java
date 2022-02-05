package com.reflectoring;

import java.lang.annotation.*;

@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Company{
    String name() default "ABC";
    String city() default "XYZ";
}
