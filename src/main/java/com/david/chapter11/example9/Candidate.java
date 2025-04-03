package com.david.chapter11.example9;

/**
 * 후보자 클래스
 */
public class Candidate {
    private String name;
    private String originState;
    
    public Candidate(String name, String originState) {
        this.name = name;
        this.originState = originState;
    }
    
    public String getName() {
        return name;
    }
    
    public String getOriginState() {
        return originState;
    }
} 