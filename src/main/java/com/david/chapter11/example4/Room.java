package com.david.chapter11.example4;

/**
 * 방 클래스
 */
public class Room {
    private NumberRange daysTempRange;
    private String name;
    
    public Room(String name, NumberRange daysTempRange) {
        this.name = name;
        this.daysTempRange = daysTempRange;
    }
    
    public String getName() {
        return name;
    }
    
    public NumberRange getDaysTempRange() {
        return daysTempRange;
    }
} 