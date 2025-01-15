package com.david.chapter01;

import com.david.chapter01.Invoice.Performance;

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
        return Math.max(performance.getAudience() - 30, 0) + (int) Math.floor(performance.getAudience() / 5);
    }
}
