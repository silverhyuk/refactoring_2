package com.david.chapter11.example6;

/**
 * 온도 조절 장치 클래스 (전역 변수를 가진 클래스)
 */
public class Thermostat {
    public static double selectedTemperature = 22.0; // 기본값 22도
    public static double currentTemperature = 21.0;
    
    public static void setSelectedTemperature(double temperature) {
        selectedTemperature = temperature;
    }
    
    public static void setCurrentTemperature(double temperature) {
        currentTemperature = temperature;
    }
} 