package com.reflectoring.security.repository;

import com.reflectoring.security.persistence.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {


    List<Book> findByGenre(String genre);

    @PostAuthorize("returnObject.size() > 0")
    List<Book> findAll();
}
