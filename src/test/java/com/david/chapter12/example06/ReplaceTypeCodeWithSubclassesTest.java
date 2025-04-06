package com.david.chapter12.example06;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.function.Executable;

public class ReplaceTypeCodeWithSubclassesTest {

    @Test
    public void testEmployeeTypeBefore() {
        EmployeeTypeBefore engineer = new EmployeeTypeBefore("김엔지니어", "engineer");
        EmployeeTypeBefore manager = new EmployeeTypeBefore("박매니저", "manager");
        EmployeeTypeBefore salesman = new EmployeeTypeBefore("이세일즈", "salesman");

        // 타입 검증
        assertEquals("engineer", engineer.getType());
        assertEquals("manager", manager.getType());
        assertEquals("salesman", salesman.getType());

        // 보너스 계산 검증
        assertEquals(50, engineer.getBonus());
        assertEquals(100, manager.getBonus());
        assertEquals(200, salesman.getBonus());
    }

    @Test
    public void testEmployeeTypeBeforeInvalidType() {
        assertThrows(IllegalArgumentException.class, () -> {
            new EmployeeTypeBefore("잘못된타입", "invalid");
        });
    }

    @Test
    public void testEmployeeTypeAfter() {
        EmployeeTypeAfter engineer = new EngineerTypeAfter("김엔지니어");
        EmployeeTypeAfter manager = new ManagerTypeAfter("박매니저");
        EmployeeTypeAfter salesman = new SalesmanTypeAfter("이세일즈");

        // 타입 검증
        assertEquals("engineer", engineer.getType());
        assertEquals("manager", manager.getType());
        assertEquals("salesman", salesman.getType());

        // 보너스 계산 검증
        assertEquals(50, engineer.getBonus());
        assertEquals(100, manager.getBonus());
        assertEquals(200, salesman.getBonus());
    }

    @Test
    public void testEmployeeTypeAfterFactoryMethod() {
        EmployeeTypeAfter engineer = EmployeeTypeAfter.create("김엔지니어", "engineer");
        EmployeeTypeAfter manager = EmployeeTypeAfter.create("박매니저", "manager");
        EmployeeTypeAfter salesman = EmployeeTypeAfter.create("이세일즈", "salesman");

        // 타입 검증
        assertEquals("engineer", engineer.getType());
        assertEquals("manager", manager.getType());
        assertEquals("salesman", salesman.getType());

        // 다형적 메서드 호출 검증
        assertEquals(50, engineer.getBonus());
        assertEquals(100, manager.getBonus());
        assertEquals(200, salesman.getBonus());
    }

    @Test
    public void testEmployeeTypeAfterFactoryMethodInvalidType() {
        assertThrows(IllegalArgumentException.class, () -> {
            EmployeeTypeAfter.create("잘못된타입", "invalid");
        });
    }

    @Test
    public void testEmployeeIndirectBefore() {
        EmployeeIndirectBefore engineer = new EmployeeIndirectBefore("김엔지니어", "engineer");
        EmployeeIndirectBefore manager = new EmployeeIndirectBefore("박매니저", "manager");
        EmployeeIndirectBefore salesman = new EmployeeIndirectBefore("이세일즈", "salesman");

        // 타입 검증
        assertEquals("engineer", engineer.getType());
        assertEquals("manager", manager.getType());
        assertEquals("salesman", salesman.getType());

        // 보너스 계산 검증
        assertEquals(50, engineer.getBonus());
        assertEquals(100, manager.getBonus());
        assertEquals(200, salesman.getBonus());
    }

    @Test
    public void testEmployeeIndirectAfter() {
        EmployeeIndirectAfter engineer = new EmployeeIndirectAfter("김엔지니어", "engineer");
        EmployeeIndirectAfter manager = new EmployeeIndirectAfter("박매니저", "manager");
        EmployeeIndirectAfter salesman = new EmployeeIndirectAfter("이세일즈", "salesman");

        // 타입 검증
        assertEquals("engineer", engineer.getType());
        assertEquals("manager", manager.getType());
        assertEquals("salesman", salesman.getType());

        // 보너스 계산 검증(위임된 타입 클래스를 통해 계산)
        assertEquals(50, engineer.getBonus());
        assertEquals(100, manager.getBonus());
        assertEquals(200, salesman.getBonus());
    }

    @Test
    public void testEmployeeIndirectAfterInvalidType() {
        assertThrows(IllegalArgumentException.class, () -> {
            new EmployeeIndirectAfter("잘못된타입", "invalid");
        });
    }
}