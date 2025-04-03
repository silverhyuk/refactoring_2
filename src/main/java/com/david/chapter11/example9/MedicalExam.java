package com.david.chapter11.example9;

/**
 * 의료 검사 클래스
 */
public class MedicalExam {
    private boolean smoker;
    private int bloodPressure;
    private int weight;
    
    public MedicalExam(boolean smoker, int bloodPressure, int weight) {
        this.smoker = smoker;
        this.bloodPressure = bloodPressure;
        this.weight = weight;
    }
    
    public boolean isSmoker() {
        return smoker;
    }
    
    public int getBloodPressure() {
        return bloodPressure;
    }
    
    public int getWeight() {
        return weight;
    }
} 