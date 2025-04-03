package com.david.chapter11.example4;

/**
 * 예제 4: 객체 통째로 넘기기 (Preserve Whole Object)
 */
public class HeatingPlan {
    private NumberRange temperatureRange;
    
    public HeatingPlan(NumberRange temperatureRange) {
        this.temperatureRange = temperatureRange;
    }
    
    /**
     * 리팩토링 전: 개별 값을 매개변수로 받음
     */
    public boolean withinRange(double bottom, double top) {
        return (bottom >= temperatureRange.getLow()) && (top <= temperatureRange.getHigh());
    }
    
    /**
     * 리팩토링 후: 전체 객체를 매개변수로 받아 처리
     */
    public boolean withinRange(NumberRange aNumberRange) {
        return (aNumberRange.getLow() >= temperatureRange.getLow()) &&
               (aNumberRange.getHigh() <= temperatureRange.getHigh());
    }
    
    public NumberRange getTemperatureRange() {
        return temperatureRange;
    }
} 