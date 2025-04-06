package com.david.chapter12.example08;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ReplaceSubclassWithFieldTest {

    @Test
    public void testProductHierarchyBefore() {
        ProductBefore regular = new RegularProductBefore("일반 제품", 100.0);
        ProductBefore premium = new PremiumProductBefore("프리미엄 제품", 100.0);

        // 제품 타입 확인
        assertFalse(regular.isPremium());
        assertTrue(premium.isPremium());

        // 가격 계산 확인
        assertEquals(100.0, regular.getPrice(), 0.001);
        assertEquals(120.0, premium.getPrice(), 0.001); // 100 * 1.2 = 120
    }

    @Test
    public void testProductAfter() {
        ProductAfter regular = ProductAfter.createRegularProduct("일반 제품", 100.0);
        ProductAfter premium = ProductAfter.createPremiumProduct("프리미엄 제품", 100.0);

        // 제품 타입 확인
        assertFalse(regular.isPremium());
        assertTrue(premium.isPremium());

        // 가격 계산 확인
        assertEquals(100.0, regular.getPrice(), 0.001);
        assertEquals(120.0, premium.getPrice(), 0.001); // 100 * 1.2 = 120
    }

    @Test
    public void testEmployeeHierarchyBefore() {
        EmployeeBefore developer = new DeveloperBefore("김개발", 50000);
        EmployeeBefore manager = new ManagerBefore("박매니저", 50000);

        // 직무 타이틀 확인
        assertEquals("Developer", developer.getJobTitle());
        assertEquals("Manager", manager.getJobTitle());

        // 급여 계산 확인
        assertEquals(55000, developer.calculateSalary(), 0.001); // 50000 + 5000
        assertEquals(60000, manager.calculateSalary(), 0.001); // 50000 + 10000
    }

    @Test
    public void testEmployeeAfter() {
        EmployeeAfter developer = EmployeeAfter.createDeveloper("김개발", 50000);
        EmployeeAfter manager = EmployeeAfter.createManager("박매니저", 50000);

        // 직무 타이틀 확인
        assertEquals("Developer", developer.getJobTitle());
        assertEquals("Manager", manager.getJobTitle());

        // 급여 계산 확인
        assertEquals(55000, developer.calculateSalary(), 0.001); // 50000 + 5000
        assertEquals(60000, manager.calculateSalary(), 0.001); // 50000 + 10000
    }

    @Test
    public void testPolymorphicBehaviorMaintained() {
        // 리팩토링 전: 다형성 활용
        ProductBefore[] productsBefore = {
                new RegularProductBefore("일반 상품", 100.0),
                new PremiumProductBefore("프리미엄 상품", 100.0)
        };

        double totalPriceBefore = 0;
        for (ProductBefore product : productsBefore) {
            totalPriceBefore += product.getPrice();
        }
        assertEquals(220.0, totalPriceBefore, 0.001); // 100 + 120

        // 리팩토링 후: 필드를 사용하지만 동일한 동작 유지
        ProductAfter[] productsAfter = {
                ProductAfter.createRegularProduct("일반 상품", 100.0),
                ProductAfter.createPremiumProduct("프리미엄 상품", 100.0)
        };

        double totalPriceAfter = 0;
        for (ProductAfter product : productsAfter) {
            totalPriceAfter += product.getPrice();
        }
        assertEquals(220.0, totalPriceAfter, 0.001); // 100 + 120
    }
}