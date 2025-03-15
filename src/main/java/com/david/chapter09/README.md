# 데이터 조직화

# **9.1 Split Variable (변수 분리) 정리**

---

## **1. 배경 (Motivation)**

변수는 다양한 용도로 사용되지만, 때때로 하나의 변수가 여러 역할을 수행하게 된다. 특히 다음과 같은 경우 변수는 여러 번 할당될 수 있다.

- **루프 변수**: 반복 실행 시 값이 변하는 변수 (예: `for` 루프에서 `i`).
- **수집 변수 (Collecting Variable)**: 값이 점진적으로 누적되는 변수 (예: 합산, 문자열 연결, 컬렉션 추가).
- **중간 결과 저장 변수**: 복잡한 연산 결과를 저장하여 이후 참조하기 위한 변수.

이 중 **"중간 결과 저장 변수"**는 가능하면 **한 번만 설정되어야** 한다. 동일한 변수를 여러 번 할당하는 것은 여러 책임을 가지는 것이므로, 이를 분리하여 각각의 역할을 명확하게 해야 한다.

특히 하나의 변수를 여러 용도로 사용하면 코드 가독성이 떨어지고 유지보수가 어려워진다.

---

## **2. 절차 (Mechanics)**

1. **변수의 첫 번째 선언 및 할당에서 이름 변경**
    - 기존 변수를 새로운 이름으로 변경한다.
    - 가능하면 `const`(불변 변수)로 선언하여 한 번만 할당되도록 한다.
2. **수집 변수인지 확인**
    - 만약 변수가 `i = i + something`과 같은 방식으로 사용된다면, 이는 수집 변수이므로 분리하지 않는다.
3. **변수의 두 번째 할당 이전까지 모든 참조 변경**
    - 두 번째 할당이 등장하기 전까지 새 변수명을 적용한다.
4. **테스트 실행**
    - 변경 후 정상 동작하는지 확인한다.
5. **두 번째 할당 이후의 변수에 대해 동일한 절차 반복**
    - 새 변수 선언, 기존 변수 제거, 새로운 변수명 적용을 반복한다.

---

## **3. 예제 (Example)**

### **🚀 예제 1: 비행 거리 계산**

### **🔹 기존 코드**

```java
public class DistanceCalculator {
    public static double distanceTravelled(Scenario scenario, double time) {
        double result;
        double acc = scenario.getPrimaryForce() / scenario.getMass();
        double primaryTime = Math.min(time, scenario.getDelay());
        result = 0.5 * acc * primaryTime * primaryTime;
        double secondaryTime = time - scenario.getDelay();
        if (secondaryTime > 0) {
            double primaryVelocity = acc * scenario.getDelay();
            acc = (scenario.getPrimaryForce() + scenario.getSecondaryForce()) / scenario.getMass();
            result += primaryVelocity * secondaryTime + 0.5 * acc * secondaryTime * secondaryTime;
        }
        return result;
    }
}
class Scenario {
    private double primaryForce;
    private double secondaryForce;
    private double mass;
    private double delay;

    public double getPrimaryForce() { return primaryForce; }
    public double getSecondaryForce() { return secondaryForce; }
    public double getMass() { return mass; }
    public double getDelay() { return delay; }
}

```

**🔍 문제점:**

- `acc` 변수가 **초기 가속도**와 **보조 가속도**라는 두 가지 역할을 수행하고 있음.
- 변수의 의미가 명확하지 않음.

---

### **🔹 리팩터링 후**

```java
public double distanceTravelled(Scenario scenario, double time) {
    double result;
    final double primaryAcceleration = scenario.getPrimaryForce() / scenario.getMass();
    double primaryTime = Math.min(time, scenario.getDelay());
    result = 0.5 * primaryAcceleration * primaryTime * primaryTime;
    double secondaryTime = time - scenario.getDelay();
    if (secondaryTime > 0) {
        double primaryVelocity = primaryAcceleration * scenario.getDelay();
        final double secondaryAcceleration = (scenario.getPrimaryForce() + scenario.getSecondaryForce()) / scenario.getMass();
        result += primaryVelocity * secondaryTime + 0.5 * secondaryAcceleration * secondaryTime * secondaryTime;
    }
    return result;
}

```

✅ **변경 사항:**

- `acc` 변수를 **primaryAcceleration**(초기 가속도)와 **secondaryAcceleration**(보조 가속도)로 분리.
- 불필요한 변수 재할당을 제거하고, `final`을 사용해 불변성을 유지.

---

### **🚀 예제 2: 입력 파라미터 변경 방지**

### **🔹 기존 코드**

```java
public class DiscountCalculator {
    public static int discount(int inputValue, int quantity) {
        if (inputValue > 50) inputValue -= 2;
        if (quantity > 100) inputValue -= 1;
        return inputValue;
    }
}

```

**🔍 문제점:**

- `inputValue`가 함수의 입력값이지만 내부에서 변경됨.
- 원본 값을 유지하는 것이 좋음.

---

### **🔹 리팩터링 후**

```java
public int discount(int inputValue, int quantity) {
    int result = inputValue;
    if (inputValue > 50) result -= 2;
    if (quantity > 100) result -= 1;
    return result;
}

```

✅ **변경 사항:**

- `inputValue`를 직접 변경하는 대신, `result` 변수에 복사하여 사용.
- 가독성이 향상되었으며, 원본 값이 유지됨.

---

## **🎯 정리**

### **📌 배경 (Motivation)**

- 하나의 변수가 여러 역할을 수행하면 코드가 복잡해지고 유지보수가 어려워진다.
- 동일한 변수를 여러 번 할당하면 의도를 파악하기 어려워진다.
- 변수를 분리하여 역할을 명확하게 해야 한다.

### **📌 절차 (Mechanics)**

1. 변수명을 명확하게 변경하고, 가능하면 `const` 또는 `final`을 사용하여 불변성 유지.
2. 루프 변수 또는 수집 변수(`+=`, `.add()`)가 아니라면 분리.
3. 두 번째 할당 전까지 모든 참조 변경.
4. 변경 후 테스트 수행.
5. 동일한 절차를 반복하여 각 역할을 분리.

### **📌 예제 (Example)**

### **1️⃣ 비행 거리 계산**

- `acc` 변수를 `primaryAcceleration`과 `secondaryAcceleration`으로 분리.
- 불변성을 유지하여 코드 안정성 향상.

### **2️⃣ 입력 파라미터 변경 방지**

- 함수의 입력값을 직접 변경하는 대신 `result` 변수에 복사하여 사용.
- 가독성과 유지보수성을 개선.

---

💡 **변수 분리는 가독성을 높이고 유지보수를 쉽게 만드는 중요한 리팩터링 기법이다. 변수가 여러 역할을 수행하는지 항상 주의 깊게 살펴보자!**

# **9.2 Rename Field (필드명 변경)**

---

## **1. 배경 (Motivation)**

이름은 코드 이해에 중요한 요소이며, 특히 데이터 구조에서 필드 이름이 명확해야 한다. 필드 이름이 잘못되면 데이터를 다루는 코드의 가독성과 유지보수성이 떨어진다.

- 데이터 구조는 프로그램 전반에 걸쳐 사용되므로, **명확한 필드명**이 필수적이다.
- **명확한 변수명**은 코드의 의미를 쉽게 파악하도록 도와준다.
- 소프트웨어를 개발하면서 데이터에 대한 이해가 깊어질수록, **필드명도 개선해야** 한다.
- 클래스의 필드뿐만 아니라, **getter/setter 메서드도 이름을 변경**해야 한다.

---

## **2. 절차 (Mechanics)**

1. **필드가 지역적인 경우**, 모든 참조를 변경하고 테스트한다.
2. **레코드가 캡슐화되지 않은 경우**, `Encapsulate Record (162)`를 먼저 적용한다.
3. **클래스 내부에서 필드명을 변경**하고, getter/setter 메서드를 수정한다.
4. **테스트를 실행**하여 정상 동작하는지 확인한다.
5. **생성자에서 기존 필드명을 새로운 필드명으로 매핑**한다.
6. **모든 생성자 호출부를 새로운 필드명으로 변경**한다.
7. **이전 필드 지원을 제거**하여 완전히 새로운 필드명으로 변경한다.
8. **getter/setter 메서드명을 변경**하여 최종 마무리한다.

---

## **3. 예제 (Example)**

### **🚀 리팩토링 전**

다음은 `Organization` 클래스를 사용하여 조직 데이터를 관리하는 코드이다.

```java
public class Organization {
    private String name;
    private String country;

    public Organization(String name, String country) {
        this.name = name;
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}

// 사용 예제
Organization organization = new Organization("Acme Gooseberries", "GB");

```

**🔍 문제점**

- `name`이라는 필드명이 **조직의 "이름"인지 "직함(title)"인지 모호**하다.
- 프로젝트 전반에서 `name`을 `title`로 변경하고 싶다면, 직접 필드명을 바꾸는 것은 **코드 변경량이 많아 위험**할 수 있다.
- 이를 해결하려면 **Encapsulate Record 패턴을 적용**한 후 필드명을 점진적으로 변경해야 한다.

---

### **🔹 리팩토링 1단계: 필드 캡슐화 및 중간 변수 추가**

- 기존 `name`을 `_title`로 변경하고, getter/setter에서는 기존 `name`을 유지한다.

```java
public class Organization {
    private String _title;  // 기존 name을 변경
    private String country;

    public Organization(String name, String country) {
        this._title = name;  // 기존 name을 유지
        this.country = country;
    }

    public String getName() {  // 기존 name을 유지
        return _title;
    }

    public void setName(String name) {  // 기존 name을 유지
        this._title = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}

```

✅ **이 단계에서는 기존 코드와 완전히 동일하게 동작하지만, 내부적으로 `name` 대신 `_title`을 사용하도록 변경했다.**

---

### **🔹 리팩토링 2단계: 생성자에서 새로운 필드 지원 추가**

- 기존 `name`을 `title`로 대체하고, `title`이 없으면 `name`을 사용한다.

```java
public class Organization {
    private String _title;
    private String country;

    public Organization(String name, String title, String country) {
        this._title = (title != null) ? title : name; // name과 title을 분리
        this.country = country;
    }

    public String getName() {  // 기존 name 유지
        return _title;
    }

    public void setName(String name) {  // 기존 name 유지
        this._title = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}

```

✅ **이제 생성자에서 `title`을 사용할 수도 있고, 기존 `name`도 지원된다.**

---

### **🔹 리팩토링 3단계: 모든 호출부를 새로운 필드로 변경**

이제 `name`이 아니라 `title`을 사용하도록 코드를 수정한다.

```java
Organization organization = new Organization(null, "Acme Gooseberries", "GB");

```

✅ **이제 `title`을 사용하는 코드로 변경 가능하며, 기존 `name` 사용은 제거 가능하다.**

---

### **🔹 리팩토링 4단계: 기존 필드 제거 및 메서드명 변경**

이제 완전히 `title`로 변경하고, `name` 관련 메서드를 `title`로 바꾼다.

```java
public class Organization {
    private String title;
    private String country;

    public Organization(String title, String country) {
        this.title = title;
        this.country = country;
    }

    public String getTitle() {  // 기존 getName() -> getTitle()로 변경
        return title;
    }

    public void setTitle(String title) {  // 기존 setName() -> setTitle()로 변경
        this.title = title;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}

```

✅ **최종적으로 `title`을 사용하도록 변경되었으며, 더 이상 `name`이 존재하지 않는다.**

---

## **🎯 정리**

### **📌 배경 (Motivation)**

- **데이터 구조의 필드명은 코드 이해도를 크게 좌우**하므로, 적절한 이름을 선택해야 한다.
- **클래스 내부에서 필드를 직접 사용하는 것이 아니라, 캡슐화를 통해 변경 가능성을 낮춰야 한다.**
- **getter/setter 메서드도 함께 변경해야 한다.**

### **📌 절차 (Mechanics)**

1. **레코드가 캡슐화되지 않았다면 Encapsulate Record를 적용**한다.
2. **새로운 필드명을 내부 변수로 사용하고, 기존 필드명과 공존**하도록 수정한다.
3. **생성자에서 새로운 필드를 지원하고, 기존 필드 사용자를 점진적으로 변경**한다.
4. **모든 호출부에서 새로운 필드를 사용하도록 수정**한다.
5. **기존 필드 지원을 제거하고, getter/setter 메서드명을 변경**한다.

### **📌 예제 (Example)**

### **1️⃣ 리팩토링 전**

- `name` 필드가 `title`을 의미하지만, 코드에서 명확하지 않음.
- 데이터 구조 변경 시 **일괄 수정이 어려움**.

### **2️⃣ 리팩토링 후**

- **캡슐화를 적용**하여 데이터 구조를 보호.
- **생성자에서 새로운 필드명을 지원**하고, 기존 필드를 제거.
- **getter/setter를 새로운 필드명으로 변경**하여 코드 일관성 유지.

---

💡 **데이터 구조를 명확하게 만들기 위해 필드명을 변경하는 것은 중요하다. 캡슐화를 활용하면 더욱 안전하게 리팩토링할 수 있다!**

# **9.3 Replace Derived Variable with Query (파생 변수를 계산으로 대체)**

---

## **1. 배경 (Motivation)**

소프트웨어에서 **가장 큰 문제 중 하나는 가변 데이터(Mutable Data)** 이다.

- **가변 데이터가 많으면 코드가 서로 강하게 결합(Coupling)** 되어 유지보수가 어려워진다.
- 변수 값을 **수정하지 않고 계산할 수 있다면, 변수 업데이트 과정에서 생기는 버그를 방지**할 수 있다.
- 특히, **계산된 값은 원본 데이터가 변경될 때 자동으로 반영**되므로, 이를 적극 활용해야 한다.
- 다만, **원본 데이터가 불변(Immutable)인 경우**라면, 파생 변수를 유지하는 것도 괜찮다.

✅ **가능하면 파생 변수(derived variable)를 제거하고, 필요할 때마다 계산해서 사용하는 것이 좋다.**

---

## **2. 절차 (Mechanics)**

1. **변수가 갱신되는 모든 위치를 확인한다.**
    - 필요하면 **Split Variable (240)** 을 적용하여 여러 개의 업데이트를 분리한다.
2. **변수 값을 계산하는 함수를 만든다.**
3. **Introduce Assertion (302)** 을 적용하여, 기존 변수 값과 계산된 값이 동일한지 확인한다.
4. **Encapsulate Variable (132)** 을 적용하여 변수를 캡슐화한다.
5. **모든 변수를 참조하는 코드에서 새로운 계산 함수로 대체한다.**
6. **테스트를 수행하여 변경된 코드가 올바르게 동작하는지 확인한다.**
7. **Remove Dead Code (237)** 를 적용하여 불필요한 변수를 제거한다.

---

## **3. 예제 (Example)**

### **🚀 리팩토링 전 (Java 코드)**

```java
import java.util.ArrayList;
import java.util.List;

class Adjustment {
    int amount;

    public Adjustment(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }
}

class ProductionPlan {
    private int production;
    private List<Adjustment> adjustments = new ArrayList<>();

    public ProductionPlan() {
        this.production = 0;
    }

    public int getProduction() {
        return production;
    }

    public void applyAdjustment(Adjustment adjustment) {
        adjustments.add(adjustment);
        production += adjustment.getAmount();
    }
}

```

**🔍 문제점**

- `production` 값이 **조정값(adjustments)들을 합산하는 변수로 사용**된다.
- `production` 값이 **수동으로 갱신**되기 때문에 **데이터 정합성이 깨질 가능성**이 있다.
- `production` 값은 `adjustments` 값에서 **언제든 계산할 수 있으므로** 굳이 변수로 유지할 필요가 없다.

---

### **🔹 리팩토링 1단계: 검증용 Assertion 추가**

먼저 `production` 변수 값과 직접 계산한 값이 같은지 검증하는 코드를 추가한다.

```java
import java.util.ArrayList;
import java.util.List;

class ProductionPlan {
    private List<Adjustment> adjustments = new ArrayList<>();

    public int getProduction() {
        assert calculateProduction() == getProduction();
        return calculateProduction();
    }

    private int calculateProduction() {
        return adjustments.stream().mapToInt(Adjustment::getAmount).sum();
    }

    public void applyAdjustment(Adjustment adjustment) {
        adjustments.add(adjustment);
    }
}

```

✅ `calculateProduction()`을 추가하여 `production`을 직접 계산하도록 했다.

---

### **🔹 리팩토링 2단계: 기존 변수 제거**

검증이 통과하면 기존 `production` 변수를 제거하고, `calculateProduction()`을 직접 사용하도록 변경한다.

```java
import java.util.ArrayList;
import java.util.List;

class ProductionPlan {
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

```

✅ 이제 `production` 변수를 더 이상 사용하지 않고, 필요할 때마다 `calculateProduction()`을 호출해서 계산한다.

---

### **🚀 더 복잡한 예제: 초기 생산량 포함**

### **🔹 리팩토링 전 (Java 코드)**

이전 예제에서는 초기 `production` 값이 0이었지만, 초기값을 설정할 수도 있다.

```java
import java.util.ArrayList;
import java.util.List;

class ProductionPlan {
    private int production;
    private List<Adjustment> adjustments = new ArrayList<>();

    public ProductionPlan(int initialProduction) {
        this.production = initialProduction;
    }

    public int getProduction() {
        return production;
    }

    public void applyAdjustment(Adjustment adjustment) {
        adjustments.add(adjustment);
        production += adjustment.getAmount();
    }
}

```

**🔍 문제점**

- `production` 값이 `initialProduction`과 `adjustments` 값을 조합하여 생성된다.
- `applyAdjustment()`에서 `production` 값을 직접 변경한다.

---

### **🔹 리팩토링 후 (Java 코드)**

초기 생산량(`initialProduction`)을 따로 저장하고, 계산을 통해 `production` 값을 반환하도록 수정한다.

```java
import java.util.ArrayList;
import java.util.List;

class ProductionPlan {
    private int initialProduction;
    private List<Adjustment> adjustments = new ArrayList<>();

    public ProductionPlan(int initialProduction) {
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

```

✅ **변경된 점**

- `production` 변수를 **완전히 제거**하고, 필요할 때 `getProduction()`을 통해 계산한다.
- `applyAdjustment()`에서 **생산량을 직접 변경하지 않고, adjustments 리스트만 관리**한다.

---

## **🎯 정리**

### **📌 배경 (Motivation)**

- **가변 데이터를 최소화해야 한다.**
- **파생 변수(derived variable)는 직접 계산할 수 있다면 제거하는 것이 좋다.**
- **계산된 값은 항상 최신 데이터를 반영하므로, 값 동기화 문제를 방지할 수 있다.**
- **데이터 구조가 불변(Immutable)인 경우 예외적으로 유지 가능하다.**

### **📌 절차 (Mechanics)**

1. **변수가 갱신되는 모든 위치를 찾는다.**
2. **계산을 수행하는 함수(`calculateProduction()`)를 만든다.**
3. **Introduce Assertion (302)** 을 적용하여 기존 값과 비교한다.
4. **모든 `getProduction()` 호출을 `calculateProduction()`으로 변경한다.**
5. **테스트를 수행하여 문제가 없는지 확인한다.**
6. **기존 변수를 제거한다.**

### **📌 예제 (Example)**

### **1️⃣ 기본적인 예제**

- `production` 변수를 **계산된 값으로 대체**하여 불필요한 업데이트 제거.

### **2️⃣ 초기 생산량 포함 예제**

- 초기값(`initialProduction`)과 조정값(`adjustments`)을 조합하여 계산하도록 개선.

---

💡 **가변 변수를 없애면 코드가 더 단순하고 유지보수하기 쉬워진다. 가능하면 변수를 직접 수정하지 않고, 계산된 값을 활용하는 것이 좋다!**

# **9.4 Change Reference to Value (참조를 값으로 변경)**

---

## **1. 배경 (Motivation)**

객체를 다른 객체 내부에 포함할 때, **참조(reference)로 다룰지 값(value)으로 다룰지 결정**해야 한다.

- **참조로 다룰 경우**: 내부 객체의 속성을 직접 변경 가능하며, 여러 객체가 공유할 수 있다.
- **값으로 다룰 경우**: 속성을 변경할 때마다 **새로운 객체를 생성**하여 대체해야 한다.

✅ **값 객체(Value Object)로 만들면 불변성을 유지할 수 있어, 코드의 안정성이 증가하고, 멀티스레드 및 분산 시스템에서도 유리하다.**

✅ **반면, 여러 객체가 동일한 데이터를 공유해야 하는 경우에는 참조로 유지하는 것이 적절하다.**

---

## **2. 절차 (Mechanics)**

1. **객체가 불변(immutable)인지 확인하거나 불변으로 변경할 수 있는지 검토한다.**
2. **Remove Setting Method (331)** 을 적용하여 setter 메서드를 제거한다.
3. **생성자에서 모든 속성을 초기화하도록 변경한다.**
4. **값을 변경할 때 기존 객체를 수정하는 것이 아니라, 새 객체를 생성하여 교체하도록 수정한다.**
5. **equals() 및 hashCode() 메서드를 오버라이드하여 값 기반 비교를 구현한다.**
6. **테스트를 수행하여 값 객체로 동작하는지 확인한다.**

---

## **3. 예제 (Example)**

### **🚀 리팩토링 전**

아래 `Person` 클래스는 `TelephoneNumber`를 참조 타입으로 포함하고 있으며, setter를 통해 전화번호의 속성을 변경할 수 있다.

```java
class TelephoneNumber {
    private String areaCode;
    private String number;

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}

class Person {
    private TelephoneNumber telephoneNumber = new TelephoneNumber();

    public String getOfficeAreaCode() {
        return telephoneNumber.getAreaCode();
    }

    public void setOfficeAreaCode(String areaCode) {
        telephoneNumber.setAreaCode(areaCode);
    }

    public String getOfficeNumber() {
        return telephoneNumber.getNumber();
    }

    public void setOfficeNumber(String number) {
        telephoneNumber.setNumber(number);
    }
}

```

**🔍 문제점**

- `TelephoneNumber`가 **mutable(가변적)** 하므로, `Person` 객체가 알지 못하는 상태에서 전화번호가 변경될 수 있다.
- `setAreaCode()` 및 `setNumber()` 를 통해 `TelephoneNumber` 객체의 상태를 직접 변경할 수 있다.
- `TelephoneNumber`가 값 객체로 사용될 경우, setter를 제거하고 **값을 변경할 때 새로운 객체를 생성**하는 것이 바람직하다.

---

### **🔹 리팩토링 1단계: 값 객체를 불변(Immutable)으로 변경**

- **setter를 제거하고**, 모든 필드를 **생성자로 초기화**하도록 변경.

```java
class TelephoneNumber {
    private final String areaCode;
    private final String number;

    public TelephoneNumber(String areaCode, String number) {
        this.areaCode = areaCode;
        this.number = number;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public String getNumber() {
        return number;
    }
}

```

✅ 이제 `TelephoneNumber`는 불변(Immutable) 객체가 되었으며, 값을 변경할 수 없다.

---

### **🔹 리팩토링 2단계: Person 클래스에서 값 변경 시 새 객체를 생성**

- **값을 변경할 때 기존 객체를 수정하는 것이 아니라, 새로운 `TelephoneNumber` 객체를 생성하여 교체**한다.

```java
class Person {
    private TelephoneNumber telephoneNumber;

    public Person(String areaCode, String number) {
        this.telephoneNumber = new TelephoneNumber(areaCode, number);
    }

    public String getOfficeAreaCode() {
        return telephoneNumber.getAreaCode();
    }

    public void setOfficeAreaCode(String areaCode) {
        this.telephoneNumber = new TelephoneNumber(areaCode, telephoneNumber.getNumber());
    }

    public String getOfficeNumber() {
        return telephoneNumber.getNumber();
    }

    public void setOfficeNumber(String number) {
        this.telephoneNumber = new TelephoneNumber(telephoneNumber.getAreaCode(), number);
    }
}

```

✅ 이제 `setOfficeAreaCode()` 또는 `setOfficeNumber()`를 호출할 때마다 **새로운 `TelephoneNumber` 객체를 생성하여 교체**하도록 변경했다.

✅ `telephoneNumber` 객체는 **불변(Immutable)** 이므로, 다른 객체에서 예기치 않게 변경될 위험이 없다.

---

### **🔹 리팩토링 3단계: equals() 및 hashCode() 오버라이드**

값 객체는 **동일성(Identity) 비교가 아닌 값(Value) 비교를 수행**해야 하므로, `equals()` 및 `hashCode()`를 오버라이드한다.

```java
import java.util.Objects;

class TelephoneNumber {
    private final String areaCode;
    private final String number;

    public TelephoneNumber(String areaCode, String number) {
        this.areaCode = areaCode;
        this.number = number;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public String getNumber() {
        return number;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TelephoneNumber that = (TelephoneNumber) obj;
        return Objects.equals(areaCode, that.areaCode) && Objects.equals(number, that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(areaCode, number);
    }
}

```

✅ **이제 같은 `areaCode`와 `number` 값을 가지는 객체는 `equals()` 메서드를 통해 동일한 것으로 간주된다.**

---

### **🚀 최종 리팩토링 결과 (값 객체 적용)**

```java
import java.util.Objects;

class TelephoneNumber {
    private final String areaCode;
    private final String number;

    public TelephoneNumber(String areaCode, String number) {
        this.areaCode = areaCode;
        this.number = number;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public String getNumber() {
        return number;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TelephoneNumber that = (TelephoneNumber) obj;
        return Objects.equals(areaCode, that.areaCode) && Objects.equals(number, that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(areaCode, number);
    }
}

class Person {
    private TelephoneNumber telephoneNumber;

    public Person(String areaCode, String number) {
        this.telephoneNumber = new TelephoneNumber(areaCode, number);
    }

    public String getOfficeAreaCode() {
        return telephoneNumber.getAreaCode();
    }

    public void setOfficeAreaCode(String areaCode) {
        this.telephoneNumber = new TelephoneNumber(areaCode, telephoneNumber.getNumber());
    }

    public String getOfficeNumber() {
        return telephoneNumber.getNumber();
    }

    public void setOfficeNumber(String number) {
        this.telephoneNumber = new TelephoneNumber(telephoneNumber.getAreaCode(), number);
    }
}

```

---

## **🎯 정리**

### **📌 배경 (Motivation)**

- **참조가 아니라 값으로 다루면 불변성을 유지할 수 있어, 예기치 않은 변경을 방지할 수 있다.**
- **값 객체는 비교가 간단하며, 멀티스레드 환경에서도 안전하다.**
- **참조를 사용해야 하는 경우는 여러 객체가 공유할 데이터를 관리해야 할 때이다.**

### **📌 절차 (Mechanics)**

1. **값 객체를 불변(Immutable)으로 만든다.**
2. **setter를 제거하고, 생성자로 모든 속성을 초기화하도록 변경한다.**
3. **값을 변경할 때 기존 객체를 수정하지 않고, 새 객체를 생성하여 교체한다.**
4. **equals() 및 hashCode()를 오버라이드하여 값 비교를 가능하게 한다.**
5. **테스트를 수행하여 정상 동작을 확인한다.**

✅ **값 객체를 사용하면 데이터 변경이 예측 가능해지고, 코드의 안정성이 향상된다!**

# **9.5 Change Value to Reference (값을 참조로 변경)**

---

## **1. 배경 (Motivation)**

데이터 구조에서 **동일한 논리적 데이터**(예: 동일한 고객 ID)가 여러 개의 객체에 복사되어 존재할 수 있다.

- **값(Value)로 다룰 경우:**
    - 각 주문 객체가 개별적으로 고객 정보를 저장한다.
    - 동일한 고객 ID라도 별개의 객체로 존재할 수 있다.
    - 데이터를 갱신할 때, 모든 복사본을 찾아 일괄 수정해야 한다.
    - 이를 놓치면 **데이터 불일치 문제**가 발생할 수 있다.
- **참조(Reference)로 다룰 경우:**
    - 하나의 고객 객체만 존재하며, 모든 주문이 이 객체를 참조한다.
    - 데이터를 수정하면 모든 관련 객체에서 동일한 값을 참조하게 된다.
    - 메모리를 절약할 수 있으며, 데이터 정합성이 유지된다.

✅ **데이터 일관성을 유지해야 하는 경우, 값 복사 방식 대신 참조를 사용하는 것이 좋다.**

✅ **이때, 참조 객체를 저장하고 관리하는 "저장소(Repository)"를 활용하는 것이 일반적이다.**

---

## **2. 절차 (Mechanics)**

1. **저장소(Repository) 객체를 생성**하여 참조 객체를 관리한다.
2. **생성자에서 기존 값 객체를 저장소에서 가져오도록 변경**한다.
3. **기존 생성자는 값을 직접 할당하는 방식에서, 저장소를 통해 참조 객체를 가져오도록 수정**한다.
4. **테스트를 실행하여 변경이 올바르게 작동하는지 확인한다.**
5. **글로벌 저장소 의존성을 줄이고, 필요한 경우 저장소를 인자로 전달하도록 수정한다.**

---

## **3. 예제 (Example)**

### **🚀 리팩토링 전**

`Order` 클래스가 고객 정보를 값으로 저장하는 방식이다.

```java
class Customer {
    private String id;

    public Customer(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}

class Order {
    private String number;
    private Customer customer;

    public Order(String number, String customerId) {
        this.number = number;
        this.customer = new Customer(customerId); // 각 주문마다 새로운 Customer 객체 생성
    }

    public Customer getCustomer() {
        return customer;
    }
}

```

**🔍 문제점**

- 고객 ID가 동일하더라도 주문마다 **새로운 `Customer` 객체가 생성됨**.
- **주문이 많아질수록 중복된 `Customer` 객체가 계속 생성**되어 메모리 낭비 발생.
- **고객 정보가 변경되면 모든 `Order` 객체를 찾아 수정해야 하는 문제 발생**.

---

### **🔹 리팩토링 1단계: Repository 객체 생성**

- **Customer 객체를 한 번만 생성하고, 저장소에서 재사용하도록 변경**.

```java
import java.util.HashMap;
import java.util.Map;

class CustomerRepository {
    private static final Map<String, Customer> customers = new HashMap<>();

    public static Customer getCustomer(String id) {
        return customers.computeIfAbsent(id, Customer::new);
    }
}

```

✅ **`computeIfAbsent()`를 사용하여 고객 ID가 없으면 새로 생성하고, 있으면 기존 객체를 반환**.

✅ **이제 동일한 ID를 가진 고객은 하나의 `Customer` 객체만 유지됨**.

---

### **🔹 리팩토링 2단계: Order 클래스가 Customer 객체를 참조하도록 변경**

- `Order` 클래스는 `CustomerRepository`를 사용하여 고객 객체를 가져오도록 변경.

```java
class Order {
    private String number;
    private Customer customer;

    public Order(String number, String customerId) {
        this.number = number;
        this.customer = CustomerRepository.getCustomer(customerId);
    }

    public Customer getCustomer() {
        return customer;
    }
}

```

✅ **이제 `Order` 클래스에서 `Customer` 객체를 직접 생성하지 않고, 저장소에서 가져와 공유**.

✅ **동일한 고객 ID를 가지는 주문들은 모두 같은 `Customer` 객체를 참조**.

---

### **🔹 리팩토링 3단계: 글로벌 저장소 의존성 줄이기**

- 글로벌 저장소(`CustomerRepository`) 사용을 줄이고, 필요할 때 생성자에서 저장소를 주입하도록 변경.

```java
class Order {
    private String number;
    private Customer customer;

    public Order(String number, String customerId, CustomerRepository customerRepository) {
        this.number = number;
        this.customer = customerRepository.getCustomer(customerId);
    }

    public Customer getCustomer() {
        return customer;
    }
}

```

✅ **이제 `CustomerRepository`를 외부에서 주입받도록 변경하여, `Order`가 글로벌 저장소에 직접 의존하지 않음**.

---

### **🚀 최종 리팩토링 결과 (참조 방식 적용)**

```java
import java.util.HashMap;
import java.util.Map;

class Customer {
    private String id;

    public Customer(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}

class CustomerRepository {
    private static final Map<String, Customer> customers = new HashMap<>();

    public Customer getCustomer(String id) {
        return customers.computeIfAbsent(id, Customer::new);
    }
}

class Order {
    private String number;
    private Customer customer;

    public Order(String number, String customerId, CustomerRepository customerRepository) {
        this.number = number;
        this.customer = customerRepository.getCustomer(customerId);
    }

    public Customer getCustomer() {
        return customer;
    }
}

// 사용 예제
public class Main {
    public static void main(String[] args) {
        CustomerRepository customerRepository = new CustomerRepository();

        Order order1 = new Order("001", "C123", customerRepository);
        Order order2 = new Order("002", "C123", customerRepository);

        System.out.println(order1.getCustomer() == order2.getCustomer()); // true (같은 객체)
    }
}

```

✅ **이제 같은 `customerId`를 가진 주문들은 동일한 `Customer` 객체를 공유**.

✅ **불필요한 중복 객체 생성을 방지하며, 메모리 사용이 최적화됨**.

✅ **주문 데이터가 변경되더라도 모든 참조된 객체에서 변경 사항이 일관되게 유지됨**.

---

## **🎯 정리**

### **📌 배경 (Motivation)**

- **값(Value)으로 사용하면 동일한 고객 ID를 가지더라도 각 주문이 별도의 `Customer` 객체를 가짐**.
- **고객 정보가 변경될 때 모든 `Order` 객체를 찾아 수정해야 하는 문제 발생**.
- **데이터 일관성을 유지하려면 동일한 고객 ID를 참조로 관리하는 것이 더 적절함**.

### **📌 절차 (Mechanics)**

1. **`Customer` 객체를 관리하는 `CustomerRepository`를 생성**.
2. **`Customer` 객체를 직접 생성하는 대신 `CustomerRepository.getCustomer(id)`를 통해 참조를 가져오도록 변경**.
3. **글로벌 저장소 의존성을 줄이기 위해 `CustomerRepository`를 `Order` 생성자에서 주입받도록 변경**.
4. **테스트를 수행하여 데이터 일관성이 유지되는지 확인**.

### **📌 예제 (Example)**

### **1️⃣ 리팩토링 전**

- 주문이 고객 데이터를 **값 복사 방식으로 관리**하여 동일한 고객이 여러 객체로 존재.
- 데이터 변경 시 모든 복사본을 찾아 업데이트해야 하는 문제 발생.

### **2️⃣ 리팩토링 후**

- 고객 정보를 **`CustomerRepository`에서 관리하고 참조를 반환하도록 변경**.
- **모든 `Order` 객체가 동일한 고객 객체를 참조**하므로, 데이터 일관성이 유지됨.

---

💡 **값을 참조로 변경하면 데이터 일관성이 향상되며, 불필요한 객체 복사를 방지하여 성능 최적화도 가능하다!**