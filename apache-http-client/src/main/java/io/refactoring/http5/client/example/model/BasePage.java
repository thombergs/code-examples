package io.refactoring.http5.client.example.model;

/**
 * Represents ansible abstract page.
 *
 * @param <T> type of paginated data item
 */
public abstract class BasePage<T> extends PaginatedEntities<T> {

  /** The page number. */
  private Long page;

  /** The number of items per page. */
  private Long perPage;

  /** The number of total items. */
  private Long total;

  /** The number of pages. */
  private Long totalPages;

  public Long getPage() {
    return page;
  }

  public void setPage(Long page) {
    this.page = page;
  }

  public Long getPerPage() {
    return perPage;
  }

  public void setPerPage(Long perPage) {
    this.perPage = perPage;
  }

  public Long getTotal() {
    return total;
  }

  public void setTotal(Long total) {
    this.total = total;
  }

  public Long getTotalPages() {
    return totalPages;
  }

  public void setTotalPages(Long totalPages) {
    this.totalPages = totalPages;
  }
}
