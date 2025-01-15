package com.david.chapter01;

public class PerformanceCalculator {
    private Invoice.Performance performance;
    private Play play;

    public PerformanceCalculator(Invoice.Performance performance, Play play) {
        this.performance = performance;
        this.play = play;
    }

    public static PerformanceCalculator create(Invoice.Performance performance, Play play) {
        return new PerformanceCalculator(performance, play);
    }

    public Play getPlay() {
        return play;
    }

    public double getAmount() {
        double result = 0;
        switch (play.getType()) {
            case "tragedy":
                result = 40000;
                if (performance.getAudience() > 30) {
                    result += 1000 * (performance.getAudience() - 30);
                }
                break;
            case "comedy":
                result = 30000;
                if (performance.getAudience() > 20) {
                    result += 10000 + 500 * (performance.getAudience() - 20);
                }
                result += 300 * performance.getAudience();
                break;
            default:
                throw new IllegalArgumentException("알 수 없는 장르: " + play.getType());
        }
        return result;
    }

    public int getVolumeCredits() {
        int result = 0;
        result += Math.max(performance.getAudience() - 30, 0);
        if ("comedy".equals(play.getType())) {
            result += Math.floor(performance.getAudience() / 5);
        }
        return result;
    }
}
