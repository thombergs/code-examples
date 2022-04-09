package com.reflectoring.lombok;

import com.reflectoring.lombok.repository.BookRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

@SpringBootTest(classes = Application.class)
@Transactional
public class PersistenceTest {

    private static Logger log = LoggerFactory.getLogger(PersistenceTest.class);

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void loadData() {
        log.info("Books : {}", bookRepository.findAll());
        Assertions.assertEquals(2, bookRepository.count());
    }
}
