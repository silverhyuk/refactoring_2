package com.david.chapter11.example1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SecuritySystemTest {
    
    private SecuritySystem securitySystem;
    private List<String> peopleWithDon;
    private List<String> peopleWithJohn;
    private List<String> normalPeople;
    
    @BeforeEach
    void setUp() {
        securitySystem = new SecuritySystem();
        peopleWithDon = Arrays.asList("Alice", "Bob", "Don", "Eve");
        peopleWithJohn = Arrays.asList("Alice", "John", "Eve");
        normalPeople = Arrays.asList("Alice", "Bob", "Eve");
    }
    
    @Test
    void originalFunction_findsAndAlertsDon() {
        String result = securitySystem.alertForMiscreant(peopleWithDon);
        assertEquals("Don", result);
    }
    
    @Test
    void originalFunction_findsAndAlertsJohn() {
        String result = securitySystem.alertForMiscreant(peopleWithJohn);
        assertEquals("John", result);
    }
    
    @Test
    void originalFunction_returnsEmptyWhenNoSuspect() {
        String result = securitySystem.alertForMiscreant(normalPeople);
        assertTrue(result.isEmpty());
    }
    
    @Test
    void queryFunction_findsDon() {
        String result = securitySystem.findMiscreant(peopleWithDon);
        assertEquals("Don", result);
    }
    
    @Test
    void queryFunction_findsJohn() {
        String result = securitySystem.findMiscreant(peopleWithJohn);
        assertEquals("John", result);
    }
    
    @Test
    void queryFunction_returnsEmptyWhenNoSuspect() {
        String result = securitySystem.findMiscreant(normalPeople);
        assertTrue(result.isEmpty());
    }
} 