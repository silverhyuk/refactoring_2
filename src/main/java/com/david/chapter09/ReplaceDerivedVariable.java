package com.david.chapter09;

import java.util.ArrayList;
import java.util.List;

/**
 * 9.3 Replace Derived Variable with Query (파생 변수를 계산으로 대체) 예제
 * 
 * 문제: 파생 변수(다른 데이터에서 계산 가능한 변수)는 데이터 불일치 위험을 증가시킵니다.
 * 해결: 필요할 때마다 계산하는 메서드로 대체하여 데이터 일관성을 유지합니다.
 */
public class ReplaceDerivedVariable {

    public static void main(String[] args) {
        // 리팩토링 전 사용 예제
        ProductionPlanBefore planBefore = new ProductionPlanBefore();
        planBefore.applyAdjustment(new Adjustment(10));
        planBefore.applyAdjustment(new Adjustment(20));
        System.out.println("Before: Production = " + planBefore.getProduction());
        
        // 리팩토링 후 사용 예제
        ProductionPlanAfter planAfter = new ProductionPlanAfter();
        planAfter.applyAdjustment(new Adjustment(10));
        planAfter.applyAdjustment(new Adjustment(20));
        System.out.println("After: Production = " + planAfter.getProduction());
        
        // 초기값이 있는 경우 예제
        ProductionPlanWithInitial planWithInitial = new ProductionPlanWithInitial(100);
        planWithInitial.applyAdjustment(new Adjustment(10));
        planWithInitial.applyAdjustment(new Adjustment(20));
        System.out.println("With Initial: Production = " + planWithInitial.getProduction());
    }
}

/**
 * 조정값 클래스
 */
class Adjustment {
    private int amount;

    public Adjustment(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }
}

/**
 * 리팩토링 전: 파생 변수(production)를 직접 관리
 */
class ProductionPlanBefore {
    private int production;
    private List<Adjustment> adjustments = new ArrayList<>();

    public ProductionPlanBefore() {
        this.production = 0;
    }

    public int getProduction() {
        return production;
    }

    public void applyAdjustment(Adjustment adjustment) {
        adjustments.add(adjustment);
        production += adjustment.getAmount(); // 파생 변수 직접 수정
    }
}

/**
 * 리팩토링 중간 단계: 검증 코드 추가
 */
class ProductionPlanIntermediate {
    private int production;
    private List<Adjustment> adjustments = new ArrayList<>();

    public ProductionPlanIntermediate() {
        this.production = 0;
    }

    public int getProduction() {
        // 검증: 계산된 값과 저장된 값이 일치하는지 확인
        assert production == calculateProduction() : "Production 값이 일치하지 않습니다.";
        return production;
    }
    
    private int calculateProduction() {
        return adjustments.stream().mapToInt(Adjustment::getAmount).sum();
    }

    public void applyAdjustment(Adjustment adjustment) {
        adjustments.add(adjustment);
        production += adjustment.getAmount();
    }
}

/**
 * 리팩토링 후: 파생 변수 제거하고 계산으로 대체
 */
class ProductionPlanAfter {
    private List<Adjustment> adjustments = new ArrayList<>();

    public int getProduction() {
        return calculateProduction();
    }
    
    private int calculateProduction() {
        return adjustments.stream().mapToInt(Adjustment::getAmount).sum();
    }

    public void applyAdjustment(Adjustment adjustment) {
        adjustments.add(adjustment);
    }
}

/**
 * 초기값이 있는 경우: 초기값과 조정값을 분리하여 관리
 */
class ProductionPlanWithInitial {
    private int initialProduction;
    private List<Adjustment> adjustments = new ArrayList<>();

    public ProductionPlanWithInitial(int initialProduction) {
        this.initialProduction = initialProduction;
    }

    public int getProduction() {
        return initialProduction + calculateAdjustments();
    }
    
    private int calculateAdjustments() {
        return adjustments.stream().mapToInt(Adjustment::getAmount).sum();
    }

    public void applyAdjustment(Adjustment adjustment) {
        adjustments.add(adjustment);
    }
}