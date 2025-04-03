package com.david.chapter11.example8;

import java.util.HashMap;
import java.util.Map;

/**
 * 예제 8: 생성자를 팩터리 함수로 바꾸기 (Replace Constructor with Factory Function)
 */
public class Employee {
    private String name;
    private String typeCode;
    
    /**
     * 리팩토링 전: public 생성자를 직접 호출
     */
    public Employee(String name, String typeCode) {
        this.name = name;
        this.typeCode = typeCode;
    }
    
    /**
     * 리팩토링 후: 생성자를 private으로 변경하고 팩터리 함수 추가
     */
    private Employee(String name, String typeCode, boolean ignoreForJavaCompliance) {
        this.name = name;
        this.typeCode = typeCode;
    }
    
    public static Employee createEmployee(String name, String typeCode) {
        return new Employee(name, typeCode, true);
    }
    
    public static Employee createEngineer(String name) {
        return new Employee(name, "E", true);
    }
    
    public static Employee createManager(String name) {
        return new Employee(name, "M", true);
    }
    
    public static Employee createSalesman(String name) {
        return new Employee(name, "S", true);
    }
    
    public String getName() {
        return name;
    }
    
    public String getType() {
        return legalTypeCodes.get(typeCode);
    }
    
    public String getTypeCode() {
        return typeCode;
    }
    
    // 타입 코드 매핑
    private static final Map<String, String> legalTypeCodes;
    static {
        legalTypeCodes = new HashMap<>();
        legalTypeCodes.put("E", "Engineer");
        legalTypeCodes.put("M", "Manager");
        legalTypeCodes.put("S", "Salesman");
    }
} 