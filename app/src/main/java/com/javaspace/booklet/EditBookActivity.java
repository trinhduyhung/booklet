package com.javaspace.booklet;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class EditBookActivity extends AddEditBookActivity {

    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((Button) findViewById(R.id.btn_add_book)).setText(R.string.update_book);

        int bookId = getIntent().getIntExtra(BookDetailActivity.SELECTED_BOOK_ID, -1);
        book = repository.getBookById(bookId);

        if (savedInstanceState == null) {
            fillTheBookDetail();
        } else if (!userPickedCoverImage()) {
            setCoverImage();
        }

        findViewById(R.id.btn_add_book).setOnClickListener(v -> {
            if (shouldAddBook()) {
                if (userPickedCoverImage()) {
                    loadBitmapFromCoverImageUri();
                } else {
                    repository.editBook(updateBook());
                    startBooksActivity();
                }
            }
        });

    }

    private void fillTheBookDetail() {
        title.setText(book.getTitle());
        edition.setText(book.getEdition());
        author.setText(book.getAuthor());
        published.setText(String.valueOf(book.getPublishedTime()));
        pages.setText(String.valueOf(book.getPages()));
        summary.setText(book.getSummary());
        setCoverImage();
    }

    private void setCoverImage() {
        String coverPath = book.getCoverImgPath();
        Picasso.get().load(new MediaSaver(this).getFile(coverPath))
                .placeholder(R.drawable.not_yet_available).into(cover);
    }

    private Book updateBook() {
        book.setTitle(title.getText().toString());
        book.setEdition(edition.getText().toString());
        book.setAuthor(author.getText().toString());
        book.setPublisher(publisher.getText().toString());
        book.setPublishedTime(published.getText().toString());
        book.setPages(Integer.parseInt(pages.getText().toString()));
        book.setSummary(summary.getText().toString());
        return book;
    }

    private Book updateBookWithNewCoverImage() {
        book.setCoverImgPath(mediaSaver.getSavedImageFileName());
        return updateBook();
    }

    private void loadBitmapFromCoverImageUri() {
        Picasso.get().load(newCoverImageUri).into(new Target() {
            @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                mediaSaver = new MediaSaver(EditBookActivity.this);
                if (mediaSaver.saveImageFromBitmap(bitmap)) {
                    repository.editBook(updateBookWithNewCoverImage());
                    startBooksActivity();
                }
            }

            @Override public void onBitmapFailed(Exception e, Drawable errorDrawable) { }

            @Override public void onPrepareLoad(Drawable placeHolderDrawable) {}
        });
    }
}