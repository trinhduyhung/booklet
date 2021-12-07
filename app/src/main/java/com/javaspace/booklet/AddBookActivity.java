package com.javaspace.booklet;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class AddBookActivity extends AppCompatActivity {

    private static final String TAG = "AddBookActivity";

    public static final int PICK_IMAGE = 1;
    public static final String IMAGE_COVER_KEY = "IMAGE_COVER_KEY";
    private Repository repository;
    private Uri coverImageUri = null;
    private ImageView imgCover;
    private MediaSaver mediaSaver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        repository = new Repository(getApplication());

        imgCover = findViewById(R.id.img_cover);

        if (savedInstanceState != null) {
            String cover = savedInstanceState.getString(IMAGE_COVER_KEY);
            coverImageUri = Uri.parse(cover);
            imgCover.setImageURI(coverImageUri);
        }

        EditText title = findViewById(R.id.edt_title);
        EditText edition = findViewById(R.id.edt_edition);
        EditText author = findViewById(R.id.edt_author);
        EditText year = findViewById(R.id.edt_published_year);
        EditText pages = findViewById(R.id.edt_pages);
        EditText summary = findViewById(R.id.edt_summary);

        imgCover = findViewById(R.id.img_cover);
        imgCover.setOnClickListener(v -> pickCoverImage());

        findViewById(R.id.btn_add_book).setOnClickListener(v -> {

            if (shouldAddBook(title, edition, author, year, pages, summary)) {
                Book book = new Book();
                book.setTitle(title.getText().toString());
                book.setEdition(edition.getText().toString());
                book.setAuthor(author.getText().toString());
                book.setPublishedTime(year.getText().toString());
                book.setPages(Integer.parseInt(pages.getText().toString()));
                book.setCoverImgPath(mediaSaver.getFileName());
                book.setSummary(summary.getText().toString());
                repository.addBook(book);

                Intent intent = new Intent(this, BooksActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

        });

    }
    
    private boolean shouldAddBook(EditText title, EditText edition, EditText author, EditText year,
                                  EditText pages, EditText summary) {
        boolean shouldAddBook = true;
        if (title.getText().toString().isEmpty()) {
            shouldAddBook = false;
            title.setError(getString(R.string.title_not_empty));
        } else if (edition.getText().toString().isEmpty()) {
            shouldAddBook = false;
            edition.setError(getString(R.string.edition_not_empty));
        } else if (author.getText().toString().isEmpty()) {
            shouldAddBook = false;
            author.setError(getString(R.string.author_not_empty));
        } else if (year.getText().toString().isEmpty()) {
            shouldAddBook = false;
            year.setError(getString(R.string.year_not_empty));
        } else if (pages.getText().toString().isEmpty()) {
            shouldAddBook = false;
            pages.setError(getString(R.string.page_not_empty));
        } else if (summary.getText().toString().isEmpty()) {
            shouldAddBook = false;
            summary.setError(getString(R.string.summary_not_empty));
        } else if (coverImageUri == null) {
            shouldAddBook = false;
            Toast.makeText(this, getText(R.string.cover_not_empty), Toast.LENGTH_SHORT).show();
        }
        return shouldAddBook;
    }

    private void pickCoverImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_cover_image)), PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            getBitmapFromUri(data.getData());
        }
    }

    private void getBitmapFromUri(Uri uri) {
        Picasso.get().load(uri).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                mediaSaver = new MediaSaver(AddBookActivity.this);
                if (mediaSaver.saveImageFromBitmap(bitmap)) {
                    coverImageUri = uri;
                    imgCover.setImageURI(coverImageUri);
                }
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                Log.e(TAG, "onBitmapFailed: ", e);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                Log.e(TAG, "onPrepareLoad");
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (coverImageUri != null) {
            outState.putString(IMAGE_COVER_KEY, coverImageUri.toString());
        }
    }
}