package com.example.saguisa_librarybooktracker;

public class RegularBook extends Books{

    public RegularBook(String bookCode, String title, String author, int numOfDaysBorrowed, boolean isBorrowed) {
        super(bookCode, title, author, numOfDaysBorrowed, isBorrowed);
    }

    @Override
    public double computeCost() {
        return 20.00 * getNumOfDaysBorrowed();
    }
}
