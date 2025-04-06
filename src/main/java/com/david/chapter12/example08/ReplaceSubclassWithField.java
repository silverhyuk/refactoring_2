package com.david.chapter12.example08;

// 리팩토링 전: 서브클래스를 통한 변형 표현
abstract class ProductBefore {
    protected String name;
    protected double price;

    public ProductBefore(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public abstract boolean isPremium();
}

class RegularProductBefore extends ProductBefore {
    public RegularProductBefore(String name, double price) {
        super(name, price);
    }

    @Override
    public boolean isPremium() {
        return false;
    }
}

class PremiumProductBefore extends ProductBefore {
    public PremiumProductBefore(String name, double price) {
        super(name, price);
    }

    @Override
    public boolean isPremium() {
        return true;
    }

    @Override
    public double getPrice() {
        // 프리미엄 제품의 가격 계산 로직
        return super.getPrice() * 1.2; // 20% 추가 비용
    }
}

// 리팩토링 후: 서브클래스를 필드로 대체
class ProductAfter {
    private String name;
    private double price;
    private boolean isPremium;

    private ProductAfter(String name, double price, boolean isPremium) {
        this.name = name;
        this.price = price;
        this.isPremium = isPremium;
    }

    // 팩토리 메서드
    public static ProductAfter createRegularProduct(String name, double price) {
        return new ProductAfter(name, price, false);
    }

    public static ProductAfter createPremiumProduct(String name, double price) {
        return new ProductAfter(name, price, true);
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        if (isPremium) {
            return price * 1.2; // 20% 추가 비용
        }
        return price;
    }

    public boolean isPremium() {
        return isPremium;
    }
}

// 또 다른 예시: 리팩토링 전 - 직원 타입을 상속으로 표현
abstract class EmployeeBefore {
    protected String name;
    protected double baseSalary;

    public EmployeeBefore(String name, double baseSalary) {
        this.name = name;
        this.baseSalary = baseSalary;
    }

    public String getName() {
        return name;
    }

    public abstract double calculateSalary();

    public abstract String getJobTitle();
}

class DeveloperBefore extends EmployeeBefore {
    public DeveloperBefore(String name, double baseSalary) {
        super(name, baseSalary);
    }

    @Override
    public double calculateSalary() {
        return baseSalary + 5000; // 개발자 보너스
    }

    @Override
    public String getJobTitle() {
        return "Developer";
    }
}

class ManagerBefore extends EmployeeBefore {
    public ManagerBefore(String name, double baseSalary) {
        super(name, baseSalary);
    }

    @Override
    public double calculateSalary() {
        return baseSalary + 10000; // 관리자 보너스
    }

    @Override
    public String getJobTitle() {
        return "Manager";
    }
}

// 리팩토링 후: 직원 타입을 필드로 표현
class EmployeeAfter {
    private String name;
    private double baseSalary;
    private String jobType;

    private EmployeeAfter(String name, double baseSalary, String jobType) {
        this.name = name;
        this.baseSalary = baseSalary;
        this.jobType = jobType;
    }

    // 팩토리 메서드
    public static EmployeeAfter createDeveloper(String name, double baseSalary) {
        return new EmployeeAfter(name, baseSalary, "developer");
    }

    public static EmployeeAfter createManager(String name, double baseSalary) {
        return new EmployeeAfter(name, baseSalary, "manager");
    }

    public String getName() {
        return name;
    }

    public double calculateSalary() {
        return switch (jobType) {
            case "developer" -> baseSalary + 5000;
            case "manager" -> baseSalary + 10000;
            default -> baseSalary;
        };
    }

    public String getJobTitle() {
        return switch (jobType) {
            case "developer" -> "Developer";
            case "manager" -> "Manager";
            default -> "Employee";
        };
    }
}

public class ReplaceSubclassWithField {
    // 이 클래스는 리팩토링 예제를 담는 컨테이너 역할만 합니다
}