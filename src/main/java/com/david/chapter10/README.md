# 조건부 로직 간소화

### 주요 리팩토링 기법

1. **Decompose Conditional (조건문 분해, p.260)**
    - 복잡한 조건문을 더 작은 메서드로 분해하여 가독성을 높입니다.
2. **Consolidate Conditional Expression (조건식 통합, p.263)**
    - 여러 개의 분리된 조건식을 하나의 명확한 논리 표현으로 통합하여 가독성을 개선합니다.
3. **Replace Nested Conditional with Guard Clauses (중첩된 조건문을 가드 절로 변경, p.266)**
    - 특정한 예외 상황을 먼저 검사하고, 해당 경우 즉시 리턴하여 중첩된 조건문을 줄입니다. (early return 패턴)
4. **Replace Conditional with Polymorphism (조건문을 다형성으로 변경, p.272)**
    - 동일한 조건 분기 로직이 여러 곳에서 반복될 경우, 이를 다형성을 활용하여 제거합니다.
5. **Introduce Special Case (특수 케이스 도입, p.289)**
    - 특정 값(예: `null`)을 처리하는 조건문이 반복될 경우, 이를 별도의 클래스로 추출하여 중복을 줄이고 코드 명확성을 높입니다.
    - 흔히 **Introduce Null Object (널 객체 패턴)**과 연결됩니다.
6. **Introduce Assertion (명시적 검사 추가, p.302)**
    - 프로그램 상태를 명확히 하고 예기치 않은 동작을 방지하기 위해 `assert` 구문을 추가합니다.

# 10.1 **Decompose Conditional (조건문 분해)**

### **📌 배경 (Motivation)**

복잡한 조건문은 코드의 가독성을 떨어뜨리고 유지보수를 어렵게 만듭니다.

- 긴 함수 자체도 가독성을 저하시키지만, **조건문이 많아질수록 이해하기 더 어려워짐**
- 조건식이 **"무엇이 실행되는지"는 보여주지만, "왜 그렇게 실행되는지"를 숨길 수 있음**
- **조건과 실행 로직을 분리하면 코드의 의도가 더욱 명확해짐**

따라서, **Extract Function(함수 추출)** 기법을 활용하여 조건문을 분리하면 **조건의 의미와 실행 동작을 명확히 이해할 수 있음**.

---

### **⚙️ 절차 (Mechanics)**

1. **Extract Function(함수 추출, p.106)** 을 사용하여 조건식을 별도의 함수로 이동
2. 조건문의 각 분기를 별도의 함수로 이동
3. 리팩토링 후 삼항 연산자(`? :`)를 사용하여 코드 가독성을 향상

---

### **💡 예제: 여름 요금과 일반 요금 계산**

다음은 특정 날짜가 **여름 기간**인지에 따라 요금이 달라지는 코드입니다.

```java
if (!aDate.isBefore(plan.summerStart) && !aDate.isAfter(plan.summerEnd))
    charge = quantity * plan.summerRate;
else
    charge = quantity * plan.regularRate + plan.regularServiceCharge;

```

### **🚀 단계별 리팩토링**

1️⃣ **조건식 분리**

조건 부분을 `summer()` 함수로 추출:

```java
if (summer())
    charge = quantity * plan.summerRate;
else
    charge = quantity * plan.regularRate + plan.regularServiceCharge;

boolean summer() {
    return !aDate.isBefore(plan.summerStart) && !aDate.isAfter(plan.summerEnd);
}

```

2️⃣ **각 실행 로직을 별도 함수로 추출**

`summerCharge()`와 `regularCharge()`를 생성:

```java
if (summer())
    charge = summerCharge();
else
    charge = regularCharge();

boolean summer() {
    return !aDate.isBefore(plan.summerStart) && !aDate.isAfter(plan.summerEnd);
}

double summerCharge() {
    return quantity * plan.summerRate;
}

double regularCharge() {
    return quantity * plan.regularRate + plan.regularServiceCharge;
}

```

3️⃣ **삼항 연산자 사용하여 코드 간결화**

리팩토링 후 최종 코드:

```java
charge = summer() ? summerCharge() : regularCharge();

boolean summer() {
    return !aDate.isBefore(plan.summerStart) && !aDate.isAfter(plan.summerEnd);
}

double summerCharge() {
    return quantity * plan.summerRate;
}

double regularCharge() {
    return quantity * plan.regularRate + plan.regularServiceCharge;
}

```

---

### **🎯 리팩토링의 효과**

✅ **조건문의 의도가 더 명확해짐** (`summer()` 함수)

✅ **각 분기의 동작을 쉽게 이해할 수 있음** (`summerCharge()`, `regularCharge()`)

✅ **함수가 작아지면서 재사용성이 높아짐**

✅ **삼항 연산자를 활용해 한 줄로 표현 가능 → 가독성 향상**

# **10.2 Consolidate Conditional Expression (조건식 통합)**

## **📌 배경 (Motivation)**

코드를 작성하다 보면 **여러 개의 독립적인 조건을 검사하지만, 결과는 동일한 경우**를 자주 발견할 수 있습니다.

이러한 경우, **조건문을 논리 연산자(AND/OR)를 사용하여 하나의 조건식으로 통합**하면 코드가 더 명확해지고 중복을 줄일 수 있습니다.

### **왜 조건식을 통합해야 하는가?**

1. **코드 가독성 향상**
    - 단순한 조건들의 나열은 개별 검사처럼 보일 수 있지만, 사실상 하나의 논리적 검사라는 점을 명확히 드러낼 수 있습니다.
    - 독립적인 여러 조건이 동일한 동작을 수행한다면, 이를 하나의 논리적인 검사로 합치는 것이 더 직관적입니다.
2. **리팩토링을 쉽게 만들기 위해**
    - 조건을 통합한 후, 이를 **Extract Function (함수 추출)** 기법과 결합하면 **코드의 의도**를 더 명확히 할 수 있습니다.
    - 단순한 **"무엇을 실행하는가"**가 아니라 **"왜 실행하는가"**를 표현할 수 있습니다.

### **언제 통합하지 말아야 하는가?**

- 조건들이 **서로 독립적인 검사이며, 논리적으로 하나로 묶을 이유가 없는 경우**에는 조건식을 통합하지 않는 것이 좋습니다.
- 단순히 코드를 줄이는 것이 아니라, **논리적으로 자연스럽게 표현되는지를 고려해야 합니다.**

---

## **⚙️ 절차 (Mechanics)**

1️⃣ **조건식이 부작용(Side Effect)을 발생시키는지 확인**

- 만약 조건식 내부에서 변수를 변경하는 등의 **부작용이 있다면**, 먼저 이를 제거해야 합니다.
- `Separate Query from Modifier (쿼리와 변경 분리, p.306)` 기법을 사용하여 변경 로직을 분리합니다.

2️⃣ **두 개의 조건을 논리 연산자로 결합**

- **연속적인 `if` 문** → `OR (||)` 연산자로 결합
- **중첩된 `if` 문** → `AND (&&)` 연산자로 결합

3️⃣ **테스트 실행**

- 변경 후에도 기존과 동일한 동작을 수행하는지 확인합니다.

4️⃣ **모든 조건을 하나로 통합**

- 동일한 결과를 반환하는 조건식을 하나의 `if` 문으로 만듭니다.

5️⃣ **Extract Function(함수 추출) 적용**

- 통합한 조건식을 **의미 있는 이름의 함수**로 추출하여 코드의 **의도를 명확히 표현**합니다.

---

## **💡 예제**

### **🎯 예제 1: OR 연산자 활용**

아래와 같은 코드가 있습니다.

```java
double disabilityAmount(Employee anEmployee) {
    if (anEmployee.getSeniority() < 2) return 0;
    if (anEmployee.getMonthsDisabled() > 12) return 0;
    if (anEmployee.isPartTime()) return 0;

    // compute the disability amount
}

```

➡ **세 개의 조건이 동일한 결과(`return 0;`)를 반환하므로, 이를 OR 연산자로 통합할 수 있습니다.**

### **1️⃣ 조건문 통합**

```java
double disabilityAmount(Employee anEmployee) {
    if ((anEmployee.getSeniority() < 2)
        || (anEmployee.getMonthsDisabled() > 12)
        || (anEmployee.isPartTime())) return 0;

    // compute the disability amount
}

```

### **2️⃣ Extract Function 적용**

```java
double disabilityAmount(Employee anEmployee) {
    if (isNotEligibleForDisability(anEmployee)) return 0;

    // compute the disability amount
}

private boolean isNotEligibleForDisability(Employee anEmployee) {
    return (anEmployee.getSeniority() < 2)
        || (anEmployee.getMonthsDisabled() > 12)
        || (anEmployee.isPartTime());
}

```

✅ **이제 `isNotEligibleForDisability` 함수가 코드의 의도를 명확히 설명하므로 가독성이 향상되었습니다.**

---

### **🎯 예제 2: AND 연산자 활용**

중첩된 `if` 문을 보면 다음과 같은 코드가 있을 수 있습니다.

```java
if (anEmployee.isOnVacation()) {
    if (anEmployee.getSeniority() > 10) {
        return 1;
    }
}
return 0.5;

```

### **1️⃣ 조건문 통합**

```java
if (anEmployee.isOnVacation() && anEmployee.getSeniority() > 10)
    return 1;
return 0.5;

```

✅ **논리적으로 `AND (&&)` 연산자를 사용하여 하나의 조건으로 묶었습니다.**

✅ **불필요한 중첩을 제거하여 코드가 더 간결해졌습니다.**

---

### **🎯 예제 3: 복합적인 조건 통합**

만약 AND와 OR이 섞인 경우, **Extract Function**을 적극 활용하여 가독성을 유지해야 합니다.

```java
if ((anEmployee.getSeniority() < 2) || (anEmployee.getMonthsDisabled() > 12)) return 0;
if (anEmployee.isPartTime() && anEmployee.isOnVacation()) return 0;

```

➡ **이 경우, 각 논리적 의미를 함수로 분리하여 코드 가독성을 높일 수 있습니다.**

```java
if (isNotEligibleForDisability(anEmployee)) return 0;
if (isPartTimeAndOnVacation(anEmployee)) return 0;

private boolean isNotEligibleForDisability(Employee anEmployee) {
    return (anEmployee.getSeniority() < 2) || (anEmployee.getMonthsDisabled() > 12);
}

private boolean isPartTimeAndOnVacation(Employee anEmployee) {
    return anEmployee.isPartTime() && anEmployee.isOnVacation();
}

```

✅ **논리적인 단위를 함수로 추출하여 코드의 의도를 명확히 표현함**

✅ **복잡한 AND/OR 조합을 한눈에 이해할 수 있도록 개선됨**

---

## **📌 정리**

|  | 기존 코드 | 리팩토링 후 |
| --- | --- | --- |
| **가독성** | 여러 개의 조건이 개별적으로 작성되어 있음 | 하나의 조건으로 통합하여 논리 명확 |
| **중복 제거** | 동일한 결과를 반환하는 여러 조건이 중복 | 논리 연산자로 통합 |
| **코드 의도** | `if` 문을 봐도 왜 이런 조건이 필요한지 알기 어려움 | `Extract Function`을 사용하여 조건의 의미를 명확하게 표현 |

### **✨ 핵심 요약**

✅ **동일한 결과를 반환하는 조건문은 논리 연산자(`||`, `&&`)를 활용하여 통합**

✅ **Extract Function을 사용하여 조건의 의도를 명확히 표현**

✅ **AND/OR 연산이 혼합될 경우, 별도의 함수로 분리하여 가독성 유지**



# **10.3 Replace Nested Conditional with Guard Clauses (중첩 조건문을 가드 절로 변경)**

---

## **📌 배경 (Motivation)**

조건문에는 두 가지 주요 패턴이 있습니다.

1. **일반적인 흐름을 처리하는 경우**
    - `if-else`를 사용하여 **모든 경우를 동등한 중요도로 처리**
    - 예: 계절별 요금을 결정하는 로직
2. **예외적인 경우를 먼저 처리하는 경우 (Guard Clause 활용)**
    - 예외적인 상황을 **먼저 처리하고 조기에 반환(return)** 하여 **핵심 로직을 강조**
    - 가독성이 향상되며, **중첩된 조건문을 제거**할 수 있음

💡 **"Guard Clause (가드 절)"란?**

특정 조건이 발생하면 **즉시 반환(return)** 하여 나머지 로직을 실행하지 않도록 하는 방식입니다.

→ 코드가 더 **평면적(flat)** 으로 정리되며, 가독성이 크게 향상됩니다.

---

## **⚙️ 절차 (Mechanics)**

1️⃣ **가장 바깥쪽 조건을 가드 절로 변환**

- 중첩된 `if` 문을 하나씩 변환하면서 코드를 단순화

2️⃣ **변경 후 테스트 실행**

- 리팩토링 후 기존과 동일한 동작을 수행하는지 확인

3️⃣ **다른 조건들도 반복하여 가드 절로 변환**

- 모든 예외적인 조건을 가드 절로 정리

4️⃣ **모든 가드 절이 동일한 값을 반환하는 경우, 조건을 통합**

- `Consolidate Conditional Expression (263)`을 적용하여 가독성을 높임

5️⃣ **불필요한 변수 제거**

- 중간 결과 저장 변수가 필요 없다면 제거하여 코드를 단순화

---

## **💡 예제 1: 중첩된 조건문 리팩토링**

### **📍 기존 코드**

아래 코드는 직원의 급여를 계산하는 함수입니다.

하지만 **중첩된 조건문이 많아 가독성이 떨어집니다.**

```java
public class Employee {
    boolean isSeparated;
    boolean isRetired;

    public Employee(boolean isSeparated, boolean isRetired) {
        this.isSeparated = isSeparated;
        this.isRetired = isRetired;
    }

    public boolean isSeparated() {
        return isSeparated;
    }

    public boolean isRetired() {
        return isRetired;
    }
}

public class PayrollService {
    public static Map<String, Object> payAmount(Employee employee) {
        Map<String, Object> result = new HashMap<>();

        if (employee.isSeparated()) {
            result.put("amount", 0);
            result.put("reasonCode", "SEP");
        } else {
            if (employee.isRetired()) {
                result.put("amount", 0);
                result.put("reasonCode", "RET");
            } else {
                // 급여 계산 로직
                result.put("amount", computeSalary());
                result.put("reasonCode", "OK");
            }
        }
        return result;
    }

    private static int computeSalary() {
        return 1000; // 급여 계산 로직
    }
}

```

➡ **중첩된 `if-else`로 인해 코드가 복잡함.**

---

### **📍 단계별 리팩토링**

### **1️⃣ 첫 번째 조건을 Guard Clause로 변경**

```java
public static Map<String, Object> payAmount(Employee employee) {
    if (employee.isSeparated()) {
        return Map.of("amount", 0, "reasonCode", "SEP");
    }

    Map<String, Object> result = new HashMap<>();
    if (employee.isRetired()) {
        result.put("amount", 0);
        result.put("reasonCode", "RET");
    } else {
        result.put("amount", computeSalary());
        result.put("reasonCode", "OK");
    }
    return result;
}

```

➡ `if (employee.isSeparated())`에서 **즉시 반환**하도록 변경

### **2️⃣ 두 번째 조건도 Guard Clause로 변경**

```java
public static Map<String, Object> payAmount(Employee employee) {
    if (employee.isSeparated()) {
        return Map.of("amount", 0, "reasonCode", "SEP");
    }
    if (employee.isRetired()) {
        return Map.of("amount", 0, "reasonCode", "RET");
    }
    return Map.of("amount", computeSalary(), "reasonCode", "OK");
}

```

➡ **이제 모든 예외적인 조건을 가드 절로 변경**

✅ **최종 결과: 중첩된 조건문이 제거되고 코드가 단순해짐**

---

## **💡 예제 2: 조건을 뒤집어 가드 절로 변환**

### **📍 기존 코드**

```java
public static double adjustedCapital(Instrument anInstrument) {
    double result = 0;
    if (anInstrument.getCapital() > 0) {
        if (anInstrument.getInterestRate() > 0 && anInstrument.getDuration() > 0) {
            result = (anInstrument.getIncome() / anInstrument.getDuration()) * anInstrument.getAdjustmentFactor();
        }
    }
    return result;
}

```

➡ **중첩된 조건문으로 인해 가독성이 떨어짐.**

---

### **📍 단계별 리팩토링**

### **1️⃣ 첫 번째 조건을 Guard Clause로 변환**

```java
public static double adjustedCapital(Instrument anInstrument) {
    if (anInstrument.getCapital() <= 0) return 0;

    double result = 0;
    if (anInstrument.getInterestRate() > 0 && anInstrument.getDuration() > 0) {
        result = (anInstrument.getIncome() / anInstrument.getDuration()) * anInstrument.getAdjustmentFactor();
    }
    return result;
}

```

➡ `if (anInstrument.getCapital() <= 0)`로 **조기 반환하여 중첩을 제거**

### **2️⃣ 두 번째 조건도 Guard Clause로 변환**

```java
public static double adjustedCapital(Instrument anInstrument) {
    if (anInstrument.getCapital() <= 0) return 0;
    if (anInstrument.getInterestRate() <= 0 || anInstrument.getDuration() <= 0) return 0;

    return (anInstrument.getIncome() / anInstrument.getDuration()) * anInstrument.getAdjustmentFactor();
}

```

✅ **최종 결과: 중첩된 조건문을 제거하고, 예외적인 경우를 먼저 처리하여 가독성을 향상**

---

## **📌 정리**

|  | 기존 코드 | 리팩토링 후 |
| --- | --- | --- |
| **가독성** | 중첩된 `if-else`가 많아 복잡 | Guard Clause를 사용하여 간결한 코드 |
| **불필요한 로직 제거** | 동일한 `return` 문이 반복 | 예외 조건을 먼저 처리하여 중복 제거 |
| **유지보수성** | 변경 시 여러 곳 수정 필요 | 조건이 명확해져 유지보수 용이 |

### **✨ 핵심 요약**

✅ **중첩된 `if-else` 문을 Guard Clause로 변환하여 평면적인 코드 구조 유지**

✅ **예외적인 조건을 먼저 처리하여, 주요 로직이 강조되도록 리팩토링**

✅ **불필요한 변수 제거하여 코드 단순화**

# **10.4 Replace Conditional with Polymorphism (조건문을 다형성으로 변경)**

---

## **📌 배경 (Motivation)**

조건문이 많아지면 코드의 복잡도가 급격히 증가하여 유지보수가 어려워집니다.

이러한 경우 **다형성을 활용하여 개별 클래스로 분리하면 코드의 가독성과 확장성이 향상됩니다.**

### **🔹 언제 다형성을 적용하는가?**

1. **객체의 타입에 따라 로직이 달라지는 경우**
    - 예: 책, 음악, 음식 등의 처리 방식이 서로 다를 때
    - `switch` 또는 `if-else` 문이 여러 곳에서 반복될 경우
2. **공통적인 기본 로직(Base Case)이 있고, 일부 동작이 다를 때**
    - 기본 동작을 슈퍼클래스(Superclass)에 정의
    - 특정 동작만 서브클래스(Subclass)에서 오버라이딩
3. **기능 추가 시 기존 코드를 수정하지 않고 확장할 수 있도록 설계할 때**
    - 새로운 조건 추가 시 `if-else` 또는 `switch`를 수정하지 않고, 새로운 클래스를 추가하는 방식

---

## **⚙️ 절차 (Mechanics)**

1️⃣ **다형성을 적용할 클래스를 생성**

- 기존 조건문에서 분기되는 타입을 개별 클래스로 분리
- 팩토리 메서드(Factory Method)를 사용하여 적절한 인스턴스를 반환

2️⃣ **기본 로직을 슈퍼클래스에 이동**

- `Extract Function(106)`을 활용하여 공통 로직을 분리

3️⃣ **조건문이 적용되는 각 경우를 서브클래스로 이동**

- 기존 `switch` 또는 `if-else`의 개별 로직을 서브클래스에서 구현

4️⃣ **슈퍼클래스의 기본 구현을 명확히 설정**

- `abstract` 메서드 또는 `throw Exception`을 사용하여 구현을 강제

---

## **💡 예제 1: 새(Bird) 타입별 속도 및 깃털 상태**

### **📍 기존 코드 (조건문을 사용한 방식)**

```java
public class Bird {
    private String type;
    private int numberOfCoconuts;
    private int voltage;
    private boolean isNailed;

    public Bird(String type, int numberOfCoconuts, int voltage, boolean isNailed) {
        this.type = type;
        this.numberOfCoconuts = numberOfCoconuts;
        this.voltage = voltage;
        this.isNailed = isNailed;
    }

    public String getPlumage() {
        switch (type) {
            case "EuropeanSwallow":
                return "average";
            case "AfricanSwallow":
                return (numberOfCoconuts > 2) ? "tired" : "average";
            case "NorwegianBlueParrot":
                return (voltage > 100) ? "scorched" : "beautiful";
            default:
                return "unknown";
        }
    }

    public int getAirSpeedVelocity() {
        switch (type) {
            case "EuropeanSwallow":
                return 35;
            case "AfricanSwallow":
                return 40 - 2 * numberOfCoconuts;
            case "NorwegianBlueParrot":
                return (isNailed) ? 0 : 10 + voltage / 10;
            default:
                return 0;
        }
    }
}

```

➡ **문제점:**

- `switch` 문이 여러 개 존재하여 **유지보수가 어려움**
- 새로운 새 종류를 추가할 때 **기존 코드 수정이 필요**

---

### **📍 단계별 리팩토링**

### **1️⃣ 개별 클래스로 분리**

```java
abstract class Bird {
    public abstract String getPlumage();
    public abstract int getAirSpeedVelocity();
}

class EuropeanSwallow extends Bird {
    @Override
    public String getPlumage() {
        return "average";
    }

    @Override
    public int getAirSpeedVelocity() {
        return 35;
    }
}

class AfricanSwallow extends Bird {
    private int numberOfCoconuts;

    public AfricanSwallow(int numberOfCoconuts) {
        this.numberOfCoconuts = numberOfCoconuts;
    }

    @Override
    public String getPlumage() {
        return (numberOfCoconuts > 2) ? "tired" : "average";
    }

    @Override
    public int getAirSpeedVelocity() {
        return 40 - 2 * numberOfCoconuts;
    }
}

class NorwegianBlueParrot extends Bird {
    private int voltage;
    private boolean isNailed;

    public NorwegianBlueParrot(int voltage, boolean isNailed) {
        this.voltage = voltage;
        this.isNailed = isNailed;
    }

    @Override
    public String getPlumage() {
        return (voltage > 100) ? "scorched" : "beautiful";
    }

    @Override
    public int getAirSpeedVelocity() {
        return (isNailed) ? 0 : 10 + voltage / 10;
    }
}

```

➡ **각 새 타입을 개별 클래스로 분리하여 조건문 제거**

### **2️⃣ 팩토리 메서드 추가**

```java
class BirdFactory {
    public static Bird createBird(String type, int numberOfCoconuts, int voltage, boolean isNailed) {
        switch (type) {
            case "EuropeanSwallow":
                return new EuropeanSwallow();
            case "AfricanSwallow":
                return new AfricanSwallow(numberOfCoconuts);
            case "NorwegianBlueParrot":
                return new NorwegianBlueParrot(voltage, isNailed);
            default:
                throw new IllegalArgumentException("Unknown bird type");
        }
    }
}

```

➡ **새로운 새 타입이 추가될 때, 기존 코드 수정 없이 팩토리 메서드만 확장 가능**

### **3️⃣ 최종 코드**

```java
public class Main {
    public static void main(String[] args) {
        Bird bird1 = BirdFactory.createBird("EuropeanSwallow", 0, 0, false);
        Bird bird2 = BirdFactory.createBird("AfricanSwallow", 3, 0, false);
        Bird bird3 = BirdFactory.createBird("NorwegianBlueParrot", 0, 120, false);

        System.out.println("Bird 1 - Plumage: " + bird1.getPlumage() + ", Speed: " + bird1.getAirSpeedVelocity());
        System.out.println("Bird 2 - Plumage: " + bird2.getPlumage() + ", Speed: " + bird2.getAirSpeedVelocity());
        System.out.println("Bird 3 - Plumage: " + bird3.getPlumage() + ", Speed: " + bird3.getAirSpeedVelocity());
    }
}

```

✅ **리팩토링 결과:**

- **조건문이 제거되고 다형성을 활용하여 코드가 단순해짐**
- **새로운 새 타입 추가 시 기존 코드 수정 없이 확장 가능**

---

## **📌 정리**

|  | 기존 코드 | 리팩토링 후 |
| --- | --- | --- |
| **구조** | `switch-case`로 여러 개의 조건문 존재 | 개별 클래스로 분리하여 다형성 활용 |
| **확장성** | 새로운 조건 추가 시 기존 코드 수정 필요 | 새로운 클래스 추가만으로 확장 가능 |
| **가독성** | 조건이 많아 가독성이 낮음 | **각 새의 동작이 분리되어 코드가 명확함** |

### **✨ 핵심 요약**

✅ **`switch` 또는 `if-else`를 다형성으로 변환하여 중복 제거**

✅ **공통 로직을 슈퍼클래스에 두고, 서브클래스에서 개별 동작을 구현**

✅ **팩토리 메서드를 사용하여 객체 생성을 캡슐화하여 확장성을 확보**

# **10.5 Introduce Special Case (특수 케이스 도입)**

---

## **📌 배경 (Motivation)**

특정 데이터 값(예: `null`, `"unknown"`)이 있을 때, 여러 곳에서 동일한 방식으로 처리하는 경우가 많습니다.

이러한 반복적인 처리를 **"특수 케이스(Special Case) 객체"** 로 캡슐화하면 코드 중복을 줄이고 가독성을 향상시킬 수 있습니다.

### **🔹 언제 특수 케이스를 도입하는가?**

1. **특정 값(예: `"unknown"`, `null`)을 매번 체크해야 하는 경우**
    - `"unknown"`을 만나면 `"occupant"`으로 변환
    - `null`이면 기본값을 설정
    - `null` 처리를 위한 `Null Object` 패턴과 유사함
2. **여러 곳에서 같은 처리를 반복하는 경우**
    - 동일한 조건을 `if-else` 또는 `switch` 문으로 반복 처리하는 경우
3. **데이터 구조가 고정적인 기본값을 가지는 경우**
    - 예: `"unknown"`인 경우 기본 청구 요금제를 적용

---

## **⚙️ 절차 (Mechanics)**

1️⃣ **특수 케이스를 판별하는 `isUnknown` 속성을 추가**

- 기본적으로 `false`를 반환

2️⃣ **특수 케이스 클래스를 생성**

- `isUnknown`이 `true`를 반환하도록 설정

3️⃣ **특수 케이스 비교 코드를 함수로 추출**

- `Extract Function (106)`을 사용하여 `isUnknown()`을 별도 함수로 만들기

4️⃣ **특수 케이스 객체를 반환하도록 변경**

- `"unknown"` 또는 `null` 값을 **특수 케이스 객체** 로 대체

5️⃣ **기본 값 처리를 특수 케이스 클래스 내부로 이동**

- `name`, `billingPlan`, `paymentHistory` 등의 기본값을 직접 반환하도록 수정

6️⃣ **더 이상 필요 없는 `if-else` 조건을 제거**

- 기존 조건문을 삭제하고 **다형성을 활용하여 처리**

---

## **💡 예제: 고객(Customer) 정보의 특수 케이스 처리**

### **📍 기존 코드 (조건문을 사용한 방식)**

```java
public class Site {
    private Customer customer;

    public Site(Customer customer) {
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }
}

public class Customer {
    private String name;
    private String billingPlan;
    private PaymentHistory paymentHistory;

    public Customer(String name, String billingPlan, PaymentHistory paymentHistory) {
        this.name = name;
        this.billingPlan = billingPlan;
        this.paymentHistory = paymentHistory;
    }

    public String getName() {
        return name;
    }

    public String getBillingPlan() {
        return billingPlan;
    }

    public PaymentHistory getPaymentHistory() {
        return paymentHistory;
    }
}

public class PaymentHistory {
    private int weeksDelinquentInLastYear;

    public PaymentHistory(int weeksDelinquentInLastYear) {
        this.weeksDelinquentInLastYear = weeksDelinquentInLastYear;
    }

    public int getWeeksDelinquentInLastYear() {
        return weeksDelinquentInLastYear;
    }
}

```

### **📍 문제점**

- `customer`가 `"unknown"` 또는 `null`일 때 매번 별도의 처리가 필요함
- 중복 코드가 많아 유지보수가 어려움

---

### **📍 단계별 리팩토링**

### **1️⃣ `isUnknown` 속성 추가 (기본 `false` 반환)**

```java
public class Customer {
    public boolean isUnknown() {
        return false;
    }
}

```

### **2️⃣ 특수 케이스 클래스 생성**

```java
public class UnknownCustomer extends Customer {
    @Override
    public boolean isUnknown() {
        return true;
    }

    @Override
    public String getName() {
        return "occupant";
    }

    @Override
    public String getBillingPlan() {
        return "basic";  // 기본 요금제 적용
    }

    @Override
    public PaymentHistory getPaymentHistory() {
        return new NullPaymentHistory();
    }
}

```

### **3️⃣ `Null Object` 패턴 적용 (PaymentHistory)**

```java
public class NullPaymentHistory extends PaymentHistory {
    public NullPaymentHistory() {
        super(0); // 연체 주 수를 0으로 설정
    }
}

```

### **4️⃣ `getCustomer()`에서 `"unknown"` 대신 특수 케이스 반환**

```java
public class Site {
    private Customer customer;

    public Site(Customer customer) {
        this.customer = customer;
    }

    public Customer getCustomer() {
        return (customer == null) ? new UnknownCustomer() : customer;
    }
}

```

➡ **이제 `customer`가 `null`이면 `UnknownCustomer` 객체를 반환하여 별도의 `if-else` 체크 없이 사용 가능**

---

### **📍 리팩토링 후 최종 코드**

### **💡 기존 코드 (if-else로 `"unknown"` 처리)**

```java
Site site = new Site(null);
Customer customer = site.getCustomer();

String customerName;
if (customer.isUnknown()) {
    customerName = "occupant";
} else {
    customerName = customer.getName();
}

String billingPlan;
if (customer.isUnknown()) {
    billingPlan = "basic";
} else {
    billingPlan = customer.getBillingPlan();
}

int weeksDelinquent;
if (customer.isUnknown()) {
    weeksDelinquent = 0;
} else {
    weeksDelinquent = customer.getPaymentHistory().getWeeksDelinquentInLastYear();
}

```

➡ **`if-else`가 많아서 중복 코드가 발생함**

### **✅ 리팩토링 후 (특수 케이스 적용)**

```java
Site site = new Site(null);
Customer customer = site.getCustomer();

String customerName = customer.getName();
String billingPlan = customer.getBillingPlan();
int weeksDelinquent = customer.getPaymentHistory().getWeeksDelinquentInLastYear();

```

✅ **`if-else`가 제거되고, `customer` 객체의 다형성을 활용하여 간결하게 처리됨**

✅ **새로운 특수 케이스 추가 시 기존 코드를 수정할 필요 없음**

---

## **📌 정리**

|  | 기존 코드 | 리팩토링 후 |
| --- | --- | --- |
| **조건문 처리** | `"unknown"`을 체크하는 `if-else`가 반복됨 | **특수 케이스 객체를 반환하여 다형성 활용** |
| **가독성** | 조건문이 많아 코드가 복잡함 | **객체의 메서드를 호출하는 방식으로 단순화** |
| **확장성** | 새로운 특수 케이스 추가 시 기존 코드 수정 필요 | **새로운 특수 케이스 클래스 추가만으로 확장 가능** |

### **✨ 핵심 요약**

✅ **특수 케이스(예: `"unknown"`, `null`)를 별도의 객체로 만들어 중복 코드 제거**

✅ **각 특수 케이스는 기본값을 직접 반환하여 `if-else` 없이 사용 가능**

✅ **다형성을 활용하여 유지보수가 쉬운 코드 구조로 개선**

# **10.6 Introduce Assertion (명시적 검사 추가)**

---

## **📌 배경 (Motivation)**

프로그램의 특정 부분은 **항상 특정 조건을 만족해야만 정상적으로 동작**합니다.

이러한 조건을 **명확하게 표현하지 않으면 코드의 의도를 파악하기 어려워지고, 디버깅이 어려워질 수 있습니다.**

### **🔹 왜 Assertion을 도입하는가?**

1. **숨겨진 가정(Assumption)을 명시적으로 드러냄**
    - 예: `할인율(discountRate)`은 항상 **0 이상**이어야 한다.
    - 예: `루트 연산(sqrt)`은 **음수가 입력되지 않아야 한다.**
2. **코드의 가독성을 향상시킴**
    - `assert`는 **"이 조건이 항상 참이어야 한다."**는 의미를 명확히 전달
    - 주석보다 더 강력한 문서화 효과
3. **디버깅과 유지보수에 도움을 줌**
    - `assert`가 실패하면 **즉시 오류가 발생**하므로 문제를 빠르게 찾을 수 있음
    - 프로그램이 잘못된 상태에서 계속 실행되는 것을 방지
4. **런타임 영향 없음 (일부 언어에서는 비활성화 가능)**
    - Java에서는 `ea`(enable assertions) 플래그가 없으면 `assert`문이 무시됨
    - **배포 환경에서 성능 저하 없이 유지 가능**

---

## **⚙️ 절차 (Mechanics)**

1️⃣ **가정(Assumption)이 있는 코드에서 이를 `assert`로 명시적으로 표현**

- `assert`가 실패하면 예외를 발생시키고, 문제를 조기에 발견할 수 있도록 함

2️⃣ **`assert`가 실행 흐름을 변경하지 않도록 주의**

- `assert`는 프로그램의 논리를 검증하는 용도로만 사용해야 하며, 실행 로직을 변경하면 안 됨

3️⃣ **중복된 조건을 제거하여 유지보수성을 높임**

- `Extract Function(106)`을 사용하여 중복 검사를 하나의 함수로 분리

4️⃣ **외부 입력을 검증할 때는 `assert` 대신 명시적인 예외 처리를 사용**

- `assert`는 **개발자 오류(Programmer Error) 체크용**
- 사용자 입력과 같은 **외부 데이터 검증에는 `if-else` 또는 예외(Exception)를 활용**

---

## **💡 예제 1: 할인율 검증**

### **📍 기존 코드**

```java
public class Customer {
    private Double discountRate;

    public Customer(Double discountRate) {
        this.discountRate = discountRate;
    }

    public double applyDiscount(double amount) {
        return (discountRate != null) ? amount - (discountRate * amount) : amount;
    }
}

```

### **📍 문제점**

- `discountRate`는 **항상 0 이상이어야 하지만, 코드에서 이를 명확히 보장하지 않음**
- `discountRate`가 **음수일 경우에도 적용되어 버림**

---

### **📍 단계별 리팩토링**

### **1️⃣ `if-else`를 `assert`로 변환**

```java
public double applyDiscount(double amount) {
    if (discountRate == null) return amount;
    assert discountRate >= 0 : "할인율은 음수가 될 수 없습니다.";
    return amount - (discountRate * amount);
}

```

➡ `assert discountRate >= 0;`을 추가하여 **음수 할인율을 방지**

### **2️⃣ `assert`를 `setter`에 추가하여 값 설정 시 검증**

```java
public void setDiscountRate(Double discountRate) {
    assert discountRate == null || discountRate >= 0 : "할인율은 0 이상이어야 합니다.";
    this.discountRate = discountRate;
}

```

➡ **할인율이 처음부터 잘못된 값으로 설정되지 않도록 보장**

---

### **📍 최종 리팩토링 코드**

```java
public class Customer {
    private Double discountRate;

    public Customer(Double discountRate) {
        setDiscountRate(discountRate);
    }

    public double applyDiscount(double amount) {
        if (discountRate == null) return amount;
        assert discountRate >= 0 : "할인율은 음수가 될 수 없습니다.";
        return amount - (discountRate * amount);
    }

    public void setDiscountRate(Double discountRate) {
        assert discountRate == null || discountRate >= 0 : "할인율은 0 이상이어야 합니다.";
        this.discountRate = discountRate;
    }
}

```

✅ **결과:**

- `assert`를 통해 **코드 가정이 명확해짐**
- 할인율이 **음수로 설정될 가능성을 원천 차단**

---

## **💡 예제 2: 제곱근 계산 검증**

### **📍 기존 코드**

```java
public double calculateSqrt(double value) {
    return Math.sqrt(value);
}

```

### **📍 문제점**

- `Math.sqrt(value)`는 **음수 값이 입력되면 `NaN`을 반환**
- **음수 값이 들어가지 않도록 명확한 검증이 필요함**

---

### **📍 리팩토링 후**

```java
public double calculateSqrt(double value) {
    assert value >= 0 : "제곱근 계산 시 음수 값이 입력될 수 없습니다.";
    return Math.sqrt(value);
}

```

✅ **결과:**

- **음수 입력 시 즉시 예외 발생**하여 문제를 조기에 발견할 수 있음
- **의도를 명확히 전달하여 유지보수가 쉬워짐**

---

## **📌 정리**

|  | 기존 코드 | 리팩토링 후 |
| --- | --- | --- |
| **가정(Assumption) 명시 여부** | 코드에서 추론해야 함 | `assert`를 통해 명확히 표현 |
| **디버깅 및 유지보수** | 오류 발생 시 원인 파악이 어려움 | `assert`로 문제 발생 시점 즉시 감지 |
| **프로그램 실행 흐름** | 할인율이 음수일 경우도 정상 실행 | **잘못된 입력 시 즉시 예외 발생** |

### **✨ 핵심 요약**

✅ **숨겨진 가정을 `assert`를 통해 명확히 드러냄**

✅ **중요한 조건을 `assert`로 확인하여 디버깅을 쉽게 만듦**

✅ **외부 입력 검증은 `assert`가 아닌 예외 처리로 수행**