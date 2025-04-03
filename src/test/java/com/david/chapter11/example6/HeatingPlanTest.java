package com.david.chapter11.example6;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HeatingPlanTest {
    
    private HeatingPlan heatingPlan;
    
    @BeforeEach
    void setUp() {
        // 18도에서 26도 사이의 온도를 허용하는 난방 계획
        heatingPlan = new HeatingPlan(18.0, 26.0);
        // 테스트 시작 전에 전역 온도를 초기화
        Thermostat.setSelectedTemperature(22.0);
    }
    
    @Test
    void getTargetTemperature_returnsSelectedTemperature_whenInRange() {
        Thermostat.setSelectedTemperature(20.0); // 범위 내 온도
        assertEquals(20.0, heatingPlan.getTargetTemperature(), 0.01);
    }
    
    @Test
    void getTargetTemperature_returnsMaxTemperature_whenSelectedExceedsMax() {
        Thermostat.setSelectedTemperature(30.0); // 최대치 초과
        assertEquals(26.0, heatingPlan.getTargetTemperature(), 0.01); // 최대 26도로 제한됨
    }
    
    @Test
    void getTargetTemperature_returnsMinTemperature_whenSelectedBelowMin() {
        Thermostat.setSelectedTemperature(15.0); // 최소치 미달
        assertEquals(18.0, heatingPlan.getTargetTemperature(), 0.01); // 최소 18도로 제한됨
    }
    
    @Test
    void targetTemperature_withParameter_returnsSelectedTemperature_whenInRange() {
        assertEquals(20.0, heatingPlan.targetTemperature(20.0), 0.01);
    }
    
    @Test
    void targetTemperature_withParameter_returnsMaxTemperature_whenSelectedExceedsMax() {
        assertEquals(26.0, heatingPlan.targetTemperature(30.0), 0.01);
    }
    
    @Test
    void targetTemperature_withParameter_returnsMinTemperature_whenSelectedBelowMin() {
        assertEquals(18.0, heatingPlan.targetTemperature(15.0), 0.01);
    }
    
    @Test
    void refactoredTargetTemperature_matchesOriginalVersion_forDifferentInputs() {
        // 리팩토링 전과 후의 결과를 다양한 입력값으로 비교
        
        // 범위 내 입력
        Thermostat.setSelectedTemperature(20.0);
        assertEquals(heatingPlan.getTargetTemperature(), 
                     heatingPlan.targetTemperature(Thermostat.selectedTemperature), 0.01);
        
        // 범위 초과 입력
        Thermostat.setSelectedTemperature(30.0);
        assertEquals(heatingPlan.getTargetTemperature(), 
                     heatingPlan.targetTemperature(Thermostat.selectedTemperature), 0.01);
        
        // 범위 미달 입력
        Thermostat.setSelectedTemperature(15.0);
        assertEquals(heatingPlan.getTargetTemperature(), 
                     heatingPlan.targetTemperature(Thermostat.selectedTemperature), 0.01);
    }
} 