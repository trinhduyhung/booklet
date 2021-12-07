package com.javaspace.booklet;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

public class Repository {

    private final BookDao bookDao;

    public Repository(Application application) {
        bookDao = AppDatabase.getDatabase(application).bookStore();
    }

    public void addBook(Book book) {
        try {
            new AddBookAsyncTask(bookDao).execute(book).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Book getBookById(int bookId) {
        try {
            return new GetBookByIdAsyncTask(bookDao).execute(bookId).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void remove(int bookId) {
        try {
            new RemoveBookAsyncTask(bookDao).execute(bookId).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editBook(Book book) {
        try {
            new EditBookAsyncTask(bookDao).execute(book).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Book> getAllBooks() {
        try {
            return new GetAllBooksAsyncTask(bookDao).execute().get();
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

    private static class RemoveBookAsyncTask extends AsyncTask<Integer, Void, Void> {

        private BookDao dao;

        public RemoveBookAsyncTask(BookDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            dao.remove(integers[0]);
            return null;
        }
    }

    private static class EditBookAsyncTask extends AsyncTask<Book, Void, Void> {

        private BookDao dao;

        public EditBookAsyncTask(BookDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Book... books) {
            dao.editBook(books[0]);
            return null;
        }
    }

    private static class AddBookAsyncTask extends AsyncTask<Book, Void, Void> {

        private BookDao dao;

        public AddBookAsyncTask(BookDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Book... books) {
            dao.addBook(books[0]);
            return null;
        }
    }
}
