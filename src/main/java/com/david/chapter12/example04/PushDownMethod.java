package com.david.chapter12.example04;

// 리팩토링 전의 코드를 표현하는 클래스들
abstract class EmployeePushDownBefore {
    protected String name;

    public EmployeePushDownBefore(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // 모든 직원에게 적용되지 않는 메서드
    public String getQuotaReport() {
        return "Quota not applicable";
    }
}

class EngineerPushDownBefore extends EmployeePushDownBefore {
    public EngineerPushDownBefore(String name) {
        super(name);
    }

    // 엔지니어는 할당량 보고서(Quota Report)를 사용하지 않음
}

class SalesmanPushDownBefore extends EmployeePushDownBefore {
    public SalesmanPushDownBefore(String name) {
        super(name);
    }

    @Override
    public String getQuotaReport() {
        return "Sales quota met";
    }
}

// 리팩토링 후의 코드를 표현하는 클래스들
abstract class EmployeePushDownAfter {
    protected String name;

    public EmployeePushDownAfter(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // getQuotaReport 메서드는 상위 클래스에서 제거됨
}

class EngineerPushDownAfter extends EmployeePushDownAfter {
    public EngineerPushDownAfter(String name) {
        super(name);
    }

    // 엔지니어는 할당량 보고서(Quota Report) 메서드가 없음
}

class SalesmanPushDownAfter extends EmployeePushDownAfter {
    public SalesmanPushDownAfter(String name) {
        super(name);
    }

    // 영업사원에만 추가된 할당량 보고서 메서드
    public String getQuotaReport() {
        return "Sales quota met";
    }
}

public class PushDownMethod {
    // 이 클래스는 리팩토링 예제를 담는 컨테이너 역할만 합니다
}