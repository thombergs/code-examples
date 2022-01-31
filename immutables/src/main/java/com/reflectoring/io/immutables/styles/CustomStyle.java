package com.reflectoring.io.immutables.styles;

import org.immutables.value.Value;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// How does styles work
// Where we can put it
// Thing to be aware of and tests of those

@Target({ElementType.PACKAGE,ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
@Value.Style(
        of = "new",
        strictBuilder = true,
        allParameters = true,
        visibility = Value.Style.ImplementationVisibility.PUBLIC

)
public @interface CustomStyle {}
