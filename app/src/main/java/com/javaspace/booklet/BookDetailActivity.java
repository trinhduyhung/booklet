package com.javaspace.booklet;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class BookDetailActivity extends AppCompatActivity {

    private Book book;
    private int bookId;
    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        bookId = getIntent().getIntExtra(BooksActivity.SELECTED_BOOK_ID, -1);
        repository = new Repository(getApplication());

        book = repository.getBookById(bookId);

        if (bookId == -1 || book == null) {
            Toast.makeText(this, "Book not found", Toast.LENGTH_LONG).show();
        } else {

            ((TextView) findViewById(R.id.txt_book_title)).setText(book.getTitle());
            ((TextView) findViewById(R.id.txt_book_author_edition)).
                    setText(String.format("%s, %s", book.getAuthor(), book.getEdition()));
            ((TextView) findViewById(R.id.txt_book_pages)).
                    setText(String.format("%s pages long", book.getPages()));

            ((TextView) findViewById(R.id.txt_book_published_year)).
                    setText(String.format("Published in %s", book.getPublishedTime()));
            ((TextView) findViewById(R.id.txt_book_summary)).setText(book.getSummary());

            displayReadingStatus();

            if (book.hasNotStartedReadingYet()) {
                findViewById(R.id.txt_book_started_date).setVisibility(View.INVISIBLE);
                findViewById(R.id.txt_book_finished_date).setVisibility(View.INVISIBLE);
            } else {
                displayStartedTime();
                if (book.isFinished()) {
                    displayFinishedTime();
                }
            }

            ImageView imgCover = ((ImageView) findViewById(R.id.img_book_cover));
            Picasso.get().load(new MediaSaver(this).getFile(book.getCoverImgPath())).into(imgCover);



        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.book_detail_menu, menu);
        if (book.isReading()) {
            menu.removeItem(R.id.start_reading_menu_item);
        } else if (book.isFinished()) {
            menu.removeItem(R.id.finish_reading_menu_item);
            menu.removeItem(R.id.pause_reading_menu_item);
        } else if(book.hasNotStartedReadingYet()) {
            menu.removeItem(R.id.finish_reading_menu_item);
            menu.removeItem(R.id.pause_reading_menu_item);
        } else {
            menu.removeItem(R.id.finish_reading_menu_item);
            menu.removeItem(R.id.pause_reading_menu_item);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.start_reading_menu_item) {
            onStartReadingMenuItemClicked();
        } else if (item.getItemId() == R.id.pause_reading_menu_item) {
            onPauseReadingMenuItemClicked();
        } else if (item.getItemId() == R.id.finish_reading_menu_item) {
            onFinishReadingMenuItemClicked();
        } else if (item.getItemId() == R.id.edit_menu_item) {
            Intent intent = new Intent(BookDetailActivity.this, EditBookActivity.class);
            intent.putExtra(BooksActivity.SELECTED_BOOK_ID, bookId);
            startActivity(intent);
        } else if (item.getItemId() == R.id.remove_menu_item) {
            repository.remove(bookId);

            Intent intent = new Intent(this, BooksActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private static class Stopwatch {

        private long startTime = 0;
        private long stopTime = 0;
        private boolean running = false;

        public void start() {
            this.startTime = System.currentTimeMillis();
            this.running = true;
        }

        public void stop() {
            this.stopTime = System.currentTimeMillis();
            this.running = false;
        }

        // elapsed time in milliseconds
        public long getElapsedTime() {
            if (running) {
                return System.currentTimeMillis() - startTime;
            }
            return stopTime - startTime;
        }

        // elapsed time in seconds
        public long getElapsedTimeSecs() {
            if (running) {
                return ((System.currentTimeMillis() - startTime) / 1000);
            }
            return ((stopTime - startTime) / 1000);
        }
    }

    private Stopwatch stopwatch;

    private void onStartReadingMenuItemClicked() {
        book.startReading();
        invalidateOptionsMenu();
        displayReadingStatus();
        displayStartedTime();
        stopwatch.start();
    }

    private void onPauseReadingMenuItemClicked() {
        book.pauseReading();
        startTracking();
        invalidateOptionsMenu();
        displayReadingStatus();
        stopwatch.stop();
        book.setSpentTime(stopwatch.getElapsedTime() + book.getSpentTime());
    }

    private void onFinishReadingMenuItemClicked() {
        book.finishReading();
        invalidateOptionsMenu();
        displayReadingStatus();
        displayFinishedTime();
        if (stopwatch.running) {
            stopwatch.stop();
            book.setSpentTime(stopwatch.getElapsedTime() + book.getSpentTime());
        }
    }

    private void displayReadingStatus() {
        ((TextView) findViewById(R.id.txt_book_status)).setText(book.getReadingStatus());
    }

    private void displayStartedTime() {
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String startedTime = dt.format(book.getStartedTime());
        TextView txtStartedDate = findViewById(R.id.txt_book_started_date);
        txtStartedDate.setVisibility(View.VISIBLE);
        txtStartedDate.setText(String.format("Started on %s", startedTime));
    }

    private void displayFinishedTime() {
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String startedTime = dt.format(book.getStartedTime());
        TextView txtStartedDate = findViewById(R.id.txt_book_finished_date);
        txtStartedDate.setVisibility(View.VISIBLE);
        txtStartedDate.setText(String.format("Finished on %s", startedTime));
    }

    private void startTracking() {
        WorkManager wm = WorkManager.getInstance(getApplicationContext());

        PeriodicWorkRequest.Builder builder =
                new PeriodicWorkRequest.Builder(TrackIdleTimeWorker.class, 15 * 60, TimeUnit.SECONDS);

        PeriodicWorkRequest request = builder.build();

        wm.enqueueUniquePeriodicWork("tracking-idle-time", ExistingPeriodicWorkPolicy.KEEP, request);
    }

}