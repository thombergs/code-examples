package io.reflectoring.reactive.batch;

class Message {

  private final String content;

  Message(String content) {
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
