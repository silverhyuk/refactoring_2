package com.david.chapter12.example02;

// 리팩토링 전의 코드를 표현하는 클래스들
class EmployeeParentBefore {
    // 상위 클래스에는 name 필드 없음
}

class SalesmanBefore extends EmployeeParentBefore {
    private String name;

    public SalesmanBefore(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

class EngineerBefore extends EmployeeParentBefore {
    private String name;

    public EngineerBefore(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

// 리팩토링 후의 코드를 표현하는 클래스들
class EmployeeParentAfter {
    protected String name;

    public EmployeeParentAfter(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

class SalesmanAfter extends EmployeeParentAfter {
    public SalesmanAfter(String name) {
        super(name);
    }
}

class EngineerAfter extends EmployeeParentAfter {
    public EngineerAfter(String name) {
        super(name);
    }
}

public class PullUpField {
    // 이 클래스는 리팩토링 예제를 담는 컨테이너 역할만 합니다
}