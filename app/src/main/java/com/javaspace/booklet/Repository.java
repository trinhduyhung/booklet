package com.javaspace.booklet;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class Repository {

    private final BookDao bookStore;

    public Repository(Application application) {
        bookStore = AppDatabase.getDatabase(application).bookStore();
    }

    public void addBook(Book book) {
        AsyncTask.execute(() -> bookStore.addBook(book));
    }

    public Book getBookById(int bookId) {
        try {
            return new GetBookByIdAsyncTask(bookStore).execute(bookId).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void remove(int bookId) {
        bookStore.remove(bookId);
    }

    public void editBook(Book book) {
        bookStore.editBook(book);
    }

    public List<Book> getAllBooks() {
        try {
            return new GetAllBooksAsyncTask(bookStore).execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class GetAllBooksAsyncTask extends AsyncTask<Void, Void, List<Book>> {

        private BookDao dao;

        public GetAllBooksAsyncTask(BookDao dao) {
            this.dao = dao;
        }

        @Override
        protected List<Book> doInBackground(Void... voids) {
            return dao.getAllBooks();
        }
    }

    private static class GetBookByIdAsyncTask extends AsyncTask<Integer, Void, Book> {

        private BookDao dao;

        public GetBookByIdAsyncTask(BookDao dao) {
            this.dao = dao;
        }

        @Override
        protected Book doInBackground(Integer... integers) {
            return dao.getBookById(integers[0]);
        }
    }
}
