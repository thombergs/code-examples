package io.reflectoring.reactive.batch;

class Logger {

  void log(String string) {
    System.out.println(String.format("%s %s: %s", System.currentTimeMillis(), Thread.currentThread().getName(), string));
  }

}
