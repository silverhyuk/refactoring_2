package com.david.chapter08.movefield.after;

import java.time.LocalDate;

public class CustomerContract {
    private LocalDate startDate;
    private double discountRate; // 할인율 필드 추가

    public CustomerContract(LocalDate startDate, double discountRate) {
        this.startDate = startDate;
        this.discountRate = discountRate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }
}