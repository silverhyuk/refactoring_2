package com.david.chapter11.example7;

/**
 * 예제 7: 세터 제거하기 (Remove Setting Method)
 */
public class Person {
    
    /**
     * 리팩토링 전: ID와 이름 모두 세터가 있는 클래스
     */
    public static class BeforeRefactoring {
        private String name;
        private String id;
        
        public BeforeRefactoring() { }
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public String getId() {
            return id;
        }
        
        public void setId(String id) {
            this.id = id;
        }
    }
    
    /**
     * 리팩토링 후: ID는 생성자에서만 설정 가능하도록 변경 (불변 필드)
     */
    public static class AfterRefactoring {
        private String name;
        private final String id;  // final로 변경하여 불변성 강조
        
        public AfterRefactoring(String id) {
            this.id = id;
        }
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public String getId() {
            return id;
        }
        // setId 메서드 제거
    }
} 