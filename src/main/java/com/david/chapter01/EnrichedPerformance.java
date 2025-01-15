package com.david.chapter01;

public class EnrichedPerformance extends Invoice.Performance {

    private Play play;
    private double amount;
    private int volumeCredits;

    public EnrichedPerformance(Invoice.Performance performance) {
        super(performance.getPlayID(), performance.getAudience());
    }

    public static EnrichedPerformance create(Invoice.Performance performance) {
        return new EnrichedPerformance(performance);
    }

    public Play getPlay() {
        return play;
    }

    public void setPlay(Play play) {
        this.play = play;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getVolumeCredits() {
        return volumeCredits;
    }

    public void setVolumeCredits(int volumeCredits) {
        this.volumeCredits = volumeCredits;
    }
}
