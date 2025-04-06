package com.david.chapter12.example03;

// 리팩토링 전의 코드를 표현하는 클래스들
class PartyConstructorBefore {
    // 기본 상위 클래스
}

class EmployeeConstructorBefore extends PartyConstructorBefore {
    private String name;
    private int id;
    private int monthlyCost;

    public EmployeeConstructorBefore(String name, int id, int monthlyCost) {
        super();
        this.name = name;
        this.id = id;
        this.monthlyCost = monthlyCost;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getMonthlyCost() {
        return monthlyCost;
    }
}

class DepartmentConstructorBefore extends PartyConstructorBefore {
    private String name;
    private java.util.List<EmployeeConstructorBefore> staff;

    public DepartmentConstructorBefore(String name, java.util.List<EmployeeConstructorBefore> staff) {
        super();
        this.name = name;
        this.staff = new java.util.ArrayList<>(staff);
    }

    public String getName() {
        return name;
    }

    public java.util.List<EmployeeConstructorBefore> getStaff() {
        return new java.util.ArrayList<>(staff);
    }
}

// 리팩토링 후의 코드를 표현하는 클래스들
class PartyConstructorAfter {
    protected String name;

    public PartyConstructorAfter(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

class EmployeeConstructorAfter extends PartyConstructorAfter {
    private int id;
    private int monthlyCost;

    public EmployeeConstructorAfter(String name, int id, int monthlyCost) {
        super(name);
        this.id = id;
        this.monthlyCost = monthlyCost;
    }

    public int getId() {
        return id;
    }

    public int getMonthlyCost() {
        return monthlyCost;
    }
}

class DepartmentConstructorAfter extends PartyConstructorAfter {
    private java.util.List<EmployeeConstructorAfter> staff;

    public DepartmentConstructorAfter(String name, java.util.List<EmployeeConstructorAfter> staff) {
        super(name);
        this.staff = new java.util.ArrayList<>(staff);
    }

    public java.util.List<EmployeeConstructorAfter> getStaff() {
        return new java.util.ArrayList<>(staff);
    }
}

// Post-construction logic example
class EmployeeBefore {
    private String name;
    private int grade;

    public EmployeeBefore(String name) {
        this.name = name;
    }

    protected boolean isPrivileged() {
        return false;
    }

    protected void assignCar() {
        // 차량 할당 로직
    }
}

class ManagerBefore extends EmployeeBefore {
    private int grade;

    public ManagerBefore(String name, int grade) {
        super(name);
        this.grade = grade;

        // grade 설정 이후에만 실행 가능
        if (isPrivileged())
            assignCar();
    }

    @Override
    protected boolean isPrivileged() {
        return grade > 4;
    }
}

class EmployeeAfter {
    private String name;

    public EmployeeAfter(String name) {
        this.name = name;
    }

    protected boolean isPrivileged() {
        return false;
    }

    protected void assignCar() {
        // 차량 할당 로직
    }

    protected void finishConstruction() {
        if (isPrivileged())
            assignCar();
    }
}

class ManagerAfter extends EmployeeAfter {
    private int grade;

    public ManagerAfter(String name, int grade) {
        super(name);
        this.grade = grade;
        finishConstruction();
    }

    @Override
    protected boolean isPrivileged() {
        return grade > 4;
    }
}

public class PullUpConstructorBody {
    // 이 클래스는 리팩토링 예제를 담는 컨테이너 역할만 합니다
}