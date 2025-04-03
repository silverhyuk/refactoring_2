package com.david.chapter11.example6;

/**
 * 예제 6: 질의 함수를 매개변수로 바꾸기 (Replace Query with Parameter)
 */
public class HeatingPlan {
    private final double _min;
    private final double _max;
    
    public HeatingPlan(double min, double max) {
        this._min = min;
        this._max = max;
    }
    
    /**
     * 리팩토링 전: 전역 Thermostat 객체에 직접 의존하는 함수
     */
    public double getTargetTemperature() {
        if (Thermostat.selectedTemperature > this._max) return this._max;
        else if (Thermostat.selectedTemperature < this._min) return this._min;
        else return Thermostat.selectedTemperature;
    }
    
    /**
     * 리팩토링 후: 외부에서 선택 온도를 매개변수로 전달받는 함수
     */
    public double targetTemperature(double selectedTemperature) {
        if (selectedTemperature > this._max) return this._max;
        else if (selectedTemperature < this._min) return this._min;
        else return selectedTemperature;
    }
} 