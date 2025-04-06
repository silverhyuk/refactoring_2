package com.david.chapter12.example05;

// 리팩토링 전의 코드를 표현하는 클래스들
abstract class EmployeeFieldBefore {
    protected String name;
    protected int salesQuota; // 모든 직원이 사용하지 않는 필드

    public EmployeeFieldBefore(String name, int salesQuota) {
        this.name = name;
        this.salesQuota = salesQuota;
    }

    public String getName() {
        return name;
    }

    public int getSalesQuota() {
        return salesQuota;
    }
}

class EngineerFieldBefore extends EmployeeFieldBefore {
    public EngineerFieldBefore(String name) {
        super(name, 0); // 엔지니어는 salesQuota 값이 의미 없음
    }

    public void develop() {
        // salesQuota 필드를 사용하지 않음
    }
}

class SalesmanFieldBefore extends EmployeeFieldBefore {
    public SalesmanFieldBefore(String name, int salesQuota) {
        super(name, salesQuota);
    }

    public boolean isQuotaMet(int sales) {
        return sales >= salesQuota;
    }
}

// 리팩토링 후의 코드를 표현하는 클래스들
abstract class EmployeeFieldAfter {
    protected String name;

    public EmployeeFieldAfter(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

class EngineerFieldAfter extends EmployeeFieldAfter {
    public EngineerFieldAfter(String name) {
        super(name);
    }

    public void develop() {
        // salesQuota 필드가 이제 상위 클래스에 없음
    }
}

class SalesmanFieldAfter extends EmployeeFieldAfter {
    private int salesQuota; // 영업사원 클래스로 필드를 내림

    public SalesmanFieldAfter(String name, int salesQuota) {
        super(name);
        this.salesQuota = salesQuota;
    }

    public int getSalesQuota() {
        return salesQuota;
    }

    public boolean isQuotaMet(int sales) {
        return sales >= salesQuota;
    }
}

public class PushDownField {
    // 이 클래스는 리팩토링 예제를 담는 컨테이너 역할만 합니다
}