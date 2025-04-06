package com.david.chapter12.example07;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ReplaceTypeCodeWithStateStrategyTest {

    @Test
    public void testEmployeeStateBeforeCalculateSalary() {
        EmployeeStateBefore engineer = new EmployeeStateBefore("김엔지니어", "engineer", 50000);
        EmployeeStateBefore manager = new EmployeeStateBefore("박매니저", "manager", 50000);
        EmployeeStateBefore salesman = new EmployeeStateBefore("이세일즈", "salesman", 50000);

        // 기본 급여 + 직무별 추가 급여 계산 검증
        assertEquals(55000, engineer.calculateSalary()); // 50000 + 5000
        assertEquals(60000, manager.calculateSalary()); // 50000 + 10000
        assertEquals(60000, salesman.calculateSalary()); // 50000 + 8000 + 2000(commission)
    }

    @Test
    public void testEmployeeStateBeforeChangeType() {
        EmployeeStateBefore employee = new EmployeeStateBefore("김직원", "engineer", 50000);
        assertEquals("engineer", employee.getType());
        assertEquals(55000, employee.calculateSalary());

        // 직원 타입 변경
        employee.setType("manager");
        assertEquals("manager", employee.getType());
        assertEquals(60000, employee.calculateSalary());
    }

    @Test
    public void testEmployeeStateAfterCalculateSalary() {
        EmployeeStateAfter engineer = new EmployeeStateAfter("김엔지니어", "engineer", 50000);
        EmployeeStateAfter manager = new EmployeeStateAfter("박매니저", "manager", 50000);
        EmployeeStateAfter salesman = new EmployeeStateAfter("이세일즈", "salesman", 50000);

        // 기본 급여 + 직무별 추가 급여 계산 검증 (전략 패턴 적용)
        assertEquals(55000, engineer.calculateSalary()); // 50000 + 5000
        assertEquals(60000, manager.calculateSalary()); // 50000 + 10000
        assertEquals(60000, salesman.calculateSalary()); // 50000 + 8000 + 2000(commission)
    }

    @Test
    public void testEmployeeStateAfterChangeType() {
        EmployeeStateAfter employee = new EmployeeStateAfter("김직원", "engineer", 50000);
        assertEquals("engineer", employee.getTypeCode());
        assertEquals(55000, employee.calculateSalary());

        // 직원 타입 변경 (전략 교체)
        employee.setTypeCode("manager");
        assertEquals("manager", employee.getTypeCode());
        assertEquals(60000, employee.calculateSalary());
    }

    @Test
    public void testEmployeeTypeInterface() {
        EmployeeType engineer = EmployeeType.createEmployeeType("engineer");
        EmployeeType manager = EmployeeType.createEmployeeType("manager");
        EmployeeType salesman = EmployeeType.createEmployeeType("salesman");

        // 타입 코드 검증
        assertEquals("engineer", engineer.getTypeCode());
        assertEquals("manager", manager.getTypeCode());
        assertEquals("salesman", salesman.getTypeCode());

        // 급여 계산 검증
        assertEquals(55000, engineer.calculateSalary(50000)); // 50000 + 5000
        assertEquals(60000, manager.calculateSalary(50000)); // 50000 + 10000
        assertEquals(60000, salesman.calculateSalary(50000)); // 50000 + 8000 + 2000(commission)
    }

    @Test
    public void testEmployeeTypeFactory() {
        assertThrows(IllegalArgumentException.class, () -> {
            EmployeeType.createEmployeeType("invalid");
        });
    }
}