package com.david.chapter08.movefield.after;

import java.time.LocalDate;

public class Customer {
    private String name;
    private CustomerContract contract;

    public Customer(String name, double discountRate) {
        this.name = name;
        // 계약 객체 생성 시 할인율을 전달합니다.
        this.contract = new CustomerContract(LocalDate.now(), discountRate);
    }

    // 할인율 getter: 이제 계약의 할인율을 반환
    public double getDiscountRate() {
        return contract.getDiscountRate();
    }

    // 할인율 setter: 내부적으로 계약 객체의 할인율을 수정
    private void setDiscountRate(double discountRate) {
        contract.setDiscountRate(discountRate);
    }

    public void becomePreferred() {
        // 할인율을 0.03 증가시킵니다.
        setDiscountRate(getDiscountRate() + 0.03);
        // 기타 부가 작업 수행
    }

    public double applyDiscount(double amount) {
        return amount - (amount * getDiscountRate());
    }
}