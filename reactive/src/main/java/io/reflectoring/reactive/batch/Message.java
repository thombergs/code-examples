package io.reflectoring.reactive.batch;

public class Message {

  private final String content;

  public Message(String content) {
    this.content = content;
  }

  public String getContent() {
    return content;
  }

  @Override
  public String toString() {
    return content;
  }
}
