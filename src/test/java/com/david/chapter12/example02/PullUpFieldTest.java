package com.david.chapter12.example02;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PullUpFieldTest {

    @Test
    public void testNameFieldBeforeRefactoring() {
        // 리팩토링 전 테스트
        SalesmanBefore salesman = new SalesmanBefore("John");
        EngineerBefore engineer = new EngineerBefore("Mary");

        assertEquals("John", salesman.getName());
        assertEquals("Mary", engineer.getName());
    }

    @Test
    public void testNameFieldAfterRefactoring() {
        // 리팩토링 후 테스트
        SalesmanAfter salesman = new SalesmanAfter("John");
        EngineerAfter engineer = new EngineerAfter("Mary");

        // 두 클래스 모두 상위 클래스에서 구현된 getName() 메서드를 사용
        assertEquals("John", salesman.getName());
        assertEquals("Mary", engineer.getName());
    }

    @Test
    public void testPolymorphismAfterRefactoring() {
        // 리팩토링 후에는 EmployeeParentAfter 타입으로 일관되게 처리 가능
        EmployeeParentAfter[] employees = new EmployeeParentAfter[] {
                new SalesmanAfter("John"),
                new EngineerAfter("Mary")
        };

        assertEquals("John", employees[0].getName());
        assertEquals("Mary", employees[1].getName());
    }
}