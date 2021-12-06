package com.javaspace.booklet;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BookDao {

    @Insert
    void addBook(Book book);

    @Update
    void editBook(Book book);

    @Query("SELECT * FROM book ORDER BY title")
    List<Book> getAllBooks();

    @Query("SELECT * FROM book WHERE id = :bookId")
    Book getBookById(int bookId);

    @Query("DELETE FROM book WHERE id = :bookId")
    void remove(int bookId);
}
