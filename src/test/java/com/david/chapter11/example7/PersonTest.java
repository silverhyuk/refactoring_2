package com.david.chapter11.example7;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PersonTest {
    
    @Test
    void beforeRefactoring_canSetNameAndId() {
        Person.BeforeRefactoring person = new Person.BeforeRefactoring();
        
        // 빈 생성자로 객체를 생성한 후 세터로 설정
        person.setName("홍길동");
        person.setId("12345");
        
        // 제대로 설정되었는지 검증
        assertEquals("홍길동", person.getName());
        assertEquals("12345", person.getId());
    }
    
    @Test
    void beforeRefactoring_canChangeIdAfterCreation() {
        Person.BeforeRefactoring person = new Person.BeforeRefactoring();
        
        // ID를 처음 설정
        person.setId("12345");
        assertEquals("12345", person.getId());
        
        // ID를 나중에 변경 가능 (이는 바람직하지 않은 동작이지만, 변경 전에는 가능함)
        person.setId("67890");
        assertEquals("67890", person.getId());
    }
    
    @Test
    void afterRefactoring_mustSetIdAtCreation() {
        // 생성자에서 ID를 반드시 설정해야 함
        Person.AfterRefactoring person = new Person.AfterRefactoring("12345");
        
        // ID가 제대로 설정되었는지 검증
        assertNotNull(person.getId());
        assertEquals("12345", person.getId());
    }
    
    @Test
    void afterRefactoring_canSetNameAfterCreation() {
        Person.AfterRefactoring person = new Person.AfterRefactoring("12345");
        
        // 이름은 생성 후에도 설정 가능
        person.setName("홍길동");
        assertEquals("홍길동", person.getName());
    }
    
    @Test
    void afterRefactoring_cannotChangeIdAfterCreation() {
        Person.AfterRefactoring person = new Person.AfterRefactoring("12345");
        
        // ID는 생성 후에 변경 불가능 (컴파일 에러 발생)
        // person.setId("67890"); // 이 코드는 컴파일되지 않음
        
        // ID는 초기값을 유지
        assertEquals("12345", person.getId());
    }
} 