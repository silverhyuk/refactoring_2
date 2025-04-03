package com.david.chapter11.example10;

/**
 * 고객 클래스
 */
public class Customer {
    private String name;
    private double baseRate;
    private String customerType;
    
    public Customer(String name, double baseRate, String customerType) {
        this.name = name;
        this.baseRate = baseRate;
        this.customerType = customerType;
    }
    
    public String getName() {
        return name;
    }
    
    public double getBaseRate() {
        return baseRate;
    }
    
    public String getCustomerType() {
        return customerType;
    }
} 