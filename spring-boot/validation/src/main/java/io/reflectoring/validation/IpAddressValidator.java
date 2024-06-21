package io.reflectoring.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IpAddressValidator implements ConstraintValidator<IpAddress, String> {

  private static final Pattern IP_ADDRESS_PATTERN = Pattern.compile("^([0-9]{1,3})\\.([0-9]{1,3})\\.([0-9]{1,3})\\.([0-9]{1,3})$");

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    Matcher matcher = IP_ADDRESS_PATTERN.matcher(value);
    boolean isValidIpAddress = matcher.matches();
    
    if (isValidIpAddress) {
        isValidIpAddress = isValidOctets(matcher);
    }
    return isValidIpAddress;
  }

  private boolean isValidOctets(Matcher matcher) {
    for (int i = 1; i <= 4; i++) {
      int octet = Integer.parseInt(matcher.group(i));
      if (octet < 0 || octet > 255) {
        return false;
      }
    }
    return true;
  }

}