package com.javaspace.booklet;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TrackIdleTimeWorker extends Worker {

    private static final String TAG = "TrackIdleTimeWorker";

    public TrackIdleTimeWorker(@NonNull Context context,
                               @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "doWork: I'm working");

        List<Book> books = InMemoryBookStore.getInstance().getAddedBooks();

        StringBuilder booksToRead = new StringBuilder();

        for (Book book : books) {
            Date pausedTime = book.getPausedTime();
            Date now = new Date();
            long differenceInMillis = now.getTime() - pausedTime.getTime();

            // TODO read time interval from SharedPreference later

            long differenceInMinutes = TimeUnit.MINUTES.convert(differenceInMillis, TimeUnit.MILLISECONDS);

            // if we pause reading for 1 minute, we will be notified
            if (differenceInMinutes <= 10) {
                booksToRead.append(book.getTitle());
                booksToRead.append("\n");
            }
        }

        if (!booksToRead.toString().isEmpty()) {
            NotificationUtil.showWarningNotification(getApplicationContext(), booksToRead.toString());
        }

        return Result.success();
    }

}
