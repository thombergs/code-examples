package com.reflectoring.begginersguide.services;

import com.reflectoring.begginersguide.domain.User;
import org.springframework.stereotype.Service;

@Service
public class BorrowBookService {

    GetUserService getUserService;
    GetBookService getBookService;

    public void borrow(long bookId, long userId){
        User user = getUserService.getById(userId);
        if(user.getBorrowedBooks().stream().anyMatch(book -> book.getId()== bookId)){
            throw new IllegalStateException("User already borrowed " +
                    "the book");
        }
    }
}
