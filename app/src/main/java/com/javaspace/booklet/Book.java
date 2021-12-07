package com.javaspace.booklet;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "book")
public class Book {

    // Reading, Never started, Paused, Finished
    public enum ReadingStatus {
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

    //private static int count;

    @PrimaryKey(autoGenerate=true)
    private int id;

    private String title;
    
    private String edition;

    private String author;

    @ColumnInfo(name = "published_time")
    private String publishedTime;

    private int pages;              // number of pages

    @ColumnInfo(name = "cover_img_path")
    private String coverImgPath;
    
    private String summary;

    @ColumnInfo(name = "started_time")
    private Date startedTime;       // started reading on this day

    @ColumnInfo(name = "spent_time")
    private long spentTime;

    @ColumnInfo(name = "current_page")
    private int currentPage;       // paused at this page

    @ColumnInfo(name = "paused_time")
    private Date pausedTime;        // when was the last pause

    @ColumnInfo(name = "finished_time")
    private Date finishedTime;      // when did you finish

    private ReadingStatus status = ReadingStatus.NEVER_STARTED;

    /**
     * Should be called after calling Book's constructor and when the In-memory book store is being
     * used
     */
    public void initializeId() {
        //id = count++;
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

    public String getPublishedTime() {
        return publishedTime;
    }

    public int getPages() {
        return pages;
    }

    public int getCurrentPage() {
        return currentPage;
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

    public Date getFinishedTime() {
        return finishedTime;
    }

   public ReadingStatus getStatus() {
        return status;
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

    public void setPublishedTime(String publishedTime) {
        this.publishedTime = publishedTime;
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

    public void setStartedTime(Date startedTime) {
        this.startedTime = startedTime;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setPausedTime(Date pausedTime) {
        this.pausedTime = pausedTime;
    }

    public void setFinishedTime(Date finishedTime) {
        this.finishedTime = finishedTime;
    }

    public void setStatus(ReadingStatus status) {
        this.status = status;
    }

    public String getReadingStatus() {
        return status.toString();
    }

    public long getSpentTime() {
        return spentTime;
    }

    public long getRealSpentTime() {
        if (isReading() && spentTime == 0) {

        }
        return 0;
    }

    public void setSpentTime(long spentTime) {
        this.spentTime = spentTime;
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
        return String.format("%s. (%s/%s)", status.toString(), currentPage, pages);
    }

    public void copy(Book book) {
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.edition = book.getEdition();
        this.pages = book.getPages();
        this.publishedTime = book.getPublishedTime();
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
                ", year=" + publishedTime +
                ", coverImgPath='" + coverImgPath + '\'' +
                '}';
    }
}
