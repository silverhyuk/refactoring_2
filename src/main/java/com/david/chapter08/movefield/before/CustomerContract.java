package com.david.chapter08.movefield.before;

import java.time.LocalDate;

public class CustomerContract {
    private LocalDate startDate;

    public CustomerContract(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }
}