package io.reflectoring.resilience4j.bulkhead.utils;

public class RequestTrackingIdHolder {
  private static ThreadLocal<String> threadLocal = new ThreadLocal<>();

  public static String getRequestTrackingId() {
    return threadLocal.get();
  }

  public static void setRequestTrackingId(String id) {
    if (threadLocal.get() != null) {
      threadLocal.set(null);
      threadLocal.remove();
    }
    threadLocal.set(id);
  }

  public static void clear() {
    threadLocal.set(null);
    threadLocal.remove();
  }
}