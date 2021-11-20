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

    public List<Book> getAddedBooks() {
        return addedBooks;
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
