package io.refactoring.http5.client.example.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

/** Represents data objects with identifiers. */
public abstract class DataWithId extends DataObject {
  private long id;

  /* Date of creation, for example, 2024-02-18T13:47:25.842Z
   */
  @JsonFormat(
      shape = JsonFormat.Shape.STRING,
      pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
      timezone = "GMT")
  private Date createdAt;

  /* Date of update, for example, 2024-02-18T13:47:25.842Z
   */
  @JsonFormat(
          shape = JsonFormat.Shape.STRING,
          pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
          timezone = "GMT")
  private Date updatedAt;

  /* Date of deletion, for example, 2024-02-18T13:47:25.842Z
   */
  @JsonFormat(
          shape = JsonFormat.Shape.STRING,
          pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
          timezone = "GMT")
  private Date deletedAt;

  /**
   * Gets id.
   *
   * @return the id
   */
public long getId() {
    return id;
  }

  /**
   * Sets id.
   *
   * @param id the id
   */
public void setId(long id) {
    this.id = id;
  }

  /**
   * Gets created at.
   *
   * @return the created at
   */
public Date getCreatedAt() {
    return createdAt;
  }

  /**
   * Sets created at.
   *
   * @param createdAt the created at
   */
public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  /**
   * Gets updated at.
   *
   * @return the updated at
   */
public Date getUpdatedAt() {
    return updatedAt;
  }

  /**
   * Sets updated at.
   *
   * @param updatedAt the updated at
   */
public void setUpdatedAt(Date updatedAt) {
    this.updatedAt = updatedAt;
  }

  /**
   * Gets deleted at.
   *
   * @return the deleted at
   */
public Date getDeletedAt() {
    return deletedAt;
  }

  /**
   * Sets deleted at.
   *
   * @param deletedAt the deleted at
   */
public void setDeletedAt(Date deletedAt) {
    this.deletedAt = deletedAt;
  }
}
