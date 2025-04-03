package com.david.chapter11.example3;

import java.time.LocalDate;

/**
 * 예제 3: 플래그 인수 제거하기 (Remove Flag Argument)
 */
public class DeliveryService {
    
    /**
     * 리팩토링 전: 플래그 인수 (isRush)를 통해 배송일 계산을 분기하는 함수
     */
    public LocalDate deliveryDate(Order order, boolean isRush) {
        int deliveryTime = 0;
        if (isRush) {
            if (order.getDeliveryState().equals("MA") || order.getDeliveryState().equals("CT"))
                deliveryTime = 1;
            else if (order.getDeliveryState().equals("NY") || order.getDeliveryState().equals("NH"))
                deliveryTime = 2;
            else
                deliveryTime = 3;
            return order.getPlacedOn().plusDays(1 + deliveryTime);
        } else {
            if (order.getDeliveryState().equals("MA") ||
                order.getDeliveryState().equals("CT") ||
                order.getDeliveryState().equals("NY"))
                deliveryTime = 2;
            else if (order.getDeliveryState().equals("ME") ||
                     order.getDeliveryState().equals("NH"))
                deliveryTime = 3;
            else
                deliveryTime = 4;
            return order.getPlacedOn().plusDays(2 + deliveryTime);
        }
    }
    
    /**
     * 리팩토링 후: 긴급 배송을 위한 명시적 함수
     */
    public LocalDate rushDeliveryDate(Order order) {
        int deliveryTime;
        if (order.getDeliveryState().equals("MA") || order.getDeliveryState().equals("CT"))
            deliveryTime = 1;
        else if (order.getDeliveryState().equals("NY") || order.getDeliveryState().equals("NH"))
            deliveryTime = 2;
        else
            deliveryTime = 3;
        return order.getPlacedOn().plusDays(1 + deliveryTime);
    }
    
    /**
     * 리팩토링 후: 일반 배송을 위한 명시적 함수
     */
    public LocalDate regularDeliveryDate(Order order) {
        int deliveryTime;
        if (order.getDeliveryState().equals("MA") ||
            order.getDeliveryState().equals("CT") ||
            order.getDeliveryState().equals("NY"))
            deliveryTime = 2;
        else if (order.getDeliveryState().equals("ME") ||
                 order.getDeliveryState().equals("NH"))
            deliveryTime = 3;
        else
            deliveryTime = 4;
        return order.getPlacedOn().plusDays(2 + deliveryTime);
    }
} 