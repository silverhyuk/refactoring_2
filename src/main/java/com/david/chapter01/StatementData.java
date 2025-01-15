package com.david.chapter01;

import com.david.chapter01.Invoice.Performance;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StatementData {
    private final Invoice invoice;
    private final Map<String, Play> plays;
    private final String customer;
    private final List<Performance> performances;
    private final double totalAmount;
    private final int totalVolumeCredits;


    public StatementData(Invoice invoice, Map<String, Play> plays) {
        this.plays = plays;
        this.invoice = invoice;
        this.customer = invoice.getCustomer();
        this.performances = invoice.getPerformances().stream()
            .map(this::enrichPerformance)
            .collect(Collectors.toList());
        this.totalAmount = totalAmount();
        this.totalVolumeCredits = totalVolumeCredits();
    }

    public static StatementData create(Invoice invoice, Map<String, Play> plays) {
        return new StatementData(invoice, plays);
    }

    public EnrichedPerformance enrichPerformance(Invoice.Performance aPerformance) {
        PerformanceCalculator calculator = PerformanceCalculator.create(aPerformance, playFor(aPerformance));
        EnrichedPerformance result = EnrichedPerformance.create(aPerformance);
        result.setPlay(calculator.getPlay());
        result.setAmount(calculator.getAmount());
        result.setVolumeCredits(calculator.getVolumeCredits());
        return result;
    }

    public String getCustomer() {
        return customer;
    }

    public List<Performance> getPerformances() {
        return performances;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public int getTotalVolumeCredits() {
        return totalVolumeCredits;
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
        PerformanceCalculator calculator = PerformanceCalculator.create(aPerformance, playFor(aPerformance));
        return calculator.getAmount();
    }
}
