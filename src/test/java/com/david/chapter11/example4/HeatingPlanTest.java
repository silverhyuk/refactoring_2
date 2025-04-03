package com.david.chapter11.example4;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HeatingPlanTest {
    
    private HeatingPlan heatingPlan;
    private Room room;
    
    @BeforeEach
    void setUp() {
        // 난방계획: 18도에서 22도까지 허용
        heatingPlan = new HeatingPlan(new NumberRange(18.0, 22.0));
        
        // 방: 16도에서 20도까지 온도 변화
        room = new Room("거실", new NumberRange(16.0, 20.0));
    }
    
    @Test
    void originalWithinRange_returnsFalseWhenOutside() {
        // 범위에서 벗어남 (아래쪽 경계)
        assertFalse(heatingPlan.withinRange(16.0, 20.0));
    }
    
    @Test
    void originalWithinRange_returnsTrueWhenInside() {
        // 범위 내
        assertTrue(heatingPlan.withinRange(18.0, 21.0));
    }
    
    @Test
    void refactoredWithinRange_worksSameAsOriginal() {
        NumberRange roomRange = new NumberRange(16.0, 20.0);
        // 원래 메서드와 리팩토링된 메서드의 결과 비교
        boolean originalResult = heatingPlan.withinRange(roomRange.getLow(), roomRange.getHigh());
        boolean refactoredResult = heatingPlan.withinRange(roomRange);
        
        // 같은 결과를 반환해야 함
        assertTrue(originalResult == refactoredResult);
    }
    
    @Test
    void refactoredWithinRange_usingRoomObject() {
        // 객체 통째로 전달하는 방식 테스트 (클라이언트 코드)
        assertFalse(heatingPlan.withinRange(room.getDaysTempRange()));
        
        // 범위 내인 경우
        Room warmRoom = new Room("따뜻한 방", new NumberRange(18.0, 21.0));
        assertTrue(heatingPlan.withinRange(warmRoom.getDaysTempRange()));
    }
} 