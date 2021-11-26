package com.javaspace.booklet;

import static androidx.recyclerview.widget.LinearLayoutManager.VERTICAL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class BooksActivity extends AppCompatActivity {

    private final InMemoryBookStore bookStore = InMemoryBookStore.getInstance();
    public final static String SELECTED_BOOK_ID = "com.example.booklet.book_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        RecyclerView listBooks = findViewById(R.id.list_books);
        View txtNoBooks = findViewById(R.id.txt_no_books);

        if (bookStore.isEmpty()) {
            txtNoBooks.setVisibility(View.VISIBLE);
            listBooks.setVisibility(View.GONE);
        } else {
            txtNoBooks.setVisibility(View.GONE);
            listBooks.setVisibility(View.VISIBLE);

            listBooks.setLayoutManager(new LinearLayoutManager(this, VERTICAL, false));
            BookAdapter adapter = new BookAdapter(bookStore.getAddedBooks(), new OnItemClickListener() {
                @Override
                public void onClick(int bookId) {
                    Intent intent = new Intent(BooksActivity.this, BookDetailActivity.class);
                    intent.putExtra(SELECTED_BOOK_ID, bookId);
                    startActivity(intent);
                }
            });
            listBooks.setAdapter(adapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.books_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_menu_item) {
            startActivity(new Intent(this, AddBookActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    interface OnItemClickListener {
        void onClick(int bookId);
    }

    static class BookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        List<Book> books;
        OnItemClickListener listener;

        public BookAdapter(List<Book> books, OnItemClickListener listener) {
            this.books = books;
            this.listener = listener;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView = inflater.inflate(R.layout.book_item, parent, false);
            return new BookViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            Book book = books.get(position);
            BookViewHolder vh = ((BookViewHolder) holder);
            vh.bind(book);
            vh.itemView.setOnClickListener(v -> listener.onClick(book.getId()));
        }

        @Override
        public int getItemCount() {
            return books.size();
        }

        static class BookViewHolder extends RecyclerView.ViewHolder {

            TextView txtTitle;
            TextView txtAuthorEdition;
            TextView txtPublishedYear;
            TextView txtProgress;
            ImageView imgCover;
            View itemView;

            public void bind(Book book) {
                txtTitle.setText(book.getTitle());
                txtAuthorEdition.setText(String.format("%s, %s", book.getAuthor(), book.getEdition()));
                txtPublishedYear.setText(String.format("Published in %s", book.getYear()));
                imgCover.setImageURI(Uri.parse(book.getCoverImgPath()));
                txtProgress.setText(book.getProgress());
            }

            public BookViewHolder(@NonNull View itemView) {
                super(itemView);
                this.itemView = itemView;
                txtTitle = itemView.findViewById(R.id.txt_item_title);
                txtAuthorEdition = itemView.findViewById(R.id.txt_item_author_edition);
                txtPublishedYear = itemView.findViewById(R.id.txt_item_published_year);
                txtProgress = itemView.findViewById(R.id.txt_item_progress);
                imgCover = itemView.findViewById(R.id.img_item_cover);
            }
        }
    }
}