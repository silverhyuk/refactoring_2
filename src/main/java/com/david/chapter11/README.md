# **11.1** 질의 **함수와** 변경 **함수** 분리하기 (Separate Query from Modifier)

### 1. 배경 (Motivation)

- 어떤 **함수가 값을 반환**하면서도 **부작용(side effect)**이 존재하면, 그 함수는 테스트하기 어렵고 재사용성이 떨어집니다.
- 값만 반환하는 함수(순수 함수)는 여러 번 호출하더라도 안정적이며, 다른 위치로 옮기기 쉬우며, 테스트도 단순해집니다.
- 이 원칙은 **Command-Query Separation (CQS)** 원칙에 기반합니다.
    - **Command**: 상태를 변경하지만 값을 반환하지 않음
    - **Query**: 값을 반환하지만 상태를 변경하지 않음
- 함수를 설계할 때 명확히 구분함으로써 코드의 의도와 안정성이 명확해집니다.

---

### 2. 절차 (Mechanics)

1. 원래 함수를 복사하고, **query 성격**을 가진 새 이름을 붙임
2. 반환값을 분석해 새 함수에서 **부작용 제거**
3. 정적 분석기로 확인
4. 원래 함수 호출부를 찾아:
    - 반환값을 사용하는 경우 → **query 함수 호출 + modifier 함수 호출로 분리**
5. 원래 함수에서 **return 제거**
6. **테스트**
7. 중복 코드가 있으면 `Substitute Algorithm` 기법을 적용하여 중복 제거

---

### 3. 예시

### JavaScript (리팩토링 전)

```jsx
function alertForMiscreant(people) {
  for (const p of people) {
    if (p === "Don") {
      setOffAlarms();
      return "Don";
    }
    if (p === "John") {
      setOffAlarms();
      return "John";
    }
  }
  return "";
}

```

---

### 리팩토링 순서

### 1) Query 함수 분리

```jsx
function findMiscreant(people) {
  for (const p of people) {
    if (p === "Don" || p === "John") return p;
  }
  return "";
}

```

### 2) Modifier 함수 수정

```jsx
function alertForMiscreant(people) {
  if (findMiscreant(people) !== "") {
    setOffAlarms();
  }
}

```

---

### Java 버전으로 변환

### 🔻 리팩토링 전 (Java)

```java
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

```

---

### 🔺 리팩토링 후 (Java)

```java
public String findMiscreant(List<String> people) {
    for (String p : people) {
        if (p.equals("Don") || p.equals("John")) {
            return p;
        }
    }
    return "";
}

public void alertForMiscreant(List<String> people) {
    if (!findMiscreant(people).isEmpty()) {
        setOffAlarms();
    }
}

```

---

### 효과 정리

| 항목 | 리팩토링 전 | 리팩토링 후 |
| --- | --- | --- |
| 테스트 용이성 | ❌ 어려움 (사이드 이펙트 존재) | ✅ 순수 함수로 테스트 용이 |
| 코드 가독성 | ❌ 의도가 불명확 | ✅ 알람 로직과 찾기 로직 분리 |
| 재사용성 | ❌ 한 함수에 로직 집중 | ✅ 여러 곳에서 query 재사용 가능 |
| 유지보수 | ❌ 경로별 영향 파악 어려움 | ✅ 분리로 변경 영향 최소화 |

---

# 11.2 함수 매개변수화하기 (Parameterize Function)

### 1. 배경 (Motivation)

- **중복 제거:** 유사한 로직을 가지면서 리터럴 값만 다른 여러 함수를 하나의 매개변수화된 함수로 통합해 중복을 제거합니다.
- **유연성 및 재사용성 증가:** 함수에 매개변수를 도입하면 다른 값들을 손쉽게 전달할 수 있어 함수의 활용도가 높아집니다.
- **유지보수 용이:** 코드 중복이 줄어들어 변경이나 확장이 필요한 경우 한 곳만 수정하면 되어 안정적입니다.

### 2. 절차 (Mechanics)

1. **유사 함수 식별:** 리터럴 값만 다른 여러 함수를 확인합니다.
2. **기준 함수 선택 및 선언 변경:** 하나의 함수를 기준으로 Change Function Declaration 기법을 이용해 리터럴을 매개변수로 전환합니다.
3. **호출자 수정:** 각 함수 호출부에서 기존 리터럴 값을 새 매개변수로 전달하도록 변경합니다.
4. **함수 본문 수정:** 함수 내부의 리터럴을 해당 매개변수로 대체합니다.
5. **테스트:** 각 단계별로 테스트를 진행하여 변경사항이 올바르게 동작하는지 검증합니다.
6. **유사 함수 통합:** 나머지 유사한 함수 호출을 매개변수화된 함수로 대체하고, 필요에 따라 추가 조정을 진행합니다.

### 3. 예시 (Java 코드)

### 예제 1: 임금 인상 함수

**리팩토링 전:**

```java
public void tenPercentRaise(Person aPerson) {
    aPerson.setSalary(aPerson.getSalary() * 1.10);
}

public void fivePercentRaise(Person aPerson) {
    aPerson.setSalary(aPerson.getSalary() * 1.05);
}

```

**리팩토링 후:**

```java
public void raise(Person aPerson, double factor) {
    aPerson.setSalary(aPerson.getSalary() * (1 + factor));
}

```

*호출 시 예:*

```java
raise(aPerson, 0.10); // 10% 인상
raise(aPerson, 0.05); // 5% 인상

```

---

### 예제 2: 요금 계산 함수

**리팩토링 전:**

```java
public BigDecimal baseCharge(double usage) {
    if (usage < 0) return BigDecimal.ZERO;
    BigDecimal amount = BigDecimal.valueOf(bottomBand(usage)).multiply(new BigDecimal("0.03"))
            .add(BigDecimal.valueOf(middleBand(usage)).multiply(new BigDecimal("0.05")))
            .add(BigDecimal.valueOf(topBand(usage)).multiply(new BigDecimal("0.07")));
    return usd(amount);
}

public double bottomBand(double usage) {
    return Math.min(usage, 100);
}

public double middleBand(double usage) {
    return usage > 100 ? Math.min(usage, 200) - 100 : 0;
}

public double topBand(double usage) {
    return usage > 200 ? usage - 200 : 0;
}

```

**리팩토링 후:**

```java
public BigDecimal baseCharge(double usage) {
    if (usage < 0) return BigDecimal.ZERO;
    BigDecimal amount = BigDecimal.valueOf(withinBand(usage, 0, 100)).multiply(new BigDecimal("0.03"))
            .add(BigDecimal.valueOf(withinBand(usage, 100, 200)).multiply(new BigDecimal("0.05")))
            .add(BigDecimal.valueOf(withinBand(usage, 200, Double.POSITIVE_INFINITY)).multiply(new BigDecimal("0.07")));
    return usd(amount);
}

public double withinBand(double usage, double bottom, double top) {
    return usage > bottom ? Math.min(usage, top) - bottom : 0;
}

```

---

### 정리

- **장점:**
    - 중복 코드를 제거해 유지보수와 확장성을 높입니다.
    - 매개변수를 활용하여 다양한 상황에 유연하게 대응할 수 있습니다.
    - 테스트 시 각 로직이 하나의 함수에 집중되어 변경 영향이 최소화됩니다.

---

# 11.3 플래그 인수 제거하기 (Remove Flag Argument)

### 1. 배경 (Motivation)

- **문제점:**
    - 함수에 플래그 인수(예: boolean, enum, 문자열)를 넘겨 로직 분기를 제어하면, 호출자가 코드의 의도를 파악하기 어렵습니다.
    - 특히 boolean 값은 호출 시 `true`나 `false`만 보이므로, 어떤 의미인지 명확하지 않습니다.
- **해결 목표:**
    - 플래그 인수를 제거하여 각 역할(예: 프리미엄 vs. 일반)이 명확하게 드러나도록 API를 개선합니다.
    - 이를 통해 코드 가독성을 높이고, 분석 도구가 API의 차이를 더 쉽게 인지할 수 있도록 합니다.

---

### 2. 절차 (Mechanics)

1. **명시적 함수 생성:**
    - 플래그 인수의 각 값을 처리하는 별도의 함수를 생성합니다.
2. **조건 분해:**
    - 함수 내부의 분기 조건이 명확하다면, Decompose Conditional 기법을 사용해 로직을 분리합니다.
3. **호출자 변경:**
    - 플래그 인수를 리터럴로 전달하는 호출부를 찾아, 해당 플래그에 맞는 명시적 함수 호출로 대체합니다.
4. **기타 고려사항:**
    - 일부 호출자가 플래그를 데이터로 전달하는 경우, 기존 인터페이스를 보존하면서 새 함수를 추가할 수 있습니다.
    - 플래그 인수를 제거한 후, 불필요한 함수는 삭제합니다.

---

### 3. 예시 (Java 코드)

### 🔻 리팩토링 전 (플래그 인수 사용)

```java
public LocalDate deliveryDate(Order anOrder, boolean isRush) {
    int deliveryTime = 0;
    if (isRush) {
        if (anOrder.getDeliveryState().equals("MA") || anOrder.getDeliveryState().equals("CT"))
            deliveryTime = 1;
        else if (anOrder.getDeliveryState().equals("NY") || anOrder.getDeliveryState().equals("NH"))
            deliveryTime = 2;
        else
            deliveryTime = 3;
        return anOrder.getPlacedOn().plusDays(1 + deliveryTime);
    } else {
        if (anOrder.getDeliveryState().equals("MA") ||
            anOrder.getDeliveryState().equals("CT") ||
            anOrder.getDeliveryState().equals("NY"))
            deliveryTime = 2;
        else if (anOrder.getDeliveryState().equals("ME") ||
                 anOrder.getDeliveryState().equals("NH"))
            deliveryTime = 3;
        else
            deliveryTime = 4;
        return anOrder.getPlacedOn().plusDays(2 + deliveryTime);
    }
}

```

### 🔺 리팩토링 후 (플래그 인수 제거)

- **별도 함수로 분리:**
    - 프리미엄(긴급) 배송과 일반 배송을 명시적 함수로 구분합니다.

```java
public LocalDate rushDeliveryDate(Order anOrder) {
    int deliveryTime;
    if (anOrder.getDeliveryState().equals("MA") || anOrder.getDeliveryState().equals("CT"))
        deliveryTime = 1;
    else if (anOrder.getDeliveryState().equals("NY") || anOrder.getDeliveryState().equals("NH"))
        deliveryTime = 2;
    else
        deliveryTime = 3;
    return anOrder.getPlacedOn().plusDays(1 + deliveryTime);
}

public LocalDate regularDeliveryDate(Order anOrder) {
    int deliveryTime;
    if (anOrder.getDeliveryState().equals("MA") ||
        anOrder.getDeliveryState().equals("CT") ||
        anOrder.getDeliveryState().equals("NY"))
        deliveryTime = 2;
    else if (anOrder.getDeliveryState().equals("ME") ||
             anOrder.getDeliveryState().equals("NH"))
        deliveryTime = 3;
    else
        deliveryTime = 4;
    return anOrder.getPlacedOn().plusDays(2 + deliveryTime);
}

```

- **호출자 변경:**
    - 기존에 `deliveryDate(anOrder, true)` 또는 `deliveryDate(anOrder, false)`를 호출하던 부분은 각각 `rushDeliveryDate(anOrder)`와 `regularDeliveryDate(anOrder)`로 변경합니다.

---

### 정리

- **가독성 향상:** 함수 호출 시 플래그 대신 의미를 담은 함수명이 사용되어 의도가 명확해집니다.
- **유지보수 용이:** 각 로직이 분리되어 수정이나 확장이 필요할 때 영향 범위가 줄어듭니다.
- **API 개선:** 도구와 코드 리뷰 시에도 각 함수의 역할이 명확하게 드러나, 분석과 사용이 용이해집니다.

---

# 11.4 객체 통째로 넘기기 (Preserve Whole Object)

### 1. 배경 (Motivation)

- **문제점:**
    - 여러 값을 객체에서 개별적으로 추출하여 함수에 전달하면, 함수가 추가 데이터를 필요로 할 때 매개변수 목록을 변경해야 합니다.
    - 중복 코드가 발생하며, 호출자가 전달할 값들을 직접 가공해야 하는 부담이 있습니다.
- **목적:**
    - 전체 객체를 전달해 함수 내부에서 필요한 값을 추출하면, 매개변수 목록이 단순해지고 향후 변경에도 유연하게 대응할 수 있습니다.
    - 데이터 중복과 불필요한 분해 로직을 제거해 코드 응집도를 높입니다.

---

### 2. 절차 (Mechanics)

1. **새 함수 생성:**
    - 임시 이름(예: `xxNEWwithinRange`)을 가진 함수를 생성하여, 새로운 인터페이스(전체 객체)를 정의합니다.
2. **매핑 작업:**
    - 새 함수의 본문에서 기존 함수 호출 시, 새 매개변수(전체 객체)에서 필요한 값을 추출해 전달합니다.
3. **호출자 변경:**
    - 각 호출부에서 개별 값을 추출하는 코드를 제거하고, 새 함수에 전체 객체를 전달하도록 수정합니다.
4. **중복 제거 및 정리:**
    - 모든 호출자를 변경한 후, 기존 함수를 인라인 처리하고 임시 접두어를 제거합니다.

---

### 3. 예시 (Java 코드)

### 🔻 리팩토링 전

**Caller 코드:**

```java
// 개별 값을 추출하여 전달
double low = room.getDaysTempRange().getLow();
double high = room.getDaysTempRange().getHigh();
if (!plan.withinRange(low, high)) {
    alerts.add("room temperature went outside range");
}

```

**HeatingPlan 클래스:**

```java
public class HeatingPlan {
    private NumberRange temperatureRange;

    public boolean withinRange(double bottom, double top) {
        return (bottom >= temperatureRange.getLow()) && (top <= temperatureRange.getHigh());
    }

    // Getter/Setter 등
}

```

---

### 🔺 리팩토링 후

**HeatingPlan 클래스 (변경 후):**

```java
public class HeatingPlan {
    private NumberRange temperatureRange;

    // 전체 객체를 받아 처리하는 새로운 withinRange 메서드
    public boolean withinRange(NumberRange aNumberRange) {
        return (aNumberRange.getLow() >= temperatureRange.getLow()) &&
               (aNumberRange.getHigh() <= temperatureRange.getHigh());
    }

    // Getter/Setter 등
}

```

**Caller 코드 (변경 후):**

```java
// 전체 객체를 전달하여 호출
if (!plan.withinRange(room.getDaysTempRange())) {
    alerts.add("room temperature went outside range");
}

```

---

### 4. 효과 및 요약

- **유지보수성 향상:** 매개변수 목록이 단순해져, 객체에 추가 데이터가 필요할 때 호출부 수정 부담이 줄어듭니다.
- **중복 제거:** 여러 호출자에서 분리하여 사용하던 개별 값 추출 로직이 제거됩니다.
- **응집도 증가:** 데이터 관련 로직이 해당 객체 내부에 집중되어 코드의 응집력과 명확성이 향상됩니다.

---

# 11.5 매개변수를 질의 함수로 바꾸기 (Replace Parameter with Query)

### 1. 배경 (Motivation)

- **중복 제거:**
    - 함수 호출 시, 호출자가 미리 계산한 값을 넘기는 것은 중복입니다. 함수가 스스로 결정할 수 있는 값을 굳이 외부에서 전달받을 필요가 없습니다.
- **단순한 API:**
    - 매개변수 목록이 짧아질수록 함수의 사용법과 의도가 명확해집니다.
- **책임 이전:**
    - 값을 결정하는 책임을 호출자에서 함수 내부로 이전하여 호출자의 부담을 줄입니다.
- **주의점:**
    - 함수 내부에서 추가 의존성이 발생하지 않도록 주의해야 합니다. (예: 전역 상태나 불필요한 외부 접근)

---

### 2. 절차 (Mechanics)

1. **값 계산 추출:**
    - 제거할 매개변수가 이미 다른 매개변수나 객체의 상태에서 쉽게 계산될 수 있다면, 해당 계산을 별도 함수(혹은 질의 메서드)로 추출합니다.
2. **함수 본문 수정:**
    - 함수 내에서 전달받은 매개변수 대신 추출한 질의 메서드를 호출하도록 변경합니다.
3. **매개변수 제거:**
    - Change Function Declaration 기법을 사용해 함수 선언에서 해당 매개변수를 제거합니다.
4. **테스트:**
    - 각 단계별로 테스트하여 기능이 정상 동작하는지 확인합니다.

---

### 3. 예시 (Java 코드)

### 🔻 리팩토링 전

```java
public class Order {
    private int quantity;
    private double itemPrice;

    public double getFinalPrice() {
        double basePrice = this.quantity * this.itemPrice;
        int discountLevel;
        if (this.quantity > 100) {
            discountLevel = 2;
        } else {
            discountLevel = 1;
        }
        return discountedPrice(basePrice, discountLevel);
    }

    public double discountedPrice(double basePrice, int discountLevel) {
        switch (discountLevel) {
            case 1: return basePrice * 0.95;
            case 2: return basePrice * 0.90;
            default: throw new IllegalStateException("Invalid discount level");
        }
    }
}

```

---

### 🔺 리팩토링 후

- **1단계:** 할인 등급 계산을 질의 함수로 추출
- **2단계:** `discountedPrice` 함수 내부에서 `this.getDiscountLevel()`을 호출하여 할인 등급을 구함
- **3단계:** 매개변수 목록에서 `discountLevel` 제거

```java
public class Order {
    private int quantity;
    private double itemPrice;

    public double getFinalPrice() {
        double basePrice = this.quantity * this.itemPrice;
        return discountedPrice(basePrice);
    }

    // 할인 등급을 결정하는 질의 메서드
    private int getDiscountLevel() {
        return (this.quantity > 100) ? 2 : 1;
    }

    // discountLevel 매개변수를 제거하고, 내부에서 getDiscountLevel() 호출
    public double discountedPrice(double basePrice) {
        int discountLevel = getDiscountLevel();
        switch (discountLevel) {
            case 1: return basePrice * 0.95;
            case 2: return basePrice * 0.90;
            default: throw new IllegalStateException("Invalid discount level");
        }
    }
}

```

---

### 4. 효과 및 요약

- **코드 중복 제거:** 할인 등급 계산 로직이 하나의 질의 메서드로 집중되어 중복이 사라집니다.
- **단순한 API:** `discountedPrice` 함수의 매개변수 목록이 단순해져 호출 시 부담이 줄어듭니다.
- **유지보수성 향상:** 할인 등급 계산 로직의 변경이 필요한 경우, 단일 질의 메서드만 수정하면 되어 확장 및 변경에 유연합니다.

---

# 11.6 질의 함수를 매개변수로 바꾸기 (Replace Query with Parameter)

### 1. 배경 (Motivation)

- **내부 의존성 제거:**

  함수 내에서 전역 변수나 모듈 내의 불필요한 요소에 직접 접근하는 대신, 해당 값을 호출자가 제공하도록 만들어 의존성을 줄입니다.

- **참조 투명성 확보:**

  동일한 매개변수 값으로 항상 같은 결과를 반환하도록 하여 테스트와 이해가 용이해집니다.

- **모듈화 및 책임 분산:**

  함수의 책임을 호출자에게 일부 이전함으로써, 함수 자체는 순수한 계산만 담당하도록 개선합니다.

- **트레이드오프:**

  호출자가 매개변수를 전달해야 하므로 인터페이스는 다소 복잡해질 수 있지만, 모듈 간 결합도가 낮아지고 코드의 재사용성과 테스트 용이성이 향상됩니다.


---

### 2. 절차 (Mechanics)

1. **쿼리 코드 추출:**
    - 함수 내부에서 직접 참조하고 있는 값을 `Extract Variable` 기법으로 분리합니다.
2. **새 함수 생성:**
    - 분리한 변수(예: `selectedTemperature`)를 인자로 받는 새 함수를 `Extract Function` 기법으로 만들어, 기존 로직을 해당 함수로 옮깁니다.
3. **변수 인라인 처리:**
    - `Inline Variable` 기법을 사용해 새 함수 호출 시 추출한 변수를 직접 전달하도록 수정합니다.
4. **기존 함수 인라인:**
    - 원래 함수가 단순히 새 함수를 호출하는 형태가 된다면 `Inline Function` 기법을 적용해 코드를 간소화합니다.
5. **함수 이름 변경:**
    - 임시로 사용한 접두어(예: `xxNEW`)를 제거하여 새 함수가 원래 함수 이름을 대신 사용하도록 합니다.
6. **호출부 수정:**
    - 호출자 측에서는 더 이상 전역(혹은 모듈 내부) 값을 직접 사용하지 않고, 매개변수를 통해 값을 전달하도록 변경합니다.

---

### 3. 예시 (Java 코드)

### 🔻 리팩토링 전

**HeatingPlan 클래스 (전역 의존성 존재):**

```java
public class HeatingPlan {
    private final double _min;
    private final double _max;

    // 전역 thermostat 객체에 직접 접근 (의존성 문제)
    public double getTargetTemperature() {
        if (Thermostat.selectedTemperature > this._max) return this._max;
        else if (Thermostat.selectedTemperature < this._min) return this._min;
        else return Thermostat.selectedTemperature;
    }
}

```

**호출자 코드:**

```java
if (thePlan.getTargetTemperature() > Thermostat.currentTemperature) {
    setToHeat();
} else if (thePlan.getTargetTemperature() < Thermostat.currentTemperature) {
    setToCool();
} else {
    setOff();
}

```

---

### 🔺 리팩토링 후

**1단계: 내부 쿼리 값 추출 후 새 함수 생성**

- 우선, `Thermostat.selectedTemperature`를 매개변수로 받아 처리하도록 새 함수를 만듭니다.

```java
public class HeatingPlan {
    private final double _min;
    private final double _max;

    // 새로 만든 메서드 (초기 이름: xxNEWtargetTemperature)
    public double xxNEWtargetTemperature(double selectedTemperature) {
        if (selectedTemperature > this._max) return this._max;
        else if (selectedTemperature < this._min) return this._min;
        else return selectedTemperature;
    }

    // 기존 메서드는 이제 단순히 새 함수 호출로 대체
    public double getTargetTemperature() {
        return xxNEWtargetTemperature(Thermostat.selectedTemperature);
    }
}

```

**2단계: 호출자 변경 및 인라인 처리**

- 호출자에서는 직접 `Thermostat.selectedTemperature`를 전달하도록 변경합니다.

```java
if (thePlan.xxNEWtargetTemperature(Thermostat.selectedTemperature) > Thermostat.currentTemperature) {
    setToHeat();
} else if (thePlan.xxNEWtargetTemperature(Thermostat.selectedTemperature) < Thermostat.currentTemperature) {
    setToCool();
} else {
    setOff();
}

```

**3단계: 이름 변경 및 인라인 함수 적용**

- 임시 이름을 제거하여 최종 인터페이스로 만듭니다.

```java
public class HeatingPlan {
    private final double _min;
    private final double _max;

    // 최종 targetTemperature 메서드: 외부에서 선택 온도를 매개변수로 제공
    public double targetTemperature(double selectedTemperature) {
        if (selectedTemperature > this._max) return this._max;
        else if (selectedTemperature < this._min) return this._min;
        else return selectedTemperature;
    }
}

```

**호출자 코드 (최종):**

```java
if (thePlan.targetTemperature(Thermostat.selectedTemperature) > Thermostat.currentTemperature) {
    setToHeat();
} else if (thePlan.targetTemperature(Thermostat.selectedTemperature) < Thermostat.currentTemperature) {
    setToCool();
} else {
    setOff();
}

```

---

### 4. 효과 및 요약

- **의존성 제거:**
    - HeatingPlan 클래스 내부에서 전역 객체(Thermostat)에 직접 의존하지 않고, 필요한 값을 호출자가 제공하게 되어 모듈 간 결합도가 낮아집니다.
- **참조 투명성 확보:**
    - 같은 입력값에 대해 항상 같은 결과를 반환하여 테스트와 이해가 용이해집니다.
- **책임 분산:**
    - 호출자가 선택 온도를 제공함으로써, HeatingPlan은 오직 온도 범위 비교만 수행합니다.
- **트레이드오프:**
    - 호출자가 매개변수를 전달해야 하므로 호출부가 다소 복잡해지지만, 전체 시스템의 모듈화와 테스트 용이성이 향상됩니다.

---

# 11.7 세터 제거하기 (Remove Setting Method)

### 1. 배경 (Motivation)

- **불변성 확보:**

  객체 생성 후 변경되어서는 안 되는 필드(예: ID)는 세터를 제공하면 값이 변경될 가능성이 있으므로, 의도와 달리 변하지 않도록 불변하게 만드는 것이 좋습니다.

- **의도 명확화:**

  생성자에서 한 번만 값을 설정하게 하고 이후에는 수정 불가능하게 함으로써, 객체의 상태가 안정적임을 명시할 수 있습니다.

- **부작용 최소화:**

  세터 메서드를 제거하면 외부에서 객체 상태를 변경할 수 없으므로, 프로그램 내에서 예기치 않은 변경으로 인한 부작용을 줄일 수 있습니다.


---

### 2. 절차 (Mechanics)

1. **생성자에 필요한 값 추가:**
    - 세터 없이 초기 값이 설정될 수 있도록, 생성자에 해당 필드를 추가합니다.
2. **생성자 내에서 세터 호출:**
    - 만약 기존에 세터를 통해 초기화되던 필드라면, 생성자 내에서 세터를 호출하여 값을 설정한 후, 외부 호출은 제거합니다.
3. **외부 호출 제거:**
    - 생성자 외부에서 해당 세터가 호출되는 부분을 모두 제거하고, 새 생성자 호출로 대체합니다.
4. **세터 메서드 인라인 및 제거:**
    - 필요없는 세터 메서드를 인라인 처리한 후, 클래스에서 제거하여 불변성을 강화합니다.
5. **테스트:**
    - 각 단계마다 테스트를 진행해 기능이 올바르게 동작하는지 확인합니다.

---

### 3. 예시 (Java 코드)

### 🔻 리팩토링 전

**Person 클래스 (세터 포함):**

```java
public class Person {
    private String name;
    private String id;

    public Person() { }

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

```

**객체 생성 코드:**

```java
Person martin = new Person();
martin.setName("martin");
martin.setId("1234");

```

---

### 🔺 리팩토링 후

**불변성을 반영한 Person 클래스 (ID는 생성자를 통해 설정, 세터 제거):**

```java
public class Person {
    private String name;
    private final String id;  // 불변 필드로 선언

    public Person(String id) {
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
}

```

**객체 생성 코드 (세터 호출 제거):**

```java
Person martin = new Person("1234");
martin.setName("martin");

```

---

### 4. 효과 및 요약

- **불변성 강화:**
    - ID와 같이 객체 생성 후 변경되면 안 되는 필드는 생성자에서 초기화되고, 이후 변경할 수 없어 객체의 상태가 안정적입니다.
- **의도 명료화:**
    - 세터 메서드를 제거함으로써, 해당 필드의 수정이 불가능하다는 의도를 명확하게 전달합니다.
- **코드 단순화:**
    - 생성자에 필요한 모든 값이 포함되므로, 외부에서 객체 생성 후 별도의 초기화가 필요하지 않습니다.

---

# 11.8 생성자를 팩터리 함수로 바꾸기 (Replace Constructor with Factory Function)

### 1. 배경 (Motivation)

- **제한 사항:**
    - 생성자는 항상 해당 클래스의 인스턴스를 반환하며, 이름이 고정되어 있어 명확한 의도를 드러내기 어렵습니다.
    - 생성자를 직접 호출하면 환경이나 매개변수에 따라 서브클래스나 프록시 객체로 대체하기 어렵습니다.
- **목표:**
    - 팩터리 함수는 내부에서 생성자를 호출하더라도 반환 객체를 자유롭게 변경할 수 있으므로, 더 유연하고 명확한 인터페이스를 제공합니다.
    - 생성자 호출 대신 팩터리 함수를 사용하면 API가 개선되고, 호출자에게 더 직관적인 이름을 제공할 수 있습니다.

---

### 2. 절차 (Mechanics)

1. **팩터리 함수 생성:**
    - 팩터리 함수의 본문은 단순히 생성자를 호출하는 코드로 작성합니다.
2. **호출부 변경:**
    - 기존 생성자 호출을 모두 팩터리 함수 호출로 대체합니다.
3. **특정 역할 팩터리:**
    - 필요 시, 더 구체적인 팩터리 함수(예: createEngineer)로 호출을 변경하여, 코드 내 리터럴 사용을 줄입니다.
4. **생성자 가시성 제한:**
    - 생성자의 접근 제어자를 private 등으로 제한해 외부에서 직접 호출하지 못하도록 합니다.
5. **테스트:**
    - 각 단계별로 기능 테스트를 진행하여, 변경 후에도 올바르게 객체가 생성되는지 확인합니다.

---

### 3. 예시 (Java 코드)

### 🔻 리팩토링 전

```java
public class Employee {
    private String name;
    private String typeCode;

    // public 생성자: 직접 호출
    public Employee(String name, String typeCode) {
        this.name = name;
        this.typeCode = typeCode;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        // 예: "E" → "Engineer"
        return legalTypeCodes.get(typeCode);
    }

    // 타입 코드 매핑
    public static final Map<String, String> legalTypeCodes = new HashMap<>();
    static {
        legalTypeCodes.put("E", "Engineer");
        legalTypeCodes.put("M", "Manager");
        legalTypeCodes.put("S", "Salesman");
    }
}

```

객체 생성 시:

```java
Employee candidate = new Employee(document.getName(), document.getEmpType());
Employee leadEngineer = new Employee(document.getLeadEngineer(), "E");

```

---

### 🔺 리팩토링 후

**Employee 클래스 – 생성자 가시성 제한 및 팩터리 함수 추가**

```java
public class Employee {
    private String name;
    private String typeCode;

    // 생성자를 private으로 제한하여 외부에서 직접 호출하지 못하게 함
    private Employee(String name, String typeCode) {
        this.name = name;
        this.typeCode = typeCode;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return legalTypeCodes.get(typeCode);
    }

    // 타입 코드 매핑
    private static final Map<String, String> legalTypeCodes = new HashMap<>();
    static {
        legalTypeCodes.put("E", "Engineer");
        legalTypeCodes.put("M", "Manager");
        legalTypeCodes.put("S", "Salesman");
    }

    // 기본 팩터리 함수
    public static Employee createEmployee(String name, String typeCode) {
        return new Employee(name, typeCode);
    }

    // 특정 역할을 위한 팩터리 함수 (예: 엔지니어)
    public static Employee createEngineer(String name) {
        return new Employee(name, "E");
    }
}

```

**호출자 코드 – 팩터리 함수로 대체**

```java
Employee candidate = Employee.createEmployee(document.getName(), document.getEmpType());
Employee leadEngineer = Employee.createEngineer(document.getLeadEngineer());

```

---

### 4. 효과 및 요약

- **유연한 객체 생성:**
    - 팩터리 함수를 사용하면 반환 객체를 상황에 맞게 조정할 수 있어, 서브클래스나 프록시 객체 반환 등 다양한 확장이 용이합니다.
- **명확한 인터페이스:**
    - 팩터리 함수의 이름을 통해 객체 생성 의도를 명확히 전달할 수 있습니다.
- **캡슐화 강화:**
    - 생성자를 private으로 제한하여 객체 생성 로직이 팩터리 함수로 집중됨으로써, 코드의 응집력과 유지보수성이 향상됩니다.

---

# 11.9 함수를 명령으로 바꾸기 (Replace Function with Command)

### 1. 배경 (Motivation)

- **함수의 한계:**
    - 단순한 함수는 기본적인 작업에는 충분하지만, 복잡한 로직이나 상태 관리, 실행 취소(undo) 등 추가 기능이 필요할 경우에는 한계가 있습니다.
- **명령 객체(Command Object)의 장점:**
    - 하나의 객체에 실행 요청과 실행 결과를 캡슐화하여, 함수의 생명주기와 내부 상태를 세밀하게 다룰 수 있습니다.
    - 인수들을 객체의 필드로 옮겨서, 복잡한 계산 과정을 개별 메서드(예: `scoreSmoking()`)로 분리할 수 있습니다.
    - 테스트와 디버깅 측면에서 각 하위 기능을 독립적으로 확인할 수 있습니다.
- **사용 시점:**
    - 복잡한 함수를 보다 명확하게 이해하고 유지보수하기 위해, 그리고 확장이 필요할 때 명령 객체로 전환합니다.
    - 단순한 경우에는 함수만 사용하는 것이 좋으나, 추가 기능이 필요할 때 명령 패턴을 적용합니다.

---

### 2. 절차 (Mechanics)

1. **빈 명령 클래스 생성:**
    - 원래 함수 이름을 따서 명령 객체 클래스를 생성하고, 실행을 위한 메서드(일반적으로 `execute()` 혹은 `call()`)를 준비합니다.
2. **함수 이동:**
    - 기존 함수의 본문을 새 클래스의 실행 메서드로 옮깁니다.
3. **포워딩 함수 유지:**
    - 기존 함수는 새 명령 객체를 생성하여 실행 메서드를 호출하는 포워딩 함수로 남겨둡니다.
4. **매개변수 → 필드 전환:**
    - 함수의 매개변수들을 명령 객체의 필드로 옮기고, 생성자를 통해 초기화합니다.
5. **내부 변수 리팩토링:**
    - 복잡한 로직에 사용되는 지역 변수들을 객체 필드로 전환하고, 필요에 따라 별도 메서드로 추출합니다.
6. **테스트:**
    - 단계별로 변경 후 기능이 정상 작동하는지 확인합니다.

---

### 3. 예시 (Java 코드)

### 🔻 리팩토링 전

```java
public class Scoring {
    // 단순 함수: candidate, medicalExam, scoringGuide를 인자로 받아 계산 수행
    public int score(Candidate candidate, MedicalExam medicalExam, ScoringGuide scoringGuide) {
        int result = 0;
        int healthLevel = 0;
        boolean highMedicalRiskFlag = false;

        if (medicalExam.isSmoker()) {
            healthLevel += 10;
            highMedicalRiskFlag = true;
        }

        String certificationGrade = "regular";
        if (scoringGuide.stateWithLowCertification(candidate.getOriginState())) {
            certificationGrade = "low";
            result -= 5;
        }

        // ... 추가 로직 ...
        result -= Math.max(healthLevel - 5, 0);
        return result;
    }
}

```

---

### 🔺 리팩토링 후

**1. 명령 객체(Scorer) 생성 및 매개변수 필드 전환**

```java
public class Scorer {
    private Candidate candidate;
    private MedicalExam medicalExam;
    private ScoringGuide scoringGuide;

    // 내부 상태로 관리할 변수들
    private int result;
    private int healthLevel;
    private boolean highMedicalRiskFlag;
    private String certificationGrade;

    public Scorer(Candidate candidate, MedicalExam medicalExam, ScoringGuide scoringGuide) {
        this.candidate = candidate;
        this.medicalExam = medicalExam;
        this.scoringGuide = scoringGuide;
    }

    // execute() 메서드: 명령 객체의 핵심 실행 로직
    public int execute() {
        this.result = 0;
        this.healthLevel = 0;
        this.highMedicalRiskFlag = false;

        scoreSmoking();

        this.certificationGrade = "regular";
        if (scoringGuide.stateWithLowCertification(candidate.getOriginState())) {
            this.certificationGrade = "low";
            this.result -= 5;
        }

        // ... 추가 로직 (필요한 경우 하위 메서드로 분리) ...
        this.result -= Math.max(this.healthLevel - 5, 0);
        return this.result;
    }

    private void scoreSmoking() {
        if (medicalExam.isSmoker()) {
            this.healthLevel += 10;
            this.highMedicalRiskFlag = true;
        }
    }

    // 추가적인 하위 기능들을 메서드로 분리할 수 있음
}

```

**2. 기존 함수에서 명령 객체로 포워딩**

```java
public class Scoring {
    // 기존 score() 함수는 명령 객체(Scorer)를 생성하여 실행을 위임
    public int score(Candidate candidate, MedicalExam medicalExam, ScoringGuide scoringGuide) {
        return new Scorer(candidate, medicalExam, scoringGuide).execute();
    }
}

```

---

### 4. 효과 및 요약

- **모듈화 및 확장성:**
    - 복잡한 함수를 명령 객체로 캡슐화하여, 하위 기능을 개별 메서드로 분리하고 상태를 객체 필드로 관리함으로써 유지보수와 확장이 용이해집니다.
- **테스트 용이성:**
    - 명령 객체 내의 각 기능을 독립적으로 테스트할 수 있으며, 동일 입력에 대해 항상 같은 결과(참조 투명성)를 보장할 수 있습니다.
- **유연성:**
    - 추가적인 기능(예: undo, 파라미터 설정 라이프사이클, 커스터마이징 등)을 쉽게 확장할 수 있습니다.
- **단점:**
    - 호출자가 매개변수를 명령 객체 생성 시 직접 제공해야 하므로 인터페이스가 다소 복잡해질 수 있으나, 복잡한 로직 관리에는 그만한 가치가 있습니다.

---

# 11.10 명령을 함수로 바꾸기 (Replace Command with Function)

### 1. 배경 (Motivation)

- **명령 객체의 장점:**

  복잡한 계산이나 상태 관리, 실행 취소(undo) 등 추가 기능을 지원할 수 있어, 복잡한 연산을 캡슐화하기에 유용합니다.

- **단순한 경우에는 부담:**

  하지만 함수가 그리 복잡하지 않다면 명령 객체를 사용함으로써 생기는 상태 관리, 여러 메서드, 별도의 생성자 호출 등이 오히려 불필요한 복잡성을 추가할 뿐입니다.

- **목표:**

  단순한 로직에는 명령 객체 대신 일반 함수로 전환하여 인터페이스를 단순화하고, 유지보수와 테스트를 용이하게 만드는 것입니다.


---

### 2. 절차 (Mechanics)

1. **명령 객체 생성 및 실행 메서드 호출 추출:**
    - 기존의 command 객체 생성과 실행(`execute()` 혹은 `getCharge()` 등) 부분을 감싸는 함수를 추출합니다.
2. **보조 메서드 인라인 처리:**
    - command 객체 내의 보조 기능(예: `getBaseCharge()`)을 인라인하여, 실행 로직을 하나의 함수로 통합합니다.
3. **매개변수 이동:**
    - 생성자에서 전달받던 매개변수들을 실행 메서드의 파라미터로 옮기고, 내부 필드 대신 이를 직접 사용하도록 수정합니다.
4. **호출부 인라인:**
    - 이제 호출자는 더 이상 command 객체를 생성할 필요 없이, 바로 새로 생성된 함수만 호출하면 됩니다.
5. **불필요한 코드 제거:**
    - 최종적으로 command 클래스는 더 이상 사용되지 않으므로 제거합니다.

---

### 3. 예시 (Java 코드)

### 🔻 리팩토링 전 (Command 객체 사용)

**명령 객체 (ChargeCalculator):**

```java
public class ChargeCalculator {
    private Customer customer;
    private double usage;
    private Provider provider;

    public ChargeCalculator(Customer customer, double usage, Provider provider) {
        this.customer = customer;
        this.usage = usage;
        this.provider = provider;
    }

    public double getBaseCharge() {
        return customer.getBaseRate() * usage;
    }

    public double getCharge() {
        double baseCharge = getBaseCharge();
        return baseCharge + provider.getConnectionCharge();
    }
}

```

**호출자 코드:**

```java
double monthCharge = new ChargeCalculator(customer, usage, provider).getCharge();

```

---

### 🔺 리팩토링 후 (일반 함수 사용)

**최종 함수로 전환:**

```java
public double charge(Customer customer, double usage, Provider provider) {
    double baseCharge = customer.getBaseRate() * usage;
    return baseCharge + provider.getConnectionCharge();
}

```

**호출자 코드:**

```java
double monthCharge = charge(customer, usage, provider);

```

---

### 4. 효과 및 요약

- **단순화:**
    - 복잡한 명령 객체를 일반 함수로 전환하여, 불필요한 클래스와 상태 관리 코드를 제거함으로써 코드를 단순하게 만듭니다.
- **유지보수성 및 테스트:**
    - 순수 함수는 입력에 대해 항상 동일한 출력을 제공하므로 테스트와 디버깅이 용이합니다.
- **유연성의 희생:**
    - 명령 객체가 제공하는 확장성(예: undo 기능, 파라미터 설정 단계 관리 등)은 포기하지만, 단순한 계산에서는 그만큼의 복잡성을 감수할 필요가 없으므로 전체 코드의 가독성과 유지보수성이 향상됩니다.

---

이와 같이, 함수의 복잡성이 그리 높지 않은 경우 명령 객체 대신 일반 함수로 전환하면, 불필요한 복잡성을 제거하고 보다 명확하고 단순한 인터페이스를 제공할 수 있습니다.