package com.javaspace.booklet;

import androidx.annotation.NonNull;

import java.util.Date;

public class Book {

    // Reading, Never started, Paused, Finished
    enum ReadingStatus {
        READING("I am reading"),
        NEVER_STARTED("I have not started yet"),
        PAUSING("I will be back soon"),
        FINISHED("I have finished");

        String status;

        ReadingStatus(String status) {
            this.status = status;
        }

        @NonNull
        @Override
        public String toString() {
            return status;
        }
    }

    private static int count;
    private int id;
    private String title;
    private String edition;
    private String author;
    private int year;               // year of publication
    private int pages;              // number of pages
    private String coverImgPath;
    private String summary;
    private Date startedTime;       // started reading on this day
    private int pausedAtPage;       // paused at this page
    private Date pausedTime;        // when was the last pause
    private Date finishedTime;      // when did you finish
    private ReadingStatus status = ReadingStatus.NEVER_STARTED;

    /**
     * Should be called after calling Book's constructor and when the In-memory book store is being
     * used
     */
    public void initializeId() {
        id = count++;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getEdition() {
        return edition;
    }

    public String getAuthor() {
        return author;
    }

    public int getYear() {
        return year;
    }

    public int getPages() {
        return pages;
    }

    public String getCoverImgPath() {
        return coverImgPath;
    }

    public String getSummary() {
        return summary;
    }

    public Date getPausedTime() {
        return pausedTime;
    }

    public void setId(int bookId) {
        this.id = bookId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setCoverImgPath(String coverImgPath) {
        this.coverImgPath = coverImgPath;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getStatus() {
        return status.toString();
    }

    public void startReading() {
        status = ReadingStatus.READING;
        startedTime = new Date();
    }

    public void pauseReading() {
        status = ReadingStatus.PAUSING;
        pausedTime = new Date();
    }

    public void finishReading() {
        status = ReadingStatus.FINISHED;
        finishedTime = new Date();
    }

    public boolean isReading() {
        return status == ReadingStatus.READING;
    }

    public boolean isFinished() {
        return status == ReadingStatus.FINISHED;
    }

    public boolean isPaused() {
        return status == ReadingStatus.PAUSING;
    }

    public boolean hasNotStartedReadingYet() {
        return status == ReadingStatus.NEVER_STARTED;
    }

    public Date getStartedTime() {
        return startedTime;
    }

    public String getProgress() {
        return String.format("%s. (%s/%s)", status.toString(), pausedAtPage, pages);
    }

    public void copy(Book book) {
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.edition = book.getEdition();
        this.pages = book.getPages();
        this.year = book.getYear();
        this.summary = book.getSummary();
        this.coverImgPath = book.getCoverImgPath();
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", edition=" + edition +
                ", author='" + author + '\'' +
                ", year=" + year +
                ", coverImgPath='" + coverImgPath + '\'' +
                '}';
    }
}
