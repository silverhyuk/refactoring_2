package com.david.chapter12.example09;

// 리팩토링 전: 불필요하게 분리된 계층 구조
class PartyCollapseBefore {
    protected String name;

    public PartyCollapseBefore(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

class DepartmentCollapseBefore extends PartyCollapseBefore {
    private int budget;
    private int headCount;

    public DepartmentCollapseBefore(String name, int budget, int headCount) {
        super(name);
        this.budget = budget;
        this.headCount = headCount;
    }

    public int getBudget() {
        return budget;
    }

    public int getHeadCount() {
        return headCount;
    }

    public int getBudgetPerHead() {
        return headCount > 0 ? budget / headCount : 0;
    }
}

// 리팩토링 후: 계층 구조 합치기
class DepartmentCollapseAfter {
    private String name;
    private int budget;
    private int headCount;

    public DepartmentCollapseAfter(String name, int budget, int headCount) {
        this.name = name;
        this.budget = budget;
        this.headCount = headCount;
    }

    public String getName() {
        return name;
    }

    public int getBudget() {
        return budget;
    }

    public int getHeadCount() {
        return headCount;
    }

    public int getBudgetPerHead() {
        return headCount > 0 ? budget / headCount : 0;
    }
}

public class CollapseHierarchy {
    // 이 클래스는 리팩토링 예제를 담는 컨테이너 역할만 합니다
}