package com.david.chapter12.example09;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CollapseHierarchyTest {

    @Test
    public void testBeforeRefactoring() {
        // 리팩토링 전 테스트
        PartyCollapseBefore party = new PartyCollapseBefore("일반 파티");
        DepartmentCollapseBefore department = new DepartmentCollapseBefore("개발부서", 10000, 5);

        // 상위 클래스인 Party 객체는 name만 가짐
        assertEquals("일반 파티", party.getName());

        // Department는 상속받은 name과 자체 필드 2개를 가짐
        assertEquals("개발부서", department.getName());
        assertEquals(10000, department.getBudget());
        assertEquals(5, department.getHeadCount());
        assertEquals(2000, department.getBudgetPerHead());

        // 다형성 테스트
        PartyCollapseBefore polyParty = department;
        assertEquals("개발부서", polyParty.getName());
        // 다형성을 통해 Party 타입으로는 Department 전용 메서드에 접근 불가
        // budget, headCount 등에 접근하려면 캐스팅 필요
    }

    @Test
    public void testAfterRefactoring() {
        // 리팩토링 후 테스트
        DepartmentCollapseAfter department = new DepartmentCollapseAfter("개발부서", 10000, 5);

        // 모든 필드가 하나의 클래스에 통합됨
        assertEquals("개발부서", department.getName());
        assertEquals(10000, department.getBudget());
        assertEquals(5, department.getHeadCount());
        assertEquals(2000, department.getBudgetPerHead());

        // 더 이상 Party 클래스 없음 - 계층 구조 단순화됨
    }

    @Test
    public void testEdgeCases() {
        // 특수 케이스 테스트
        DepartmentCollapseBefore departmentBefore = new DepartmentCollapseBefore("빈 부서", 1000, 0);
        DepartmentCollapseAfter departmentAfter = new DepartmentCollapseAfter("빈 부서", 1000, 0);

        // 0으로 나누기 예외 처리 확인 (동작 동일성 확인)
        assertEquals(0, departmentBefore.getBudgetPerHead());
        assertEquals(0, departmentAfter.getBudgetPerHead());
    }
}