package com.david.chapter11.example5;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderTest {
    
    @Test
    void getFinalPrice_calculatesCorrectly_forSmallOrder() {
        Order order = new Order(50, 10);
        double basePrice = 50 * 10; // 500
        double expected = basePrice * 0.95; // 5% 할인
        
        assertEquals(expected, order.getFinalPrice(), 0.01);
    }
    
    @Test
    void getFinalPrice_calculatesCorrectly_forLargeOrder() {
        Order order = new Order(120, 10);
        double basePrice = 120 * 10; // 1200
        double expected = basePrice * 0.90; // 10% 할인
        
        assertEquals(expected, order.getFinalPrice(), 0.01);
    }
    
    @Test
    void getFinalPriceRefactored_matchesOriginalVersion_forSmallOrder() {
        Order order = new Order(50, 10);
        assertEquals(order.getFinalPrice(), order.getFinalPriceRefactored(), 0.01);
    }
    
    @Test
    void getFinalPriceRefactored_matchesOriginalVersion_forLargeOrder() {
        Order order = new Order(120, 10);
        assertEquals(order.getFinalPrice(), order.getFinalPriceRefactored(), 0.01);
    }
    
    @Test
    void discountedPrice_appliesCorrectDiscount_forLevel1() {
        Order order = new Order(50, 10);
        double basePrice = 500;
        
        assertEquals(basePrice * 0.95, order.discountedPrice(basePrice, 1), 0.01);
    }
    
    @Test
    void discountedPrice_appliesCorrectDiscount_forLevel2() {
        Order order = new Order(120, 10);
        double basePrice = 1200;
        
        assertEquals(basePrice * 0.90, order.discountedPrice(basePrice, 2), 0.01);
    }
    
    @Test
    void discountedPriceRefactored_matchesOriginalVersion() {
        Order smallOrder = new Order(50, 10);
        Order largeOrder = new Order(120, 10);
        double basePrice = 1000;
        
        // 리팩토링된 버전이 원래 버전과 같은 결과를 내는지 확인
        assertEquals(smallOrder.discountedPrice(basePrice, 1), 
                     smallOrder.discountedPriceRefactored(basePrice), 0.01);
        
        assertEquals(largeOrder.discountedPrice(basePrice, 2), 
                     largeOrder.discountedPriceRefactored(basePrice), 0.01);
    }
} 