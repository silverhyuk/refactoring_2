package com.david.chapter09;

/**
 * 9.1 Split Variable (변수 분리) 예제
 * 
 * 문제: 하나의 변수가 여러 역할을 수행하면 코드 이해와 유지보수가 어려워집니다.
 * 해결: 각 역할에 맞는 변수로 분리하여 명확성을 높입니다.
 */
public class SplitVariable {

    /**
     * 리팩토링 전: 하나의 변수(acc)가 두 가지 역할을 수행
     */
    public static double distanceTravelledBefore(Scenario scenario, double time) {
        double result;
        double acc = scenario.getPrimaryForce() / scenario.getMass(); // 초기 가속도 계산
        double primaryTime = Math.min(time, scenario.getDelay());
        result = 0.5 * acc * primaryTime * primaryTime;
        
        double secondaryTime = time - scenario.getDelay();
        if (secondaryTime > 0) {
            double primaryVelocity = acc * scenario.getDelay();
            acc = (scenario.getPrimaryForce() + scenario.getSecondaryForce()) / scenario.getMass(); // 보조 가속도 계산에 같은 변수 사용
            result += primaryVelocity * secondaryTime + 0.5 * acc * secondaryTime * secondaryTime;
        }
        return result;
    }

    /**
     * 리팩토링 후: 각 역할에 맞는 변수로 분리
     */
    public static double distanceTravelledAfter(Scenario scenario, double time) {
        double result;
        final double primaryAcceleration = scenario.getPrimaryForce() / scenario.getMass(); // 초기 가속도
        double primaryTime = Math.min(time, scenario.getDelay());
        result = 0.5 * primaryAcceleration * primaryTime * primaryTime;
        
        double secondaryTime = time - scenario.getDelay();
        if (secondaryTime > 0) {
            double primaryVelocity = primaryAcceleration * scenario.getDelay();
            final double secondaryAcceleration = (scenario.getPrimaryForce() + scenario.getSecondaryForce()) / scenario.getMass(); // 보조 가속도
            result += primaryVelocity * secondaryTime + 0.5 * secondaryAcceleration * secondaryTime * secondaryTime;
        }
        return result;
    }

    /**
     * 리팩토링 전: 입력 파라미터를 직접 수정
     */
    public static int discountBefore(int inputValue, int quantity) {
        if (inputValue > 50) inputValue -= 2; // 입력 파라미터 직접 수정
        if (quantity > 100) inputValue -= 1; // 입력 파라미터 직접 수정
        return inputValue;
    }

    /**
     * 리팩토링 후: 입력 파라미터를 수정하지 않고 결과 변수 사용
     */
    public static int discountAfter(int inputValue, int quantity) {
        int result = inputValue; // 결과 변수 별도 생성
        if (inputValue > 50) result -= 2;
        if (quantity > 100) result -= 1;
        return result;
    }

    /**
     * 메인 메서드: 리팩토링 전후 코드 실행 및 결과 비교
     */
    public static void main(String[] args) {
        System.out.println("===== 변수 분리 리팩토링 예제 실행 =====\n");
        
        // 1. 거리 계산 예제 테스트
        System.out.println("1. 거리 계산 예제 (Distance Calculation):");
        System.out.println("----------------------------------------");
        
        // 시나리오 생성 (primaryForce, secondaryForce, mass, delay)
        Scenario scenario1 = new Scenario(100.0, 50.0, 5.0, 10.0);
        Scenario scenario2 = new Scenario(200.0, 100.0, 10.0, 5.0);
        
        // 다양한 시간 값으로 테스트
        double[] testTimes = {5.0, 10.0, 15.0, 20.0};
        
        for (Scenario scenario : new Scenario[]{scenario1, scenario2}) {
            System.out.println("\n시나리오: primaryForce=" + scenario.getPrimaryForce() + 
                             ", secondaryForce=" + scenario.getSecondaryForce() + 
                             ", mass=" + scenario.getMass() + 
                             ", delay=" + scenario.getDelay());
            
            System.out.println("\n시간\t리팩토링 전\t리팩토링 후\t결과 동일?");
            System.out.println("----\t-----------\t-----------\t---------");
            
            for (double time : testTimes) {
                double before = distanceTravelledBefore(scenario, time);
                double after = distanceTravelledAfter(scenario, time);
                boolean isSame = Math.abs(before - after) < 0.0001; // 부동소수점 비교를 위한 오차 허용
                
                System.out.printf("%.1f\t%.2f\t\t%.2f\t\t%s\n", 
                                time, before, after, isSame ? "✓" : "✗");
            }
        }
        
        // 2. 할인 계산 예제 테스트
        System.out.println("\n\n2. 할인 계산 예제 (Discount Calculation):");
        System.out.println("----------------------------------------");
        
        // 테스트 케이스 배열 (inputValue, quantity)
        int[][] testCases = {
            {40, 90},  // inputValue <= 50, quantity <= 100
            {60, 90},  // inputValue > 50, quantity <= 100
            {40, 110}, // inputValue <= 50, quantity > 100
            {60, 110}  // inputValue > 50, quantity > 100
        };
        
        System.out.println("\n입력값\t수량\t리팩토링 전\t리팩토링 후\t결과 동일?");
        System.out.println("-----\t----\t-----------\t-----------\t---------");
        
        for (int[] testCase : testCases) {
            int inputValue = testCase[0];
            int quantity = testCase[1];
            
            int before = discountBefore(inputValue, quantity);
            int after = discountAfter(inputValue, quantity);
            boolean isSame = before == after;
            
            System.out.printf("%d\t%d\t%d\t\t%d\t\t%s\n", 
                            inputValue, quantity, before, after, isSame ? "✓" : "✗");
        }
    }
}

class Scenario {
    private double primaryForce;
    private double secondaryForce;
    private double mass;
    private double delay;

    public Scenario(double primaryForce, double secondaryForce, double mass, double delay) {
        this.primaryForce = primaryForce;
        this.secondaryForce = secondaryForce;
        this.mass = mass;
        this.delay = delay;
    }

    public double getPrimaryForce() { return primaryForce; }
    public double getSecondaryForce() { return secondaryForce; }
    public double getMass() { return mass; }
    public double getDelay() { return delay; }
}