package com.example.saguisa_librarybooktracker;

public abstract class Books {
    private String bookCode;
    private String title;
    private String author;
    private int numOfDaysBorrowed;
    private boolean isBorrowed;

    public Books(String bookCode, String title, String author, int numOfDaysBorrowed, boolean isBorrowed) {
        this.bookCode = bookCode;
        this.title = title;
        this.author = author;
        this.numOfDaysBorrowed = numOfDaysBorrowed;
        this.isBorrowed = isBorrowed;
    }

    public String getBookCode() {
        return bookCode;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getNumOfDaysBorrowed() {
        return numOfDaysBorrowed;
    }

    public boolean isBorrowed() {
        return isBorrowed;
    }

    public void setBorrowed(boolean borrowed) {
        isBorrowed = borrowed;
    }

    public abstract double computeCost();
}
