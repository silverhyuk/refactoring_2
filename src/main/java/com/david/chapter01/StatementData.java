package com.david.chapter01;

import com.david.chapter01.Invoice.Performance;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StatementData {
    private final Invoice invoice;
    private final Map<String, Play> plays;
    private final String customer;
    private final List<EnrichedPerformance> performances;
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

    public List<EnrichedPerformance> getPerformances() {
        return performances;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public int getTotalVolumeCredits() {
        return totalVolumeCredits;
    }

    public double totalAmount() {
        return this.performances.stream()
            .mapToDouble(EnrichedPerformance::getAmount)
            .sum();
    }

    public int totalVolumeCredits() {
        return this.performances.stream()
            .mapToInt(EnrichedPerformance::getVolumeCredits)
            .sum();
    }

    public Play playFor(Invoice.Performance aPerformance) {
        return plays.get(aPerformance.getPlayID());
    }

    public double amountFor(Invoice.Performance aPerformance) {
        PerformanceCalculator calculator = PerformanceCalculator.create(aPerformance, playFor(aPerformance));
        return calculator.getAmount();
    }
}
