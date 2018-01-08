package com.example.demo.connectionchecking.junit4;


import com.example.demo.connectionchecking.ConnectionChecker;
import org.junit.ClassRule;
import org.junit.Test;
import static org.junit.Assert.fail;

public class ConnectionCheckingJunit4Test {

  @ClassRule
  public static AssumingConnection assumingConnection = new AssumingConnection(new ConnectionChecker("http://my.integration.system"));

  @Test
  public void testOnlyWhenConnected() {
    fail("Booh!");
  }

}
