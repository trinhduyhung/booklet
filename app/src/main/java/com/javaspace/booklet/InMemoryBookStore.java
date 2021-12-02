package com.javaspace.booklet;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

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

    public void remove(int bookId) {
        int foundId = -1;
        for (int i = 0; i < addedBooks.size(); i++) {
            if (addedBooks.get(i).getId() == bookId) {
                foundId = i;
                break;
            }
        }
        if (foundId != -1) {
            addedBooks.remove(foundId);
        }
    }
}
