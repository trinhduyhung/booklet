package com.javaspace.booklet;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Book.class}, version = 1)
@TypeConverters({Converters.class})
abstract class AppDatabase extends RoomDatabase {

    public abstract BookDao bookStore();

    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context, AppDatabase.class, "book_store").build();
                }
            }
        }

        return INSTANCE;

    }
}
