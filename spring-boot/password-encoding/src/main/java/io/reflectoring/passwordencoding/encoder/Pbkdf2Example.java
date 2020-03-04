package io.reflectoring.passwordencoding.encoder;

import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

class Pbkdf2Example {

  public String encode(String plainPassword) {

    String pepper = "pepper"; // secret key used by password encoding
    int iterations = 200000; // number of hash iteration
    int hashWidth = 256; // hash with in bits

    Pbkdf2PasswordEncoder pbkdf2PasswordEncoder =
        new Pbkdf2PasswordEncoder(pepper, iterations, hashWidth);
    return pbkdf2PasswordEncoder.encode(plainPassword);
  }
}
