package io.reflectoring.passwordencoding.encoder;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;

class BCryptExample {

  public String encode(String plainPassword) {
    int strength = 10;
    BCryptPasswordEncoder bCryptPasswordEncoder =
        new BCryptPasswordEncoder(strength, new SecureRandom());
    return bCryptPasswordEncoder.encode(plainPassword);
  }
}
