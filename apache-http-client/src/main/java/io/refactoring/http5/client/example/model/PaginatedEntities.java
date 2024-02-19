package io.refactoring.http5.client.example.model;

import java.util.ArrayList;

/**
 * Represents paginated entities.
 *
 * @param <T> type of paginated data item
 */
public class PaginatedEntities<T> extends DataObject {
  private ArrayList<T> data;

  public ArrayList<T> getData() {
    return data(true);
  }

  public void setData(ArrayList<T> data) {
    this.data = data;
  }

  protected ArrayList<T> data(boolean autoCreate) {
    if (data == null && autoCreate) {
      data = new ArrayList<>();
    }
    return data;
  }

  /**
   * Adds given item to paginated data list.
   *
   * @param item source item
   */
  public void addContent(T item) {
    data(true).add(item);
  }
}
