package com.david.chapter12.example07;

// 리팩토링 전: 타입 코드를 직접 사용하는 방식
class EmployeeStateBefore {
    private String name;
    private String type; // "engineer", "manager", "salesman"
    private int baseSalary;

    public EmployeeStateBefore(String name, String type, int baseSalary) {
        this.name = name;
        this.type = type;
        this.baseSalary = baseSalary;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int calculateSalary() {
        return switch (type) {
            case "engineer" -> baseSalary + 5000;
            case "manager" -> baseSalary + 10000;
            case "salesman" -> baseSalary + 8000 + calculateCommission();
            default -> baseSalary;
        };
    }

    private int calculateCommission() {
        // 세일즈맨만의 수수료 계산 로직
        return 2000;
    }
}

// 리팩토링 후: 상태/전략 패턴 적용
class EmployeeStateAfter {
    private String name;
    private EmployeeType type;
    private int baseSalary;

    public EmployeeStateAfter(String name, String typeCode, int baseSalary) {
        this.name = name;
        this.type = EmployeeType.createEmployeeType(typeCode);
        this.baseSalary = baseSalary;
    }

    public String getTypeCode() {
        return type.getTypeCode();
    }

    public void setTypeCode(String typeCode) {
        this.type = EmployeeType.createEmployeeType(typeCode);
    }

    public int calculateSalary() {
        return type.calculateSalary(baseSalary);
    }
}

// 전략 인터페이스
interface EmployeeType {
    String getTypeCode();

    int calculateSalary(int baseSalary);

    static EmployeeType createEmployeeType(String typeCode) {
        return switch (typeCode) {
            case "engineer" -> new Engineer();
            case "manager" -> new Manager();
            case "salesman" -> new Salesman();
            default -> throw new IllegalArgumentException("Invalid employee type: " + typeCode);
        };
    }
}

// 구체적인 전략 클래스들
class Engineer implements EmployeeType {
    @Override
    public String getTypeCode() {
        return "engineer";
    }

    @Override
    public int calculateSalary(int baseSalary) {
        return baseSalary + 5000;
    }
}

class Manager implements EmployeeType {
    @Override
    public String getTypeCode() {
        return "manager";
    }

    @Override
    public int calculateSalary(int baseSalary) {
        return baseSalary + 10000;
    }
}

class Salesman implements EmployeeType {
    @Override
    public String getTypeCode() {
        return "salesman";
    }

    @Override
    public int calculateSalary(int baseSalary) {
        return baseSalary + 8000 + calculateCommission();
    }

    private int calculateCommission() {
        // 세일즈맨만의 수수료 계산 로직
        return 2000;
    }
}

public class ReplaceTypeCodeWithStateStrategy {
    // 이 클래스는 리팩토링 예제를 담는 컨테이너 역할만 합니다
}