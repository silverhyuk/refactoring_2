package com.david.chapter08.movestatement.before;

public class PricingCalculator {
    
    public double calculatePrice(Order order) {
        // 기본 가격 계산
        double basePrice = order.getQuantity() * order.getItemPrice();
        
        // 할인 계산 - 이 부분이 함수로 이동될 수 있는 후보
        int discountableUnits = Math.max(0, order.getQuantity() - 500);
        double discount = discountableUnits * order.getItemPrice() * 0.05;
        if (order.isRepeatCustomer()) {
            discount += basePrice * 0.02;
        }
        
        // 배송비 계산
        double shippingCost = Math.min(basePrice * 0.1, 100.0);
        if (basePrice > 1000) {
            shippingCost = 0;
        }
        
        // 최종 가격 계산
        return basePrice - discount + shippingCost;
    }
}

class Order {
    private int quantity;
    private double itemPrice;
    private boolean repeatCustomer;
    
    public Order(int quantity, double itemPrice, boolean repeatCustomer) {
        this.quantity = quantity;
        this.itemPrice = itemPrice;
        this.repeatCustomer = repeatCustomer;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public double getItemPrice() {
        return itemPrice;
    }
    
    public boolean isRepeatCustomer() {
        return repeatCustomer;
    }
}