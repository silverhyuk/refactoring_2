package com.david.chapter11.example2;

/**
 * 예제 2: 함수 매개변수화하기 (Parameterize Function)
 * 리팩토링 전의 코드: 리터럴 값만 다른 여러 유사 함수가 존재
 */
public class SalaryCalculator {
    
    /**
     * 리팩토링 전: 10% 급여 인상 함수
     */
    public void tenPercentRaise(Employee employee) {
        employee.setSalary(employee.getSalary() * 1.10);
    }
    
    /**
     * 리팩토링 전: 5% 급여 인상 함수
     */
    public void fivePercentRaise(Employee employee) {
        employee.setSalary(employee.getSalary() * 1.05);
    }
    
    /**
     * 리팩토링 후: 인상률을 매개변수로 받는 통합 함수
     */
    public void raise(Employee employee, double factor) {
        employee.setSalary(employee.getSalary() * (1 + factor));
    }
    
    /**
     * 요금 계산 예제 - 리팩토링 전
     */
    public double baseCharge(double usage) {
        if (usage < 0) return 0;
        return bottomBand(usage) * 0.03 + middleBand(usage) * 0.05 + topBand(usage) * 0.07;
    }
    
    private double bottomBand(double usage) {
        return Math.min(usage, 100);
    }
    
    private double middleBand(double usage) {
        return usage > 100 ? Math.min(usage, 200) - 100 : 0;
    }
    
    private double topBand(double usage) {
        return usage > 200 ? usage - 200 : 0;
    }
    
    /**
     * 리팩토링 후: 범위 계산 함수를 매개변수화
     */
    public double baseChargeRefactored(double usage) {
        if (usage < 0) return 0;
        return withinBand(usage, 0, 100) * 0.03 
                + withinBand(usage, 100, 200) * 0.05
                + withinBand(usage, 200, Double.POSITIVE_INFINITY) * 0.07;
    }
    
    private double withinBand(double usage, double bottom, double top) {
        return usage > bottom ? Math.min(usage, top) - bottom : 0;
    }
} 