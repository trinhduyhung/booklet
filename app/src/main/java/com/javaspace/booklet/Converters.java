package com.javaspace.booklet;

import androidx.room.TypeConverter;

import java.util.Date;

public class Converters {

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static int fromReadingStatusToInt(Book.ReadingStatus value) {
        return value.ordinal();
    }

    @TypeConverter
    public static Book.ReadingStatus fromIntToReadingStatus(int value) {
        return (Book.ReadingStatus.values()[value]);
    }
}
