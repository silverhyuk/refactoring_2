package com.david.chapter01;

import com.david.chapter01.Invoice.Performance;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class StatementData {
    private Invoice invoice;
    private Map<String, Play> plays;
    private String customer;
    private List<Performance> performances;
    private double totalAmount;
    private int totalVolumeCredits;

    public StatementData(Invoice invoice, Map<String, Play> plays) {
        this.plays = plays;
        this.invoice = invoice;
        this.customer = invoice.getCustomer();
        this.performances = invoice.getPerformances();
        this.totalAmount = totalAmount();
        this.totalVolumeCredits = totalVolumeCredits();
    }

    public String getCustomer() {
        return customer;
    }

    public List<Performance> getPerformances() {
        return performances;
    }


    public double totalAmount() {
        double result = 0;
        for (Invoice.Performance perf : invoice.getPerformances()) {
            result += amountFor(perf);
        }
        return result;
    }

    public int totalVolumeCredits() {
        int result = 0;
        for (Invoice.Performance perf : invoice.getPerformances()) {
            result += volumnCreditsFor(perf);
        }
        return result;
    }

    public int volumnCreditsFor(Invoice.Performance aPerformance) {
        int result = 0;
        // 포인트 적립
        result += Math.max(aPerformance.getAudience() - 30, 0);

        // 희극 관객 5명마다 추가 포인트
        if ("comedy".equals(playFor(aPerformance).getType())) {
            result += (int) Math.floor((double) aPerformance.getAudience() / 5);
        }

        return result;
    }

    public Play playFor(Invoice.Performance aPerformance) {
        return plays.get(aPerformance.getPlayID());
    }

    public double amountFor(Invoice.Performance aPerformance) {
        double result = 0;

        switch (playFor(aPerformance).getType()) {
            case "tragedy":
                result = 40000;
                if (aPerformance.getAudience() > 30) {
                    result += 1000 * (aPerformance.getAudience() - 30);
                }
                break;

            case "comedy":
                result = 30000;
                if (aPerformance.getAudience() > 20) {
                    result += 10000 + 500 * (aPerformance.getAudience() - 20);
                }
                result += 300 * aPerformance.getAudience();
                break;

            default:
                throw new IllegalArgumentException("알 수 없는 장르: " + playFor(aPerformance).getType());
        }
        return result;
    }
}
