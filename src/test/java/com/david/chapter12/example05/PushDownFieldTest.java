package com.david.chapter12.example05;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PushDownFieldTest {

    @Test
    public void testSalesQuotaBeforeRefactoring() {
        // 리팩토링 전 테스트
        EngineerFieldBefore engineer = new EngineerFieldBefore("John");
        SalesmanFieldBefore salesman = new SalesmanFieldBefore("Mary", 10000);

        // 엔지니어에게는 의미 없는 필드가 상속됨
        assertEquals(0, engineer.getSalesQuota());
        assertEquals(10000, salesman.getSalesQuota());

        // salesQuota 사용 예시
        assertTrue(salesman.isQuotaMet(10000));
        assertFalse(salesman.isQuotaMet(9999));
    }

    @Test
    public void testSalesQuotaAfterRefactoring() {
        // 리팩토링 후 테스트
        EngineerFieldAfter engineer = new EngineerFieldAfter("John");
        SalesmanFieldAfter salesman = new SalesmanFieldAfter("Mary", 10000);

        // 엔지니어는 더 이상 salesQuota 필드나 관련 메서드가 없음
        // 컴파일 에러를 유발함: engineer.getSalesQuota()

        // 영업사원만 salesQuota 필드를 가짐
        assertEquals(10000, salesman.getSalesQuota());

        // salesQuota 사용 예시
        assertTrue(salesman.isQuotaMet(10000));
        assertFalse(salesman.isQuotaMet(9999));
    }

    @Test
    public void testFieldInheritance() {
        // 리팩토링 전에는 모든 하위 클래스가 salesQuota에 접근 가능
        EmployeeFieldBefore engineerBefore = new EngineerFieldBefore("John");
        assertEquals(0, engineerBefore.getSalesQuota());

        // 리팩토링 후에는 부모 클래스 타입으로 접근해도 salesQuota에 접근할 수 없음
        EmployeeFieldAfter salesmanAfter = new SalesmanFieldAfter("Mary", 10000);

        // 타입 확인 후 필드 값 검증
        if (salesmanAfter instanceof SalesmanFieldAfter) {
            int quota = ((SalesmanFieldAfter) salesmanAfter).getSalesQuota();
            assertEquals(10000, quota);
        }
    }
}