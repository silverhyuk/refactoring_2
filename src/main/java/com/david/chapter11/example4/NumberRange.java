package com.david.chapter11.example4;

/**
 * 범위를 나타내는 클래스
 */
public class NumberRange {
    private final double low;
    private final double high;
    
    public NumberRange(double low, double high) {
        this.low = low;
        this.high = high;
    }
    
    public double getLow() {
        return low;
    }
    
    public double getHigh() {
        return high;
    }
    
    public boolean contains(double value) {
        return value >= low && value <= high;
    }
} 