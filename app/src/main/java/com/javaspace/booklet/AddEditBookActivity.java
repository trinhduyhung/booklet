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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class AddEditBookActivity extends AppCompatActivity {

    private static final String TAG = "AddEditBookActivity";
    public static final int PICK_IMAGE = 1;
    public static final String IMAGE_COVER_URI_KEY = "IMAGE_COVER_KEY";

    protected Repository repository;

    protected EditText title;
    protected EditText edition;
    protected EditText author;
    protected EditText publisher;
    protected EditText published;
    protected EditText pages;
    protected EditText summary;
    protected ImageView cover;
    protected Uri newCoverImageUri;
    protected MediaSaver mediaSaver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_book);

        repository = new Repository(getApplication());

        title = findViewById(R.id.edt_title);
        edition = findViewById(R.id.edt_edition);
        author = findViewById(R.id.edt_author);
        published = findViewById(R.id.edt_published_year);
        publisher = findViewById(R.id.edt_publisher);
        pages = findViewById(R.id.edt_pages);
        summary = findViewById(R.id.edt_summary);
        cover = findViewById(R.id.img_cover);

        // TODO look back
        if (savedInstanceState != null && savedInstanceState.getString(IMAGE_COVER_URI_KEY) != null) {
            newCoverImageUri = Uri.parse(savedInstanceState.getString(IMAGE_COVER_URI_KEY));
            cover.setImageURI(newCoverImageUri);
        }

        cover.setOnClickListener(v -> pickCoverImage());
    }

    protected boolean shouldAddBook() {
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
        } else if (publisher.getText().toString().isEmpty()) {
            shouldAddBook = false;
            author.setError(getString(R.string.publisher_not_empty));
        } else if (published.getText().toString().isEmpty()) {
            shouldAddBook = false;
            published.setError(getString(R.string.year_not_empty));
        } else if (pages.getText().toString().isEmpty()) {
            shouldAddBook = false;
            pages.setError(getString(R.string.page_not_empty));
        } else if (summary.getText().toString().isEmpty()) {
            shouldAddBook = false;
            summary.setError(getString(R.string.summary_not_empty));
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
            loadThenDisplayBitmap(data.getData());
        }
    }

    private void loadThenDisplayBitmap(Uri uri) {
        Picasso.get().load(uri).into(new Target() {
            @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                newCoverImageUri = uri;
                cover.setImageBitmap(bitmap);
            }

            @Override public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                Log.e(TAG, "onBitmapFailed: ", e);
            }

            @Override public void onPrepareLoad(Drawable placeHolderDrawable) {
                Log.e(TAG, "onPrepareLoad");
            }
        });
    }

    @Override protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (userPickedCoverImage()) {
            outState.putString(IMAGE_COVER_URI_KEY, newCoverImageUri.toString());
        }
    }

    protected boolean userPickedCoverImage() {
        return newCoverImageUri != null;
    }

    protected void startBooksActivity() {
        Intent intent = new Intent(this, BooksActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
