package com.example.saguisa_librarybooktracker;

public class PremiumBook extends Books{

    public PremiumBook(String bookCode, String title, String author, int numOfDaysBorrowed, boolean isBorrowed) {
        super(bookCode, title, author, numOfDaysBorrowed, isBorrowed);
    }

    @Override
    public double computeCost() {
        double cost = 50.00; // per day
        if (getNumOfDaysBorrowed() > 7) {
            cost += (getNumOfDaysBorrowed() - 7) * 25.00;
        }
        return cost * getNumOfDaysBorrowed();
    }

}
