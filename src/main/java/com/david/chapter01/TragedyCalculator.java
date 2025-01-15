package com.david.chapter01;

public class TragedyCalculator extends PerformanceCalculator {


    public TragedyCalculator(Invoice.Performance performance, Play play) {
        super(performance, play);
    }

    @Override
    public double getAmount() {
        double result = 40000;
        if (performance.getAudience() > 30) {
            result += 1000 * (performance.getAudience() - 30);
        }
        return result;
    }

    @Override
    public int getVolumeCredits() {
        return Math.max(performance.getAudience() - 30, 0);
    }
}
