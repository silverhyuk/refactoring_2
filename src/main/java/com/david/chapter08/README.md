# 8장
# 기능 이동

리팩토링 8장은 프로그램의 요소들을 단순히 생성·제거·이름 변경하는 것을 넘어, 다른 컨텍스트로 옮기는 작업의 중요성을 강조합니다. 핵심 포인트는 다음과 같습니다:

- **요소 이동**
    - **함수 이동:** 클래스나 모듈 간에 함수를 옮길 때는 *함수 옮기기* 기법을 사용합니다.
    - **필드 이동:** 유사하게 필드는 *필드 옮기기*를 통해 다른 컨텍스트로 이전할 수 있습니다.
- **문장 단위 이동**
    - 함수 내부/외부로 개별 문장을 옮길 때는 *문장을 함수 안으로 옮기기* 또는 *문장을 호출한 곳으로 옮기기* 기법을 사용합니다.
    - 같은 함수 내에서 문장들의 순서를 조정할 때는 *문장 슬라이드*를 활용합니다.
    - 기존 함수와 동일한 역할을 하는 문장 묶음은 *인라인 코드를 함수 호출로 바꾸기*를 통해 중복을 제거합니다.
- **반복문 관련 리팩토링**
    - **반복문 쪼개기:** 반복문이 한 가지 작업만 수행하도록 분리합니다.
    - **반복문을 파이프라인으로 바꾸기:** 반복문 전체를 파이프라인 구조로 대체하여 가독성과 유지보수성을 높입니다.
- **불필요한 코드 제거**
    - *죽은 코드 제거하기* 기법을 사용해 사용되지 않거나 중복된 코드를 과감히 제거합니다.
    - “디지털 화염방사기”로 불필요한 코드를 불태우는 것처럼 짜릿하다는 점도 언급됩니다.

이처럼 리팩토링 8장은 코드의 명확성과 유지보수성을 높이기 위한 다양한 이동 및 정리 기법들을 다루고 있습니다. 필요 없는 부분을 제거하고, 기능별로 책임을 분리하는 것이 전체 코드의 퀄리티를 향상시키는 핵심임을 잘 보여줍니다.

# **8.1** 함수 옮기기

## **배경**

- **모듈성의 핵심 가치:**

  좋은 소프트웨어 설계는 모듈성을 기반으로 합니다. 모듈성이란 프로그램의 일부만 이해해도 대부분의 수정이 가능한 구조를 의미하며, 이를 위해 관련 요소들이 함께 그룹화되어야 합니다.

- **동적 이해와 구조 개선:**

  소프트웨어를 다루며 점점 더 깊이 이해하게 됨에 따라, 요소들을 어떤 기준으로 그룹화할지에 대한 최선의 방법이 달라집니다. 이 때문에 개발 과정에서 함수나 기타 요소를 적절한 컨텍스트로 옮기는 작업이 필요합니다.

- **함수의 위치와 컨텍스트:**

  모든 함수는 특정 컨텍스트(모듈, 클래스, 또는 함수 내부 등) 내에 존재합니다. 객체지향 프로그램에서는 클래스가 기본적인 모듈 단위로 작용하며, 함수가 서로 다른 컨텍스트에 걸쳐 있을 때, 관련 요소들을 함께 묶어 캡슐화를 강화할 수 있습니다.

- **이동의 동기:**
    - 함수가 현재 위치보다 다른 컨텍스트의 요소들과 더 많이 연관될 경우.
    - 호출하는 위치나 향후 개선 사항에 따라 접근성을 높이기 위해 이동이 필요할 경우.
    - 내부 헬퍼 함수가 독립적인 가치가 있어 별도의 컨텍스트로 옮기면 재사용성이 증대되는 경우.
- **결정 과정 및 유연성:**

  함수 이동 결정은 함수가 호출되는 방식, 호출하는 다른 함수들, 사용 데이터 등을 분석해 이루어집니다. 최적의 위치를 찾기 어려운 경우, 일단 한 컨텍스트에서 작업해보고 나중에 필요에 따라 옮길 수 있다는 유연성이 중요합니다. 필요시 Combine Functions into Class나 Extract Class 같은 기법을 활용해 새로운 컨텍스트를 만들기도 합니다.


짧게 말해, 함수 옮기기는 소프트웨어의 모듈성을 극대화하고, 캡슐화를 강화하기 위한 필수 리팩토링 기법입니다. 이해도가 깊어질수록 최적의 구조는 변화할 수 있으니, 유연하게 접근하는 것이 핵심입니다.

 

## **절차**

1. **현재 컨텍스트 분석:**
    - 선택한 함수가 사용하는 모든 요소를 확인하고, 함께 이동할 필요가 있는지 판단합니다.
2. **관련 함수 이동:**
    - 호출되는 함수 중 이동이 필요한 함수가 있다면, 의존성이 낮은 것부터 먼저 이동합니다.
    - 상위 함수가 서브 함수들을 유일하게 호출하는 경우, 서브 함수를 인라인 처리한 후 이동하고, 새 위치에서 다시 추출할 수 있습니다.
3. **다형성 고려:**
    - 선택한 함수가 다형적 메서드라면, 객체지향 언어에서 상위/하위 클래스 관계를 반영해야 합니다.
4. **대상 컨텍스트에 복사 및 조정:**
    - 함수를 새로운 컨텍스트에 복사한 후, 해당 환경에 맞게 내부 요소(원본 컨텍스트의 요소는 매개변수 또는 참조로 전달)를 조정합니다.
    - 새 컨텍스트에 적합한 함수 이름으로 변경할 필요가 있을 수 있습니다.
5. **정적 분석 수행:**
    - 대상 함수가 원본 컨텍스트에서 어떻게 참조될지 확인합니다.
6. **위임 함수 생성:**
    - 원본 함수는 대상 함수를 호출하는 위임 함수로 남겨두어, 호출하는 쪽이 변경 사항을 인지하지 않아도 되도록 합니다.
7. **테스트 및 정리:**
    - 전체 테스트를 통해 이동 작업이 올바르게 수행되었는지 확인합니다.
    - 호출자가 대상 함수에 직접 접근할 수 있다면, 위임 함수는 인라인 처리하여 제거할 수 있습니다.

## 예제 1. 중첩 함수 → 탑 레벨 함수

### (1) **초기 코드 -** 리팩토링 전: 내부 클래스 사용

이전 코드는 트랙 요약(TrackSummary) 클래스 내부에 헬퍼 역할을 하는 **내부 클래스(DistanceCalculator)** 를 포함하고 있습니다.

내부 클래스는 외부 클래스의 필드(여기서는 points)에 접근할 수 있지만, 기능이 해당 클래스에 국한되어 재사용하기 어렵습니다.

```java
public class TrackSummary {
    private Point[] points;

    public TrackSummary(Point[] points) {
        this.points = points;
    }

    public Summary summarize() {
        double totalTime = calculateTime();
        // 내부 클래스 인스턴스를 생성하여 거리 계산
        DistanceCalculator calculator = new DistanceCalculator();
        double totalDistance = calculator.calculate();
        double pace = totalTime / 60 / totalDistance;
        return new Summary(totalTime, totalDistance, pace);
    }

    // 내부 클래스: TrackSummary에 종속되어 있음
    private class DistanceCalculator {
        public double calculate() {
            double result = 0;
            for (int i = 1; i < points.length; i++) {
                result += distance(points[i - 1], points[i]);
            }
            return result;
        }

        private double distance(Point p1, Point p2) {
            final double EARTH_RADIUS = 3959; // 마일 단위
            double dLat = radians(p2.lat - p1.lat);
            double dLon = radians(p2.lon - p1.lon);
            double a = Math.pow(Math.sin(dLat / 2), 2)
                     + Math.cos(radians(p1.lat)) * Math.cos(radians(p2.lat))
                     * Math.pow(Math.sin(dLon / 2), 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            return EARTH_RADIUS * c;
        }

        private double radians(double degrees) {
            return degrees * Math.PI / 180;
        }
    }

    // 총 시간 계산 (예: 1시간 = 3600초)
    private double calculateTime() {
        return 3600;
    }

    // 요약 결과를 담는 데이터 클래스
    public static class Summary {
        public final double time;
        public final double distance;
        public final double pace;

        public Summary(double time, double distance, double pace) {
            this.time = time;
            this.distance = distance;
            this.pace = pace;
        }

        @Override
        public String toString() {
            return "Summary [time=" + time + ", distance=" + distance + ", pace=" + pace + "]";
        }
    }
}

// 좌표 정보를 담는 클래스
class Point {
    public final double lat;
    public final double lon;

    public Point(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }
}

```

---

### 리팩토링 후: 독립된 탑 레벨 클래스 사용

- 리팩토링 후에는 **DistanceCalculator** 를 별도의 최상위(탑 레벨) 클래스로 분리합니다.
- 이제 `DistanceCalculator`는 `Point[]`를 파라미터로 받아 동작하므로, TrackSummary 클래스와의 결합도가 낮아지고 재사용성이 향상됩니다.

**TrackSummary 클래스 (변경된 부분):**

```java
public class TrackSummary {
    private Point[] points;

    public TrackSummary(Point[] points) {
        this.points = points;
    }

    public Summary summarize() {
        double totalTime = calculateTime();
        // 독립된 DistanceCalculator 클래스를 호출
        double totalDistance = DistanceCalculator.calculate(points);
        double pace = totalTime / 60 / totalDistance;
        return new Summary(totalTime, totalDistance, pace);
    }

    private double calculateTime() {
        return 3600;
    }

    public static class Summary {
        public final double time;
        public final double distance;
        public final double pace;

        public Summary(double time, double distance, double pace) {
            this.time = time;
            this.distance = distance;
            this.pace = pace;
        }

        @Override
        public String toString() {
            return "Summary [time=" + time + ", distance=" + distance + ", pace=" + pace + "]";
        }
    }
}

```

**독립된 DistanceCalculator 클래스:**

```java
public class DistanceCalculator {

    // points 배열을 매개변수로 받아 계산
    public static double calculate(Point[] points) {
        double result = 0;
        for (int i = 1; i < points.length; i++) {
            result += distance(points[i - 1], points[i]);
        }
        return result;
    }

    private static double distance(Point p1, Point p2) {
        final double EARTH_RADIUS = 3959;
        double dLat = radians(p2.lat - p1.lat);
        double dLon = radians(p2.lon - p1.lon);
        double a = Math.pow(Math.sin(dLat / 2), 2)
                 + Math.cos(radians(p1.lat)) * Math.cos(radians(p2.lat))
                 * Math.pow(Math.sin(dLon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }

    private static double radians(double degrees) {
        return degrees * Math.PI / 180;
    }
}

```

**Point 클래스는 그대로 유지됩니다.**

---

## 개선된 점 및 설명

1. **재사용성과 독립성:**
    - **전:** `DistanceCalculator`가 TrackSummary 내부의 **내부 클래스**로 존재하여, 오직 TrackSummary에서만 사용할 수 있었습니다.
    - **후:** `DistanceCalculator`가 독립된 탑 레벨 클래스로 분리되어 다른 클래스나 상황에서도 재사용이 용이합니다.
2. **의존성 명시:**
    - **전:** 내부 클래스는 외부 클래스의 필드(`points`)에 암묵적으로 의존합니다.
    - **후:** 계산 메서드에 `Point[]`를 매개변수로 전달함으로써, 어떤 데이터를 사용하는지 명확히 드러납니다.
3. **정적 분석 및 테스트 용이성:**
    - **후:** 독립된 클래스로 분리된 덕분에, 단위 테스트를 진행할 때 TrackSummary와 무관하게 DistanceCalculator의 기능을 개별적으로 검증할 수 있습니다.
4. **유지보수성 개선:**
    - 코드가 모듈화되어 각 클래스가 명확한 역할을 가지므로, 향후 수정이나 확장이 쉬워집니다.

## 예제 2. 클래스 간 함수 이동

원래 계좌(Account) 클래스의 메서드 중 `overdraftCharge`가 계좌 타입(AccountType)에 더 자연스럽게 어울린다고 판단하여 이동하는 예제입니다.

### (1) **이동 전 – Account 클래스에 존재하는 형태**

```java
public class Account {
    private double daysOverdrawn;
    private AccountType type;

    public Account(double daysOverdrawn, AccountType type) {
        this.daysOverdrawn = daysOverdrawn;
        this.type = type;
    }

    public double getBankCharge() {
        double result = 4.5;
        if (daysOverdrawn > 0)
            result += getOverdraftCharge();
        return result;
    }

    // 기존 오버드래프트 요금 계산 메서드 (계좌 내부에 있음)
    public double getOverdraftCharge() {
        // 로직: 타입에 따라 요금 계산
        if (type.isPremium()) {
            double baseCharge = 10;
            if (daysOverdrawn <= 7)
                return baseCharge;
            else
                return baseCharge + (daysOverdrawn - 7) * 0.85;
        } else {
            return daysOverdrawn * 1.75;
        }
    }

    public double getDaysOverdrawn() {
        return daysOverdrawn;
    }
}

```

### (2) **이동 후 – AccountType으로 옮긴 후 위임**

먼저, 계좌 타입(AccountType) 클래스에 오버드래프트 요금 계산 로직을 옮깁니다.

```java
public class AccountType {
    private boolean premium;

    public AccountType(boolean premium) {
        this.premium = premium;
    }

    public boolean isPremium() {
        return premium;
    }

    // daysOverdrawn 값을 파라미터로 받아 요금 계산
    public double overdraftCharge(double daysOverdrawn) {
        if (premium) {
            double baseCharge = 10;
            if (daysOverdrawn <= 7)
                return baseCharge;
            else
                return baseCharge + (daysOverdrawn - 7) * 0.85;
        } else {
            return daysOverdrawn * 1.75;
        }
    }

    // 또는 Account 객체 자체를 받아 처리할 수도 있음
    public double overdraftCharge(Account account) {
        double daysOverdrawn = account.getDaysOverdrawn();
        if (premium) {
            double baseCharge = 10;
            if (daysOverdrawn <= 7)
                return baseCharge;
            else
                return baseCharge + (daysOverdrawn - 7) * 0.85;
        } else {
            return daysOverdrawn * 1.75;
        }
    }
}

```

그 다음, Account 클래스에서는 `getOverdraftCharge()` 메서드를 단순 위임 호출로 변경합니다.

```java
public class Account {
    private double daysOverdrawn;
    private AccountType type;

    public Account(double daysOverdrawn, AccountType type) {
        this.daysOverdrawn = daysOverdrawn;
        this.type = type;
    }

    public double getBankCharge() {
        double result = 4.5;
        if (daysOverdrawn > 0)
            result += getOverdraftCharge();
        return result;
    }

    // 위임 메서드: AccountType의 계산 메서드를 호출함
    public double getOverdraftCharge() {
        return type.overdraftCharge(daysOverdrawn);
        // 또는: return type.overdraftCharge(this);
    }

    public double getDaysOverdrawn() {
        return daysOverdrawn;
    }
}

```

### **설명**

- **원인 분석:**
    - 원래 `overdraftCharge` 메서드는 계좌(Account) 클래스 내에서 정의되어 있었으나, 요금 산출 로직은 계좌의 타입에 따라 달라지므로, 해당 로직은 AccountType 클래스에 두는 것이 모듈성과 응집도를 높입니다.
- **이동 절차:**
    1. Account 클래스에서 사용하던 로직을 AccountType으로 복사한 후, 필요에 따라 파라미터(예, daysOverdrawn 또는 Account 객체)를 전달합니다.
    2. Account 클래스의 기존 `overdraftCharge`는 AccountType의 메서드를 호출하는 위임(delegate) 방식으로 변경합니다.
    3. 테스트를 통해 변경 후에도 올바르게 동작하는지 확인합니다.
    4. 추가적으로, 위임 메서드를 인라인하여 Account 클래스에서 바로 AccountType의 메서드를 호출하도록 정리할 수도 있습니다.

---

두 예제 모두 함수 이동 리팩토링의 기본 원칙—즉, 관련 있는 기능끼리 함께 모으고 불필요한 의존성을 줄여 모듈성을 높이는 것—을 잘 보여줍니다. 자바에서는 지역 함수가 지원되지 않기 때문에 초기 설계부터 헬퍼 메서드를 클래스의 private static 메서드로 분리하는 방식으로 구현하며, 클래스 간 이동은 위임 메서드를 사용하여 점진적으로 변경하는 방식으로 진행합니다.

# **8.2** 필드 옮기기

## 배경

- **데이터 구조의 중요성:**

  프로그램의 행동은 많지만, 핵심은 올바른 데이터 구조에 있습니다. 적절한 데이터 구조는 단순하고 명확한 코드를 만들지만, 잘못된 구조는 불필요한 코드와 복잡성을 야기합니다.

- **초기 설계의 한계:**

  처음에 선택한 데이터 구조는 문제 도메인에 대해 제한된 정보를 바탕으로 결정되므로, 시간이 지나며 변경이 필요할 수 있습니다. 한때 합리적이었던 결정이 나중에는 부적절하게 느껴질 수 있습니다.

- **필드 이동의 필요성:**
    - **함께 전달되는 데이터:** 여러 함수 호출 시 항상 같이 전달되는 필드들은 하나의 레코드(또는 클래스)로 묶여야 관계가 명확해집니다.
    - **일관된 업데이트:** 여러 구조체에서 같은 필드를 업데이트해야 한다면, 한 곳에 모아 두면 유지보수가 쉬워집니다.
    - **데이터 의존성 최소화:** 한 레코드의 변경이 다른 레코드의 필드에 영향을 준다면, 필드가 잘못된 위치에 있음을 의미합니다.
- **캡슐화의 장점:**

  클래스에서는 접근자 메서드로 데이터를 감싸고 있기 때문에, 필드를 옮긴 후에도 접근자만 수정하면 기존 클라이언트 코드가 그대로 작동할 수 있습니다. 이는 필드 이동을 쉽게 만드는 중요한 요소입니다.

- **진화하는 설계에의 대응:**

  프로그래밍 도중 문제 도메인에 대해 더 많이 알게 되면, 기존 데이터 구조가 한계를 드러내게 됩니다. 이 때 필드 옮기기는 코드의 명확성을 회복하고 장기적으로 유지보수를 용이하게 합니다.


이러한 배경으로, 필드 옮기기 리팩토링은 단순히 코드를 정리하는 것이 아니라, **데이터의 의미와 관계를 명확히 하여 프로그램의 전반적인 구조를 건강하게 유지**하는 중요한 과정입니다.

## 절차

- **원본 필드 캡슐화 확인:**
    - 필드가 접근자(getter/setter)를 통해 캡슐화되어 있는지 확인합니다.
    - 캡슐화되지 않았다면 먼저 이를 적용하고 관련 테스트를 수행합니다.
- **대상 객체에 필드 생성:**
    - 옮길 필드를 저장할 대상 객체에 새 필드와 이에 대응하는 접근자를 추가합니다.
    - 이때, 정적 분석 도구로 변경 사항을 검증합니다.
- **원본 → 대상 참조 연결:**
    - 원본 객체가 대상 객체를 참조할 수 있도록 합니다.
    - 기존 필드나 메서드가 이미 대상 객체를 제공한다면 이를 활용하고, 없으면 새 메서드나 임시 필드를 생성할 수 있습니다.
- **접근자 수정:**
    - 원본 객체의 접근자(getter/setter)를 대상 필드를 사용하도록 변경합니다.
    - 만약 대상 필드가 여러 원본 객체와 공유된다면, setter에서 양쪽 필드를 모두 갱신한 후, Introduce Assertion 기법을 통해 업데이트 일관성을 검증합니다.
- **테스트:**
    - 접근자 수정 후 전체 동작 테스트를 수행하여 예상한 대로 작동하는지 확인합니다.
- **원본 필드 제거:**
    - 대상 필드로 완전히 전환된 후, 더 이상 사용하지 않는 원본 필드를 제거합니다.
    - 필드 제거 후에도 테스트를 통해 기능이 정상 동작하는지 최종 확인합니다.

## 예시 1

---

### 리팩토링 전 코드

**Customer 클래스:**

Customer는 할인율(discountRate)을 자신의 필드로 가지고 있으며, 계약(CustomerContract)은 시작일만 보유합니다.

```java
import java.time.LocalDate;

public class Customer {
    private String name;
    private double discountRate;
    private CustomerContract contract;

    public Customer(String name, double discountRate) {
        this.name = name;
        this.discountRate = discountRate;
        // 계약 객체 생성 시 할인율 정보는 전달하지 않음
        this.contract = new CustomerContract(LocalDate.now());
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public void becomePreferred() {
        // 할인율을 증가시킴
        this.discountRate += 0.03;
        // 기타 부가 작업 수행
    }

    public double applyDiscount(double amount) {
        return amount - (amount * discountRate);
    }
}

```

**CustomerContract 클래스:**

CustomerContract는 시작일만 저장하는 단순 클래스입니다.

```java
import java.time.LocalDate;

public class CustomerContract {
    private LocalDate startDate;

    public CustomerContract(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }
}

```

---

### 리팩토링 후 코드

리팩토링 목표는 할인율(discountRate)을 Customer에서 CustomerContract로 옮기는 것입니다.

아래 코드에서는 **Encapsulate Variable** 기법을 사용하여 할인율 접근을 캡슐화한 후, CustomerContract에 할인율 필드를 추가하고, Customer는 이를 위임(delegate)하도록 수정합니다.

**CustomerContract 클래스 (변경됨):**

이제 계약 객체는 할인율을 보관하며 getter와 setter를 제공합니다.

```java
import java.time.LocalDate;

public class CustomerContract {
    private LocalDate startDate;
    private double discountRate; // 할인율 필드 추가

    public CustomerContract(LocalDate startDate, double discountRate) {
        this.startDate = startDate;
        this.discountRate = discountRate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }
}

```

**Customer 클래스 (변경됨):**

생성자에서 계약 객체를 먼저 생성한 후, 할인율 값을 계약에 전달합니다.

또한, 할인율 접근은 이제 계약 객체를 통해 이루어지며, `becomePreferred()` 메서드는 할인율을 증가시킬 때 위임 메서드(`setDiscountRate`)를 호출합니다.

```java
import java.time.LocalDate;

public class Customer {
    private String name;
    private CustomerContract contract;

    public Customer(String name, double discountRate) {
        this.name = name;
        // 계약 객체 생성 시 할인율을 전달합니다.
        this.contract = new CustomerContract(LocalDate.now(), discountRate);
    }

    // 할인율 getter: 이제 계약의 할인율을 반환
    public double getDiscountRate() {
        return contract.getDiscountRate();
    }

    // 할인율 setter: 내부적으로 계약 객체의 할인율을 수정
    private void setDiscountRate(double discountRate) {
        contract.setDiscountRate(discountRate);
    }

    public void becomePreferred() {
        // 할인율을 0.03 증가시킵니다.
        setDiscountRate(getDiscountRate() + 0.03);
        // 기타 부가 작업 수행
    }

    public double applyDiscount(double amount) {
        return amount - (amount * getDiscountRate());
    }
}

```

---

### 개선된 점 및 설명

1. **데이터의 적절한 위치 지정:**
    - **전:** 할인율이 Customer 객체에 존재하여, 여러 곳에서 할인율 관련 로직을 처리할 때 Customer와 계약 간의 관계가 불분명했습니다.
    - **후:** 할인율을 CustomerContract로 옮김으로써, 계약과 관련된 데이터로서 할인율의 의미를 명확하게 구분하고, 관련 로직이 한 곳에 집중되도록 개선했습니다.
2. **캡슐화와 위임:**
    - Customer는 더 이상 할인율 필드를 직접 다루지 않고, 계약 객체의 할인율 필드에 접근하는 위임 메서드를 통해 할인율을 읽고 변경합니다.
    - 이를 통해 할인율 변경 시 유지보수가 용이하며, 필요한 경우 추가 검증 로직을 쉽게 삽입할 수 있습니다.
3. **생성자 순서 조정:**
    - 할인율 설정 전에 계약 객체를 생성하도록 순서를 변경하여, 초기화 시 발생할 수 있는 “null” 참조 오류를 방지했습니다.
4. **유지보수성 및 재사용성 향상:**
    - 데이터와 그에 따른 접근 방식이 명확히 분리되었으므로, 이후 계약 관련 로직을 확장하거나 다른 고객 관련 기능과 연동할 때 재사용하기 편리해집니다.

이와 같이, 필드를 옮기는 리팩토링은 데이터 구조의 명확성을 높여 코드의 이해도와 유지보수성을 개선하는 중요한 작업입니다.

## 예시2: 공유 객체로 이동하기

**이자율(interest rate)을 Account에서 AccountType으로 옮기는** 리팩토링 전과 후의 자바 코드 예제와 그에 대한 설명입니다.

---

### 리팩토링 전

각 Account 객체는 개별적으로 이자율을 보유하고 있습니다.

AccountType은 단순히 계좌 유형의 이름만 가지고 있습니다.

**Account.java**

```java
public class Account {
    private String number;
    private AccountType type;
    private double interestRate; // 각 계좌마다 개별 이자율 보유

    public Account(String number, AccountType type, double interestRate) {
        this.number = number;
        this.type = type;
        this.interestRate = interestRate;
    }

    public double getInterestRate() {
        return interestRate;
    }

    // 기타 메서드들...
}

```

**AccountType.java**

```java
public class AccountType {
    private String name;

    public AccountType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // 이자율 관련 필드 없음
}

```

---

### 리팩토링 후

이제 모든 Account 객체는 계좌 유형(AccountType)에 저장된 이자율을 공유하도록 변경합니다.

먼저, AccountType에 이자율 필드를 추가하고 getter를 제공합니다.

그 후, Account에서는 생성자에서 전달받은 이자율과 AccountType에 저장된 이자율이 일치하는지 확인(assertion)을 하고, 더 이상 개별 필드를 보유하지 않습니다.

**AccountType.java (변경됨)**

```java
public class AccountType {
    private String name;
    private double interestRate; // 이자율 필드 추가

    public AccountType(String name, double interestRate) {
        this.name = name;
        this.interestRate = interestRate;
    }

    public String getName() {
        return name;
    }

    public double getInterestRate() {
        return interestRate;
    }
}

```

**Account.java (변경됨)**

```java
public class Account {
    private String number;
    private AccountType type;
    // 더 이상 개별 interestRate 필드가 없음

    // 생성자에서 전달받은 interestRate가 type의 이자율과 일치하는지 확인합니다.
    public Account(String number, AccountType type, double interestRate) {
        this.number = number;
        this.type = type;
        // Assertion: 생성 시 전달받은 이자율이 계좌 유형의 이자율과 일치해야 함
        assert interestRate == type.getInterestRate() : "Interest rate mismatch!";
    }

    // 이자율 접근은 AccountType에 위임합니다.
    public double getInterestRate() {
        return type.getInterestRate();
    }

    // 기타 메서드들...
}

```

---

### 개선된 점 및 설명

1. **데이터 중앙 집중화 및 공유**
    - **전:** 각 Account 객체마다 별도의 이자율이 저장되어 있었습니다.
    - **후:** 동일한 계좌 유형에 속한 모든 Account는 AccountType에 저장된 이자율을 공유하므로, 데이터가 중앙 집중화되어 일관성을 유지할 수 있습니다.
2. **데이터 일관성 검사**
    - Account 생성자에서 전달된 이자율과 AccountType의 이자율이 일치하는지 assertion으로 확인하여, 데이터 불일치로 인한 오류를 미리 잡을 수 있습니다.
3. **코드 단순화**
    - Account 객체에서는 더 이상 개별적으로 이자율을 관리할 필요가 없으므로, 관련 필드와 수정 로직을 제거하여 코드가 간결해졌습니다.
4. **유지보수성 및 확장성**
    - 계좌 유형의 이자율 변경이 필요한 경우, AccountType 클래스만 수정하면 되므로 전체 시스템의 유지보수성이 향상됩니다.

이와 같이, 필드를 공유 객체로 옮김으로써 데이터의 중앙 관리와 일관성 확보가 가능해지며, 관련 로직이 한 곳에 모여 유지보수가 용이해집니다.

# 8.3 문장을 함수로 옮기기

## **배경**

1. **중복 제거의 중요성**
    - 중복된 코드가 여러 곳에 존재하면 유지보수가 어렵고, 변경 시 실수가 발생할 가능성이 높아집니다.
    - 특정 함수가 호출될 때마다 동일한 코드가 실행된다면, 해당 문장을 함수 내부로 이동하여 중복을 제거하는 것이 바람직합니다.
    - 이렇게 하면 코드 변경이 필요할 때 한 곳만 수정하면 되므로 일관성을 유지할 수 있습니다.
2. **문장 이동의 기준**
    - 함수 내부로 옮기는 것이 논리적으로 맞는 경우:
      → 해당 문장들이 함수의 핵심 역할과 자연스럽게 어우러지는 경우, 함수 내부로 이동하는 것이 적절합니다.
    - 함수 내부에서는 어색하지만, 항상 같이 호출되어야 하는 경우:
      → 별도의 함수로 추출(Extract Function)한 후 호출하는 방식이 적절할 수 있습니다.
3. **유연한 접근 방식**
    - 처음에는 문장들을 함수 내부로 이동시켜 중복을 제거하지만,
      이후 필요에 따라 다시 함수 외부로 옮길 수도 있습니다.
    - `Move Statements to Callers` 를 사용하여 필요할 때 다시 호출하는 쪽에서 실행되도록 조정할 수 있습니다.
4. **점진적인 리팩토링**
    - `Extract Function`을 활용하여 먼저 문장을 별도 함수로 추출한 후,
      코드의 역할과 위치를 다시 검토하여 최적의 구조로 조정할 수 있습니다.
    - 경우에 따라 추출된 함수와 기존 함수의 역할을 재구성하여 가독성과 유지보수성을 더욱 높일 수 있습니다.

## **절차**

1. **문장 이동을 준비 (Slide Statements)**
    - 반복되는 코드가 함수 호출과 **인접하지 않은 경우**, `Slide Statements` 기법을 사용하여 **함수 호출 바로 앞**으로 이동시킵니다.
    - 이렇게 하면 코드 이동이 쉬워지고, 리팩토링 후에도 기존 로직이 유지되기 쉽습니다.
2. **단일 호출자(Single Caller)인 경우**
    - 만약 해당 함수를 **오직 한 곳에서만 호출하고 있다면**,
      → 반복되는 코드를 **잘라서(target function으로 이동) 붙여넣고**,
      → **테스트를 수행**한 후, 추가 작업 없이 마무리할 수 있습니다.
3. **여러 곳에서 호출(Multiple Callers)하는 경우**
    - 하나의 호출 지점에서 **Extract Function (106)** 을 사용하여,**원래 함수 호출 + 이동할 문장**을 포함하는 새 함수로 추출합니다.
    - 새 함수의 이름은 임시적으로 지정하되, 추후 찾기 쉬운 이름을 사용합니다.
4. **모든 호출자를 새 함수로 변경**
    - 다른 호출자들도 **기존 함수 대신 새 함수**를 사용하도록 변환합니다.
    - **각 변환 후 테스트**하여 정상 동작을 확인합니다.
5. **기존 함수 제거 (Inline Function)**
    - 모든 호출이 새로운 함수로 대체되었으면,
      기존 함수를 **Inline Function** 기법을 사용하여 완전히 제거합니다.
6. **새 함수 이름 변경 (Rename Function)**
    - 새 함수의 이름을 **기존 함수와 동일한 이름**으로 변경하여, 원래 함수처럼 사용할 수 있도록 합니다.
    - 또는, **더 의미 있는 이름**이 있다면 적절한 이름으로 변경합니다.

## 예제

### **리팩토링 전 코드**

아래 코드는 **HTML 데이터를 출력하는 메서드들**을 정의하고 있습니다.

각 메서드는 **사진 데이터(사진 제목, 위치, 날짜 등)를 출력하는 코드가 중복**되어 있습니다.

```java
import java.util.Date;

public class HtmlRenderer {

    public static String renderPerson(Person person) {
        StringBuilder result = new StringBuilder();
        result.append("<p>").append(person.getName()).append("</p>\n");
        result.append(renderPhoto(person.getPhoto())).append("\n");
        result.append("<p>title: ").append(person.getPhoto().getTitle()).append("</p>\n");
        result.append(emitPhotoData(person.getPhoto()));
        return result.toString();
    }

    public static String photoDiv(Photo p) {
        return "<div>\n"
                + "<p>title: " + p.getTitle() + "</p>\n"
                + emitPhotoData(p)
                + "\n</div>";
    }

    public static String emitPhotoData(Photo aPhoto) {
        StringBuilder result = new StringBuilder();
        result.append("<p>location: ").append(aPhoto.getLocation()).append("</p>\n");
        result.append("<p>date: ").append(aPhoto.getDate().toString()).append("</p>");
        return result.toString();
    }

    public static String renderPhoto(Photo photo) {
        return "<img src='" + photo.getUrl() + "' />";
    }
}

```

### **관련 클래스**

```java
public class Person {
    private String name;
    private Photo photo;

    public Person(String name, Photo photo) {
        this.name = name;
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public Photo getPhoto() {
        return photo;
    }
}

public class Photo {
    private String title;
    private String location;
    private Date date;
    private String url;

    public Photo(String title, String location, Date date, String url) {
        this.title = title;
        this.location = location;
        this.date = date;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public Date getDate() {
        return date;
    }

    public String getUrl() {
        return url;
    }
}

```

---

### **리팩토링 후 코드**

중복된 **사진 제목(title) 출력 코드**를 `emitPhotoData` 내부로 이동하여 중복을 제거했습니다.

```java
import java.util.Date;

public class HtmlRenderer {

    public static String renderPerson(Person person) {
        StringBuilder result = new StringBuilder();
        result.append("<p>").append(person.getName()).append("</p>\n");
        result.append(renderPhoto(person.getPhoto())).append("\n");
        result.append(emitPhotoData(person.getPhoto())); // 중복 제거
        return result.toString();
    }

    public static String photoDiv(Photo p) {
        return "<div>\n"
                + emitPhotoData(p) // 중복 제거
                + "\n</div>";
    }

    public static String emitPhotoData(Photo aPhoto) {
        return "<p>title: " + aPhoto.getTitle() + "</p>\n"
                + "<p>location: " + aPhoto.getLocation() + "</p>\n"
                + "<p>date: " + aPhoto.getDate().toString() + "</p>";
    }

    public static String renderPhoto(Photo photo) {
        return "<img src='" + photo.getUrl() + "' />";
    }
}

```

---

### **리팩토링 과정 설명**

### **1. 중복된 코드 식별**

- `renderPerson`과 `photoDiv` 두 개의 메서드에서 `<p>title: ${photo.title}</p>` 출력이 반복되고 있었습니다.

### **2. 새로운 함수로 추출**

- `photoDiv`에서 `<p>title: ${photo.title}</p>`와 `emitPhotoData(photo)`를 묶어서 `zznew(photo)`라는 새로운 함수로 만들었습니다.

### **3. 기존 호출자 변경**

- `renderPerson`에서도 `<p>title: ${photo.title}</p>`를 제거하고, `emitPhotoData(photo)`를 호출하도록 변경했습니다.

### **4. 기존 함수 인라인 및 최종 정리**

- `zznew(photo)` 함수를 `emitPhotoData(photo)`로 인라인하여 최종적으로 하나의 함수로 정리했습니다.
- 함수명을 `emitPhotoData()`로 유지하면서 더 의미 있는 형태로 변경했습니다.

---

## **개선된 점**

✅ **중복 제거** → `photoDiv()`와 `renderPerson()`에서 중복된 `<p>title: ${photo.title}</p>` 제거

✅ **가독성 향상** → 사진과 관련된 데이터는 `emitPhotoData()`에서 한 번에 처리

✅ **유지보수 용이** → 사진 정보 출력 방식을 변경할 때 **한 곳만 수정하면 모든 함수에서 반영됨**

---

# 8.4 문장을 호출한 곳으로 옮기기

## **배경**

1. **함수 경계(boundary)의 변화**
    - 함수는 프로그램의 **추상화(abstraction) 단위**이지만, 처음 설계한 함수의 경계가 항상 적절하지는 않습니다.
    - 소프트웨어가 점점 확장되면서, 하나의 함수가 여러 역할을 하게 되거나, 일부 기능이 **특정 호출자(caller)에서만 다르게 동작해야 하는 상황**이 발생할 수 있습니다.
2. **변하는 동작을 함수 내부에서 분리해야 하는 경우**
    - 함수가 여러 곳에서 사용될 때, **일부 호출자는 기존 동작을 그대로 유지하지만, 다른 호출자는 일부 기능이 달라야 할 경우**가 있습니다.
    - 이런 경우, 변하는 코드만 함수 내부에서 제거하고, 이를 호출자(caller) 쪽으로 이동시키는 것이 좋습니다.
3. **슬라이드(Slide) 및 이동(Move) 기법 활용**
    - **Slide Statements**  → 변하는 코드(예: 특정 호출자에서만 실행해야 하는 코드)를 함수의 앞이나 뒤로 이동시켜 정리합니다.
    - **Move Statements to Callers** → 변하는 코드를 함수 내부에서 제거하고, 개별 호출자(caller)들이 직접 실행하도록 이동합니다.
4. **함수의 전체적인 구조 재조정 필요성**
    - 때때로, 코드 일부만 이동하는 것으로는 적절한 함수 경계를 만들기 어렵습니다.
    - 이 경우, **Inline Function** 을 사용하여 함수를 완전히 제거한 뒤, **필요한 부분만 새로운 함수로 추출(Extract Function)** 하여 더 적절한 경계를 설정할 수 있습니다.

## **절차**

1. **간단한 경우:**
    - 호출자가 **1~2개**이고, 함수 내부의 일부 문장만 옮기면 되는 경우:
        - **이동할 문장을 잘라서(cut) 호출자에 붙여넣고(paste), 필요한 수정 후 테스트**합니다.
        - 테스트가 통과하면 완료.
2. **더 복잡한 경우:**
    - 함수 내부에서 **이동하지 않을 문장**을 먼저 유지하기 위해 **Extract Function** 을 사용합니다.
    - 즉, **이동하지 않을 코드만을 새로운 함수로 분리**하고, 이 함수를 **임시적인 이름**으로 저장합니다.
3. **메서드가 서브클래스에서 오버라이드된 경우:**
    - 모든 서브클래스에서 동일한 코드 추출을 수행하여 **남은 메서드가 동일한 형태가 되도록 정리**합니다.
    - 이후 **서브클래스의 중복된 메서드를 제거**합니다.
4. **기존 함수 제거 (Inline Function)**
    - 원래 함수를 `Inline Function`을 사용하여 제거합니다.
    - 즉, 기존 호출자들이 새로운 방식으로 변경되었다면, 더 이상 필요 없는 원래 함수를 삭제합니다.
5. **추출된 함수의 이름 변경 (Change Function Declaration)**
    - **Extract Function**으로 생성한 임시 함수의 이름을 원래 함수 이름으로 변경합니다.
    - 또는, 더 적절한 이름을 생각해 적용할 수도 있습니다.

## **예제**

### **리팩토링 전 코드**

현재 `emitPhotoData()` 함수가 **사진 정보를 출력하는 역할**을 하지만,

- `renderPerson()`과 `listRecentPhotos()` 두 개의 함수에서 호출되고 있습니다.
- 두 함수 중 하나(`listRecentPhotos()`)에서는 **위치(location) 출력 방식을 다르게 적용하고 싶음**
- 따라서, `emitPhotoData()`에서 `<p>location: ...</p>` 부분을 함수 내부에서 제거하고 호출자(caller)로 이동시킬 계획입니다.

```java
public class HtmlRenderer {
    public static void renderPerson(PrintStream outStream, Person person) {
        outStream.println("<p>" + person.getName() + "</p>");
        renderPhoto(outStream, person.getPhoto());
        emitPhotoData(outStream, person.getPhoto());
    }

    public static void listRecentPhotos(PrintStream outStream, List<Photo> photos) {
        photos.stream()
              .filter(p -> p.getDate().after(recentDateCutoff()))
              .forEach(p -> {
                  outStream.println("<div>");
                  emitPhotoData(outStream, p);
                  outStream.println("</div>");
              });
    }

    public static void emitPhotoData(PrintStream outStream, Photo photo) {
        outStream.println("<p>title: " + photo.getTitle() + "</p>");
        outStream.println("<p>date: " + photo.getDate() + "</p>");
        outStream.println("<p>location: " + photo.getLocation() + "</p>");
    }

    public static void renderPhoto(PrintStream outStream, Photo photo) {
        outStream.println("<img src='" + photo.getUrl() + "' />");
    }

    public static Date recentDateCutoff() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -30); // 최근 30일 기준
        return cal.getTime();
    }
}

```

---

### **리팩토링 후 코드**

- `<p>location: ...</p>` 줄을 **emitPhotoData() 내부에서 제거**하고,
  대신 이를 호출하는 `renderPerson()`과 `listRecentPhotos()`에서 직접 처리하도록 변경하였습니다.
- 이 과정에서 기존 `emitPhotoData()`를 먼저 **Extract Function (106)** 을 사용해 분리한 후,
  최종적으로 기존 함수를 **Inline Function (115)** 하여 제거했습니다.

```java
public class HtmlRenderer {
    public static void renderPerson(PrintStream outStream, Person person) {
        outStream.println("<p>" + person.getName() + "</p>");
        renderPhoto(outStream, person.getPhoto());
        emitPhotoData(outStream, person.getPhoto());
        outStream.println("<p>location: " + person.getPhoto().getLocation() + "</p>"); // 이동된 코드
    }

    public static void listRecentPhotos(PrintStream outStream, List<Photo> photos) {
        photos.stream()
              .filter(p -> p.getDate().after(recentDateCutoff()))
              .forEach(p -> {
                  outStream.println("<div>");
                  emitPhotoData(outStream, p);
                  outStream.println("<p>location: " + p.getLocation() + "</p>"); // 이동된 코드
                  outStream.println("</div>");
              });
    }

    public static void emitPhotoData(PrintStream outStream, Photo photo) {
        outStream.println("<p>title: " + photo.getTitle() + "</p>");
        outStream.println("<p>date: " + photo.getDate() + "</p>");
    }

    public static void renderPhoto(PrintStream outStream, Photo photo) {
        outStream.println("<img src='" + photo.getUrl() + "' />");
    }

    public static Date recentDateCutoff() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -30);
        return cal.getTime();
    }
}

```

---

### **리팩토링 과정 설명**

1. **emitPhotoData()에서 `<p>location: ...</p>` 코드 분리**
    - `emitPhotoData()`는 **공통적인 사진 정보(title, date)를 출력**해야 하지만,**위치(location) 출력 방식은 호출자마다 다르게 적용할 필요**가 있음.
    - 따라서 `<p>location: ...</p>` 부분을 `emitPhotoData()`에서 제거.
2. **Extract Function 적용**
    - `emitPhotoData()`에서 **제거할 부분을 새로운 함수(zztmp)로 임시 분리**하여 테스트 후 수정.
3. **Inline Function 적용**
    - 기존 호출자(`renderPerson`, `listRecentPhotos`)에서 `emitPhotoData()` 호출 뒤 `<p>location: ...</p>`을 추가.
4. **이름 변경 (Change Function Declaration)**
    - 기존 `zztmp()` 임시 함수는 제거하고, 기존 `emitPhotoData()`를 유지.

---

### **개선된 점 및 설명**

✅ **위치 출력 방식을 개별 호출자에서 제어**할 수 있음

✅ **emitPhotoData()는 공통적인 역할(title, date)만 수행**하여 함수의 책임이 명확해짐

✅ **기존 함수 구조를 유지하면서 필요한 코드만 이동**하여 변경의 범위를 최소화함

✅ **유지보수성 향상**: 이후 `renderPerson()`과 `listRecentPhotos()`에서 각각 다른 방식으로 위치 정보를 처리할 수 있음

# 8.5 인라인 코드를 함수 호출로 바꾸기

## **배경**

1. **함수의 역할**
    - 함수는 **코드를 이해하기 쉽게 만들고, 중복을 제거하는 역할**을 합니다.
    - 적절한 이름을 가진 함수는 **코드의 목적을 설명**하며, 내부 구현보다 **의도가 더 명확해지도록** 도와줍니다.
2. **중복 제거의 중요성**
    - 동일한 기능을 여러 곳에서 **직접 구현하는 인라인 코드**가 존재하면, 유지보수가 어렵고 실수의 가능성이 증가합니다.
    - 함수 호출로 대체하면, **한 곳만 수정하면 모든 호출자가 자동으로 변경을 반영**할 수 있습니다.
3. **함수로 대체할 때의 고려 사항**
    - 인라인 코드가 이미 **기존 함수와 동일한 동작**을 수행한다면, 이를 함수 호출로 변경하는 것이 바람직합니다.
    - 단, 기존 함수와 **유사하지만 실제로는 다른 역할을 수행하는 경우** 함수 호출로 대체하지 않는 것이 좋습니다.
    - **함수 이름이 코드의 목적과 부합하는지 확인**하고, 필요하면 `Rename Function (124)`을 사용하여 의미를 명확히 합니다.
4. **라이브러리 함수 활용**
    - 동일한 기능을 수행하는 **표준 라이브러리 함수가 있는 경우**, 직접 구현하지 않고 **라이브러리 함수를 호출**하는 방식으로 최적화할 수 있습니다.
    - 예: 직접 문자열 조작을 수행하는 코드가 있다면, `String.format()`이나 `java.util.StringJoiner`를 활용하는 것이 더 바람직합니다.

## **절차**

1. **기존 함수 확인**
    - 인라인 코드가 **기존 함수와 동일한 동작을 수행하는지** 확인합니다.
    - 함수의 목적이 적절한지 판단하고, 필요하면 **Rename Function (124)** 을 적용하여 의미를 명확히 합니다.
2. **인라인 코드 제거 및 함수 호출로 변경**
    - 중복된 인라인 코드를 찾아서 **해당 부분을 함수 호출로 대체**합니다.
    - 만약 라이브러리 함수가 적절하다면, 직접 구현하는 대신 라이브러리 함수를 사용합니다.
3. **테스트 수행**
    - 변경 후 전체 테스트를 실행하여, 기존 기능이 그대로 유지되는지 확인합니다.
    - 테스트가 통과하면 리팩토링 완료!

### **핵심 요약**

✅ **기존 함수와 동일한 역할을 하는 인라인 코드가 있는지 확인**

✅ **인라인 코드를 함수 호출로 변경**

✅ **테스트를 실행하여 정상 동작 확인**

# 8.6 문장 슬라이드하기

## **배경**

1. **코드의 가독성과 이해도 향상**
    - **관련된 코드가 함께 배치**되어 있으면 가독성이 좋아지고 유지보수가 쉬워집니다.
    - 특정 데이터 구조를 다루는 코드가 **다른 데이터 구조 관련 코드 사이에 섞여 있으면 이해하기 어려움**
    - 따라서, **서로 관련된 코드끼리 모아서 배치**하는 것이 중요합니다.
2. **변수 선언 위치 최적화**
    - **일부 개발자는 변수 선언을 함수의 맨 위에 배치**하지만,
    - **실제로는 변수를 사용하기 직전에 선언하는 것이 코드 가독성과 유지보수성에 유리**할 수 있음.
3. **리팩토링 준비 단계로서의 활용**
    - **Extract Function** 을 수행하려면 먼저 관련 코드가 **하나의 블록으로 모여 있어야 함**
    - 따라서, **문장 슬라이드(Slide Statements)** 를 먼저 적용하여 코드를 정리한 후, **함수 추출을 진행하는 것이 일반적**

## **절차**

1. **이동할 코드 블록의 목표 위치 결정**
    - 관련 코드끼리 더 가깝게 배치하기 위해 **이동할 코드(코드 조각, fragment)의 목표 위치를 정합니다.**
    - 이동하려는 코드와 기존 코드 사이에 **의존성(interference)이 있는지 확인**합니다.
    - **의존성(Interference) 발생 시, 이동을 중단하거나 더 작은 단위로 이동을 시도**해야 합니다.
2. **의존성 규칙 검토 (Slide 가능 여부 판단)**
    - **변수를 선언하기 전에 해당 변수를 사용하는 코드보다 앞으로 이동할 수 없음.**
    - **코드 조각을 참조하는 코드보다 뒤로 이동할 수 없음.**
    - **이동하는 코드가 참조하는 요소를 변경하는 코드 위로 이동할 수 없음.**
    - **이동하는 코드가 변수를 변경하는 경우, 그 변수를 참조하는 다른 코드보다 뒤로 이동할 수 없음.**
3. **코드 이동**
    - 위의 규칙을 충족하는 경우, **원래 위치에서 잘라내고(cut), 목표 위치에 붙여넣음(paste).**
4. **테스트 수행**
    - 변경 후 **전체 테스트를 실행하여 코드가 정상적으로 작동하는지 확인.**
    - 만약 테스트가 실패하면 **더 작은 단위로 이동을 시도**하거나 **한 번에 이동하는 코드 양을 줄여서 점진적으로 조정.**

## 예제1

### **리팩토링 전 코드**

다음은 **상품 가격을 계산하는 코드**입니다.

- 현재 **할인 관련 코드가 분산되어 있어서 가독성이 떨어지는 상태**입니다.
- `discount` 변수를 너무 일찍 선언했으며, **관련 로직과 멀리 떨어져 있어 수정이 어려움**.

```java
public class PricingCalculator {

    public static void main(String[] args) {
        calculateCharge();
    }

    public static void calculateCharge() {
        PricingPlan pricingPlan = retrievePricingPlan(); // 가격 정책 가져오기
        Order order = retrieveOrder(); // 주문 정보 가져오기

        double baseCharge = pricingPlan.getBase();
        double charge;
        double chargePerUnit = pricingPlan.getUnit();
        int units = order.getUnits();
        double discount; // 너무 일찍 선언됨

        charge = baseCharge + units * chargePerUnit;

        int discountableUnits = Math.max(units - pricingPlan.getDiscountThreshold(), 0);
        discount = discountableUnits * pricingPlan.getDiscountFactor(); // 할인 계산
        if (order.isRepeatCustomer()) discount += 20; // 반복 고객 추가 할인

        charge = charge - discount; // 최종 가격 반영
        chargeOrder(charge);
    }

    private static PricingPlan retrievePricingPlan() {
        return new PricingPlan(100, 5, 10, 2);
    }

    private static Order retrieveOrder() {
        return new Order(15, true);
    }

    private static void chargeOrder(double charge) {
        System.out.println("Final Charge: " + charge);
    }
}

```

**🔍 문제점**

- `discount` 변수가 너무 일찍 선언됨 → 사용되는 위치(할인 계산)와 멀리 떨어져 있음.
- **할인 계산 관련 코드가 분산**되어 있어서 유지보수하기 어려움.
- `retrieveOrder()` 호출이 너무 상단에 위치하여, **실제 사용하는 코드와 가깝게 배치하는 것이 필요**.

---

### **리팩토링 후 코드**

- **변수 선언을 사용 직전에 배치**하여 가독성을 개선.
- **할인 관련 코드(라인 9-11)를 한 곳으로 모음**.
- **주문 정보를 가져오는 코드(retrieveOrder())를 사용하는 위치에 가깝게 이동**.

```java
public class PricingCalculator {

    public static void main(String[] args) {
        calculateCharge();
    }

    public static void calculateCharge() {
        PricingPlan pricingPlan = retrievePricingPlan(); // 가격 정책 가져오기

        double baseCharge = pricingPlan.getBase();
        double chargePerUnit = pricingPlan.getUnit();

        Order order = retrieveOrder(); // 주문 정보를 여기로 이동

        int units = order.getUnits();
        double charge = baseCharge + units * chargePerUnit; // 초기 계산

        // 할인 계산 관련 코드 모으기
        int discountableUnits = Math.max(units - pricingPlan.getDiscountThreshold(), 0);
        double discount = discountableUnits * pricingPlan.getDiscountFactor();
        if (order.isRepeatCustomer()) discount += 20;

        charge = charge - discount; // 최종 가격 반영
        chargeOrder(charge);
    }

    private static PricingPlan retrievePricingPlan() {
        return new PricingPlan(100, 5, 10, 2);
    }

    private static Order retrieveOrder() {
        return new Order(15, true);
    }

    private static void chargeOrder(double charge) {
        System.out.println("Final Charge: " + charge);
    }
}

```

---

### **리팩토링 과정 설명**

1. **할인 관련 코드(라인 9-11)를 모아서 논리적으로 그룹화**
    - 할인과 관련된 연산(`discountableUnits`, `discount`, `order.isRepeatCustomer()`)이 **한 블록 안에서 수행되도록 변경**.
    - 이제 **할인 정책을 수정할 때 한 곳만 보면 되므로 유지보수성이 향상됨**.
2. **변수 선언 위치 최적화**
    - `discount` 변수를 너무 일찍 선언하지 않고, **사용하는 부분에서 선언하도록 변경**.
    - `order`를 상단이 아니라 **사용하는 위치에 가깝게 배치**하여, **의미적으로 더 명확한 코드가 됨**.
3. **가독성과 유지보수성 개선**
    - 관련된 코드가 함께 배치되어 있어, **한눈에 이해하기 쉬운 코드로 변경됨**.
    - 할인 관련 로직을 `Extract Function (106)`을 적용할 수 있도록 정리 완료.

## 예제2

### **리팩토링 전 코드**

현재 **if-else 블록 내에서 중복된 로직이 포함되어 있음**

- `allocatedResources.add(result);`가 **조건문 내부에서 반복적으로 실행**됨
- 이를 **조건문 외부로 이동(Slide Statements 적용)**하여 **중복 제거** 가능

```java
import java.util.ArrayList;
import java.util.List;

public class ResourceAllocator {

    private List<Resource> availableResources = new ArrayList<>();
    private List<Resource> allocatedResources = new ArrayList<>();

    public Resource allocateResource() {
        Resource result;

        if (availableResources.isEmpty()) {
            result = createResource();
            allocatedResources.add(result); // 중복된 코드
        } else {
            result = availableResources.remove(availableResources.size() - 1);
            allocatedResources.add(result); // 중복된 코드
        }

        return result;
    }

    private Resource createResource() {
        return new Resource("New Resource");
    }
}

class Resource {
    private String name;

    public Resource(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

```

**🔍 문제점**

- **`allocatedResources.add(result);`가 조건문 내부에서 중복**
- **중복을 제거하고 코드 가독성을 향상**시키기 위해 **조건문 외부로 이동 가능**

---

### **리팩토링 후 코드**

- **중복된 `allocatedResources.add(result);`를 if-else 블록 밖으로 이동**
- **가독성 개선 & 유지보수성 향상**

```java
import java.util.ArrayList;
import java.util.List;

public class ResourceAllocator {

    private List<Resource> availableResources = new ArrayList<>();
    private List<Resource> allocatedResources = new ArrayList<>();

    public Resource allocateResource() {
        Resource result;

        if (availableResources.isEmpty()) {
            result = createResource();
        } else {
            result = availableResources.remove(availableResources.size() - 1);
        }

        allocatedResources.add(result); // 조건문 외부로 이동하여 중복 제거

        return result;
    }

    private Resource createResource() {
        return new Resource("New Resource");
    }
}

class Resource {
    private String name;

    public Resource(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

```

---

### **리팩토링 과정 설명**

1. **조건문 내부에서 중복된 문장(`allocatedResources.add(result);`) 식별**
    - if 블록과 else 블록에서 동일한 코드가 반복적으로 사용됨
    - 따라서, **조건문 외부로 이동 가능**
2. **문장 슬라이드(Slide Statements) 적용**
    - `allocatedResources.add(result);`를 if-else 블록 **외부로 이동하여 중복 제거**
    - 논리적으로 변경되지 않으며, **불필요한 반복을 줄이고 가독성을 향상**
3. **코드 가독성과 유지보수성 개선**
    - 같은 동작을 두 번 작성하는 대신, **한 번만 실행하도록 최적화**
    - 유지보수 시 **조건문 내부만 수정하면 되고, 공통 코드에는 영향을 주지 않음**

# 8.7 반복문 쪼개기

## 배경

1. **단일 루프에서 여러 작업을 수행하는 문제**
    - 일부 코드는 **한 개의 반복문에서 여러 작업을 동시에 처리**하려고 합니다.
    - 이렇게 하면 성능은 조금 최적화될 수 있지만, **코드를 이해하고 유지보수하기 어려워집니다.**
    - **한 작업을 수정하려고 해도 다른 작업까지 고려해야 하므로 수정이 어렵고, 버그 발생 가능성이 높아짐.**
2. **반복문 쪼개기의 장점**
    - **루프를 나누면 개별 작업에 집중할 수 있음.**
    - 각 루프는 **하나의 역할만 담당하므로 가독성이 향상됨.**
    - **수정 시 영향 범위가 좁아져 유지보수성이 개선됨.**
    - **각 루프를 함수로 추출(Extract Function)할 가능성이 커짐**, 즉 더 나은 코드 구조로 발전 가능.
3. **반복문 성능 문제에 대한 오해**
    - 많은 개발자들이 **반복문을 여러 번 실행하면 성능이 저하된다고 우려**함.
    - 하지만 대부분의 경우, **루프 자체가 성능의 병목이 되는 경우는 드뭄.**
    - **리팩토링(가독성 개선)과 성능 최적화는 별도로 고려해야 함.**
    - 코드가 명확해지면 이후 더 강력한 최적화가 가능해짐.
4. **반복문 쪼개기 후 추가 리팩토링 가능**
    - **각 루프를 별도 함수로 추출(Extract Function)** 하여 더 높은 추상화 수준을 만들 수 있음.
    - 결과적으로 **코드가 더 모듈화되고 재사용성이 증가**함.

## **절차**

1. **반복문을 복사 (Copy the loop)**
    - 원래 루프를 그대로 **복사하여 두 개의 루프를 만듦**
    - 각 루프에서 **하나의 작업만 수행하도록 분리**
2. **중복되는 부작용(사이드 이펙트) 제거 (Identify and eliminate duplicate side effects)**
    - 두 개의 루프가 동일한 데이터나 변수를 수정하는 경우, **필요한 작업만 남기고 불필요한 변경 제거**
3. **테스트 수행 (Test)**
    - 분리한 루프가 **기존과 동일한 결과를 내는지 테스트**
    - 리팩토링 후 **기능이 정상적으로 작동하는지 확인**
4. **Extract Function (106) 적용 고려**
    - 반복문이 특정 작업을 수행하는 것이 명확해졌다면,
    - **각 루프를 별도 함수로 추출하여 가독성과 재사용성을 개선**

## **예제**

### **리팩토링 전 코드**

현재 **단일 루프에서 두 가지 작업(총 급여 계산 + 최연소 나이 찾기)** 를 수행하고 있음.

- 루프가 두 가지 역할을 하므로 유지보수가 어려움
- 수정할 때 **루프 내부의 다른 로직도 신경 써야 해서 가독성이 낮아짐**

```java
import java.util.List;

public class PeopleAnalyzer {

    public static String analyzePeople(List<Person> people) {
        int youngest = people.isEmpty() ? Integer.MAX_VALUE : people.get(0).getAge();
        int totalSalary = 0;

        for (Person p : people) {
            if (p.getAge() < youngest) youngest = p.getAge(); // 최연소 나이 찾기
            totalSalary += p.getSalary(); // 총 급여 계산
        }

        return "youngestAge: " + youngest + ", totalSalary: " + totalSalary;
    }
}

```

---

### **리팩토링 1단계: 반복문 쪼개기 (Split Loop)**

- **두 가지 역할을 하는 루프를 복사**하여 개별 루프로 분리
- 각 루프가 **하나의 역할만 수행하도록 변경**

```java
import java.util.List;

public class PeopleAnalyzer {

    public static String analyzePeople(List<Person> people) {
        int youngest = people.isEmpty() ? Integer.MAX_VALUE : people.get(0).getAge();
        int totalSalary = 0;

        // 첫 번째 루프: 총 급여 계산
        for (Person p : people) {
            totalSalary += p.getSalary();
        }

        // 두 번째 루프: 최연소 나이 찾기
        for (Person p : people) {
            if (p.getAge() < youngest) youngest = p.getAge();
        }

        return "youngestAge: " + youngest + ", totalSalary: " + totalSalary;
    }
}

```

---

### **리팩토링 2단계: 함수 추출 (Extract Function)**

- 각 루프를 개별 함수로 분리하여 **코드 가독성을 향상**
- `totalSalary()` 와 `youngestAge()` 함수로 분리하여 **분리된 책임을 명확히 함**

```java
import java.util.List;

public class PeopleAnalyzer {

    public static String analyzePeople(List<Person> people) {
        return "youngestAge: " + youngestAge(people) + ", totalSalary: " + totalSalary(people);
    }

    private static int totalSalary(List<Person> people) {
        int totalSalary = 0;
        for (Person p : people) {
            totalSalary += p.getSalary();
        }
        return totalSalary;
    }

    private static int youngestAge(List<Person> people) {
        int youngest = people.isEmpty() ? Integer.MAX_VALUE : people.get(0).getAge();
        for (Person p : people) {
            if (p.getAge() < youngest) youngest = p.getAge();
        }
        return youngest;
    }
}

```

---

### **리팩토링 3단계: 루프를 스트림 API로 변환 (Replace Loop with Pipeline)**

- `totalSalary()`를 `reduce()` 메서드로 변환하여 **더 간결한 코드로 개선**
- `youngestAge()`는 `map()` + `min()`을 활용하여 **더 효율적인 코드로 변경**

```java
import java.util.List;

public class PeopleAnalyzer {

    public static String analyzePeople(List<Person> people) {
        return "youngestAge: " + youngestAge(people) + ", totalSalary: " + totalSalary(people);
    }

    private static int totalSalary(List<Person> people) {
        return people.stream().mapToInt(Person::getSalary).sum(); // 스트림으로 총 급여 계산
    }

    private static int youngestAge(List<Person> people) {
        return people.stream().mapToInt(Person::getAge).min().orElse(Integer.MAX_VALUE); // 스트림으로 최연소 나이 찾기
    }
}

```

---

### **리팩토링 과정 설명**

1. **반복문을 분리 (Split Loop 적용)**
    - 단일 루프에서 **급여 계산과 나이 비교를 분리하여 각각의 루프가 하나의 역할만 담당**
    - 유지보수 및 코드 가독성 향상
2. **각 역할을 개별 함수로 추출 (Extract Function 적용)**
    - `totalSalary()` 와 `youngestAge()`를 분리하여 **코드가 더 읽기 쉬워지고 재사용성이 증가**
3. **루프를 스트림 API로 변환 (Replace Loop with Pipeline 적용)**
    - `reduce()`, `map()`, `min()` 등의 **스트림 API 활용하여 코드 간결화 및 가독성 개선**

---

### **개선된 점**

✅ **각 루프가 하나의 역할만 수행 → 가독성 향상**

✅ **루프 내부에서 여러 변수를 관리하지 않아도 됨 → 유지보수성 향상**

✅ **각 기능을 개별 함수로 추출 → 코드 재사용성이 증가**

✅ **스트림 API 활용으로 코드 간결화 및 최적화 가능**

# 8.8 반복문을 파이프라인으로 바꾸기

## **배경**

1. **전통적인 반복문(Loop)의 문제점**
    - 대부분의 프로그래머는 **for-loop 또는 while-loop을 사용하여 컬렉션을 순회**하는 방식에 익숙함.
    - 그러나 **반복문은 로직이 길어질수록 가독성이 떨어지고 유지보수성이 낮아짐**.
    - 중간에 **필터링(filtering), 변환(mapping), 집계(reducing) 등의 처리가 많아지면 코드가 복잡해지고 실수가 발생할 가능성 증가**.
2. **파이프라인(Collection Pipeline) 방식의 장점**
    - **가독성이 높음** → 데이터를 변환하고 처리하는 흐름이 **위에서 아래로 자연스럽게** 읽힘.
    - **유지보수성 향상** → 각 처리 단계가 분리되어 있어 **부분적인 수정이 쉬움**.
    - **함수형 스타일 적용 가능** → `map()`, `filter()`, `reduce()` 등의 함수형 연산을 사용하면 **더 간결하고 직관적인 코드 작성 가능**.
3. **자주 사용하는 컬렉션 파이프라인 연산**
    - `map(Function<T, R>)` → 컬렉션의 요소를 다른 형태로 변환
    - `filter(Predicate<T>)` → 특정 조건을 만족하는 요소만 선택
    - `reduce(BinaryOperator<T>)` → 모든 요소를 하나의 값으로 축소
    - `collect(Collector<T, A, R>)` → 결과를 새로운 컬렉션으로 변환

## **절차**

1. **반복문에서 순회하는 컬렉션을 새로운 변수에 저장**
    - 기존 루프에서 처리하는 컬렉션을 **별도의 변수로 선언**하여 **파이프라인 변환 준비**.
    - 기존 코드와 동작이 동일하게 유지되도록 **단계적으로 변경할 수 있음**.
2. **반복문의 각 작업을 하나씩 파이프라인 연산으로 변환**
    - 루프 내부의 개별 동작을 `map()`, `filter()`, `reduce()` 등의 **스트림 연산으로 대체**.
    - **각 변환 후 테스트**를 실행하여 기존 동작과 동일한지 확인.
    - 한 번에 모든 작업을 변환하지 말고, **단계적으로 변경 후 테스트하는 것이 안전함**.
3. **반복문을 제거하고 최종 결과를 반환**
    - 모든 동작이 컬렉션 파이프라인으로 변환되면,
    - **기존 반복문을 제거하고 최종 결과를 변수에 할당**.
4. **누적 변수(accumulator)를 사용한 경우, 최종 결과를 할당**
    - `reduce()`, `sum()`, `collect()` 등을 사용하여 누적 변수에 할당.
    - 예: `for` 문에서 `totalSalary += p.salary;` → `totalSalary = people.stream().mapToInt(Person::getSalary).sum();`

## 예제

### **리팩토링 전 코드 (전통적인 반복문)**

이 코드에서는 CSV 데이터를 줄(line) 단위로 읽고,

1. 첫 번째 줄(헤더) 제거
2. 빈 줄 무시
3. `"India"`인 행만 필터링
4. 도시와 전화번호 정보 추출

**문제점:**

- **for 문 내에서 여러 작업(필터링, 매핑, 변환)을 수행**
- **각 단계를 따로 볼 수 없어서 가독성이 떨어짐**
- **수정 시 모든 조건을 고려해야 하므로 유지보수가 어려움**

```java
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OfficeDataProcessor {

    public static List<Office> acquireData(String input) {
        String[] lines = input.split("\n");
        boolean firstLine = true;
        List<Office> result = new ArrayList<>();

        for (String line : lines) {
            if (firstLine) {
                firstLine = false; // 첫 번째 줄(헤더) 건너뛰기
                continue;
            }
            if (line.trim().isEmpty()) continue; // 빈 줄 무시

            String[] fields = line.split(",");
            if (fields[1].trim().equals("India")) {
                result.add(new Office(fields[0].trim(), fields[2].trim())); // 도시와 전화번호 저장
            }
        }
        return result;
    }
}

```

---

### **리팩토링 1단계: 컬렉션을 파이프라인 변수로 변경**

- **첫 번째 줄(헤더) 제거 → `Arrays.stream(lines).skip(1)`**
- **빈 줄 제거 → `.filter(line -> !line.trim().isEmpty())`**
- **각 줄을 필드 배열로 변환 → `.map(line -> line.split(","))`**
- **해당 국가(India) 필터링 → `.filter(fields -> fields[1].trim().equals("India"))`**
- **필요한 데이터 추출 → `.map(fields -> new Office(fields[0].trim(), fields[2].trim()))`**

```java
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class OfficeDataProcessor {

    public static List<Office> acquireData(String input) {
        return Arrays.stream(input.split("\n"))
                .skip(1) // 첫 번째 줄(헤더) 제거
                .filter(line -> !line.trim().isEmpty()) // 빈 줄 제거
                .map(line -> line.split(",")) // 각 줄을 필드 배열로 변환
                .filter(fields -> fields[1].trim().equals("India")) // 특정 국가(India) 필터링
                .map(fields -> new Office(fields[0].trim(), fields[2].trim())) // 필요한 데이터 추출
                .collect(Collectors.toList()); // 최종 결과 리스트로 변환
    }
}

```

---

### **리팩토링 과정 설명**

1. **반복문을 스트림(Stream)으로 변환**
    - `Arrays.stream(lines)`를 사용하여 **배열을 스트림 형태로 변환**
    - `.skip(1)` → **첫 번째 줄(헤더)을 제거**
    - `.filter(line -> !line.trim().isEmpty())` → **빈 줄을 필터링하여 제거**
2. **데이터 변환 과정 분리**
    - `.map(line -> line.split(","))` → **각 줄을 필드 배열로 변환**
    - `.filter(fields -> fields[1].trim().equals("India"))` → **인도(India) 데이터만 필터링**
    - `.map(fields -> new Office(fields[0].trim(), fields[2].trim()))` → **필요한 데이터(도시, 전화번호) 추출**
3. **최종 데이터 리스트로 변환**
    - `.collect(Collectors.toList())` → **변환된 데이터들을 리스트로 수집**

---

### **리팩토링 2단계: 변수명 최적화 및 코드 정리**

- `fields` 대신 `columns` 사용하여 **데이터 배열임을 명확히 표현**
- **가독성을 높이기 위해 연산을 한 줄씩 정렬**

```java
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class OfficeDataProcessor {

    public static List<Office> acquireData(String input) {
        return Arrays.stream(input.split("\n"))
                .skip(1) // 첫 번째 줄(헤더) 제거
                .filter(line -> !line.trim().isEmpty()) // 빈 줄 제거
                .map(line -> line.split(",")) // 각 줄을 필드 배열로 변환
                .filter(columns -> columns[1].trim().equals("India")) // 특정 국가(India) 필터링
                .map(columns -> new Office(columns[0].trim(), columns[2].trim())) // 필요한 데이터 추출
                .collect(Collectors.toList()); // 최종 결과 리스트로 변환
    }
}

```

---

### **개선된 점**

✅ **반복문 제거 → 가독성 향상**

✅ **각 변환 단계를 개별 연산으로 분리 → 유지보수 편리**

✅ **각 작업을 `filter()`, `map()` 등으로 체인 연결 → 한눈에 데이터 흐름 파악 가능**

✅ **코드가 더 간결하고 직관적으로 변경됨**

# 8.9 죽은 코드 제거하기

## 배경

1. **죽은 코드(Dead Code)란?**
    - **사용되지 않는 함수, 변수, 클래스, 또는 로직**을 의미.
    - 프로그램 실행에는 영향을 주지 않지만, **코드베이스를 이해하는 데 방해가 됨**.
    - 유지보수 시 불필요한 혼란을 초래할 수 있음 (예: "이 함수가 왜 여기에 있지?").
2. **왜 죽은 코드를 삭제해야 하는가?**
    - **가독성 향상**: 코드를 읽을 때 **필요한 부분만 집중적으로 파악할 수 있음**.
    - **유지보수성 증가**: 오래된 코드가 남아 있으면 **새로운 기능을 추가할 때 혼란을 초래할 가능성이 있음**.
    - **불필요한 의존성 제거**: 죽은 코드가 **다른 코드에 대한 참조를 포함하고 있다면**, 해당 참조도 제거해야 함.
    - **버그 발생 가능성 감소**: 가끔 죽은 코드가 **의도치 않게 실행되거나 잘못된 동작을 유발할 수도 있음**.
3. **버전 관리 시스템이 있기 때문에 삭제해도 문제 없음**
    - 과거에는 **죽은 코드를 주석 처리하여 보관**하는 경우가 많았음.
    - 하지만 Git 같은 **버전 관리 시스템이 발전하면서, 삭제한 코드도 필요하면 언제든 복구 가능**.
    - **따라서 죽은 코드는 과감하게 삭제하는 것이 더 바람직함.**

---

## **절차**

1. **해당 코드가 실제로 사용되지 않는지 확인**
    - 함수, 변수, 클래스 등이 **다른 코드에서 호출되는지 검색** (`Find Usages` 또는 `Ctrl + Shift + F`)
    - 정적 분석 도구(예: IntelliJ의 **Code Inspection**, SonarQube 등)를 활용하면 효과적
    - **사용되지 않는 코드가 확실하면 다음 단계 진행**
2. **죽은 코드 제거**
    - **사용되지 않는 함수, 변수, 클래스, import 문 등 삭제**
    - 단순한 주석 처리 대신 **코드를 완전히 제거**
3. **테스트 실행**
    - **삭제 후에도 기존 기능이 정상적으로 동작하는지 확인**
    - CI/CD 환경에서 빌드가 정상적으로 수행되는지 점검

---

## **예제**

### **리팩토링 전 (죽은 코드 포함)**

```java
import java.util.List;

public class EmployeeService {

    private List<Employee> employees;

    public EmployeeService(List<Employee> employees) {
        this.employees = employees;
    }

    // 현재 사용되지 않는 함수 (죽은 코드)
    public void printAllEmployees() {
        for (Employee emp : employees) {
            System.out.println(emp.getName() + " - " + emp.getDepartment());
        }
    }

    public Employee findEmployeeById(int id) {
        return employees.stream()
                .filter(emp -> emp.getId() == id)
                .findFirst()
                .orElse(null);
    }
}

```

**🔍 문제점**

- `printAllEmployees()` 함수가 **어디에서도 호출되지 않음** → **불필요한 코드**
- 유지보수 시 **이 함수가 왜 존재하는지 고민할 필요가 있음**

---

### **리팩토링 후 (죽은 코드 제거)**

```java
import java.util.List;

public class EmployeeService {

    private List<Employee> employees;

    public EmployeeService(List<Employee> employees) {
        this.employees = employees;
    }

    public Employee findEmployeeById(int id) {
        return employees.stream()
                .filter(emp -> emp.getId() == id)
                .findFirst()
                .orElse(null);
    }
}

```

**✅ 개선된 점**

- **사용되지 않는 함수 제거 → 코드 가독성 및 유지보수성 향상**
- **혼란을 줄이고, 불필요한 코드 의존성을 없앰**

---

### **죽은 코드 제거 후 추가 정리**

- 필요하면 **Git commit 메시지에 "Removed unused function: printAllEmployees()"** 같은 내용을 남겨 추적 가능.
- **정적 분석 도구(예: IntelliJ Code Inspection, SonarQube)** 를 활용하면 불필요한 코드를 쉽게 찾을 수 있음.
- **주석 처리로 남기지 말고 완전히 삭제** → 필요하면 Git에서 복원 가능

---

## **최종 요약**

✅ **사용되지 않는 코드(함수, 변수, import 등)를 삭제하여 가독성과 유지보수성을 높임**

✅ **정적 분석 도구와 검색 기능을 활용하여 죽은 코드를 식별**

✅ **Git 같은 버전 관리 시스템이 있으므로, 필요하면 언제든 복구 가능 → 과감하게 삭제하는 것이 좋음**

✅ **죽은 코드를 제거하면 코드베이스가 더 깔끔해지고, 버그 발생 가능성이 줄어듦**