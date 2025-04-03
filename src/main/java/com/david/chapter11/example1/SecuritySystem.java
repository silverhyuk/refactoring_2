package com.david.chapter11.example1;

import java.util.List;

/**
 * 예제 1: 질의 함수와 변경 함수 분리하기 (Separate Query from Modifier)
 * 리팩토링 전의 코드: 값을 반환하면서 부작용도 있는 함수
 */
public class SecuritySystem {
    
    /**
     * 리팩토링 전: 값을 반환하면서 부작용도 있는 함수
     * 수상한 사람을 찾으면서 동시에 알람을 울리는 함수
     */
    public String alertForMiscreant(List<String> people) {
        for (String p : people) {
            if (p.equals("Don")) {
                setOffAlarms();
                return "Don";
            }
            if (p.equals("John")) {
                setOffAlarms();
                return "John";
            }
        }
        return "";
    }
    
    /**
     * 리팩토링 후: 질의 함수 - 수상한 사람을 찾는 순수 함수
     */
    public String findMiscreant(List<String> people) {
        for (String p : people) {
            if (p.equals("Don") || p.equals("John")) {
                return p;
            }
        }
        return "";
    }
    
    /**
     * 리팩토링 후: 변경 함수 - 알람만 울리는 함수
     */
    public void alertMiscreant(List<String> people) {
        if (!findMiscreant(people).isEmpty()) {
            setOffAlarms();
        }
    }
    
    private void setOffAlarms() {
        System.out.println("경보가 울렸습니다!");
    }
} 