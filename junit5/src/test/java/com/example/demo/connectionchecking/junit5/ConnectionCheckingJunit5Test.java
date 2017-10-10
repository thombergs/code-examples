package com.example.demo.connectionchecking.junit5;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.fail;

@AssumeConnection(uri = "http://my.integration.system")
public class ConnectionCheckingJunit5Test {

  @Test
  public void testOnlyWhenConnected() {
    fail("Booh!");
  }

}
