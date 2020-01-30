package io.reflectoring.reactive.batch;

public class Logger {

  public void log(String string) {
    System.out.println(String.format("%s %s: %s", System.currentTimeMillis(), Thread.currentThread().getName(), string));
  }

}
