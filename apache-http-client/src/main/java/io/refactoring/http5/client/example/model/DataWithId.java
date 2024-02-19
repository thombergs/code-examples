package io.refactoring.http5.client.example.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

/** Represents data objects with identifiers. */
public abstract class DataWithId extends DataObject {
  private long id;

  /* Date of creation, for example, 2024-02-18T13:47:25.842Z
   */
//  @JsonProperty("created_at")
  @JsonFormat(
      shape = JsonFormat.Shape.STRING,
      pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
      timezone = "GMT")
  private Date createdAt;

  public long getId() {
    return id;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }
}
