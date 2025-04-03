package com.david.chapter11.example2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SalaryCalculatorTest {
    
    private SalaryCalculator calculator;
    private Employee employee;
    
    @BeforeEach
    void setUp() {
        calculator = new SalaryCalculator();
        employee = new Employee("홍길동", 1000.0);
    }
    
    @Test
    void tenPercentRaise_increases_salary_by_10_percent() {
        calculator.tenPercentRaise(employee);
        assertEquals(1100.0, employee.getSalary(), 0.01);
    }
    
    @Test
    void fivePercentRaise_increases_salary_by_5_percent() {
        calculator.fivePercentRaise(employee);
        assertEquals(1050.0, employee.getSalary(), 0.01);
    }
    
    @Test
    void parameterizedRaise_increases_salary_by_given_percent() {
        calculator.raise(employee, 0.15); // 15% 인상
        assertEquals(1150.0, employee.getSalary(), 0.01);
    }
    
    @Test
    void baseCharge_calculates_correctly_for_low_usage() {
        double charge = calculator.baseCharge(50);
        assertEquals(1.5, charge, 0.01); // 50 * 0.03 = 1.5
    }
    
    @Test
    void baseCharge_calculates_correctly_for_medium_usage() {
        double charge = calculator.baseCharge(150);
        assertEquals(5.5, charge, 0.01); // (100 * 0.03) + (50 * 0.05) = 3 + 2.5 = 5.5
    }
    
    @Test
    void baseCharge_calculates_correctly_for_high_usage() {
        double charge = calculator.baseCharge(250);
        assertEquals(11.5, charge, 0.01); // (100 * 0.03) + (100 * 0.05) + (50 * 0.07) = 3 + 5 + 3.5 = 11.5
    }
    
    @Test
    void baseChargeRefactored_matches_original_implementation() {
        assertEquals(calculator.baseCharge(50), calculator.baseChargeRefactored(50), 0.01);
        assertEquals(calculator.baseCharge(150), calculator.baseChargeRefactored(150), 0.01);
        assertEquals(calculator.baseCharge(250), calculator.baseChargeRefactored(250), 0.01);
    }
} 