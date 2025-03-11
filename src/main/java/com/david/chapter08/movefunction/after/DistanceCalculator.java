package com.david.chapter08.movefunction.after;

public class DistanceCalculator {

    // points 배열을 매개변수로 받아 계산
    public static double calculate(Point[] points) {
        double result = 0;
        for (int i = 1; i < points.length; i++) {
            result += distance(points[i - 1], points[i]);
        }
        return result;
    }

    private static double distance(Point p1, Point p2) {
        final double EARTH_RADIUS = 3959; // 마일 단위
        double dLat = radians(p2.lat - p1.lat);
        double dLon = radians(p2.lon - p1.lon);
        double a = Math.pow(Math.sin(dLat / 2), 2)
                 + Math.cos(radians(p1.lat)) * Math.cos(radians(p2.lat))
                 * Math.pow(Math.sin(dLon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }

    private static double radians(double degrees) {
        return degrees * Math.PI / 180;
    }
}