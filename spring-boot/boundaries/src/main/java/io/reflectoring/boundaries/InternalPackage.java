package io.reflectoring.boundaries;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate a package-info.java file with this annotation to mark it as internal, i.e. no
 * classes outside of that package may depend on any classes within that package.
 */
@Target(ElementType.PACKAGE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface InternalPackage {

}
