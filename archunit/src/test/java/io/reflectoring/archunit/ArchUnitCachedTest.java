package io.reflectoring.archunit;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import java.math.BigDecimal;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

    @AnalyzeClasses(packages = "io.reflectoring.archunit")
    public class ArchUnitCachedTest {
        @ArchTest
        public void doNotCallDeprecatedMethodsFromTheProject(JavaClasses classes) {
            ArchRule rule = noClasses().should().dependOnClassesThat().areAnnotatedWith(Deprecated.class);
            rule.check(classes);
        }

        @ArchTest
        public void doNotCallConstructorCached(JavaClasses classes) {
            ArchRule rule = noClasses().should().callConstructor(BigDecimal.class, double.class);
            rule.check(classes);
        }
    }
