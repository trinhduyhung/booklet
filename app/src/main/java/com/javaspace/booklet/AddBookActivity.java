package com.javaspace.booklet;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class AddBookActivity extends AppCompatActivity {

    public static final int PICK_IMAGE = 1;
    public static final String IMAGE_COVER_KEY = "IMAGE_COVER_KEY";
    private final InMemoryBookStore bookStore = InMemoryBookStore.getInstance();
    private Uri coverImage = null;
    private ImageView imgCover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        imgCover = findViewById(R.id.img_cover);

        if (savedInstanceState != null) {
            String cover = savedInstanceState.getString(IMAGE_COVER_KEY);
            coverImage = Uri.parse(cover);
            imgCover.setImageURI(coverImage);
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
                book.setYear(Integer.parseInt(year.getText().toString()));
                book.setPages(Integer.parseInt(pages.getText().toString()));
                book.setCoverImgPath(coverImage.toString());
                book.setSummary(summary.getText().toString());
                bookStore.addBook(book);

                startActivity(new Intent(this, BooksActivity.class));
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
        } else if (coverImage == null) {
            shouldAddBook = false;
            summary.setError(getString(R.string.cover_not_empty));
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
            coverImage = data.getData();
            imgCover.setImageURI(coverImage);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (coverImage != null) {
            outState.putString(IMAGE_COVER_KEY, coverImage.toString());
        }
    }
}