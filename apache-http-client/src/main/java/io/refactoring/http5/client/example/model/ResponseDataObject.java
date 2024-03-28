package io.refactoring.http5.client.example.model;

/**
 * Represents response containing given data.
 *
 * @param <T> data type
 */
public class ResponseDataObject<T> extends DataObject {
  private T data;

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }
}
