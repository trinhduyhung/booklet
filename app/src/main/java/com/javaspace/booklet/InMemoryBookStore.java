package com.javaspace.booklet;

import java.util.ArrayList;
import java.util.List;

public class InMemoryBookStore {

    static InMemoryBookStore sInstance;

    // This method is not thread safe
    static InMemoryBookStore getInstance() {
        if (sInstance == null) {
            sInstance = new InMemoryBookStore();
        }

        return sInstance;
    }

    private List<Book> addedBooks = new ArrayList<>();

    public void addBook(Book book) {
        addedBooks.add(book);
    }

    public void editBook(Book book) {
        Book foundBook = getBookById(book.getId());
        foundBook.copy(book);
    }

    public List<Book> getAddedBooks() {
        return addedBooks;
    }

    public Book getBookById(int bookId) {
        Book foundBook = null;
        for (Book book : addedBooks) {
            if (book.getId() == bookId) {
                foundBook = book;
            }
        }
        return foundBook;
    }

    public boolean isEmpty() {
        return addedBooks.size() == 0;
    }

    @Override
    public String toString() {
        return "BookStore{" +
                "addedBooks=" + addedBooks +
                '}';
    }
}
