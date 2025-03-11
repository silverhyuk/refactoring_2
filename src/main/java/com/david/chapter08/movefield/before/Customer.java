package com.david.chapter08.movefield.before;

import java.time.LocalDate;

public class Customer {
    private String name;
    private double discountRate;
    private CustomerContract contract;

    public Customer(String name, double discountRate) {
        this.name = name;
        this.discountRate = discountRate;
        // 계약 객체 생성 시 할인율 정보는 전달하지 않음
        this.contract = new CustomerContract(LocalDate.now());
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public void becomePreferred() {
        // 할인율을 증가시킴
        this.discountRate += 0.03;
        // 기타 부가 작업 수행
    }

    public double applyDiscount(double amount) {
        return amount - (amount * discountRate);
    }
}