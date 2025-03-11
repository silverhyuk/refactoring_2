package com.david.chapter08.movefunction.after;

public class TrackSummary {
    private Point[] points;

    public TrackSummary(Point[] points) {
        this.points = points;
    }

    public Summary summarize() {
        double totalTime = calculateTime();
        // 독립된 DistanceCalculator 클래스의 정적 메서드를 호출
        double totalDistance = DistanceCalculator.calculate(points);
        double pace = totalTime / 60 / totalDistance;
        return new Summary(totalTime, totalDistance, pace);
    }

    // 총 시간 계산 (예: 1시간 = 3600초)
    private double calculateTime() {
        return 3600;
    }

    // 요약 결과를 담는 데이터 클래스
    public static class Summary {
        public final double time;
        public final double distance;
        public final double pace;

        public Summary(double time, double distance, double pace) {
            this.time = time;
            this.distance = distance;
            this.pace = pace;
        }

        @Override
        public String toString() {
            return "Summary [time=" + time + ", distance=" + distance + ", pace=" + pace + "]";
        }
    }
}