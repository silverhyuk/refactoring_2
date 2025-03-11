package com.david.chapter08.slidingstatements.before;

public class OrderProcessor {
    
    public double calculateTotal(Order order) {
        double result = 0;
        double price = 0;
        
        // 주문 정보 처리
        price = order.getQuantity() * order.getItemPrice();
        
        // 할인 관련 코드가 분산되어 있음
        int discountableUnits = Math.max(0, order.getQuantity() - 500);
        
        // 배송비 계산
        double shippingCost = Math.min(price * 0.1, 100.0);
        if (price > 1000) {
            shippingCost = 0;
        }
        
        // 할인 계산 (관련 코드가 분산됨)
        double discount = discountableUnits * order.getItemPrice() * 0.05;
        if (order.isRepeatCustomer()) {
            discount += price * 0.02;
        }
        
        // 최종 가격 계산
        result = price - discount + shippingCost;
        return result;
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