package io.reflectoring.passwordencoding.encoder;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

class Argon2Example {

  public String encode(String plainPassword) {
    int saltLength = 16; // salt length in bytes
    int hashLength = 32; // hash length in bytes
    int parallelism = 1; // currently is not supported
    int memory = 4096; // memory costs
    int iterations = 3;

    Argon2PasswordEncoder argon2PasswordEncoder =
        new Argon2PasswordEncoder(saltLength, hashLength, parallelism, memory, iterations);
    return argon2PasswordEncoder.encode(plainPassword);
  }
}
