# Chapter 12 : 상속을 다루기 (Dealing with Inheritance)

- **상속(Inheritance)** 은 객체지향 프로그래밍에서 코드를 재사용할 수 있는 강력한 도구입니다. 하지만 잘못 사용되면 오히려 코드 이해와 유지보수를 어렵게 만듭니다.
- 잘못된 추상화로 계층구조가 복잡해짐
- 중복 코드가 상하위 클래스에 흩어짐
- 특정 필드 값에 따라 동작이 달라질 때 하위 클래스로 분리하지 않으면 조건문이 남발됨
- 처음에는 적절했던 상속 구조가 시간이 지나면서 맞지 않게 됨

이를 해결하기 위해 다양한 리팩토링 기법이 존재합니다.

# **12.1** 메서드올리기 Pull Up Method

### 1. 배경 (Motivation)

**중복 제거는 리팩토링의 핵심입니다.**

두 개의 서브클래스에 동일한 기능을 수행하는 메서드가 있다면, 이 중복은 버그의 온상입니다. 나중에 한 쪽만 수정될 가능성이 있고, 이는 **일관성 문제**로 이어질 수 있습니다.

- 메서드가 완전히 같다면 즉시 Pull Up 가능
- 거의 같다면 Parameterize Function(매개변수화)을 먼저 고려
- 서브클래스 전용 필드/메서드가 사용된다면, 먼저 Pull Up Field/Method가 필요
- 동작 흐름은 같고 세부만 다르면 **Template Method 패턴**도 고려

---

### 2. 절차 (Mechanics)

1. 두 메서드가 동일한지 확인 (또는 동일하도록 리팩토링)
2. 슈퍼클래스에서 호출 가능한 필드/메서드만 사용하는지 확인
3. 메서드 시그니처 통일 (필요시 Change Function Declaration 적용)
4. 슈퍼클래스에 메서드 생성 후 구현 붙여넣기
5. 정적 검사 실행 (Java의 경우 컴파일 확인)
6. 한 서브클래스에서 메서드 제거 → 테스트
7. 나머지 서브클래스에서도 제거 → 테스트 반복

---

## 예제

---

### 리팩토링 전

```java
public class Employee extends Party {
    public int getAnnualCost() {
        return getMonthlyCost() * 12;
    }
}

public class Department extends Party {
    public int getTotalAnnualCost() {
        return getMonthlyCost() * 12;
    }
}

public abstract class Party {
    public abstract int getMonthlyCost();
}

```

- `monthlyCost` 필드는 추상화되어 있고, 실제 구현은 서브클래스에 있다고 가정합니다.
- 메서드 이름도 다릅니다. `Change Function Declaration`을 통해 이름을 통일해야 합니다.

---

### 리팩토링 후

```java
public abstract class Party {
    public abstract int getMonthlyCost();

    public int getAnnualCost() {
        return getMonthlyCost() * 12;
    }
}

public class Employee extends Party {
    private int monthlyCost;

    @Override
    public int getMonthlyCost() {
        return monthlyCost;
    }
}

public class Department extends Party {
    private int monthlyCost;

    @Override
    public int getMonthlyCost() {
        return monthlyCost;
    }
}

```

---

### 설명

| 항목 | 설명 |
| --- | --- |
| **중복 제거** | `getAnnualCost()` 메서드를 상위 클래스로 끌어올려 중복 제거 |
| **명확한 책임** | `monthlyCost`는 서브클래스에서만 제공되므로 `getMonthlyCost()`는 추상 메서드로 정의 |
| **컴파일 안전성** | Java에서는 컴파일 시 상위 클래스에 필요한 메서드가 있는지 확인 가능 |
| **확장성** | 새로운 하위 클래스가 추가되더라도 `getMonthlyCost()`만 구현하면 `getAnnualCost()`는 자동 제공 |

---

## 결론

**Pull Up Method**는 서브클래스 간 중복 메서드를 제거하고 **상속 구조를 정리**하는 매우 강력한 리팩토링입니다. 하지만 반드시:

- 중복이 실제로 있는지
- 상위 클래스에서 사용할 수 있는 참조만 있는지
- 공통 동작이 명확한지

를 확인한 뒤에 진행해야 합니다.

---

# 12.2 필드 올리기 Pull Up Field

### 배경 (Motivation)

서브클래스들이 독립적으로 개발되거나 나중에 통합(refactoring)되다 보면, **동일하거나 유사한 필드가 여러 서브클래스에 중복**될 수 있습니다.

이러한 중복 필드는 다음과 같은 문제를 유발합니다:

- 유지보수가 어려워지고,
- 관련 동작을 상위 클래스로 끌어올릴 수 없게 되며,
- 필드 명이 다르면 중복이라는 사실조차 놓치기 쉽습니다.

**Pull Up Field**는 이 필드들을 상위 클래스로 옮겨 **중복 선언 제거 + 관련 동작 통합**이 가능하게 합니다.

---

## 절차 (Mechanics)

1. 중복된 필드가 있는 서브클래스를 확인하고 **동일한 방식으로 사용되는지** 조사
2. 이름이 다르면 Rename Field로 **이름 통일**
3. 슈퍼클래스에 필드 추가 (`protected` 접근자 사용)
4. 서브클래스의 필드 제거
5. 테스트로 검증

---

## 예제

### 리팩토링 전

```java
public class Employee {
    // 상위 클래스에는 name 필드 없음
}

public class Salesman extends Employee {
    private String name;

    // name 사용
    public String getName() {
        return name;
    }
}

public class Engineer extends Employee {
    private String name;

    public String getName() {
        return name;
    }
}

```

- `Salesman`, `Engineer` 모두 `name` 필드를 사용하고 있으며, 필드명도 같고 사용 방식도 동일합니다.
- 이 경우 `name` 필드를 상위 클래스로 끌어올릴 수 있습니다.

---

### 리팩토링 후

```java
public class Employee {
    protected String name;

    public String getName() {
        return name;
    }
}

public class Salesman extends Employee {
    // name 필드 제거됨
}

public class Engineer extends Employee {
    // name 필드 제거됨
}

```

---

## 개선 설명

| 항목 | 설명 |
| --- | --- |
| **중복 제거** | 필드 선언을 하나로 통합하여 유지보수 간편 |
| **동작 통합 가능성 확보** | `getName()`과 같은 동작도 `Employee`에서 공통 제공 가능 |
| **확장성** | 새로운 서브클래스도 중복 없이 상위 클래스 필드를 바로 사용 가능 |
| **캡슐화 유지** | `protected` 접근자 사용으로 하위 클래스 접근 허용하면서도 외부 노출은 제한 |

---

## 결론

**Pull Up Field**는 중복 필드 제거와 함께 이후의 `Pull Up Method`, `Extract Superclass`, `Collapse Hierarchy` 같은 더 큰 리팩토링을 가능하게 하는 **전초 작업**입니다.

작은 리팩토링이지만, **코드 기반의 일관성과 구조적 단순화에 매우 큰 효과**를 줄 수 있습니다.

---

# 12.3 생성자 본문 올리기 Pull Up Constructor Body

### 배경 (Motivation)

생성자는 일반 메서드와는 달리:

- **반드시 첫 줄에서 `super()`를 호출해야 함**
- **초기화 순서에 제한이 있음**
- **상위 클래스 생성자 호출 전에는 `this` 접근 불가**

이런 제약 때문에 Pull Up Method처럼 간단하게 적용할 수 없습니다.

하지만 서브클래스들에 **공통된 초기화 코드**가 반복된다면, 생성자 본문을 상위 클래스로 올리는 것이 바람직합니다.

- 공통 초기화 로직을 상위 생성자로 이동하면 중복 제거
- 서브클래스는 고유 로직만 유지 가능
- 생성자가 복잡해질 경우, **Factory Method** 패턴도 고려할 수 있음

---

## 절차 (Mechanics)

1. 상위 클래스에 생성자가 없다면 생성
2. 서브클래스 생성자에서 `super()` 호출 확인
3. Slide Statements로 공통 코드를 `super()` 호출 직후로 이동
4. 공통 코드를 상위 클래스 생성자로 이동
5. 공통 코드에서 사용한 매개변수는 `super()` 호출로 전달
6. 테스트 실행
7. 공통 코드가 중간 이후에 실행되어야 한다면, Extract Method 후 Pull Up Method 적용

---

## 예제 1: 생성자 공통 코드 앞부분

### 리팩토링 전

```java
public class Party {
    // name 없음
}

public class Employee extends Party {
    private String name;
    private int id;
    private int monthlyCost;

    public Employee(String name, int id, int monthlyCost) {
        super();
        this.name = name;
        this.id = id;
        this.monthlyCost = monthlyCost;
    }
}

public class Department extends Party {
    private String name;
    private List<Employee> staff;

    public Department(String name, List<Employee> staff) {
        super();
        this.name = name;
        this.staff = staff;
    }
}

```

- `name` 필드 초기화가 두 생성자에 공통
- `this.name = name;`을 상위 생성자로 옮길 수 있음

---

### 리팩토링 후

```java
public class Party {
    protected String name;

    public Party(String name) {
        this.name = name;
    }
}

public class Employee extends Party {
    private int id;
    private int monthlyCost;

    public Employee(String name, int id, int monthlyCost) {
        super(name);
        this.id = id;
        this.monthlyCost = monthlyCost;
    }
}

public class Department extends Party {
    private List<Employee> staff;

    public Department(String name, List<Employee> staff) {
        super(name);
        this.staff = staff;
    }
}

```

---

## 예제 2: 공통 로직이 생성자 중간 이후에 실행돼야 할 경우

### 리팩토링 전

```java
public class Employee {
    private String name;

    public Employee(String name) {
        this.name = name;
    }

    protected boolean isPrivileged() {
        return false;
    }

    protected void assignCar() {
        // 차량 할당
    }
}

public class Manager extends Employee {
    private int grade;

    public Manager(String name, int grade) {
        super(name);
        this.grade = grade;

        // grade 설정 이후에만 실행 가능
        if (isPrivileged()) assignCar();
    }

    @Override
    protected boolean isPrivileged() {
        return grade > 4;
    }
}

```

- `assignCar()` 호출 조건이 서브클래스 필드 `grade`에 의존
- 직접 상위 생성자로 옮길 수 없음 → 메서드로 추출 후 Pull Up

---

### 리팩토링 후

```java
public class Employee {
    private String name;

    public Employee(String name) {
        this.name = name;
    }

    protected boolean isPrivileged() {
        return false;
    }

    protected void assignCar() {
        // 차량 할당
    }

    protected void finishConstruction() {
        if (isPrivileged()) assignCar();
    }
}

public class Manager extends Employee {
    private int grade;

    public Manager(String name, int grade) {
        super(name);
        this.grade = grade;
        finishConstruction();
    }

    @Override
    protected boolean isPrivileged() {
        return grade > 4;
    }
}

```

---

## 개선 설명

| 항목 | 설명 |
| --- | --- |
| **중복 제거** | `name` 초기화 또는 post-construction 로직을 상위로 이동해 코드 간결화 |
| **의도 명확화** | 공통 로직을 상위로 올려 **설계 구조를 명확히 표현** |
| **확장성 향상** | 새로운 서브클래스에서도 중복 없이 동일 로직 재사용 가능 |
| **불변성 보장** | 상위 생성자가 초기화 순서를 제어해 안정성 향상 |

---

## 결론

**Pull Up Constructor Body**는 생성자의 공통 초기화 로직을 상위로 이동하여 **중복 제거 + 구조적 안정성 향상**을 동시에 달성하는 기법입니다.

단, 생성자 특성상 **초기화 순서나 필드 접근 제약**을 고려해야 하므로 **꼼꼼한 사전 확인이 필수**입니다.

# 12.4 메서드 내리기 Push Down Method

### 배경 (Motivation)

상속 구조에서 공통 동작을 상위 클래스에 두는 것은 자연스러운 설계입니다.

하지만 다음과 같은 경우에는 오히려 그 반대가 필요합니다:

- 특정 메서드가 일부 하위 클래스에서만 의미가 있음
- 다른 하위 클래스에서는 해당 메서드를 사용하지 않거나, 오히려 혼란을 줌
- 호출자가 특정 하위 클래스임을 알고 있는 상황

이런 경우, 해당 메서드를 상위 클래스에 남겨두면:

- **상위 클래스의 책임이 불분명**해지고
- **불필요한 추상화**로 인해 코드 이해가 어려워집니다

따라서, **Push Down Method**를 통해 **책임을 명확히 하고, 클래스 응집도를 높일 수 있습니다.**

※ 단, 호출자가 어떤 하위 클래스인지 모르는 경우에는 이 리팩토링이 부적절하며, 이 경우는 **Replace Conditional with Polymorphism**을 사용하는 것이 적절합니다.

---

## 절차 (Mechanics)

1. 하위 클래스 중 어떤 클래스가 해당 메서드를 사용하는지 파악
2. 해당 메서드를 필요한 하위 클래스에 복사
3. 상위 클래스에서 메서드 제거
4. 테스트
5. 불필요한 상위 클래스에서 메서드 선언(추상 메서드 등)도 제거
6. 다시 테스트

---

## 예제

### 리팩토링 전

```java
public abstract class Employee {
    protected String name;

    public Employee(String name) {
        this.name = name;
    }

    public String getQuotaReport() {
        return "Quota not applicable";
    }
}

public class Engineer extends Employee {
    public Engineer(String name) {
        super(name);
    }
}

public class Salesman extends Employee {
    public Salesman(String name) {
        super(name);
    }

    @Override
    public String getQuotaReport() {
        return "Sales quota met";
    }
}

```

- `getQuotaReport()`는 `Salesman`에게만 의미 있음
- `Engineer`에서는 의미 없는 디폴트 메시지만 반환

---

### 리팩토링 후

```java
public abstract class Employee {
    protected String name;

    public Employee(String name) {
        this.name = name;
    }

    // getQuotaReport 제거됨
}

public class Engineer extends Employee {
    public Engineer(String name) {
        super(name);
    }
}

public class Salesman extends Employee {
    public Salesman(String name) {
        super(name);
    }

    public String getQuotaReport() {
        return "Sales quota met";
    }
}

```

---

## 개선 설명

| 항목 | 설명 |
| --- | --- |
| **응집도 향상** | `Salesman`만 사용하는 메서드는 `Salesman`에 위치해야 응집도가 높아짐 |
| **불필요한 추상화 제거** | `Engineer` 등 다른 하위 클래스에 무의미한 메서드 제거로 코드 간결 |
| **유지보수성 향상** | 각 클래스가 자기 책임에 집중하도록 구조 개선 |
| **개방 폐쇄 원칙 적용** | 상위 클래스가 더 이상 하위 클래스의 구체 동작에 간섭하지 않음 |

---

## 결론

**Push Down Method**는 "이 기능이 모든 자식에게 공통되는 것이 맞는가?"를 되묻고,

그렇지 않다면 **역방향으로 책임을 내려보내는** 리팩토링입니다.

작은 변화처럼 보일 수 있지만, 이 기법을 통해 **클래스 계층의 구조적 명확성**을 확보하고

**유지보수성, 재사용성, 설계 명확성** 모두를 향상시킬 수 있습니다.

# 12.5 필드 내리기 Push Down Field

### 배경 (Motivation)

상위 클래스에 있는 필드가 실제로는 전체 하위 클래스에서 공통적으로 사용되지 않고,

일부 하위 클래스에서만 사용된다면:

- 해당 필드를 **상위 클래스에 남겨두는 것은 부적절한 추상화**입니다.
- 오히려 **혼란을 야기하고 유지보수성**을 떨어뜨립니다.

이 경우, 그 필드를 **필요한 하위 클래스에만 선언**하고, 상위 클래스에서 제거하는 것이 더 적절합니다.

---

## 절차 (Mechanics)

1. 필드를 사용하는 모든 하위 클래스 파악
2. 그 하위 클래스들에 **필드를 선언**하고,
3. 상위 클래스에서 **필드 제거**
4. 테스트 실행
5. 필요 없는 하위 클래스에 남은 필드가 있다면 제거
6. 다시 테스트

---

## 예제

### 리팩토링 전

```java
public abstract class Employee {
    protected int salesQuota; // 모든 하위 클래스에 필요하지 않음
}

public class Engineer extends Employee {
    public void develop() {
        // salesQuota 사용하지 않음
    }
}

public class Salesman extends Employee {
    public boolean isQuotaMet(int sales) {
        return sales >= salesQuota;
    }
}

```

- `salesQuota`는 `Salesman`만 사용하는 필드
- `Engineer`는 해당 필드를 전혀 사용하지 않음

---

### 리팩토링 후

```java
public abstract class Employee {
    // salesQuota 제거됨
}

public class Engineer extends Employee {
    public void develop() {
        // 여전히 salesQuota 사용하지 않음
    }
}

public class Salesman extends Employee {
    private int salesQuota;

    public boolean isQuotaMet(int sales) {
        return sales >= salesQuota;
    }
}

```

---

## 개선 설명

| 항목 | 설명 |
| --- | --- |
| **불필요한 필드 제거** | 상위 클래스가 더 이상 필요하지 않은 필드를 갖지 않음 |
| **의도 명확화** | 해당 필드가 어떤 하위 클래스의 전용인지 명확하게 드러남 |
| **클래스 응집도 향상** | 각 클래스는 자신에게 필요한 정보만 포함 |
| **설계 개선** | 상위 클래스가 오직 공통 요소만 보유 → 추상화의 질 향상 |

---

## 결론

**Push Down Field**는 작지만 의미 있는 리팩토링입니다.

불필요한 추상화를 제거하고, 각 클래스가 자신의 목적에 맞는 데이터만 유지하도록 정리함으로써

**코드베이스의 명확성, 유지보수성, 설계 일관성**을 높이는 데 기여합니다.

# 12.6 타입 코드를 서브클래스로 바꾸기 Replace Type Code with Subclasses

### 배경 (Motivation)

프로그램에서 동일한 개념의 여러 종류를 다룰 때, **초기에는 단순히 `type` 필드**로 구분합니다.

예:

- 직원 종류: `"engineer"`, `"manager"`, `"salesman"`
- 주문 우선순위: `"rush"`, `"regular"`

처음에는 문제가 없지만, 다음과 같은 문제가 생기기 시작하면 **서브클래스가 더 적절한 모델**이 됩니다:

1. **동작이 타입에 따라 다르게 분기**됨

   → `if-else`, `switch-case`가 자꾸 늘어남

2. **특정 타입에서만 의미 있는 필드/메서드**가 생김

   → 타입 확인 + 예외 처리 등으로 코드 복잡도 증가


이때 서브클래스를 만들고, **다형성으로 조건문을 대체**하면 코드가 더 명확하고 확장 가능해집니다.

---

## 절차 (Mechanics)

### 직접 상속(direct inheritance) 방식

1. `type` 필드를 **Getter로 캡슐화**
2. 타입 값 하나를 골라, 해당 타입을 위한 **서브클래스 생성** 및 `getType()` 오버라이딩
3. **팩토리 메서드**를 만들어, `type`에 따라 해당 서브클래스를 생성하도록 분기
4. 테스트 → 다른 타입도 위와 같이 반복
5. 모든 타입 서브클래스를 만든 뒤, 상위 클래스에서 `type` 필드 제거
6. `type`을 기반으로 한 조건 분기 메서드를 **Replace Conditional with Polymorphism**으로 리팩토링
7. 불필요해진 `getType()` 제거

---

## 예제: 직접 상속 방식 (Direct Inheritance)

### 리팩토링 전

```java
public class Employee {
    private String name;
    private String type;

    public Employee(String name, String type) {
        validateType(type);
        this.name = name;
        this.type = type;
    }

    private void validateType(String type) {
        if (!type.equals("engineer") && !type.equals("manager") && !type.equals("salesman")) {
            throw new IllegalArgumentException("Invalid type: " + type);
        }
    }

    public String getType() {
        return type;
    }

    public String toString() {
        return name + " (" + getType() + ")";
    }
}

```

### 리팩토링 후

```java
public abstract class Employee {
    protected String name;

    public Employee(String name) {
        this.name = name;
    }

    public abstract String getType();

    @Override
    public String toString() {
        return name + " (" + getType() + ")";
    }

    public static Employee create(String name, String type) {
        return switch (type) {
            case "engineer" -> new Engineer(name);
            case "manager" -> new Manager(name);
            case "salesman" -> new Salesman(name);
            default -> throw new IllegalArgumentException("Invalid type: " + type);
        };
    }
}

public class Engineer extends Employee {
    public Engineer(String name) {
        super(name);
    }

    @Override
    public String getType() {
        return "engineer";
    }
}

public class Manager extends Employee {
    public Manager(String name) {
        super(name);
    }

    @Override
    public String getType() {
        return "manager";
    }
}

public class Salesman extends Employee {
    public Salesman(String name) {
        super(name);
    }

    @Override
    public String getType() {
        return "salesman";
    }
}

```

---

## 예제: 간접 상속 방식 (Indirect Inheritance)

직원이 **정규직/계약직**으로 나뉘는 구조도 있고, 그 안에 **type code**가 또 있는 경우, 직접 상속이 곤란할 수 있습니다. 이럴 때는 **type을 객체로 캡슐화하고, 그 객체를 서브클래싱**합니다.

### 핵심 구조

```java
public class Employee {
    private String name;
    private EmployeeType type;

    public Employee(String name, String typeCode) {
        this.name = name;
        this.type = EmployeeType.create(typeCode);
    }

    public String getType() {
        return type.toString();
    }

    public String toString() {
        return name + " (" + type.getCapitalizedName() + ")";
    }
}

```

### 타입 계층

```java
public abstract class EmployeeType {
    public abstract String toString();

    public String getCapitalizedName() {
        String raw = toString();
        return raw.substring(0, 1).toUpperCase() + raw.substring(1).toLowerCase();
    }

    public static EmployeeType create(String code) {
        return switch (code) {
            case "engineer" -> new EngineerType();
            case "manager" -> new ManagerType();
            case "salesman" -> new SalesmanType();
            default -> throw new IllegalArgumentException("Invalid type: " + code);
        };
    }
}

public class EngineerType extends EmployeeType {
    public String toString() {
        return "engineer";
    }
}

public class ManagerType extends EmployeeType {
    public String toString() {
        return "manager";
    }
}

public class SalesmanType extends EmployeeType {
    public String toString() {
        return "salesman";
    }
}

```

---

## 개선 설명

| 항목 | 설명 |
| --- | --- |
| **조건문 제거** | `if(type == "...")` 대신 다형성으로 분기 처리 |
| **필드 의미 명확화** | 특정 동작이 어떤 타입에 속하는지 구조적으로 표현 |
| **확장 용이** | 새로운 타입이 추가되더라도 switch-case 수정 없이 서브클래스만 추가하면 됨 |
| **기능 분리** | type 관련 기능은 EmployeeType 클래스에 집중 → 단일 책임 원칙 준수 |

---

## 결론

**Replace Type Code with Subclasses**는 "타입 값으로 분기하는 코드가 복잡해지고 있을 때",

다형성을 도입하여 **설계 구조를 명확하게 정리하는 리팩토링**입니다.

- 직접 상속 방식은 단순하고 깔끔하지만 type이 mutable한 경우엔 불가능
- 간접 상속 방식은 유연하지만 더 복잡한 설계가 필요

이 리팩토링을 통해 조건문 중심의 코드에서 벗어나 **확장 가능하고 유지보수가 쉬운 객체지향 설계**로 발전할 수 있습니다.

# 12.7 서브클래스 제거하기 Remove Subclass

### 배경 (Motivation)

서브클래스는 다음과 같은 경우에 유용합니다:

- 타입별로 **동작이 다를 때** (→ 다형성)
- 타입별로 **구조(필드)가 다를 때**

하지만 다음과 같은 경우엔 오히려 제거하는 것이 낫습니다:

- **서브클래스가 하는 일이 거의 없음**
- 단지 한두 개 필드나 메서드만 다름
- 조건 분기가 사라지고 동작이 통합됨
- 예전에는 필요했지만, 현재는 **불필요한 복잡성만 유발**

이럴 때는 **하위 클래스를 제거하고**, 차이를 **필드 값**으로 표현하는 것이 좋습니다.

---

## 절차 (Mechanics)

1. **Replace Constructor with Factory Function**으로 생성자 감싸기
2. 생성자 선택 로직을 팩토리 메서드로 이동
3. `instanceof`, 타입 확인 등의 조건 분기 로직이 있다면:
    - `Extract Function` 후
    - `Move Function`으로 상위 클래스로 이동
4. 서브클래스 차이를 나타낼 **필드 추가**
5. 메서드나 로직을 이 필드를 기준으로 동작하도록 변경
6. 서브클래스 삭제
7. 테스트

---

## 예제: Java로 변환

### 리팩토링 전

```java
public class Person {
    protected String name;

    public Person(String name) {
        this.name = name;
    }

    public String getGenderCode() {
        return "X";
    }
}

public class Male extends Person {
    public Male(String name) {
        super(name);
    }

    @Override
    public String getGenderCode() {
        return "M";
    }
}

public class Female extends Person {
    public Female(String name) {
        super(name);
    }

    @Override
    public String getGenderCode() {
        return "F";
    }
}

```

```java
// client code
long numberOfMales = people.stream()
    .filter(p -> p instanceof Male)
    .count();

```

---

### 리팩토링 후

### Step 1: `Person`에 `genderCode` 필드 추가

```java
public class Person {
    private String name;
    private String genderCode;

    public Person(String name, String genderCode) {
        this.name = name;
        this.genderCode = genderCode != null ? genderCode : "X";
    }

    public String getGenderCode() {
        return genderCode;
    }

    public boolean isMale() {
        return "M".equals(genderCode);
    }

    public boolean isFemale() {
        return "F".equals(genderCode);
    }
}

```

### Step 2: 팩토리 메서드로 생성 로직 이동

```java
public class PersonFactory {
    public static Person createPerson(String name, String genderCode) {
        if ("M".equals(genderCode)) return new Person(name, "M");
        if ("F".equals(genderCode)) return new Person(name, "F");
        return new Person(name, "X");
    }
}

```

### Step 3: 클라이언트 코드 변경

```java
long numberOfMales = people.stream()
    .filter(Person::isMale)
    .count();

```

### Step 4: `Male`, `Female` 클래스 제거

```java
// 삭제됨
// public class Male extends Person { ... }
// public class Female extends Person { ... }

```

---

## 개선 설명

| 항목 | 설명 |
| --- | --- |
| **클래스 수 감소** | 의미 없는 하위 클래스를 제거해 구조 단순화 |
| **유지보수성 향상** | 이름 짓기, 파일 분리, 인스턴스 검사 등의 부담 해소 |
| **팩토리로 생성 책임 집중** | 외부에서 서브클래스를 직접 생성하지 않음 |
| **조건 분기 일관화** | `instanceof` → `genderCode`로 간단히 확인 |
| **확장 가능성 유지** | "X" 같은 제3의 값도 무리 없이 표현 가능 |

---

## 결론

**Remove Subclass**는 서브클래싱이 설계상 더 이상 타당하지 않을 때,

**필드 기반 표현**으로 설계를 단순화하고, 유지보수를 쉽게 만드는 강력한 리팩토링입니다.

서브클래스를 제거하기 전에 반드시 다음을 점검하세요:

- 서브클래스에 **실질적인 동작 차이**가 있는가?
- 조건 분기 없이도 해당 기능을 필드 기반으로 처리할 수 있는가?

이 기준을 만족하면, 이 리팩토링은 **설계 복잡도를 급격히 낮추는 효과**를 줍니다.

# **12.8** 슈퍼클래스 추출하기 Extract Superclass

### 배경 (Motivation)

상속은 "미리 설계"해야만 하는 기능이 아닙니다.

많은 경우, 프로그램을 개발해나가면서 중복된 필드와 메서드가 보일 때 비로소 "상속이 필요하구나"라는 사실을 깨닫게 됩니다.

**Extract Superclass**는 다음과 같은 경우에 적합합니다:

- 서로 관련이 있지만 독립적으로 설계된 두 클래스가 있고
- 둘 사이에 **구조적/행위적 중복**이 존재하며
- **공통 슈퍼클래스가 없거나** 이미 있는 슈퍼클래스를 수정할 수 없는 경우

> 💡 상속이 맞는지 확신이 없다면 먼저 Extract Superclass로 구조를 단순화하고, 나중에 필요하면 Replace Superclass with Delegate로 위임으로 전환할 수 있습니다.
>

---

## 절차 (Mechanics)

1. 공통의 **빈 슈퍼클래스** 생성
2. 두 클래스를 해당 슈퍼클래스를 **상속하도록 변경**
3. **필드**를 슈퍼클래스로 이동 (Pull Up Field)
4. 관련 메서드들도 이동 (Pull Up Method)
5. 생성자 본문 공통 로직은 Pull Up Constructor Body로 이동
6. 남은 메서드들 중 **부분적으로만 공통된 부분**은:
    - Extract Method → Pull Up Method로 적용
7. 클라이언트 코드가 있다면 가능한 한 **슈퍼클래스를 사용하도록 조정**

---

## 예제: Java 코드로 리팩토링 적용

### 리팩토링 전

```java
public class Employee {
    private String name;
    private int id;
    private int monthlyCost;

    public Employee(String name, int id, int monthlyCost) {
        this.name = name;
        this.id = id;
        this.monthlyCost = monthlyCost;
    }

    public int getMonthlyCost() {
        return monthlyCost;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getAnnualCost() {
        return monthlyCost * 12;
    }
}

public class Department {
    private String name;
    private List<Employee> staff;

    public Department(String name, List<Employee> staff) {
        this.name = name;
        this.staff = new ArrayList<>(staff);
    }

    public List<Employee> getStaff() {
        return new ArrayList<>(staff);
    }

    public String getName() {
        return name;
    }

    public int getTotalMonthlyCost() {
        return staff.stream()
                    .mapToInt(Employee::getMonthlyCost)
                    .sum();
    }

    public int getHeadCount() {
        return staff.size();
    }

    public int getTotalAnnualCost() {
        return getTotalMonthlyCost() * 12;
    }
}

```

---

### Step 1: 공통 슈퍼클래스 `Party` 생성

```java
public abstract class Party {
    protected String name;

    public Party(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract int getMonthlyCost();

    public int getAnnualCost() {
        return getMonthlyCost() * 12;
    }
}

```

---

### Step 2: `Employee`, `Department`가 `Party`를 상속하도록 변경

```java
public class Employee extends Party {
    private int id;
    private int monthlyCost;

    public Employee(String name, int id, int monthlyCost) {
        super(name);
        this.id = id;
        this.monthlyCost = monthlyCost;
    }

    public int getId() {
        return id;
    }

    @Override
    public int getMonthlyCost() {
        return monthlyCost;
    }
}

```

```java
public class Department extends Party {
    private List<Employee> staff;

    public Department(String name, List<Employee> staff) {
        super(name);
        this.staff = new ArrayList<>(staff);
    }

    public List<Employee> getStaff() {
        return new ArrayList<>(staff);
    }

    public int getHeadCount() {
        return staff.size();
    }

    @Override
    public int getMonthlyCost() {
        return staff.stream()
                    .mapToInt(Employee::getMonthlyCost)
                    .sum();
    }
}

```

---

## 개선 설명

| 항목 | 설명 |
| --- | --- |
| **중복 제거** | `name`, `monthlyCost`, `annualCost` 등의 중복된 구조와 로직을 상위 클래스로 이동 |
| **공통 인터페이스 도입** | `Party`라는 추상 클래스를 통해 `Employee`, `Department`를 동일하게 처리 가능 |
| **클라이언트 코드 단순화** | 다형성을 통해 `List<Party>` 단일 리스트로 처리 가능 |
| **확장성 향상** | 새로운 `Party` 하위 타입 추가 시 구조적 일관성 유지 |

---

## 결론

**Extract Superclass**는 객체지향 설계에서 중복 제거와 계층화 구조 개선에 매우 효과적인 리팩토링입니다.

- 클래스 간 유사성이 발견되었을 때
- 기능 분리를 통해 재사용성과 일관성을 확보하고 싶을 때
- 이후 더 정교한 리팩토링(예: Replace Superclass with Delegate)을 고려할 수 있도록 기반을 다지고 싶을 때

이 리팩토링을 통해 **중복을 줄이고, 인터페이스를 통일하며, 구조를 명확히** 할 수 있습니다.

# **12.9** 계층 **합치기 Collapse Hierarchy

### 배경 (Motivation)

상속은 **차이를 관리**하기 위해 사용됩니다. 하지만 다음과 같은 상황에서는 오히려 불필요한 복잡성이 됩니다:

- 상위 클래스와 하위 클래스의 **구현 차이가 거의 없음**
- 원래 존재하던 차이가 **리팩토링 과정에서 사라짐**
- 설계가 단순해졌거나 **기능이 축소**되어 분리된 구조가 더 이상 유효하지 않음

이 경우, **계층을 합치는 것이 설계의 명료성과 유지보수 측면에서 더 좋습니다.**

---

## 절차 (Mechanics)

1. **남길 클래스 결정**
    - 이름이 더 적절한 쪽을 남기거나, 둘 다 애매하면 임의 선택
2. Pull Up / Push Down을 사용해 모든 **필드와 메서드 통합**
3. 삭제할 클래스를 참조하던 코드를 **남긴 클래스로 수정**
4. 비워진 클래스를 **삭제**
5. **테스트**

---

## 예제

### 리팩토링 전

```java
public class Party {
    protected String name;

    public Party(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

public class Department extends Party {
    private int budget;

    public Department(String name, int budget) {
        super(name);
        this.budget = budget;
    }

    public int getBudget() {
        return budget;
    }
}

```

- `Department`가 `Party` 외에 `budget` 필드 하나만 추가하고 있음
- `Party`에는 별도 로직 없음 → 이럴 바엔 두 클래스를 **합치는 게 더 간단함**

---

### 리팩토링 후

```java
public class Department {
    private String name;
    private int budget;

    public Department(String name, int budget) {
        this.name = name;
        this.budget = budget;
    }

    public String getName() {
        return name;
    }

    public int getBudget() {
        return budget;
    }
}

```

- `Party`의 `name` 필드와 `getName()` 메서드를 `Department`로 이동
- `Department`가 `Party`를 상속하지 않고, 모든 내용을 직접 갖도록 통합
- `Party` 클래스는 삭제

---

## 개선 설명

| 항목 | 설명 |
| --- | --- |
| **구조 단순화** | 쓸모없는 추상화 제거로 클래스 수 감소 |
| **이해도 향상** | 계층 구조가 단순해져 코드 흐름 파악 쉬움 |
| **유지보수성 향상** | 한 클래스에서 모든 필드와 동작을 직접 관리 |
| **불필요한 유연성 제거** | 실제 필요하지 않은 다형성 구조 제거로 코드 안정성 증가 |

---

## 결론

**Collapse Hierarchy**는 계층 구조가 과도하게 복잡해졌거나, 더 이상 의미 있는 분리가 없는 경우에

**간단하고 명확한 구조로 되돌리는 유용한 리팩토링**입니다.

이 리팩토링을 적용하기 전에 꼭 고려해야 할 점은:

- 해당 구조가 정말 제거해도 되는지?
- 앞으로의 변경이 이 구조를 필요로 하게 될 가능성은 없는지?

입니다. 확실하다면 이 리팩토링은 **코드 이해도와 단순성 측면에서 매우 강력한 도구**가 될 수 있습니다.

# **12.10** 서브클래스를 **위임으로 **바꾸기  Replace Subclass with Delegate

이 기법은 **상속이 주는 제한과 결합도를 해결하기 위해**, **다형성을 위임(composition)**으로 전환하여 더 유연한 설계를 만드는 강력한 도구입니다.

---

## Replace Subclass with Delegate

### 배경 (Motivation)

상속은 객체지향의 강력한 도구이지만, 다음과 같은 한계가 있습니다:

- **단일 축(단일 기준)만 다룰 수 있음**
    - 예: 사람을 *나이*로 나누거나 *소득 수준*으로 나눌 수는 있어도, 둘 다 동시에 하려면 복잡해짐
- **상위 클래스 변경이 하위 클래스에 영향을 줌**
    - 긴밀한 결합도가 높아지며 유지보수 리스크 증가
- **런타임에 하위 클래스를 바꾸기 어려움**
    - 예: 기존 객체가 PremiumBooking → 일반 Booking으로 변할 수 없음

**위임(Delegation)은 이러한 문제를 해결합니다.**

- 여러 이유에 따라 **행위를 바꾸고 싶을 때** 적합
- **동적으로 전략 교체 가능**
- **결합도 낮음**: 단지 다른 객체에게 책임을 넘길 뿐

---

## 절차 (Mechanics)

1. 서브클래스 생성자 호출이 많으면 먼저 **Factory Function**으로 감싼다
2. 새로운 위임 클래스(Delegate) 생성
    - 필요한 파라미터 및 상위 클래스에 대한 백참조 포함
3. 슈퍼클래스에 delegate 필드 추가
4. 프리미엄 생성 로직에서 delegate를 초기화하도록 수정
5. 서브클래스 메서드들을 하나씩 delegate로 이동 (Move Function)
    - `this._host`로 상위 클래스 접근
6. `super` 호출이 필요하면 `Extract Method` 또는 `Extension Method 패턴` 적용
7. 서브클래스에서 로직 제거, 슈퍼클래스에서 delegate 분기 추가
8. 서브클래스 삭제

---

## 예제

### 리팩토링 전

```java
public class Booking {
    protected Show show;
    protected LocalDate date;

    public Booking(Show show, LocalDate date) {
        this.show = show;
        this.date = date;
    }

    public boolean hasTalkback() {
        return show.hasTalkback() && !isPeakDay();
    }

    public int basePrice() {
        int price = show.getPrice();
        if (isPeakDay()) price += price * 0.15;
        return price;
    }

    public boolean isPeakDay() {
        return date.getDayOfWeek().getValue() >= 5;
    }
}

public class PremiumBooking extends Booking {
    private Extras extras;

    public PremiumBooking(Show show, LocalDate date, Extras extras) {
        super(show, date);
        this.extras = extras;
    }

    @Override
    public boolean hasTalkback() {
        return show.hasTalkback();
    }

    @Override
    public int basePrice() {
        return super.basePrice() + extras.getPremiumFee();
    }

    public boolean hasDinner() {
        return extras.hasDinner() && !isPeakDay();
    }
}

```

---

### 리팩토링 후

### Step 1: Delegate 클래스 생성

```java
public class PremiumBookingDelegate {
    private final Booking host;
    private final Extras extras;

    public PremiumBookingDelegate(Booking host, Extras extras) {
        this.host = host;
        this.extras = extras;
    }

    public boolean hasTalkback() {
        return host.getShow().hasTalkback();
    }

    public int extendBasePrice(int base) {
        return base + extras.getPremiumFee();
    }

    public boolean hasDinner() {
        return extras.hasDinner() && !host.isPeakDay();
    }
}

```

### Step 2: Booking 클래스에 delegate 적용

```java
public class Booking {
    protected Show show;
    protected LocalDate date;
    protected PremiumBookingDelegate premiumDelegate;

    public Booking(Show show, LocalDate date) {
        this.show = show;
        this.date = date;
    }

    public void becomePremium(Extras extras) {
        this.premiumDelegate = new PremiumBookingDelegate(this, extras);
    }

    public boolean hasTalkback() {
        if (premiumDelegate != null) {
            return premiumDelegate.hasTalkback();
        }
        return show.hasTalkback() && !isPeakDay();
    }

    public int basePrice() {
        int base = show.getPrice();
        if (isPeakDay()) base += base * 0.15;
        return (premiumDelegate != null) ? premiumDelegate.extendBasePrice(base) : base;
    }

    public boolean hasDinner() {
        return (premiumDelegate != null) ? premiumDelegate.hasDinner() : false;
    }

    public boolean isPeakDay() {
        return date.getDayOfWeek().getValue() >= 5;
    }

    public Show getShow() {
        return show;
    }
}

```

### Step 3: 생성 팩토리로 전환

```java
public class BookingFactory {
    public static Booking createBooking(Show show, LocalDate date) {
        return new Booking(show, date);
    }

    public static Booking createPremiumBooking(Show show, LocalDate date, Extras extras) {
        Booking booking = new Booking(show, date);
        booking.becomePremium(extras);
        return booking;
    }
}

```

---

## 장점과 단점

### 장점

| 항목 | 설명 |
| --- | --- |
| **다중 변이 가능** | 나이, 등급, 지역 등 여러 이유로 분리 가능 |
| **동적 상태 전환 가능** | `becomePremium()` 같이 런타임 변경 가능 |
| **결합도 낮음** | 슈퍼/서브 클래스간 끈끈한 관계 제거 |
| **유지보수성 향상** | 모듈 간 분리가 쉬움 (ex: delegate만 따로 개발 가능) |

### 단점

| 항목 | 설명 |
| --- | --- |
| **구조가 복잡해짐** | 위임 로직, 백참조, 분기문이 필요함 |
| **단순한 경우 오히려 과한 설계** | 상속이 더 적절한 경우도 있음 |
| **테스트 복잡도 증가 가능성** | delegate, host 객체 간의 상태 조정 필요 |

---

## 결론

**Replace Subclass with Delegate**는 다음과 같은 상황에서 유효합니다:

- **상속의 유연성 한계를 넘고 싶을 때**
- **동적 상태 변화가 필요할 때**
- **상속 구조를 리팩토링할 여지가 없을 때**
- **서브클래스가 많아지고, 서로 충돌하거나 중복이 발생할 때**

즉, 단순한 상속을 **다형성 구조 + 위임으로 확장**하는 방식으로,
구조의 유연성과 변화 대응력을 높이는 고급 리팩토링입니다.

# **12.11** 슈퍼클래스를 **위임으로 **바꾸기 Replace Superclass with Delegate

좋습니다. 이제 마지막 리팩토링 기법인 **12.11 – Replace Superclass with Delegate (슈퍼클래스를 위임으로 바꾸기)** 를 정리해 드리겠습니다.

이 리팩토링은 구조를 단순하게 하면서도 **상속으로 인한 과도한 결합**을 해소할 수 있는 강력한 전략입니다.

---

## 12.11 Replace Superclass with Delegate

**(슈퍼클래스를 위임으로 바꾸기)**

---

### 배경 (Motivation)

상속은 기능 재사용을 위해 직관적인 도구입니다. 하지만 잘못 사용되면 오히려 **모델링 오류**, **불필요한 결합**, **과도한 인터페이스 노출**로 이어질 수 있습니다.

### 대표적인 잘못된 사례:

- **Stack extends List**: List의 모든 메서드가 Stack에도 노출됨 → Stack의 의도와 어긋남
- **Scroll extends CatalogItem**: 실제 개별 물리 객체(Scroll)와 메타데이터(CatalogItem)를 혼동

이런 경우에는 상속보다 **위임(Delegation)**을 사용하는 것이 훨씬 더 명확한 모델링 방법입니다.

---

### 절차 (Mechanics)

1. **슈퍼클래스를 참조하는 필드**를 서브클래스에 추가
2. 서브클래스에서 사용하는 **슈퍼클래스의 메서드들을 forwarding 메서드**로 복제
3. 테스트
4. **상속 관계 제거**
5. (선택) 같은 슈퍼클래스를 여러 객체가 공유하도록 설계 변경
    - `Change Value to Reference` 적용

---

## 예제

### 리팩토링 전

```java
public class CatalogItem {
    private final int id;
    private final String title;
    private final List<String> tags;

    public CatalogItem(int id, String title, List<String> tags) {
        this.id = id;
        this.title = title;
        this.tags = tags;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public boolean hasTag(String tag) { return tags.contains(tag); }
}

public class Scroll extends CatalogItem {
    private final LocalDate lastCleaned;

    public Scroll(int id, String title, List<String> tags, LocalDate lastCleaned) {
        super(id, title, tags);
        this.lastCleaned = lastCleaned;
    }

    public boolean needsCleaning(LocalDate today) {
        long days = ChronoUnit.DAYS.between(lastCleaned, today);
        int threshold = hasTag("revered") ? 700 : 1500;
        return days > threshold;
    }
}

```

---

### 리팩토링 후

### Step 1: CatalogItem을 필드로 위임

```java
public class Scroll {
    private final int id;
    private final CatalogItem catalogItem;
    private final LocalDate lastCleaned;

    public Scroll(int id, CatalogItem catalogItem, LocalDate lastCleaned) {
        this.id = id;
        this.catalogItem = catalogItem;
        this.lastCleaned = lastCleaned;
    }

    public int getId() { return id; }
    public String getTitle() { return catalogItem.getTitle(); }
    public boolean hasTag(String tag) { return catalogItem.hasTag(tag); }

    public boolean needsCleaning(LocalDate today) {
        long days = ChronoUnit.DAYS.between(lastCleaned, today);
        int threshold = hasTag("revered") ? 700 : 1500;
        return days > threshold;
    }
}

```

---

### Step 2: 여러 Scroll이 같은 CatalogItem을 참조하도록 개선

→ `Change Value to Reference` 적용

```java
// 리포지토리 예시
public class CatalogRepository {
    private final Map<Integer, CatalogItem> catalogMap = new HashMap<>();

    public CatalogItem get(int id) {
        return catalogMap.get(id);
    }
}

```

```java
// 데이터 로드 시
List<Scroll> scrolls = documents.stream()
    .map(doc -> new Scroll(
        doc.getId(),
        catalogRepo.get(doc.getCatalogItemId()),
        doc.getLastCleanedDate()
    ))
    .collect(Collectors.toList());

```

---

## 요약 비교표

| 항목 | 상속 (Before) | 위임 (After) |
| --- | --- | --- |
| 구조 | `Scroll extends CatalogItem` | `Scroll → CatalogItem 위임 필드` |
| 재사용 방식 | 자동 메서드 상속 | 명시적 forwarding |
| 결합도 | 높음 | 낮음 |
| 모델링 정확도 | **물리 객체 vs 메타데이터 혼동** | 명확한 책임 분리 |
| 동작 변경 유연성 | 낮음 | 높음 (동적 교체 가능) |

---

## 언제 적용하나?

- 서브클래스가 **슈퍼클래스의 일부 기능만 필요**로 할 때
- 서브클래스가 **실제로는 다른 의미**를 갖고 있을 때 (type-instance 혼동)
- **여러 서브클래스가 공유 가능한 데이터**를 슈퍼클래스가 담고 있을 때
- **상속 구조의 변경이 자주 필요한 경우** (유지보수 어려움)

---

## 결론

> 상속보다 위임이 좋은 이유는 명확한 책임 분리와 결합도 감소입니다.
>
- 상속은 "is-a" 관계가 맞을 때만 사용해야 하며,
- 위임은 "has-a", 또는 "uses-a" 상황에서 더 강력한 대안이 될 수 있습니다.

**Replace Superclass with Delegate**는 객체지향 설계를 리팩토링하는 데 있어 **가장 깊이 있는 리팩토링 중 하나**이며,

이 기법을 잘 이해하면 더 유연하고 유지보수가 쉬운 시스템을 만들 수 있습니다.

---

# 📘 Chapter 12 전체 요약표

| 리팩토링 이름 | 설명 | 주 사용 목적 | 역 리팩토링 |
| --- | --- | --- | --- |
| **12.1 Pull Up Method** | 중복 메서드를 슈퍼클래스로 이동 | 코드 중복 제거 | Push Down Method |
| **12.2 Pull Up Field** | 중복 필드를 슈퍼클래스로 이동 | 상태 중복 제거 | Push Down Field |
| **12.3 Pull Up Constructor Body** | 생성자의 공통 코드 상위로 이동 | 중복 초기화 정리 | - |
| **12.4 Push Down Method** | 특정 서브클래스만 사용하는 메서드를 하위로 이동 | 책임 명확화 | Pull Up Method |
| **12.5 Push Down Field** | 특정 서브클래스만 사용하는 필드를 하위로 이동 | 책임 명확화 | Pull Up Field |
| **12.6 Replace Type Code with Subclasses** | 타입 코드 분기를 서브클래스로 바꿈 | 다형성 적용 | Remove Subclass |
| **12.7 Remove Subclass** | 의미 없는 서브클래스를 필드로 통합 | 구조 단순화 | Replace Type Code with Subclasses |
| **12.8 Extract Superclass** | 중복 로직을 상위 클래스로 추출 | 중복 제거 및 일반화 | Replace Superclass with Delegate |
| **12.9 Collapse Hierarchy** | 상위/하위 클래스 간 차이가 없으면 합침 | 구조 단순화 | Extract Superclass |
| **12.10 Replace Subclass with Delegate** | 서브클래스 기능을 위임 객체로 이전 | 유연성 확보, 다중 전략 | - |
| **12.11 Replace Superclass with Delegate** | 슈퍼클래스 기능을 위임 객체로 이전 | 결합도 낮추기, 더 나은 모델링 | Extract Superclass |

---

# ⚖️ 각 기법 간 비교표

| 구분 | Pull/Push | Type Code 처리 | 계층 구조 정리 | 위임 도입 |
| --- | --- | --- | --- | --- |
| **중복 제거** | ✅ Pull Up Method, Field, Constructor Body | 🔸 일부 (추출 조건 완화에 활용) | 🔸 Collapse Hierarchy | 🔸 Replace Subclass with Delegate |
| **책임 분리** | ✅ Push Down Method, Field | 🔸 Replace Type Code with Subclasses | 🔸 Extract Superclass | ✅ Delegate 계열 2종 |
| **다형성 적용** | ❌ | ✅ Replace Type Code with Subclasses | ✅ Extract Superclass | ✅ Replace Subclass with Delegate |
| **모델링 정확도** | ❌ | ✅ Replace Type Code / Remove Subclass | ✅ Collapse Hierarchy | ✅ Replace Superclass with Delegate |
| **결합도 감소** | ❌ | ❌ | ✅ Collapse Hierarchy | ✅ Delegate 계열 2종 |

---

## 📌 선택 기준 요약

| 상황 | 추천 리팩토링 |
| --- | --- |
| 중복된 메서드나 필드가 많다 | **Pull Up Method / Field / Constructor Body** |
| 특정 기능이 서브클래스에만 필요하다 | **Push Down Method / Field** |
| 조건문으로 분기하고 있다 | **Replace Type Code with Subclasses** |
| 서브클래스가 실제로 다른 개체를 표현하고 있다 | **Remove Subclass** |
| 공통 기능이 여러 클래스에 존재한다 | **Extract Superclass** |
| 상위/하위 클래스가 거의 동일하다 | **Collapse Hierarchy** |
| 런타임에 전략을 바꾸고 싶다 | **Replace Subclass with Delegate** |
| 상속 구조가 어색하거나 과도한 기능이 노출된다 | **Replace Superclass with Delegate** |

---

# 실전 팁

- 상속은 **단순한 기능 공유**에는 효과적이지만, **모델링 의미가 불분명하거나 동적 변화가 필요한 상황**에서는 **위임이 더 적합**합니다.
- `Replace Type Code with Subclasses`는 다형성을,

  `Replace Subclass with Delegate`는 **유연성과 전략 선택**을,

  `Replace Superclass with Delegate`는 **명확한 모델링과 책임 분리**를 가져옵니다.

- 리팩토링은 **단계적으로**, 테스트 가능한 최소 단위로 나누어 진행해야 합니다.