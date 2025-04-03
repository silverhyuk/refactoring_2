package com.david.chapter11.example8;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EmployeeTest {
    
    @Test
    void constructor_createsEmployee_withNameAndTypeCode() {
        // 리팩토링 전: 생성자 직접 호출
        Employee engineer = new Employee("홍길동", "E");
        
        assertEquals("홍길동", engineer.getName());
        assertEquals("Engineer", engineer.getType());
        assertEquals("E", engineer.getTypeCode());
    }
    
    @Test
    void factoryFunction_createsEmployee_withNameAndTypeCode() {
        // 리팩토링 후: 팩터리 메서드 사용
        Employee employee = Employee.createEmployee("홍길동", "E");
        
        assertEquals("홍길동", employee.getName());
        assertEquals("Engineer", employee.getType());
        assertEquals("E", employee.getTypeCode());
    }
    
    @Test
    void factoryFunction_createsEngineer() {
        // 특정 역할에 대한 팩터리 메서드
        Employee engineer = Employee.createEngineer("홍길동");
        
        assertEquals("홍길동", engineer.getName());
        assertEquals("Engineer", engineer.getType());
        assertEquals("E", engineer.getTypeCode());
    }
    
    @Test
    void factoryFunction_createsManager() {
        Employee manager = Employee.createManager("김관리");
        
        assertEquals("김관리", manager.getName());
        assertEquals("Manager", manager.getType());
        assertEquals("M", manager.getTypeCode());
    }
    
    @Test
    void factoryFunction_createsSalesman() {
        Employee salesman = Employee.createSalesman("박판매");
        
        assertEquals("박판매", salesman.getName());
        assertEquals("Salesman", salesman.getType());
        assertEquals("S", salesman.getTypeCode());
    }
    
    @Test
    void multipleFactoryFunctions_provideConsistentResults() {
        // 같은 타입 코드로 생성한 객체는 같은 타입을 가져야 함
        Employee employee1 = Employee.createEmployee("직원1", "E");
        Employee employee2 = Employee.createEngineer("직원2");
        
        assertEquals(employee1.getType(), employee2.getType());
        assertEquals(employee1.getTypeCode(), employee2.getTypeCode());
    }
} 