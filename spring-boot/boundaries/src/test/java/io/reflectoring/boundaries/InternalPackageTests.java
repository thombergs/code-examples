package io.reflectoring.boundaries;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static org.assertj.core.api.Assertions.assertThat;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

/**
 * Evaluates {@link InternalPackage} annotations and checks that those packages are not accessed from the outside.
 */
class InternalPackageTests {

  private static final String BASE_PACKAGE = "io.reflectoring";
  private final JavaClasses analyzedClasses = new ClassFileImporter().importPackages(BASE_PACKAGE);

  @Test
  void internalPackagesAreNotAccessedFromOutside() throws IOException {

    // so that the test will break when the base package is re-named
    assertPackageExists(BASE_PACKAGE);

    List<String> internalPackages = internalPackages(BASE_PACKAGE);

    for (String internalPackage : internalPackages) {
      assertPackageIsNotAccessedFromOutside(internalPackage);
    }

  }

  /**
   * Finds all packages annotated with @{@link InternalPackage}.
   */
  private List<String> internalPackages(String basePackage) {
    Reflections reflections = new Reflections(basePackage);
    return reflections.getTypesAnnotatedWith(InternalPackage.class).stream()
        .map(c -> c.getPackage().getName())
        .collect(Collectors.toList());
  }

  void assertPackageIsNotAccessedFromOutside(String internalPackage) {
    noClasses().that().resideOutsideOfPackage(packageMatcher(internalPackage))
        .should().dependOnClassesThat().resideInAPackage(packageMatcher(internalPackage))
        .check(analyzedClasses);
  }

  void assertPackageExists(String packageName) {
    assertThat(analyzedClasses.containPackage(packageName))
        .as("package %s exists", packageName)
        .isTrue();
  }

  private String packageMatcher(String fullyQualifiedPackage) {
    return fullyQualifiedPackage + "..";
  }


}
