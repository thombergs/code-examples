package io.jgoerner.s3.application.port.out.space;

public interface CheckSpaceExistence {
  boolean doesExist(String name);
}
