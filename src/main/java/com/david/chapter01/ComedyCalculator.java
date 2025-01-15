package com.david.chapter01;

public class ComedyCalculator extends PerformanceCalculator {

    public ComedyCalculator(Invoice.Performance performance, Play play) {
        super(performance, play);
    }

    @Override
    public double getAmount() {
        double result = 30000;
        if (performance.getAudience() > 20) {
            result += 10000 + 500 * (performance.getAudience() - 20);
        }
        result += 300 * performance.getAudience();
        return result;
    }

    @Override
    public int getVolumeCredits() {
        return super.getVolumeCredits() + (int) Math.floor((double) performance.getAudience() / 5);
    }
}
