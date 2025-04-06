package com.david.chapter12.example01;

// 리팩토링 전의 코드를 표현하는 클래스들
abstract class PartyBefore {
    public abstract int getMonthlyCost();
}

class EmployeeBefore extends PartyBefore {
    private int monthlyCost;

    public EmployeeBefore(int monthlyCost) {
        this.monthlyCost = monthlyCost;
    }

    @Override
    public int getMonthlyCost() {
        return monthlyCost;
    }

    public int getAnnualCost() {
        return getMonthlyCost() * 12;
    }
}

class DepartmentBefore extends PartyBefore {
    private int monthlyCost;

    public DepartmentBefore(int monthlyCost) {
        this.monthlyCost = monthlyCost;
    }

    @Override
    public int getMonthlyCost() {
        return monthlyCost;
    }

    public int getTotalAnnualCost() {
        return getMonthlyCost() * 12;
    }
}

// 리팩토링 후의 코드를 표현하는 클래스들
abstract class PartyAfter {
    public abstract int getMonthlyCost();

    public int getAnnualCost() {
        return getMonthlyCost() * 12;
    }
}

class EmployeeAfter extends PartyAfter {
    private int monthlyCost;

    public EmployeeAfter(int monthlyCost) {
        this.monthlyCost = monthlyCost;
    }

    @Override
    public int getMonthlyCost() {
        return monthlyCost;
    }
}

class DepartmentAfter extends PartyAfter {
    private int monthlyCost;

    public DepartmentAfter(int monthlyCost) {
        this.monthlyCost = monthlyCost;
    }

    @Override
    public int getMonthlyCost() {
        return monthlyCost;
    }
}

public class PullUpMethod {
    // 이 클래스는 리팩토링 예제를 담는 컨테이너 역할만 합니다
}