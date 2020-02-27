package io.reflectoring.passwordencoding.encoder;

import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

class SCryptExample {

  public String encode(String plainPassword) {
    int cpuCost = (int) Math.pow(2, 14); // factor to increase CPU costs
    int memoryCost = 8; // factor to increases memory usage
    int parallelization = 1; // currently  nor supported by Spring Security
    int keyLength = 32; // key length in bytes
    int saltLength = 64; // salt length in bytes

    SCryptPasswordEncoder sCryptPasswordEncoder =
        new SCryptPasswordEncoder(cpuCost, memoryCost, parallelization, keyLength, saltLength);
    return sCryptPasswordEncoder.encode(plainPassword);
  }
}
