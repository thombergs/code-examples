package io.reflectoring.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class IpAddressValidatorTest {

  private final IpAddressValidator validator = new IpAddressValidator();

  @Test
  void whenValidIpAddress_thenValidationSucceeds() {
    assertTrue(validator.isValid("111.111.111.111", null));
    assertTrue(validator.isValid("0.0.0.0", null));
    assertTrue(validator.isValid("255.255.255.255", null));
  }

  @Test
  void whenInvalidIpAddress_thenValidationFails() {
    assertFalse(validator.isValid("111.foo.111.111", null));
    assertFalse(validator.isValid("111.111.256.111", null));
  }

}