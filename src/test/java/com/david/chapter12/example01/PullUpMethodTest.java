package com.david.chapter12.example01;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PullUpMethodTest {

    @Test
    public void testAnnualCostBeforeRefactoring() {
        // 리팩토링 전 테스트
        EmployeeBefore employee = new EmployeeBefore(1000);
        DepartmentBefore department = new DepartmentBefore(5000);

        // 두 클래스의 연간 비용 계산 메서드 이름이 다름
        assertEquals(12000, employee.getAnnualCost());
        assertEquals(60000, department.getTotalAnnualCost());
    }

    @Test
    public void testAnnualCostAfterRefactoring() {
        // 리팩토링 후 테스트
        EmployeeAfter employee = new EmployeeAfter(1000);
        DepartmentAfter department = new DepartmentAfter(5000);

        // 두 클래스 모두 상위 클래스의 동일한 메서드를 사용
        assertEquals(12000, employee.getAnnualCost());
        assertEquals(60000, department.getAnnualCost());
    }

    @Test
    public void testPolymorphismAfterRefactoring() {
        // 리팩토링 후에는 다형성을 활용할 수 있음
        PartyAfter[] parties = new PartyAfter[] {
                new EmployeeAfter(1000),
                new DepartmentAfter(5000)
        };

        int totalAnnualCost = 0;
        for (PartyAfter party : parties) {
            totalAnnualCost += party.getAnnualCost();
        }

        assertEquals(72000, totalAnnualCost);
    }
}