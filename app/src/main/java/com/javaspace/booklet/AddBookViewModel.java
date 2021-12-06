package com.javaspace.booklet;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class AddBookViewModel extends AndroidViewModel {

    private Repository repository;

    public AddBookViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }

    public void addBook(Book book) {
        repository.addBook(book);
    }

}
