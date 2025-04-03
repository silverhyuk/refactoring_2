package com.david.chapter11.example10;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ChargeCalculatorTest {
    
    private ChargeCalculator calculator;
    private Customer regularCustomer;
    private Customer premiumCustomer;
    
    @BeforeEach
    void setUp() {
        calculator = new ChargeCalculator();
        regularCustomer = new Customer("홍길동", 10.0, "regular");
        premiumCustomer = new Customer("김회장", 8.0, "premium");
    }
    
    @Test
    void calculateChargeWithCommand_returnsCorrectCharge_forSmallOrder() {
        double charge = calculator.calculateChargeWithCommand(regularCustomer, 50, 3, 2023);
        
        // 기본 가격: 50 * 10 = 500
        // 할인 레벨: 1 (수량 <= 100)
        // 할인 비율: 0.98 (일반 할인)
        // 총 금액: 500 * 1 * 0.98 = 490
        assertEquals(490.0, charge, 0.01);
    }
    
    @Test
    void calculateChargeWithCommand_returnsCorrectCharge_forLargeOrder() {
        double charge = calculator.calculateChargeWithCommand(regularCustomer, 150, 3, 2023);
        
        // 기본 가격: 150 * 10 = 1500
        // 할인 레벨: 2 (수량 > 100)
        // 할인 비율: 0.98 (일반 할인)
        // 총 금액: 1500 * 2 * 0.98 = 2940
        assertEquals(2940.0, charge, 0.01);
    }
    
    @Test
    void calculateChargeWithCommand_appliesSpecialDiscount_forMay2023() {
        double charge = calculator.calculateChargeWithCommand(regularCustomer, 50, 5, 2023);
        
        // 기본 가격: 50 * 10 = 500
        // 할인 레벨: 1 (수량 <= 100)
        // 할인 비율: 0.95 (특별 할인)
        // 총 금액: 500 * 1 * 0.95 = 475
        assertEquals(475.0, charge, 0.01);
    }
    
    @Test
    void calculateCharge_returnsCorrectCharge_forSmallOrder() {
        double commandCharge = calculator.calculateChargeWithCommand(regularCustomer, 50, 3, 2023);
        double functionCharge = calculator.calculateCharge(regularCustomer, 50, 3, 2023);
        
        // 명령과 함수의 결과가 동일해야 함
        assertEquals(commandCharge, functionCharge, 0.01);
    }
    
    @Test
    void calculateCharge_returnsCorrectCharge_forLargeOrder() {
        double commandCharge = calculator.calculateChargeWithCommand(regularCustomer, 150, 3, 2023);
        double functionCharge = calculator.calculateCharge(regularCustomer, 150, 3, 2023);
        
        // 명령과 함수의 결과가 동일해야 함
        assertEquals(commandCharge, functionCharge, 0.01);
    }
    
    @Test
    void calculateCharge_appliesSpecialDiscount_forMay2023() {
        double commandCharge = calculator.calculateChargeWithCommand(regularCustomer, 50, 5, 2023);
        double functionCharge = calculator.calculateCharge(regularCustomer, 50, 5, 2023);
        
        // 명령과 함수의 결과가 동일해야 함
        assertEquals(commandCharge, functionCharge, 0.01);
    }
} 