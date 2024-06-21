package io.reflectoring.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class IpAddressValidatorTest {

  @Test
  void test(){
    IpAddressValidator validator = new IpAddressValidator();
    assertTrue(validator.isValid("111.111.111.111", null));
    assertFalse(validator.isValid("111.foo.111.111", null));
    assertFalse(validator.isValid("111.111.256.111", null));
  }

}