package com.david.chapter11.example5;

/**
 * 예제 5: 매개변수를 질의 함수로 바꾸기 (Replace Parameter with Query)
 */
public class Order {
    private int quantity;
    private double itemPrice;
    
    public Order(int quantity, double itemPrice) {
        this.quantity = quantity;
        this.itemPrice = itemPrice;
    }
    
    /**
     * 리팩토링 전: 할인 등급을 매개변수로 전달받아 사용
     */
    public double getFinalPrice() {
        double basePrice = this.quantity * this.itemPrice;
        int discountLevel;
        if (this.quantity > 100) {
            discountLevel = 2;
        } else {
            discountLevel = 1;
        }
        return discountedPrice(basePrice, discountLevel);
    }
    
    public double discountedPrice(double basePrice, int discountLevel) {
        switch (discountLevel) {
            case 1: return basePrice * 0.95;
            case 2: return basePrice * 0.90;
            default: throw new IllegalStateException("유효하지 않은 할인 등급: " + discountLevel);
        }
    }
    
    /**
     * 리팩토링 후: 할인 등급 계산을 질의 함수로 추출, 매개변수로 받지 않음
     */
    public double getFinalPriceRefactored() {
        double basePrice = this.quantity * this.itemPrice;
        return discountedPriceRefactored(basePrice);
    }
    
    public double discountedPriceRefactored(double basePrice) {
        return basePrice * (1 - getDiscountRate());
    }
    
    private int getDiscountLevel() {
        return (this.quantity > 100) ? 2 : 1;
    }
    
    private double getDiscountRate() {
        switch (getDiscountLevel()) {
            case 1: return 0.05;
            case 2: return 0.10;
            default: throw new IllegalStateException("유효하지 않은 할인 등급");
        }
    }
} 