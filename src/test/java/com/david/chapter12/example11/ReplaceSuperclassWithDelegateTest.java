package com.david.chapter12.example11;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class ReplaceSuperclassWithDelegateTest {

    @Test
    public void testBeforeRefactoring() {
        // 리팩토링 전 테스트
        List<String> tags = Arrays.asList("ancient", "revered");
        LocalDate lastCleaned = LocalDate.of(2021, 5, 15);

        // ScrollBefore가 CatalogItemBefore를 상속
        ScrollBefore scroll = new ScrollBefore(1, "Dead Sea Scrolls", tags, lastCleaned);

        // CatalogItem으로부터 상속받은 메서드
        assertEquals(1, scroll.getId());
        assertEquals("Dead Sea Scrolls", scroll.getTitle());
        assertTrue(scroll.hasTag("revered"));
        assertFalse(scroll.hasTag("damaged"));

        // Scroll 자체 메서드
        LocalDate today = LocalDate.of(2023, 5, 15); // 약 2년 후
        assertTrue(scroll.needsCleaning(today)); // 700일 지나면 청소 필요 (revered)

        // 문제점: Scroll은 실제 물리적 객체인데, CatalogItem은 메타데이터.
        // is-a 관계가 아닌데도 상속을 사용하고 있음
    }

    @Test
    public void testAfterRefactoring() {
        // 리팩토링 후 테스트
        List<String> tags = Arrays.asList("ancient", "revered");

        // CatalogItem을 별도 객체로 생성
        CatalogItemAfter catalogItem = new CatalogItemAfter(1, "Dead Sea Scrolls", tags);

        // Scroll이 CatalogItem을 위임으로 참조
        LocalDate lastCleaned = LocalDate.of(2021, 5, 15);
        ScrollAfter scroll = new ScrollAfter(1, catalogItem, lastCleaned);

        // 위임을 통해 접근
        assertEquals(1, scroll.getId());
        assertEquals("Dead Sea Scrolls", scroll.getTitle());
        assertTrue(scroll.hasTag("revered"));
        assertFalse(scroll.hasTag("damaged"));

        // Scroll 자체 메서드
        LocalDate today = LocalDate.of(2023, 5, 15);
        assertTrue(scroll.needsCleaning(today));
    }

    @Test
    public void testSharedCatalogItems() {
        // 공유 CatalogItem 테스트
        CatalogItemRepository repository = new CatalogItemRepository();

        // 메타데이터 생성 및 저장소에 추가
        List<String> tags1 = Arrays.asList("ancient", "revered");
        CatalogItemAfter item1 = new CatalogItemAfter(1, "Dead Sea Scrolls", tags1);
        repository.add(item1);

        List<String> tags2 = Arrays.asList("ancient", "damaged");
        CatalogItemAfter item2 = new CatalogItemAfter(2, "Egyptian Papyrus", tags2);
        repository.add(item2);

        // 서로 다른 두 물리적 스크롤이 동일한 카탈로그 항목 참조
        ScrollAfter scroll1 = new ScrollAfter(101, repository.findById(1), LocalDate.of(2021, 5, 15));
        ScrollAfter scroll2 = new ScrollAfter(102, repository.findById(1), LocalDate.of(2022, 10, 10));

        // 다른 종류의 스크롤
        ScrollAfter scroll3 = new ScrollAfter(103, repository.findById(2), LocalDate.of(2021, 3, 1));

        // 동일한 메타데이터 공유 확인
        assertEquals("Dead Sea Scrolls", scroll1.getTitle());
        assertEquals("Dead Sea Scrolls", scroll2.getTitle());
        assertEquals("Egyptian Papyrus", scroll3.getTitle());

        // ID는 서로 다름 (물리적 객체의 ID)
        assertEquals(101, scroll1.getId());
        assertEquals(102, scroll2.getId());

        // 같은 종류의 스크롤이라도 청소 필요 여부는 다를 수 있음
        LocalDate today = LocalDate.of(2023, 5, 15);
        assertTrue(scroll1.needsCleaning(today)); // 700일 이상 지남
        assertFalse(scroll2.needsCleaning(today)); // 700일 미만

        // revered 태그가 없는 scroll3는 더 긴 청소 주기 (1500일)
        assertFalse(scroll3.needsCleaning(today)); // 1500일 미만
    }
}