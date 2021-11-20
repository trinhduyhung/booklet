package com.javaspace.booklet;

public class Book {

    private static int count;
    private final int id;
    private String title;
    private String edition;
    private String author;
    private int year;           // year of publication
    private int pages;          // number of pages
    private String coverImgPath;
    private String summary;

    public Book() {
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
