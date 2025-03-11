package com.david.chapter08.splitloop.before;

import java.util.ArrayList;
import java.util.List;

public class OrderProcessor {
    
    public void processOrders(List<Order> orders) {
        double totalSales = 0;
        int totalItems = 0;
        int highPriorityCount = 0;
        
        // 하나의 반복문에서 여러 작업 수행
        for (Order order : orders) {
            totalSales += order.getAmount();
            totalItems += order.getItemCount();
            
            // 우선순위가 높은 주문 개수 계산
            if (order.getPriority() == Priority.HIGH) {
                highPriorityCount++;
            }
        }
        
        // 결과 출력
        System.out.println("판매 총액: " + totalSales);
        System.out.println("총 아이템 수: " + totalItems);
        System.out.println("우선순위 높은 주문 수: " + highPriorityCount);
    }
}

class Order {
    private double amount;
    private int itemCount;
    private Priority priority;
    
    public Order(double amount, int itemCount, Priority priority) {
        this.amount = amount;
        this.itemCount = itemCount;
        this.priority = priority;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public int getItemCount() {
        return itemCount;
    }
    
    public Priority getPriority() {
        return priority;
    }
}

enum Priority {
    LOW, NORMAL, HIGH
}