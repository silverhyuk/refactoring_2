package com.david.chapter09;

/**
 * 9.2 Rename Field (필드명 변경) 예제
 * 
 * 문제: 필드 이름이 명확하지 않으면 코드 이해와 유지보수가 어려워집니다.
 * 해결: 필드명을 더 명확한 이름으로 변경하여 코드의 가독성을 높입니다.
 */
public class RenameField {

    public static void main(String[] args) {
        // 리팩토링 전 사용 예제
        OrganizationBefore org1 = new OrganizationBefore("Acme Gooseberries", "GB");
        System.out.println("Before: " + org1.getName() + ", " + org1.getCountry());
        
        // 리팩토링 후 사용 예제
        OrganizationAfter org2 = new OrganizationAfter("Acme Gooseberries", "GB");
        System.out.println("After: " + org2.getTitle() + ", " + org2.getCountry());
    }
}

/**
 * 리팩토링 전: 'name' 필드가 모호함
 */
class OrganizationBefore {
    private String name; // 'name'이 조직의 '이름'인지 '직함(title)'인지 모호함
    private String country;

    public OrganizationBefore(String name, String country) {
        this.name = name;
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}

/**
 * 리팩토링 중간 단계: 내부적으로 필드명 변경 및 기존 메서드 유지
 */
class OrganizationIntermediate {
    private String _title; // 내부적으로 'name'을 '_title'로 변경
    private String country;

    public OrganizationIntermediate(String name, String country) {
        this._title = name; // 기존 생성자는 'name' 매개변수 사용
        this.country = country;
    }

    // 기존 메서드 유지
    public String getName() {
        return _title;
    }

    public void setName(String name) {
        this._title = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}

/**
 * 리팩토링 후: 필드명과 메서드명을 명확하게 변경
 */
class OrganizationAfter {
    private String title; // 'name'을 'title'로 변경
    private String country;

    public OrganizationAfter(String title, String country) {
        this.title = title;
        this.country = country;
    }

    public String getTitle() { // getName() -> getTitle()로 변경
        return title;
    }

    public void setTitle(String title) { // setName() -> setTitle()로 변경
        this.title = title;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}