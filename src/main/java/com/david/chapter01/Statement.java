package com.david.chapter01;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

/**
 * 청구서 출력
 */
public class Statement {

    private final Invoice invoice;
    private final Map<String, Play> plays;

    public Statement(Invoice invoice, Map<String, Play> plays) {
        this.invoice = invoice;
        this.plays = plays;
    }

    public  String statement() {
        double totalAmount = 0;
        int volumeCredits = 0;

        StringBuilder result = new StringBuilder(String.format("청구내역 (고객명: %s)\n", invoice.getCustomer()));


        for (Invoice.Performance perf : invoice.getPerformances()) {
            volumeCredits += volumnCreditsFor(perf);

            // 청구 내역 출력
            result.append(String.format("%s: %s %d석\n", playFor(perf).getName(), usd(amountFor(perf)), perf.getAudience()));
            totalAmount += amountFor(perf);
        }

        result.append(String.format("총액 %s\n", usd(totalAmount)));
        result.append(String.format("적립 포인트 %d점\n", volumeCredits));

        return result.toString();
    }

    private String usd(double aNumber) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
        return formatter.format(aNumber / 100);
    }

    private int volumnCreditsFor(Invoice.Performance aPerformance) {
        int result = 0;
        // 포인트 적립
        result += Math.max(aPerformance.getAudience() - 30, 0);

        // 희극 관객 5명마다 추가 포인트
        if ("comedy".equals(playFor(aPerformance).getType())) {
            result += (int) Math.floor((double) aPerformance.getAudience() / 5);
        }

        return result;
    }

    private Play playFor(Invoice.Performance aPerformance) {
        return plays.get(aPerformance.getPlayID());
    }

    private double amountFor(Invoice.Performance aPerformance) {
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

