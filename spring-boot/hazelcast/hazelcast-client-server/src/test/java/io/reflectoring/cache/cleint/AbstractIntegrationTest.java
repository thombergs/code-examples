package io.reflectoring.cache.cleint;

import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;

public class AbstractIntegrationTest {

  static GenericContainer firstMember =
      new FixedHostPortGenericContainer("hazelcast/hazelcast:4.0.1")
          .withFixedExposedPort(5701, 5701);

  static GenericContainer secondMember =
      new FixedHostPortGenericContainer("hazelcast/hazelcast:4.0.1")
          .withFixedExposedPort(5702, 5701);

    @BeforeAll
    public static void init() {
        firstMember.start();
        secondMember.start();
    }
}
