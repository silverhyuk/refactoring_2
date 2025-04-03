package com.david.chapter11.example10;

/**
 * 예제 10: 명령을 함수로 바꾸기 (Replace Command with Function)
 * 너무 복잡하지 않은 명령 객체는 함수로 단순화하는 것이 더 나을 수 있다.
 */
public class ChargeCalculator {
    /**
     * 클라이언트 코드 (명령 객체 사용)
     */
    public double calculateChargeWithCommand(Customer customer, int quantity, int month, int year) {
        return new ChargeCommand(customer, quantity, month, year).execute();
    }
    
    /**
     * 리팩토링 후: 명령을 단순한 함수로 변경
     */
    public double calculateCharge(Customer customer, int quantity, int month, int year) {
        double base = customer.getBaseRate() * quantity;
        int discountLevel = (quantity > 100) ? 2 : 1;
        double discountFactor = (month == 5 && year == 2023) ? 0.95 : 0.98;
        
        return base * discountLevel * discountFactor;
    }
}

/**
 * 명령 객체
 */
class ChargeCommand {
    private Customer customer;
    private int quantity;
    private int month;
    private int year;
    
    public ChargeCommand(Customer customer, int quantity, int month, int year) {
        this.customer = customer;
        this.quantity = quantity;
        this.month = month;
        this.year = year;
    }
    
    public double execute() {
        double base = customer.getBaseRate() * quantity;
        int discountLevel = (quantity > 100) ? 2 : 1;
        double discountFactor = (month == 5 && year == 2023) ? 0.95 : 0.98;
        
        return base * discountLevel * discountFactor;
    }
} 