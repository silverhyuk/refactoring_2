package com.david.chapter01;

import java.util.List;

/**
 * Invoice
 */
public class Invoice {
    private String customer;
    private List<Performance> performances;

    public String getCustomer() {
        return customer;
    }

    public List<Performance> getPerformances() {
        return performances;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public void setPerformances(List<Performance> performances) {
        this.performances = performances;
    }

    public static class Performance {
        private String playID;
        private int audience;

        public Performance() {
        }

        public Performance(String playID, int audience) {
            this.playID = playID;
            this.audience = audience;
        }

        public String getPlayID() {
            return playID;
        }

        public int getAudience() {
            return audience;
        }

    }

}
