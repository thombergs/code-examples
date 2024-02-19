package com.reflectoring.beginnersguide.repository;

import com.reflectoring.beginnersguide.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM Book " +
            "book WHERE book.currentlyAvailableNumber > 5 ")
    List<Book> findWithMoreInstancesThenFive();

    @Query( value = "SELECT b FROM book b where b.numberOfInstances > 5")
    List<Book> findWithMoreInstancesThenFiveJPQL();

    List<Book> findAllByNumberOfInstancesGreaterThan(long limit);

}
