package io.reflectoring.validation;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class IpAddressValidatorTest {

  @Test
  void test(){
    IpAddressValidator validator = new IpAddressValidator();
    assertTrue(validator.isValid("111.111.111.111", null));
    assertFalse(validator.isValid("111.foo.111.111", null));
    assertFalse(validator.isValid("111.111.256.111", null));
  }

}