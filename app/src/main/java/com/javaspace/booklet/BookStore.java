package com.javaspace.booklet;

import java.util.List;

public interface BookStore {
    void addBook(Book book);

    void editBook(Book book);

    List<Book> getAddedBooks();

    Book getBookById(int bookId);

    void remove(int bookId);
}
