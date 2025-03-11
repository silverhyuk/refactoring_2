package com.david.chapter08.movefunction.before;

public class TrackSummary {
    private Point[] points;

    public TrackSummary(Point[] points) {
        this.points = points;
    }

    public Summary summarize() {
        double totalTime = calculateTime();
        // 내부 클래스 인스턴스를 생성하여 거리 계산
        DistanceCalculator calculator = new DistanceCalculator();
        double totalDistance = calculator.calculate();
        double pace = totalTime / 60 / totalDistance;
        return new Summary(totalTime, totalDistance, pace);
    }

    // 내부 클래스: TrackSummary에 종속되어 있음
    private class DistanceCalculator {
        public double calculate() {
            double result = 0;
            for (int i = 1; i < points.length; i++) {
                result += distance(points[i - 1], points[i]);
            }
            return result;
        }

        private double distance(Point p1, Point p2) {
            final double EARTH_RADIUS = 3959; // 마일 단위
            double dLat = radians(p2.lat - p1.lat);
            double dLon = radians(p2.lon - p1.lon);
            double a = Math.pow(Math.sin(dLat / 2), 2)
                     + Math.cos(radians(p1.lat)) * Math.cos(radians(p2.lat))
                     * Math.pow(Math.sin(dLon / 2), 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            return EARTH_RADIUS * c;
        }

        private double radians(double degrees) {
            return degrees * Math.PI / 180;
        }
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