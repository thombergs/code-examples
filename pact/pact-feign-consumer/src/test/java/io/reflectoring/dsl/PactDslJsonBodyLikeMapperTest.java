package io.reflectoring.dsl;


import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import static org.junit.Assert.*;

public class PactDslJsonBodyLikeMapperTest {

  @Test
  public void createsMatchersForAllFields() {
    Root object = new Root();
    PactDslJsonBody jsonBody = PactDslJsonBodyLikeMapper.like(object);
    assertNoMatcher(jsonBody, ".nullField");
    assertMatcherType(jsonBody, ".stringField", "type");
    assertMatcherType(jsonBody, ".booleanField", "type");
    assertMatcherType(jsonBody, ".primitiveBooleanField", "type");
    assertMatcherType(jsonBody, ".integerField", "integer");
    assertMatcherType(jsonBody, ".primitiveIntegerField", "integer");
    assertMatcherType(jsonBody, ".doubleField", "decimal");
    assertMatcherType(jsonBody, ".primitiveDoubleField", "decimal");
    assertMatcherType(jsonBody, ".floatField", "decimal");
    assertMatcherType(jsonBody, ".primitiveFloatField", "decimal");
    assertMatcherType(jsonBody, ".bigDecimalField", "decimal");
    assertMatcherType(jsonBody, ".numberField", "number");
    assertMatcherType(jsonBody, ".nested.stringField", "type");
    assertMatcherType(jsonBody, ".nested.integerField", "integer");
    assertNoMatcher(jsonBody, ".nested.nullField");
    assertMatcherType(jsonBody, ".complexListField[*].stringField", "type");
    assertMatcherType(jsonBody, ".complexListField[*].integerField", "integer");
    assertMatcherType(jsonBody, ".simpleListField[*]", "integer");
  }

  private void assertMatcherType(PactDslJsonBody jsonBody, String fieldName, String expectedMatcher) {
    assertMatcher(jsonBody, fieldName);
    assertEquals(String.format("expected matcher for field '%s' to be of type '%s'", fieldName, expectedMatcher),
            ImmutableMap.of("match", expectedMatcher),
            jsonBody.getMatchers().getMatchingRules().get(fieldName).getRules().get(0).toMap());
  }

  private void assertMatcher(PactDslJsonBody jsonBody, String fieldName) {
    assertNotNull(String.format("expected a matcher for field '%s'", fieldName),
            jsonBody.getMatchers().getMatchingRules().get(fieldName));
  }

  private void assertNoMatcher(PactDslJsonBody jsonBody, String fieldName) {
    assertNull(jsonBody.getMatchers().getMatchingRules().get(fieldName));
  }

}