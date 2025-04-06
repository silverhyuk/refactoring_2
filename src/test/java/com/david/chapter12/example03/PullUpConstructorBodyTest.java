package com.david.chapter12.example03;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class PullUpConstructorBodyTest {

    @Test
    public void testConstructorBeforeRefactoring() {
        // 리팩토링 전 테스트
        EmployeeConstructorBefore employee = new EmployeeConstructorBefore("John", 123, 5000);

        List<EmployeeConstructorBefore> staff = new ArrayList<>();
        staff.add(employee);
        DepartmentConstructorBefore department = new DepartmentConstructorBefore("Engineering", staff);

        assertEquals("John", employee.getName());
        assertEquals("Engineering", department.getName());
        assertEquals(employee, department.getStaff().get(0));
    }

    @Test
    public void testConstructorAfterRefactoring() {
        // 리팩토링 후 테스트
        EmployeeConstructorAfter employee = new EmployeeConstructorAfter("John", 123, 5000);

        List<EmployeeConstructorAfter> staff = new ArrayList<>();
        staff.add(employee);
        DepartmentConstructorAfter department = new DepartmentConstructorAfter("Engineering", staff);

        // 상위 클래스의 name 필드와 getName() 메서드 사용
        assertEquals("John", employee.getName());
        assertEquals("Engineering", department.getName());
        assertEquals(employee, department.getStaff().get(0));
    }

    @Test
    public void testPostConstructionLogic() {
        // 권한 있는 매니저 (grade > 4)
        ManagerAfter seniorManager = new ManagerAfter("Senior", 5);
        // 권한 없는 매니저 (grade <= 4)
        ManagerAfter juniorManager = new ManagerAfter("Junior", 3);

        // 실제 할당 메서드는 테스트할 수 없으나, 호출 여부를 확인할 수 있는
        // 메서드가 있다면 검증 가능했을 것입니다.
        // 현재는 구조 테스트만 진행 (컴파일 확인)
        assertTrue(seniorManager.isPrivileged());
        assertFalse(juniorManager.isPrivileged());
    }
}