package com.david.chapter09;

import java.util.HashMap;
import java.util.Map;

/**
 * 9.5 Change Value to Reference (값을 참조로 변경) 예제
 * 
 * 문제: 값 객체로 관리하면 동일한 데이터가 여러 곳에 중복되어 일관성 유지가 어렵습니다.
 * 해결: 저장소(Repository)를 통해 참조 객체로 관리하여 데이터 일관성을 유지합니다.
 */
public class ChangeValueToReference {

    public static void main(String[] args) {
        // 리팩토링 전 사용 예제
        OrderBefore order1 = new OrderBefore("001", "C123");
        OrderBefore order2 = new OrderBefore("002", "C123"); // 동일한 고객 ID로 새 객체 생성
        System.out.println("Before: Same customer? " + (order1.getCustomer() == order2.getCustomer())); // false
        
        // 리팩토링 후 사용 예제
        CustomerRepository repository = new CustomerRepository();
        OrderAfter orderA = new OrderAfter("001", "C123", repository);
        OrderAfter orderB = new OrderAfter("002", "C123", repository); // 동일한 고객 ID로 참조 객체 사용
        System.out.println("After: Same customer? " + (orderA.getCustomer() == orderB.getCustomer())); // true
    }
}

/**
 * 리팩토링 전: 값으로 사용되는 Customer
 */
class CustomerValue {
    private String id;

    public CustomerValue(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}

class OrderBefore {
    private String number;
    private CustomerValue customer;

    public OrderBefore(String number, String customerId) {
        this.number = number;
        this.customer = new CustomerValue(customerId); // 매번 새로운 Customer 객체 생성
    }

    public String getNumber() {
        return number;
    }

    public CustomerValue getCustomer() {
        return customer;
    }
}

/**
 * 리팩토링 후: 참조로 관리되는 Customer
 */
class CustomerReference {
    private String id;

    public CustomerReference(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}

class CustomerRepository {
    private Map<String, CustomerReference> customers = new HashMap<>();

    public CustomerReference getCustomer(String id) {
        // 저장소에서 고객 ID로 객체를 찾거나 없으면 새로 생성하여 저장
        return customers.computeIfAbsent(id, CustomerReference::new);
    }
}

class OrderAfter {
    private String number;
    private CustomerReference customer;

    public OrderAfter(String number, String customerId, CustomerRepository repository) {
        this.number = number;
        this.customer = repository.getCustomer(customerId); // 저장소에서 Customer 객체 참조
    }

    public String getNumber() {
        return number;
    }

    public CustomerReference getCustomer() {
        return customer;
    }
}