package io.jgoerner.s3.application.port.in.space;

public interface SetTTL {
  void setTTL(String space, Integer ttlInDays);
}
