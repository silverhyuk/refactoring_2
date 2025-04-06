package com.david.chapter12.example11;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

// 리팩토링 전: 잘못된 상속 관계
class CatalogItemBefore {
    private final int id;
    private final String title;
    private final List<String> tags;

    public CatalogItemBefore(int id, String title, List<String> tags) {
        this.id = id;
        this.title = title;
        this.tags = new ArrayList<>(tags);
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean hasTag(String tag) {
        return tags.contains(tag);
    }
}

class ScrollBefore extends CatalogItemBefore {
    private final LocalDate lastCleaned;

    public ScrollBefore(int id, String title, List<String> tags, LocalDate lastCleaned) {
        super(id, title, tags);
        this.lastCleaned = lastCleaned;
    }

    public boolean needsCleaning(LocalDate today) {
        long days = ChronoUnit.DAYS.between(lastCleaned, today);
        int threshold = hasTag("revered") ? 700 : 1500;
        return days > threshold;
    }
}

// 리팩토링 후: 위임을 사용한 버전
class CatalogItemAfter {
    private final int id;
    private final String title;
    private final List<String> tags;

    public CatalogItemAfter(int id, String title, List<String> tags) {
        this.id = id;
        this.title = title;
        this.tags = new ArrayList<>(tags);
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean hasTag(String tag) {
        return tags.contains(tag);
    }
}

class ScrollAfter {
    private final int id;
    private final CatalogItemAfter catalogItem;
    private final LocalDate lastCleaned;

    public ScrollAfter(int id, CatalogItemAfter catalogItem, LocalDate lastCleaned) {
        this.id = id;
        this.catalogItem = catalogItem;
        this.lastCleaned = lastCleaned;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return catalogItem.getTitle();
    }

    public boolean hasTag(String tag) {
        return catalogItem.hasTag(tag);
    }

    public boolean needsCleaning(LocalDate today) {
        long days = ChronoUnit.DAYS.between(lastCleaned, today);
        int threshold = hasTag("revered") ? 700 : 1500;
        return days > threshold;
    }
}

// 카탈로그 아이템 저장소 - 여러 Scroll이 동일한 CatalogItem 공유
class CatalogItemRepository {
    private final List<CatalogItemAfter> items = new ArrayList<>();

    public void add(CatalogItemAfter item) {
        items.add(item);
    }

    public CatalogItemAfter findById(int id) {
        return items.stream()
                .filter(item -> item.getId() == id)
                .findFirst()
                .orElse(null);
    }
}

public class ReplaceSuperclassWithDelegate {
    // 이 클래스는 리팩토링 예제를 담는 컨테이너 역할만 합니다
}