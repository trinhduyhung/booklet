package com.javaspace.booklet;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class AddBookActivity extends AddEditBookActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findViewById(R.id.btn_add_book).setOnClickListener(v -> {
            if (shouldAddBook()) {
                loadBitmapFromCoverImageUri();
            }
        });

    }

    protected Book createBook() {
        Book book = new Book();
        book.setTitle(title.getText().toString());
        book.setEdition(edition.getText().toString());
        book.setAuthor(author.getText().toString());
        book.setPublisher(publisher.getText().toString());
        book.setPublishedTime(published.getText().toString());
        book.setPages(Integer.parseInt(pages.getText().toString()));
        book.setSummary(summary.getText().toString());
        book.setCoverImgPath(mediaSaver.getSavedImageFileName());
        return book;
    }

    private void loadBitmapFromCoverImageUri() {
        Picasso.get().load(newCoverImageUri).into(new Target() {
            @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                mediaSaver = new MediaSaver(AddBookActivity.this);
                if (mediaSaver.saveImageFromBitmap(bitmap)) {
                    repository.addBook(createBook());
                    startBooksActivity();
                }
            }

            @Override public void onBitmapFailed(Exception e, Drawable errorDrawable) { }

            @Override public void onPrepareLoad(Drawable placeHolderDrawable) {}
        });
    }

    @Override protected boolean shouldAddBook() {
        boolean shouldAddBook = super.shouldAddBook();
        if (!shouldAddBook) {
            return false;
        } else { // passed all the above validations
            if (newCoverImageUri == null) {
                shouldAddBook = false;
                Toast.makeText(this, getText(R.string.cover_not_empty), Toast.LENGTH_SHORT).show();
            }
        }
        return shouldAddBook;
    }
}