package com.david.chapter01;

public class PerformanceCalculator {
    protected Invoice.Performance performance;
    protected Play play;

    public PerformanceCalculator(Invoice.Performance performance, Play play) {
        this.performance = performance;
        this.play = play;
    }

    public static PerformanceCalculator create(Invoice.Performance performance, Play play) {
        return switch (play.getType()) {
            case "tragedy" -> new TragedyCalculator(performance, play);
            case "comedy" -> new ComedyCalculator(performance, play);
            default -> throw new IllegalArgumentException("알 수 없는 장르: " + play.getType());
        };
    }

    public Play getPlay() {
        return play;
    }

    public double getAmount() {
        throw new IllegalStateException("서브클래스에서 처리하도록 설계되었습니다.");
    }

    public int getVolumeCredits() {
        return Math.max(performance.getAudience() - 30, 0);
    }
}
