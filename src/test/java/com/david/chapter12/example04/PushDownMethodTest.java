package com.david.chapter12.example04;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PushDownMethodTest {

    @Test
    public void testQuotaReportBeforeRefactoring() {
        // 리팩토링 전 테스트
        EngineerPushDownBefore engineer = new EngineerPushDownBefore("John");
        SalesmanPushDownBefore salesman = new SalesmanPushDownBefore("Mary");

        // 엔지니어에게는 의미 없는 메서드가 상속됨
        assertEquals("Quota not applicable", engineer.getQuotaReport());
        assertEquals("Sales quota met", salesman.getQuotaReport());
    }

    @Test
    public void testQuotaReportAfterRefactoring() {
        // 리팩토링 후 테스트
        EngineerPushDownAfter engineer = new EngineerPushDownAfter("John");
        SalesmanPushDownAfter salesman = new SalesmanPushDownAfter("Mary");

        // 엔지니어는 더 이상 할당량 보고서 메서드를 가지지 않음
        // 컴파일 에러를 유발함: engineer.getQuotaReport()

        // 영업사원만 할당량 보고서 메서드를 가짐
        assertEquals("Sales quota met", salesman.getQuotaReport());
    }

    @Test
    public void testTypeSpecificBehavior() {
        // 리팩토링 후에는 타입 검사가 필요할 수 있음
        EmployeePushDownAfter engineer = new EngineerPushDownAfter("John");
        EmployeePushDownAfter salesman = new SalesmanPushDownAfter("Mary");

        // 타입 확인 후 메서드 호출
        if (salesman instanceof SalesmanPushDownAfter) {
            String quotaReport = ((SalesmanPushDownAfter) salesman).getQuotaReport();
            assertEquals("Sales quota met", quotaReport);
        }

        // 엔지니어는 해당 메서드가 없으므로 타입 검사에서 통과하지 않음
        assertFalse(engineer instanceof SalesmanPushDownAfter);
    }
}