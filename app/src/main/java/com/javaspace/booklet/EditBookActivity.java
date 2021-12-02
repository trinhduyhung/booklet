package com.javaspace.booklet;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

// TODO AddBookActivity and EditBookActivity have similar implementation
public class EditBookActivity extends AppCompatActivity {

    public static final int PICK_IMAGE = 1;
    public static final String IMAGE_COVER_KEY = "IMAGE_COVER_KEY";
    private final BookStore bookStore = InMemoryBookStore.getInstance();
    private Uri coverImage = null;
    private ImageView imgCover;

    private EditText title;
    private EditText edition;
    private EditText author;
    private EditText year;
    private EditText pages;
    private EditText summary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        title = findViewById(R.id.edt_title);
        edition = findViewById(R.id.edt_edition);
        author = findViewById(R.id.edt_author);
        year = findViewById(R.id.edt_published_year);
        pages = findViewById(R.id.edt_pages);
        summary = findViewById(R.id.edt_summary);
        imgCover = findViewById(R.id.img_cover);

        ((Button) findViewById(R.id.btn_add_book)).setText("Update book");

        int bookId = getIntent().getIntExtra(BooksActivity.SELECTED_BOOK_ID, -1);
        fillTheBookDetail(bookId);

        if (savedInstanceState != null) {
            String cover = savedInstanceState.getString(IMAGE_COVER_KEY);
            coverImage = Uri.parse(cover);
            imgCover.setImageURI(coverImage);
        }

        imgCover.setOnClickListener(v -> pickCoverImage());

        findViewById(R.id.btn_add_book).setOnClickListener(v -> {

            if (shouldAddBook(title, edition, author, year, pages, summary)) {
                Book book = new Book();
                book.setId(bookId);
                book.setTitle(title.getText().toString());
                book.setEdition(edition.getText().toString());
                book.setAuthor(author.getText().toString());
                book.setYear(Integer.parseInt(year.getText().toString()));
                book.setPages(Integer.parseInt(pages.getText().toString()));
                book.setCoverImgPath(coverImage.toString());
                book.setSummary(summary.getText().toString());
                bookStore.editBook(book);

                startActivity(new Intent(this, BooksActivity.class));
            }

        });

    }

    private void fillTheBookDetail(int bookId) {
        Book book = bookStore.getBookById(bookId);
        title.setText(book.getTitle());
        edition.setText(book.getEdition());
        author.setText(book.getAuthor());
        year.setText(String.valueOf(book.getYear()));
        pages.setText(String.valueOf(book.getPages()));
        summary.setText(book.getSummary());
        coverImage = Uri.parse(book.getCoverImgPath());
        imgCover.setImageURI(coverImage);
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