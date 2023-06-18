package com.example.azure.finalprojectazure.models;

import java.time.LocalDate;


public class CheckinForm {
    private LocalDate checkoutDate;
    private LocalDate checkinDate;
    private int numberOfGuests;

    public void setCheckoutDate(LocalDate checkoutDate) {
        this.checkoutDate = checkoutDate;
    }
    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }

    public void setcheckinDate(LocalDate checkinDate) {
        this.checkinDate = checkinDate;
    }
    public LocalDate getcheckinDate() {
        return checkinDate;
    }

    public void setNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }
    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
