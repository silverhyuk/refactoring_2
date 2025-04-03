package com.david.chapter11.example3;

import java.time.LocalDate;

/**
 * 주문 클래스
 */
public class Order {
    private String deliveryState;
    private LocalDate placedOn;
    
    public Order(String deliveryState, LocalDate placedOn) {
        this.deliveryState = deliveryState;
        this.placedOn = placedOn;
    }
    
    public String getDeliveryState() {
        return deliveryState;
    }
    
    public void setDeliveryState(String deliveryState) {
        this.deliveryState = deliveryState;
    }
    
    public LocalDate getPlacedOn() {
        return placedOn;
    }
    
    public void setPlacedOn(LocalDate placedOn) {
        this.placedOn = placedOn;
    }
} 