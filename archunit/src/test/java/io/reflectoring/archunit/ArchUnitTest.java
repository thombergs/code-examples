package io.reflectoring.archunit;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.domain.JavaMethodCall;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import com.tngtech.archunit.library.freeze.FreezingArchRule;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static org.junit.jupiter.api.Assertions.assertEquals;

@AnalyzeClasses(packages = "io.reflectoring.archunit")
public class ArchUnitTest {

    @Test
    public void myLayerAccessTest() {

        JavaClasses importedClasses = new ClassFileImporter()
            .importPackages("io.reflectoring.archunit.api");

        ArchRule apiRule= noClasses()
            .should()
            .accessClassesThat()
            .resideInAPackage("io.reflectoring.archunit.persistence");

        apiRule.check(importedClasses);
    }

    @Test
    public void freezingRules() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("io.reflectoring.archunit");

        ArchRule rule = methods().that()
            .areAnnotatedWith(Test.class)
            .should().haveFullNameNotMatching(".*\\d+.*");

        FreezingArchRule.freeze(rule).check(importedClasses);
    }

    @Test
    public void doNotCallConstructor() {
        JavaClasses importedClasses = new ClassFileImporter()
            .importPackages("io.reflectoring.archunit");
        ArchRule rule = noClasses().should()
            .callConstructor(BigDecimal.class, double.class);
        rule.check(importedClasses);
    }

    @Test
    public void instantiateLocalDateTimeWithClock() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("io.reflectoring.archunit");
        ArchRule rule = noClasses().should().callMethod(LocalDateTime.class, "now");
        rule.check(importedClasses);
    }

    @Test
    public void doNotCallDeprecatedMethodsFromTheProject() {
        JavaClasses importedClasses = new ClassFileImporter()
            .importPackages("io.reflectoring.archunit");
        ArchRule rule = noClasses().should()
            .dependOnClassesThat()
            .areAnnotatedWith(Deprecated.class);
        rule.check(importedClasses);
    }

    @Test
    public void aTestWithAnAssertion() {
        String expected = "chocolate";
        String actual = "chocolate";
        assertEquals(expected, actual);
    }

    @Test
    public void aTestWithoutAnAssertion() {
        String expected = "chocolate";
        String actual = "chocolate";
        expected.equals(actual);
    }

    @ArchTest
    public void testMethodsShouldAssertSomething(JavaClasses classes) {
        ArchRule testMethodRule = methods().that().areAnnotatedWith(Test.class)
            .should(callAnAssertion);
        testMethodRule.check(classes);
    }

    @Test
    public void testArrayList() {
        List list = new ArrayList();
        list.add("My item");
        assertEquals(1, list.size());
    }

    public ArchCondition<JavaMethod> callAnAssertion =
        new ArchCondition<>("a unit test should assert something") {
            @Override
            public void check(JavaMethod item, ConditionEvents events) {
                for (JavaMethodCall call : item.getMethodCallsFromSelf()) {
                    if((call.getTargetOwner().getPackageName().equals(
                            org.junit.jupiter.api.Assertions.class.getPackageName()
                    )
                        && call.getTargetOwner().getName().equals(
                            org.junit.jupiter.api.Assertions.class.getName()))
                        || (call.getTargetOwner().getName().equals(
                            com.tngtech.archunit.lang.ArchRule.class.getName())
                            && call.getName().equals("check"))
                    ) {
                        return;
                    }
                }
                events.add(SimpleConditionEvent.violated(
                    item, item.getDescription() + "does not assert anything.")
                );
            }
        };
    }
