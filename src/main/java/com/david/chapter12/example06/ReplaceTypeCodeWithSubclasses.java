package com.david.chapter12.example06;

// 리팩토링 전: 직접 상속 방식
class EmployeeTypeBefore {
    private String name;
    private String type; // 타입 코드: "engineer", "manager", "salesman"

    public EmployeeTypeBefore(String name, String type) {
        validateType(type);
        this.name = name;
        this.type = type;
    }

    private void validateType(String type) {
        if (!type.equals("engineer") && !type.equals("manager") && !type.equals("salesman")) {
            throw new IllegalArgumentException("Invalid type: " + type);
        }
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getBonus() {
        return switch (type) {
            case "engineer" -> 50;
            case "manager" -> 100;
            case "salesman" -> 200;
            default -> 0;
        };
    }
}

// 리팩토링 후: 직접 상속으로 타입 코드 대체
abstract class EmployeeTypeAfter {
    protected String name;

    public EmployeeTypeAfter(String name) {
        this.name = name;
    }

    public abstract String getType();

    public abstract int getBonus();

    public String getName() {
        return name;
    }

    // 팩토리 메서드
    public static EmployeeTypeAfter create(String name, String type) {
        return switch (type) {
            case "engineer" -> new EngineerTypeAfter(name);
            case "manager" -> new ManagerTypeAfter(name);
            case "salesman" -> new SalesmanTypeAfter(name);
            default -> throw new IllegalArgumentException("Invalid type: " + type);
        };
    }
}

class EngineerTypeAfter extends EmployeeTypeAfter {
    public EngineerTypeAfter(String name) {
        super(name);
    }

    @Override
    public String getType() {
        return "engineer";
    }

    @Override
    public int getBonus() {
        return 50;
    }
}

class ManagerTypeAfter extends EmployeeTypeAfter {
    public ManagerTypeAfter(String name) {
        super(name);
    }

    @Override
    public String getType() {
        return "manager";
    }

    @Override
    public int getBonus() {
        return 100;
    }
}

class SalesmanTypeAfter extends EmployeeTypeAfter {
    public SalesmanTypeAfter(String name) {
        super(name);
    }

    @Override
    public String getType() {
        return "salesman";
    }

    @Override
    public int getBonus() {
        return 200;
    }
}

// 리팩토링 전: 간접 상속 방식을 위한 클래스
class EmployeeIndirectBefore {
    private String name;
    private String typeCode;

    public EmployeeIndirectBefore(String name, String typeCode) {
        this.name = name;
        this.typeCode = typeCode;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return typeCode;
    }

    public int getBonus() {
        return switch (typeCode) {
            case "engineer" -> 50;
            case "manager" -> 100;
            case "salesman" -> 200;
            default -> 0;
        };
    }
}

// 리팩토링 후: 간접 상속 방식
class EmployeeIndirectAfter {
    private String name;
    private EmployeeType type;

    public EmployeeIndirectAfter(String name, String typeCode) {
        this.name = name;
        this.type = EmployeeType.create(typeCode);
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type.toString();
    }

    public int getBonus() {
        return type.getBonus();
    }
}

// 타입 계층 구조
abstract class EmployeeType {
    public abstract String toString();

    public abstract int getBonus();

    public static EmployeeType create(String code) {
        return switch (code) {
            case "engineer" -> new EngineerType();
            case "manager" -> new ManagerType();
            case "salesman" -> new SalesmanType();
            default -> throw new IllegalArgumentException("Invalid type: " + code);
        };
    }
}

class EngineerType extends EmployeeType {
    @Override
    public String toString() {
        return "engineer";
    }

    @Override
    public int getBonus() {
        return 50;
    }
}

class ManagerType extends EmployeeType {
    @Override
    public String toString() {
        return "manager";
    }

    @Override
    public int getBonus() {
        return 100;
    }
}

class SalesmanType extends EmployeeType {
    @Override
    public String toString() {
        return "salesman";
    }

    @Override
    public int getBonus() {
        return 200;
    }
}

public class ReplaceTypeCodeWithSubclasses {
    // 이 클래스는 리팩토링 예제를 담는 컨테이너 역할만 합니다
}