# 📌 리팩토링 2판 Chapter 03 요약

# 개요

- 리팩터링은 코드의 '냄새'를 통해 필요한 시기를 직관적으로 판단하고, 적절한 기법을 적용하여 코드의 품질을 유지하거나 개선하는 과정이다.
- 이를 위해서는 개발자의 경험과 직관이 중요한 역할을 한다.

# 3.1 기이한 이름

<aside>
💡

**이름 짓기의 중요성**

코드는 추리 소설이 아닙니다. 스릴과 미스터리보다는 명료함이 핵심이죠. 코드의 이름을 올바르게 짓는 것은 단순한 꾸밈이 아니라, 나중에 코드를 읽는 동료(혹은 미래의 당신)가 불필요하게 머리 싸매지 않도록 하는 중요한 설계 결정입니다.

이름 짓기가 어렵다고 피하기보다는, “이런 이름이면 무슨 역할을 하는지 바로 알겠지!”라는 확신을 가지고 신중하게 이름을 정해야 합니다. 만약 적절한 이름이 떠오르지 않는다면, 그 함수나 클래스의 설계 자체에 문제가 있을 가능성이 높으니 기능을 분리하거나 리팩토링할 필요가 있습니다.

</aside>

- **이름 짓기**는 코드의 명료성과 설계의 질을 결정짓는 핵심 요소이다.
- 리팩터링을 통해 이름을 개선하면 코드의 가독성과 유지보수성이 크게 향상된다.
- 이름이 명확하지 않다면 설계에 문제가 있을 수 있으므로, 이름을 정리하는 과정에서 코드의 구조를 개선할 수 있다.

### **리팩터링 전**

- **기이한 이름을 사용한 코드**

```java

// 'Order' 클래스인데, 필드와 메서드 이름이 너무 줄임말로 되어 있어 무슨 역할인지 바로 파악하기 어렵습니다.
public class Order {
    private int q;      // q: quantity(수량)인지, quality(품질)인지 알 수 없음
    private double p;   // p: price(가격)인지, point(포인트)인지 모호함

    public Order(int q, double p) {
        this.q = q;
        this.p = p;
    }

    // cal()이라는 이름만 보면 '계산'을 하는 메서드라는 건 알 수 있으나,
    // 구체적으로 무엇을 계산하는지는 전혀 드러나지 않습니다.
    public double cal() {
        return q * p;
    }
}

```

**문제점:**

- **필드 이름**: `q`와 `p`는 너무 짧고 모호해서, 실제 의미(예를 들어 수량, 단가 등)를 파악하기 어렵습니다.
- **메서드 이름**: `cal()`은 ‘계산’이라는 모호한 의미만 전달할 뿐, 계산의 목적(예: 총 가격 계산)을 알 수 없습니다.

---

### **리팩터링 후**

- **명확한 이름을 사용한 코드**

```java
// 리팩터링 후, 이름만 봐도 Order 클래스의 역할과 내부 동작이 명확해집니다.
public class Order {
    private int quantity;       // 수량
    private double unitPrice;   // 단가

    public Order(int quantity, double unitPrice) {
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    // calculateTotalPrice()를 사용함으로써 이 메서드가 주문의 총 가격을 계산함을 바로 알 수 있습니다.
    public double calculateTotalPrice() {
        return quantity * unitPrice;
    }
}

```

**개선점:**

- **필드 이름**: `quantity`와 `unitPrice`로 변경하여 각 값의 의미가 분명해졌습니다.
- **메서드 이름**: `calculateTotalPrice()`로 이름을 바꿔, 이 메서드가 주문의 총 가격을 계산하는 것임을 한눈에 알 수 있습니다.

# 3.2 중복 코드

<aside>
💡

**함수 추출하기** 나 **메서드 올리기** 같은 리팩터링 기법은 중복을 제거해 코드를 단순하고 명료하게 만들어 줍니다. 개발자라면 “내 코드는 추리 소설이 아니라, 누구나 한눈에 이해할 수 있어야 한다”는 사실을 명심해야 합니다. 중복 제거, 그 자체로 코드의 깔끔함과 유지보수의 자유를 보장해 주니까요!

</aside>

- 같은 코드 구조가 여러 곳에서 반복되면, 각 코드를 살펴볼 때마다 차이점을 확인해야 하는 부담이 생긴다.
- 한 부분을 수정할 때, 비슷한 코드들도 모두 검토하고 수정해야 하므로 유지보수가 어려워진다.
- **중복 코드 해결 방법**
    - **함수 추출하기**: 한 클래스 내의 두 메서드가 동일한 표현식을 사용한다면, 해당 표현식을 별도의 함수로 추출하여 중복을 제거.
    - **문장 슬라이드하기**: 코드가 완전히 동일하지는 않지만 비슷한 경우, 비슷한 부분을 한 곳으로 모아 함수 추출하기를 더 쉽게 적용할 수 있도록 한다.
    - **메서드 올리기**: 같은 부모 클래스를 가진 서브클래스들에 중복 코드가 있다면, 해당 코드를 부모 클래스로 옮겨 중복을 제거.

### **리팩터링 전**

- 각 서브 클래스에 중복된 `calculateBonus()` 메서드가 존재합니다.

```java
public class Employee {
    // 공통 필드와 메서드...
}

public class FullTimeEmployee extends Employee {
    public double calculateBonus() {
        return getSalary() * 0.1;
    }

    public double getSalary() {
        return 5000;
    }
}

public class PartTimeEmployee extends Employee {
    public double calculateBonus() {
        return getSalary() * 0.1;
    }

    public double getSalary() {
        return 3000;
    }
}
```

### **리팩터링 후**

- 부모 클래스인 `Employee`로 중복된 보너스 계산 메서드를 올립니다.
- 단, `getSalary()`는 각 클래스에서 다르게 구현되므로 추상 메서드로 두거나 오버라이드하게 합니다.

```java
public abstract class Employee {
    // 서브 클래스마다 구현해야 하는 메서드
    public abstract double getSalary();

    // 공통의 보너스 계산 로직을 부모로 올림
    public double calculateBonus() {
        return getSalary() * 0.1;
    }
}

public class FullTimeEmployee extends Employee {
    @Override
    public double getSalary() {
        return 5000;
    }
}

public class PartTimeEmployee extends Employee {
    @Override
    public double getSalary() {
        return 3000;
    }
}
```

- **설명**
    - `calculateBonus()` 메서드를 부모 클래스 `Employee`에 올리면, 중복된 코드를 한 곳에 모을 수 있습니다.
    - 서브 클래스에서는 `getSalary()`만 각자의 상황에 맞게 구현하면 되므로, 코드 중복과 유지보수 부담이 크게 줄어듭니다.

# 3.3 긴 함수

- **코드는 추리 소설이 아니라 설명서**

<aside>
💡

**짧은 함수, 강력한 의도 전달**

오랜 기간 잘 유지보수되는 프로그램은 한 덩어리의 거대한 함수보다, 역할이 명확히 분리된 짧은 함수들의 집합입니다. 함수를 짧게 쪼개면 각 함수의 이름이 곧 그 함수의 의도를 설명해 주므로, “이 함수는 뭐 하는 함수지?” 하고 헤매지 않아도 됩니다.

게다가 만약 하나의 기능에 문제가 생겨 수정해야 할 때, 관련 함수 하나만 집중해서 수정하면 되므로 전체 코드를 이해하는 부담이 확 줄어듭니다.

주석으로 설명하려 들기보다, “함수 추출하기”로 그 역할을 함수 이름에 담아내는 것이 코드 유지보수의 기본이라고 할 수 있습니다. 심지어 코드 한 줄짜리라도 “그게 왜 필요한지” 설명하기 위해 함수를 분리하는 게 낫습니다!

</aside>

- **짧은 함수**는 코드의 명확성과 유지보수성을 높이는 핵심 도구이며, 함수 추출하기와 같은 리팩터링 기법을 통해 코드를 작은 단위로 분리하고 의도를 명확히 표현하는 것이 중요하다
- **짧은 함수를 만드는 이유**
    - **가독성 향상**: 함수 이름만으로도 코드의 의도를 파악할 수 있도록 한다
    - **주석 대체**: 주석으로 설명하려던 코드를 함수로 추출하고, 함수 이름으로 의도를 표현한다
    - **재사용성 증가**: 작은 함수는 여러 곳에서 재사용할 수 있다
- **짧은 함수를 만드는 방법**
    - **함수 추출하기**: 코드 덩어리를 찾아 새로운 함수로 만드는 작업이 짧은 함수를 만드는 핵심이다
    - **주석 활용**: 주석이 달린 코드는 함수로 추출할 좋은 후보입니다. 주석 내용을 함수 이름으로 사용한다
    - **조건문과 반복문 처리**
        - 조건문은 **조건문 분해하기**를 적용하여 각 조건을 명확히 분리한다
        - 반복문은 반복문 안의 코드를 함수로 추출합니다. 만약 적절한 이름이 떠오르지 않는다면, 반복문 안에 두 가지 작업이 섞여 있을 가능성이 높으므로 **반복문 쪼개기**를 한다.
- **함수 추출 시 고려사항**
    - **매개변수와 임시 변수 문제**
        - 함수 추출 시 매개변수와 임시 변수가 많아지면 코드가 오히려 복잡해질 수 있다.
        - 이를 해결하기 위해 **임시 변수를 질의 함수로 바꾸기**, **매개변수 객체 만들기**, **객체 통째로 넘기기** 등의 기법을 사용한다.
    - **복잡한 함수 처리**
        - 매개변수와 임시 변수가 여전히 많다면, **함수를 명령으로 바꾸기**와 같은 더 큰 수술을 고려한다.
- **짧은 함수의 핵심 원칙**
    - 함수의 길이가 중요한 것이 아니라, **함수의 목적(의도)과 구현 코드의 괴리**가 중요하다.
    - 코드가 무엇을 하는지 명확히 설명하지 못할수록 함수로 추출하는 것이 유리하다.
    - 함수 이름은 **동작 방식**이 아니라 **의도**를 드러내야 한다.

### **리팩터링 전**

- **긴 함수 하나에 모든 로직이 섞여 있음**

```java
public class OrderProcessor {

    // 주문을 처리하는 메서드: 유효성 검사, 총액 계산, 할인 적용, 주문 출력까지 모두 수행함.
    public void processOrder(Order order) {
        // 1. 주문 유효성 검사
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        if (!order.isValid()) {
            System.out.println("Order is not valid");
            return;
        }

        // 2. 주문 총액 계산
        double totalPrice = 0;
        for (Item item : order.getItems()) {
            totalPrice += item.getPrice() * item.getQuantity();
        }

        // 3. 일정 금액 이상이면 할인 적용
        if (totalPrice > 1000) {
            totalPrice = totalPrice * 0.9;  // 10% 할인
        }

        // 4. 결과 출력
        System.out.println("Order processed with total: " + totalPrice);
    }
}
```

**문제점**

- 한 메서드 안에 여러 역할(유효성 검사, 계산, 할인 적용, 출력)이 섞여 있어 이해하기 어렵고, 유지보수 시 한 부분만 수정하기도 번거롭습니다.

---

### **리팩터링 후**

- **각 역할을 담당하는 짧은 함수들로 분리**

```java
public class OrderProcessor {

    public void processOrder(Order order) {
        validateOrder(order);
        double totalPrice = calculateTotalPrice(order);
        totalPrice = applyDiscountIfEligible(totalPrice);
        logOrder(totalPrice);
    }

    // 주문의 유효성을 확인한다.
    private void validateOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        if (!order.isValid()) {
            throw new IllegalArgumentException("Order is not valid");
        }
    }

    // 주문에 포함된 각 품목의 가격을 합산하여 총액을 계산한다.
    private double calculateTotalPrice(Order order) {
        double totalPrice = 0;
        for (Item item : order.getItems()) {
            totalPrice += calculateItemTotal(item);
        }
        return totalPrice;
    }

    // 단일 품목의 총액(가격 * 수량)을 계산한다.
    private double calculateItemTotal(Item item) {
        return item.getPrice() * item.getQuantity();
    }

    // 총액이 일정 금액 이상이면 할인(10%)을 적용한다.
    private double applyDiscountIfEligible(double totalPrice) {
        if (totalPrice > 1000) {
            return totalPrice * 0.9;
        }
        return totalPrice;
    }

    // 최종 주문 총액을 로그로 출력한다.
    private void logOrder(double totalPrice) {
        System.out.println("Order processed with total: " + totalPrice);
    }
}

```

**개선점**

- **함수 추출하기:**각 기능(유효성 검사, 총액 계산, 할인 적용, 로그 출력)을 별도의 함수로 분리했습니다.
- **의도를 드러내는 이름:**`validateOrder()`, `calculateTotalPrice()`, `calculateItemTotal()`, `applyDiscountIfEligible()`, `logOrder()`와 같이 함수 이름만 봐도 해당 함수가 무슨 일을 하는지 명확합니다.
- **짧은 함수들의 위임:**`processOrder()`는 각 단계별로 역할을 위임하고, 전체적인 흐름만 보여주므로 코드를 읽는 입장에서 “어떤 순서로 일이 진행되는지” 파악하기 쉬워집니다.

# 3.4 긴 매개변수 목록

<aside>
💡

과거 “모든 것을 매개변수로!” 전달하던 방식은 코드의 복잡도를 높이는 주요 원인이었습니다.

이런 문제들을 해결하기 위해 **매개변수를 질의 함수로 바꾸기**, **객체 통째로 넘기기**, **매개변수 객체 만들기**, **플래그 인수 제거하기**, 그리고 **여러 함수를 클래스로 묶기** 등의 리팩터링 기법을 적용할 수 있습니다.

이러한 기법들은 단순히 코드를 ‘짧게’ 만드는 것을 넘어, **의도를 명확히 드러내어 코드 자체가 문서 역할**을 하도록 만드는 데 큰 도움이 됩니다.

결과적으로, 코드를 읽고 유지보수하는 동료(혹은 미래의 자신)가 “이 함수는 무엇을 하는가?”를 한눈에 파악할 수 있도록 해줍니다.

</aside>

- **매개변수 목록을 줄이는 것**은 코드의 가독성과 유지보수성을 높이는 중요한 리팩터링 작업이다.
- 적절한 기법을 활용하여 매개변수 목록을 간소화하고, 클래스를 활용하여 공통 값을 효과적으로 관리하는 것이 핵심이다.

## 1. 매개변수를 질의 함수로 바꾸기 (Replace Parameter with Query)

- **상황:** 함수에 전달되는 매개변수 중 일부는 다른 매개변수로부터 쉽게 계산될 수 있음에도 불구하고, 불필요하게 호출자 쪽에서 계산해 전달하고 있다.

### **리팩터링 전**

- 예를 들어, 주문의 수량과 단가를 받고 총액을 별도의 매개변수로 전달하는 경우:

```java
public class OrderCalculator {
    // totalPrice가 이미 계산되어 전달됨
    public double calculateDiscount(int quantity, double unitPrice, double totalPrice) {
        if (totalPrice > 1000) {
            return totalPrice * 0.1;
        }
        return 0;
    }
}
```

### **리팩터링 후**

- 매개변수를 줄이고, 함수 내부에서 필요한 값을 질의(계산)하여 사용합니다.

```java
public class OrderCalculator {
    public double calculateDiscount(int quantity, double unitPrice) {
        double totalPrice = calculateTotalPrice(quantity, unitPrice);
        if (totalPrice > 1000) {
            return totalPrice * 0.1;
        }
        return 0;
    }

    private double calculateTotalPrice(int quantity, double unitPrice) {
        return quantity * unitPrice;
    }
}
```

- 설명

  이제 호출자는 단순히 수량과 단가만 전달하면 되고, 총액 계산 로직은 함수 내부의 질의 함수 `calculateTotalPrice()`)에서 처리되므로 중복 계산이나 전달 실수를 줄일 수 있습니다.


---

## 2. 객체 통째로 넘기기 (Preserve Whole Object)

- **상황:** 여러 매개변수들이 하나의 데이터 구조(객체)에서 온다면, 각 필드를 따로 분리해 전달할 필요 없이 원본 객체를 그대로 넘기면 코드가 훨씬 깔끔해집니다.

### **리팩터링 전**

- 여러 정보를 개별 매개변수로 전달하는 경우:

```java
public class OrderProcessor {
    public void processOrder(String customerName, String address, int quantity, double unitPrice) {
        // 주문 처리 로직...
        System.out.println("Customer: " + customerName);
        System.out.println("Address: " + address);
        System.out.println("Total: " + (quantity * unitPrice));
    }
}
```

### **리팩터링 후**

- 하나의 `Order` 객체로 정보를 묶어서 전달합니다.

```java
public class Order {
    private String customerName;
    private String address;
    private int quantity;
    private double unitPrice;

    public Order(String customerName, String address, int quantity, double unitPrice) {
        this.customerName = customerName;
        this.address = address;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    // Getter 메서드들
    public String getCustomerName() { return customerName; }
    public String getAddress() { return address; }
    public int getQuantity() { return quantity; }
    public double getUnitPrice() { return unitPrice; }
}

public class OrderProcessor {
    public void processOrder(Order order) {
        System.out.println("Customer: " + order.getCustomerName());
        System.out.println("Address: " + order.getAddress());
        System.out.println("Total: " + (order.getQuantity() * order.getUnitPrice()));
    }
}
```

> 설명:
>
>
> 주문 처리에 필요한 모든 정보를 하나의 객체로 캡슐화함으로써,
>
> 메서드 시그니처가 단순해지고 유지보수성이 높아집니다.
>

---

## 3. 매개변수 객체 만들기 (Introduce Parameter Object)

- **상황:** 항상 함께 전달되는 여러 매개변수가 있다면, 이들을 하나의 객체로 묶어서 전달하면 메서드 호출이 훨씬 간결해집니다.

### **리팩터링 전**

- 좌표 관련 네 개의 매개변수를 따로 전달하는 경우:

```java
public class Geometry {
    public int calculateArea(int x, int y, int width, int height) {
        // 단순히 넓이를 계산
        return width * height;
    }
}
```

### **리팩터링 후**

- `Rectangle`이라는 매개변수 객체를 도입합니다.

```java
public class Rectangle {
    private int x, y, width, height;

    public Rectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    // Getter 메서드들
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}

public class Geometry {
    public int calculateArea(Rectangle rectangle) {
        return rectangle.getWidth() * rectangle.getHeight();
    }
}

```

- 설명:

  관련된 값들을 하나의 객체(`Rectangle`)로 묶음으로써, 메서드의 시그니처가 간단해지고, 향후 해당 객체에 대한 추가 기능 확장이 용이해집니다.


---

## 4. 플래그 인수 제거하기 (Remove Flag Argument)

- **상황:** 메서드의 동작 방식을 변경하는 플래그(예: boolean)를 인수로 전달하는 경우, 코드가 두 가지 역할을 수행하여 혼란을 줄 수 있습니다.

### **리팩터링 전**

- 하나의 메서드에서 플래그에 따라 다른 로직을 수행:

```java
public class UserDisplay {
    public void displayUser(boolean detailed) {
        if (detailed) {
            System.out.println("Displaying detailed user information...");
            // 상세 정보 출력 로직
        } else {
            System.out.println("Displaying basic user information...");
            // 기본 정보 출력 로직
        }
    }
}
```

### **리팩터링 후**

- 플래그에 따라 분기하는 대신, 각각의 역할을 담당하는 두 개의 메서드로 분리합니다.

```java
public class UserDisplay {
    public void displayBasicUser() {
        System.out.println("Displaying basic user information...");
        // 기본 정보 출력 로직
    }

    public void displayDetailedUser() {
        System.out.println("Displaying detailed user information...");
        // 상세 정보 출력 로직
    }
}
```

- 설명: 플래그 인수를 제거하면, 각 메서드가 단일한 역할을 수행하여 코드의 가독성과 테스트 용이성이 향상됩니다.

---

## 5. 여러 함수를 클래스로 묶기 (Preserve Whole Object / Extract Class)

- **상황:** 여러 메서드가 공통의 데이터를 사용한다면, 이들을 하나의 클래스로 묶어 공통 필드로 정의하는 것이 좋습니다.

### **리팩터링 전**

- 여러 서브 클래스에 동일한 보너스 계산 로직이 중복되어 있을 경우

```java
public class FullTimeEmployee {
    public double calculateBonus() {
        return getSalary() * 0.1;
    }

    public double getSalary() {
        return 5000;
    }
}

public class PartTimeEmployee {
    public double calculateBonus() {
        return getSalary() * 0.1;
    }

    public double getSalary() {
        return 3000;
    }
}

```

### **리팩터링 후**

- 부모 클래스에 공통 기능을 옮깁니다.

```java
public abstract class Employee {
    public abstract double getSalary();

    // 공통 보너스 계산 로직
    public double calculateBonus() {
        return getSalary() * 0.1;
    }
}

public class FullTimeEmployee extends Employee {
    @Override
    public double getSalary() {
        return 5000;
    }
}

public class PartTimeEmployee extends Employee {
    @Override
    public double getSalary() {
        return 3000;
    }
}

```

- 설명: 공통 기능을 부모 클래스에 모아두면, 코드 중복이 제거되고 각 서브 클래스는 자신에게만 필요한 부분(예: `getSalary()`)만 구현하면 되어 유지보수가 쉬워집니다.

# 3.5 전역 데이터

<aside>
💡

전역 데이터는 "조금만 써도 독이 된다"는 말처럼 아주 신중하게 다뤄야 합니다.

변수 캡슐화하기를 통해 전역 데이터의 노출을 최소화하고, 코드가 점점 커져도 안정적인 관리가 가능하도록 만드는 것이 좋은 습관입니다.

</aside>

- **전역 데이터의 문제점**
    - 전역 데이터(전역 변수, 클래스 변수, 싱글턴 등)는 프로그램 어디서든 접근하고 수정할 수 있으므로, 누가 언제 값을 바꿨는지 추적하기 어렵고, 그 결과 버그가 발생할 위험이 큽니다.
    - 과거부터 전역 데이터를 맘대로 사용하면 “지옥 4층의 악마”처럼 엄청난 문제를 일으킨다고 경고해왔습니다.
- **해결책 – 변수 캡슐화하기**
    - 전역 데이터를 직접 외부에서 수정하지 못하도록 하고, 대신 접근자(getter)와 변경자(setter) 혹은 특화된 메서드를 통해서만 접근하게 만듭니다.
    - 이렇게 하면 데이터가 변경되는 부분을 한눈에 파악할 수 있고, 잘못된 사용으로 인한 부작용을 최소화할 수 있습니다.
    - 특히, 전역 데이터가 불변이면 비교적 안전하지만, 가변 전역 데이터는 아주 조심해야 합니다.

### 1. 전역 데이터를 직접 사용하는 문제점

- 아래 코드는 전역 변수를 `public` 으로 선언하여, 어디서든 직접 읽고 쓸 수 있게 한 경우입니다.

```java
// 전역 데이터 - 누구나 접근 가능하므로 문제 발생 가능성 있음
public class GlobalData {
    public static int globalCounter = 0;
}
```

```java
public class SomeService {
    public void performOperation() {
        // 전역 데이터를 직접 수정하고 사용
        GlobalData.globalCounter++;
        System.out.println("Global Counter: " + GlobalData.globalCounter);
    }
}
```

- 문제점:
    - `globalCounter`가 공개되어 있어, 코드의 다른 부분 어디서든 값이 바뀔 수 있습니다.
    - 값이 언제, 어떻게 변경되었는지 추적하기 어려워 버그 발생 시 디버깅이 난해합니다.

---

### 2. 변수 캡슐화하기를 적용한 개선된 코드

- 전역 변수를 `private`으로 숨기고, 접근 및 수정은 메서드를 통해서만 이루어지도록 합니다.

```java
public class GlobalData {
    // 전역 변수를 캡슐화하여 외부 직접 접근을 막음
    private static int globalCounter = 0;

    // 값을 읽을 수 있는 접근자(getter)
    public static int getGlobalCounter() {
        return globalCounter;
    }

    // 값을 증가시키는 메서드로 변경을 통제
    public static void incrementCounter() {
        globalCounter++;
    }

    // 필요하다면, 유효성 검증 등을 포함하여 값을 설정하는 메서드 제공
    public static void setGlobalCounter(int value) {
        if (value >= 0) {  // 예시: 음수는 허용하지 않음
            globalCounter = value;
        }
    }
}
```

```java
public class SomeService {
    public void performOperation() {
        // 직접 접근하지 않고 캡슐화된 메서드를 통해서만 데이터를 수정
        GlobalData.incrementCounter();
        System.out.println("Global Counter: " + GlobalData.getGlobalCounter());
    }
}

```

- 개선 효과:
    - `globalCounter`에 직접 접근할 수 없으므로, 값 변경이 모두 한 곳(캡슐화된 메서드)에서 관리됩니다.
    - 변경 시 유효성 검증이나 로그 남기기 등의 추가 처리를 쉽게 할 수 있어, 유지보수와 디버깅이 한층 수월해집니다.

# 3.6 가변 데이터 (Mutable Data)

<aside>
💡

- **데이터 변경의 위험:** 데이터를 무분별하게 수정하면,예상치 못한 결과와 디버깅이 어려운 버그가 발생할 수 있다.
- **해결책:**변수 캡슐화, 변수 쪼개기, 갱신 로직 분리, API 설계 시 질의 함수와 변경 함수 분리,불변 객체 사용 등으로 데이터 변경의 부작용을 최소화할 수 있다.
</aside>

- **문제점**
    - 데이터를 자유롭게 변경하면, 코드의 다른 부분에서는 전혀 다른 값을 예상하고 있을 수 있으므로 의도치 않은 결과나 디버깅하기 어려운 버그가 발생할 수 있다. 특히 아주 드문 조건에서 발생하는 버그는 원인을 찾기가 매우 어렵다.
- **함수형 프로그래밍 접근**
    - 함수형 프로그래밍은 데이터가 한 번 정해지면 변하지 않는 불변성을 기본으로 한다.
    - 만약 데이터 변경이 필요하다면 원본을 그대로 두고 변경된 복사본을 만들어 반환한다.
- **불변성의 장점을 일반 언어에서도 활용하는 방법**
    - **변수 캡슐화하기:** 값을 직접 외부에서 수정하지 못하게 하고, 정해진 메서드를 통해서만 수정하도록하여 데이터 변경 경로를 감시하고 제어한다.
    - **변수 쪼개기:** 한 변수에 여러 용도의 값이 저장되어 있다면 용도별로 분리하여,값 갱신 시 발생할 수 있는 부작용을 줄인다.
    - **갱신 로직 분리:** 값을 변경하는 코드(부작용이 있는 코드)와 부작용 없는 코드를 분리해서 관리한다.
    - **질의 함수와 변경 함수 분리하기:** API 설계 시 부작용이 있는 변경 함수를 호출하기 전에,먼저 값을 질의하는 함수와 변경 함수의 역할을 명확히 분리한다.
    - **세터 제거하기:** 불필요한 세터를 없애서 외부에서 임의로 변수를 수정하는 상황을 방지한다.
    - **내부 필드 전체 교체하기:** 구조체처럼 내부 필드에 데이터를 담고 있을 때,개별 필드를 수정하기보다는 전체를 교체하는 방식(참조를 값으로 바꾸기)을 고려한다.

이러한 기법들을 적용하면, 데이터의 무분별한 변경으로 인한 예기치 못한 결과와 디버깅 어려움을 크게 줄일 수 있다.

## 1. 변수 캡슐화하여 가변 데이터 통제하기

- **문제 상황:** 전역 변수나 클래스 변수가 `public`으로 노출되어 직접 수정되면, 어느 곳에서든 예상치 못하게 값이 변경되어 버그를 일으킬 수 있습니다.

### **리팩터링 전 (캡슐화되지 않은 가변 데이터):**

```java
// 전역 데이터 - 누구나 직접 접근하고 수정할 수 있음
public class GlobalState {
    public static int counter = 0;
}

public class ServiceA {
    public void doSomething() {
        GlobalState.counter++;
        System.out.println("ServiceA: counter = " + GlobalState.counter);
    }
}

public class ServiceB {
    public void doSomethingElse() {
        // 다른 곳에서 counter의 값을 예상하고 사용하지만, 누군가가 이미 변경했을 수 있음.
        System.out.println("ServiceB: counter = " + GlobalState.counter);
    }
}
```

### **리팩터링 후 (변수 캡슐화 적용):**

```java
// GlobalState 클래스의 내부 데이터는 private으로 감추고, 변경은 정해진 메서드를 통해서만 함.
public class GlobalState {
    private static int counter = 0;

    // 값을 읽을 수 있는 getter
    public static int getCounter() {
        return counter;
    }

    // 값을 안전하게 변경하는 메서드 (예: 증가)
    public static void incrementCounter() {
        counter++;
    }
}

public class ServiceA {
    public void doSomething() {
        GlobalState.incrementCounter();
        System.out.println("ServiceA: counter = " + GlobalState.getCounter());
    }
}

public class ServiceB {
    public void doSomethingElse() {
        System.out.println("ServiceB: counter = " + GlobalState.getCounter());
    }
}

```

- 설명:

  `counter`에 직접 접근하지 못하도록 `private`으로 감추고, `incrementCounter()`와 `getCounter()`를 통해서만 접근하게 하여 데이터 변경 경로를 한 곳에서 관리하고 감시할 수 있게 만들었습니다.


---

## 2. 불변 객체로 상태 변경 시 부작용 최소화하기

- **문제 상황:** 여러 곳에서 같은 객체를 공유하며 내부 상태를 변경할 경우, 예상치 못한 곳에서 값이 바뀌어 버그가 발생할 수 있습니다.

### **리팩터링 전 (가변 객체 사용):**

```java
public class MutableCounter {
    private int count;

    public MutableCounter(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    // 상태를 직접 변경하는 메서드 (부작용이 있음)
    public void increment() {
        count++;
    }
}

```

- 이 객체를 여러 곳에서 공유하면, 한 곳에서 `increment()` 호출 시 다른 곳의 기대와 달리 값이 변할 수 있습니다.

### **리팩터링 후 (불변 객체 사용):**

```java
public final class ImmutableCounter {
    private final int count;

    public ImmutableCounter(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    // 상태를 변경하는 대신, 새로운 인스턴스를 반환하여 원본은 그대로 둠
    public ImmutableCounter increment() {
        return new ImmutableCounter(count + 1);
    }
}

```

**사용 예:**

```java
public class CounterClient {
    public static void main(String[] args) {
        ImmutableCounter counter = new ImmutableCounter(0);
        System.out.println("Initial count: " + counter.getCount());

        // counter.increment()가 counter 자체를 변경하는 대신 새 객체를 반환함
        ImmutableCounter newCounter = counter.increment();
        System.out.println("After increment, original count: " + counter.getCount());
        System.out.println("New counter count: " + newCounter.getCount());
    }
}
```

- 설명:
    - 불변 객체를 사용하면, 한 번 생성된 데이터는 변경되지 않으므로 부작용 없이 안전하게 공유할 수 있습니다.
    - 만약 값의 변경이 필요하다면, 변경된 상태의 새 객체를 생성해 반환하므로 원본 데이터는 그대로 보존되어 예기치 않은 변경으로 인한 버그를 예방할 수 있습니다.

# 3.7 뒤엉킨 변경 (Divergent Change)

<aside>
💡

뒤엉킨 변경을 피하기 위해 각 맥락의 책임을 분리하면, 소프트웨어의 변경 범위를 한정하고 유지보수성을 크게 향상시킬 수 있습니다.

</aside>

- **변경하기 쉬운 구조**
    - 소프트웨어는 변경 시 단 한 곳만 수정할 수 있을 정도로 구조가 잘 분리되어 있어야 합니다.
    - 만약 여러 곳을 동시에 고쳐야 한다면, 이는 코드가 뒤엉켜 있다는 신호입니다.
- **뒤엉킨 변경(Tangled Change)**
    - 하나의 모듈이 여러 가지 이유(예: 데이터베이스 연동과 금융 상품 처리)로 변경되어야 한다면, 각 맥락이 섞여 있어 변경 시 서로 엉켜버리는 문제가 발생합니다.
    - 예를 들어, 새로운 데이터베이스를 추가할 때마다 여러 함수, 또는 새로운 금융 상품이 추가될 때마다 여러 함수가 수정되어야 한다면, 이는 단일 책임 원칙(SRP)이 지켜지지 않은 사례입니다.
- **해결 방법:**
    - **단계 쪼개기(Step Slicing):** 각 처리 단계별로 필요한 데이터를 별도의 데이터 구조(예, DTO)로 담아 다음 단계에 전달합니다.
    - **함수 옮기기(Move Function)/클래스 추출하기(Extract Class):** 서로 다른 맥락에 관련된 함수들을 각각 별도의 모듈이나 클래스로 분리합니다.
    - 이를 통해 데이터베이스 연동과 각각 독립된 모듈로 관리할 수 있게 되어,수정 시 해당 맥락에 국한된 코드만 이해하면 되어 유지보수가 훨씬 쉬워집니다.

## 1. 뒤엉킨 변경이 발생하는 경우 (리팩터링 전)

- 아래 코드는 한 클래스 내에 데이터베이스 조회와 금융 상품 처리를 모두 포함하여,
- 변경 사항이 발생하면 여러 함수가 동시에 수정되어야 하는 문제(뒤엉킨 변경)가 있습니다.

```java
public class TangledModule {

    // 하나의 메서드에서 데이터베이스 조회, 금융 상품 처리, DB 업데이트까지 모두 수행
    public void processFinancialProduct(String productId) {
        // [데이터베이스 맥락] 데이터베이스에서 상품 정보를 조회
        String rawData = queryDatabase(productId);

        // [금융 상품 맥락] 조회된 데이터를 기반으로 금융 상품 처리 로직 수행
        double processedValue;
        if (rawData.contains("TypeA")) {
            processedValue = processTypeA(rawData);
        } else {
            processedValue = processTypeB(rawData);
        }

        // [데이터베이스 맥락] 처리 결과를 데이터베이스에 업데이트
        updateDatabase(productId, processedValue);
    }

    // 데이터베이스에서 상품 데이터를 조회하는 로직
    private String queryDatabase(String productId) {
        // 실제 DB 호출 대신 간단히 모의 데이터 반환
        return "TypeA: 상품 데이터 for " + productId;
    }

    // 금융 상품 처리 로직 - 타입 A
    private double processTypeA(String data) {
        // 복잡한 계산 로직이 있다고 가정
        return 100.0;
    }

    // 금융 상품 처리 로직 - 타입 B
    private double processTypeB(String data) {
        // 다른 계산 로직
        return 200.0;
    }

    // 처리 결과를 데이터베이스에 업데이트하는 로직
    private void updateDatabase(String productId, double value) {
        // 실제 업데이트 대신 출력
        System.out.println("Updated product " + productId + " with value: " + value);
    }
}
```

- 문제점:
    - 데이터베이스 조회/업데이트와 금융 상품 처리라는 서로 다른 맥락의 책임이 한 클래스에 섞여 있음
    - 새로운 데이터베이스나 금융 상품 로직이 추가되면, 이 클래스의 여러 메서드를 동시에 수정해야 함

---

## 2. 각 맥락을 분리한 개선된 코드 (리팩터링 후)

- 맥락별로 클래스를 분리하여, 데이터베이스 관련 작업과 금융 상품 처리를 독립된 모듈로 분리합니다.

### (1) 데이터 전달용 DTO (Data Transfer Object)

각 맥락 간에 데이터를 전달할 때 사용할 간단한 클래스입니다.

```java
public class ProductData {
    private String productId;
    private String rawData; // 데이터베이스에서 조회한 원시 데이터

    public ProductData(String productId, String rawData) {
        this.productId = productId;
        this.rawData = rawData;
    }

    public String getProductId() {
        return productId;
    }

    public String getRawData() {
        return rawData;
    }
}

```

### (2) 데이터베이스 관련 작업을 담당하는 클래스

```java
public class DatabaseService {

    // 데이터베이스에서 상품 데이터를 조회하여 ProductData 객체로 반환
    public ProductData getProductData(String productId) {
        // 실제 DB 호출 대신 모의 데이터를 사용
        String rawData = "TypeA: 상품 데이터 for " + productId;
        return new ProductData(productId, rawData);
    }

    // 처리 결과를 데이터베이스에 업데이트
    public void updateProductResult(String productId, double result) {
        System.out.println("Updated product " + productId + " with value: " + result);
    }
}

```

### (3) 금융 상품 처리를 담당하는 클래스

```java
public class FinancialProductProcessor {

    // ProductData를 받아서 금융 상품 로직에 따라 값을 처리
    public double process(ProductData productData) {
        String data = productData.getRawData();
        if (data.contains("TypeA")) {
            return processTypeA(data);
        } else {
            return processTypeB(data);
        }
    }

    private double processTypeA(String data) {
        // 타입 A에 해당하는 계산 로직
        return 100.0;
    }

    private double processTypeB(String data) {
        // 타입 B에 해당하는 계산 로직
        return 200.0;
    }
}

```

### (4) 두 맥락을 연결하는 조정자 클래스

```java
public class FinancialProductHandler {
    private DatabaseService dbService = new DatabaseService();
    private FinancialProductProcessor productProcessor = new FinancialProductProcessor();

    // 전체 프로세스를 단계별로 분리하여 수행
    public void processFinancialProduct(String productId) {
        // 1. [데이터베이스 맥락] 데이터 조회
        ProductData productData = dbService.getProductData(productId);

        // 2. [금융 상품 맥락] 금융 상품 처리
        double result = productProcessor.process(productData);

        // 3. [데이터베이스 맥락] 처리 결과 업데이트
        dbService.updateProductResult(productId, result);
    }
}

```

### (5) 실행 예시

```java
public class Main {
    public static void main(String[] args) {
        FinancialProductHandler handler = new FinancialProductHandler();
        handler.processFinancialProduct("P12345");
    }
}
```

- 개선 효과:
    - **맥락 분리:** 데이터베이스 관련 작업과 금융 상품 처리 로직이 별도의 클래스에 분리되어 있어,각각의 책임이 명확해졌습니다.
    - **변경 국한:** 향후 데이터베이스 연동 방식이나 금융 상품 처리 로직에 변경이 필요할 경우,해당 맥락에 속한 클래스만 수정하면 되므로 유지보수가 용이해집니다.
    - **코드 이해 용이:** 각 클래스와 메서드 이름을 통해 그 역할과 의도가 명확하게 드러납니다.

# 3.8 산탄총 수술(Shotgun Surgery)

<aside>
💡

- 산탄총 수술 냄새가 나는 상황에서는 관련된 변경 사항들을 한 모듈이나 클래스에 모아두어, 수정해야 할 코드를 한 곳에서 관리하도록 만드는 것이 매우 효과적입니다.
- 리팩터링을 통해 분산되어 있는 코드들을 적절히 인라인하거나 재조직함으로써, 장기적으로 유지보수하기 쉬운 소프트웨어 구조를 만들 수 있습니다.
</aside>

### 문제 상황 – 산탄총 수술이 발생하는 코드

- 예를 들어, 사용자 정보(User)의 각 항목(이름, 이메일, 주소)을 업데이트하는 로직이 서로 다른 클래스에 흩어져 있다면, 하나의 변경이 발생할 때마다 여러 클래스를 수정해야 하는 산탄총 수술 문제가 발생합니다.

### 리팩터링 전 (여러 클래스로 분산된 업데이트 로직)

```java
// 사용자 데이터를 담는 클래스
public class User {
    private String name;
    private String email;
    private String address;

    // Getter 및 Setter
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}
```

```java

// 이름 업데이트를 담당하는 클래스
public class UserNameUpdater {
    public void updateName(User user, String newName) {
        if (newName == null || newName.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid name");
        }
        user.setName(newName);
    }
}
```

```java

// 이메일 업데이트를 담당하는 클래스
public class UserEmailUpdater {
    public void updateEmail(User user, String newEmail) {
        if (newEmail == null || !newEmail.contains("@")) {
            throw new IllegalArgumentException("Invalid email");
        }
        user.setEmail(newEmail);
    }
}

```

```java
// 주소 업데이트를 담당하는 클래스
public class UserAddressUpdater {
    public void updateAddress(User user, String newAddress) {
        if (newAddress == null || newAddress.isEmpty()) {
            throw new IllegalArgumentException("Invalid address");
        }
        user.setAddress(newAddress);
    }
}
```

- 문제점:
    - 이름, 이메일, 주소 각각을 업데이트하는 로직이 다른 클래스로 분리되어 있어,예를 들어 사용자 정보 업데이트 로직에 변경이 필요할 때, 여러 클래스를 찾아 수정해야 합니다.
    - 관련된 변경 사항이 분산되어 있어 실수로 한 곳을 놓치거나, 서로 다른 방식으로 업데이트될 위험이 있습니다.

---

### 리팩터링 후 – 관련 업데이트 로직을 한 모듈로 통합

- 이제 관련된 업데이트 로직을 하나의 클래스(예: `UserProfileManager`)에 모아두면, 한 곳에서 모든 변경 사항을 관리할 수 있어 산탄총 수술 문제를 해결할 수 있습니다.

```java

// 사용자 데이터를 담는 클래스는 그대로 둡니다.
public class User {
    private String name;
    private String email;
    private String address;

    // Getter 및 Setter
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}

```

```java

// 관련된 업데이트 로직을 한 곳에 모은 UserProfileManager 클래스
public class UserProfileManager {

    // 사용자 이름 업데이트
    public void updateName(User user, String newName) {
        if (newName == null || newName.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid name");
        }
        user.setName(newName);
    }

    // 사용자 이메일 업데이트
    public void updateEmail(User user, String newEmail) {
        if (newEmail == null || !newEmail.contains("@")) {
            throw new IllegalArgumentException("Invalid email");
        }
        user.setEmail(newEmail);
    }

    // 사용자 주소 업데이트
    public void updateAddress(User user, String newAddress) {
        if (newAddress == null || newAddress.isEmpty()) {
            throw new IllegalArgumentException("Invalid address");
        }
        user.setAddress(newAddress);
    }

    // 예시로, 여러 업데이트 작업을 하나의 메서드에서 처리할 수도 있습니다.
    public void updateUserProfile(User user, String newName, String newEmail, String newAddress) {
        updateName(user, newName);
        updateEmail(user, newEmail);
        updateAddress(user, newAddress);
    }
}

```

```java

// 클라이언트 코드
public class Main {
    public static void main(String[] args) {
        User user = new User();
        UserProfileManager manager = new UserProfileManager();

        // 한 곳에서 모든 업데이트 로직을 처리할 수 있음
        manager.updateUserProfile(user, "Alice", "alice@example.com", "123 Main Street");

        System.out.println("User Info:");
        System.out.println("Name: " + user.getName());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Address: " + user.getAddress());
    }
}

```

- 개선 효과:
    - **관련 로직 집중:** 이름, 이메일, 주소 업데이트 로직이 하나의 클래스에 모여 있어,변경 사항이 발생할 때 해당 클래스만 수정하면 됩니다.
    - **유지보수성 향상:** 각 기능이 독립적으로 분산되어 있지 않고 한 곳에 집중되어 있어,코드 이해와 디버깅이 쉬워집니다.
    - **확장 용이:** 만약 사용자 정보 업데이트 로직에 추가적인 검증이나 공통 처리 로직이 필요해지면, UserProfileManager 내부에서 쉽게 관리할 수 있습니다.

# 3.9 기능 편애 (Feature Envy)

- **모듈화 원칙:**
    - 모듈 내부에서는 서로 긴밀하게 상호작용하고, 모듈 간에는 상호작용을 최소화해야 합니다.
    - 즉, 관련된 데이터와 동작은 한 곳에 모아두어 변경 시 한 군데만 수정할 수 있도록 해야 합니다.
- **기능 편애(Feature Envy) 냄새:**
    - 어떤 함수가 자신이 속한 모듈의 데이터나 함수를 사용하는 것보다 다른 모듈의 데이터나 함수를 지나치게 참조한다면,해당 함수는 데이터가 있는 곳(즉, 기능이 속해야 할 모듈)으로 옮겨야 합니다.
    - 경우에 따라 함수의 일부만 다른 모듈의 데이터에 편애한다면, 그 부분을 독립 함수로 추출한 후 옮기는 것도 효과적입니다.
    - 함수가 여러 모듈의 데이터를 사용한다면, 가장 많은 데이터를 포함한 모듈로 옮기는 것이 일반적인 기준입니다.
- **디자인 패턴 활용:**
    - 때로는 전략 패턴, 방문자 패턴, 자기 위임 같은 복잡한 패턴을 사용해함께 변경되어야 하는 부분들을 한곳에 모으고, 변경해야 할 코드를 격리할 수 있습니다.
    - 이러한 패턴은 간접 호출(인디렉션)을 늘리지만, 수정 시 작은 동작 코드들을 개별 클래스로 분리해변경하기 쉽게 만들어 줍니다.

### 리팩터링 전 - 기능 편애가 있는 코드

```java

// 고객 데이터를 담는 클래스 (자신의 데이터와 행동을 함께 가지는 것이 바람직함)
public class Customer {
    private String firstName;
    private String lastName;
    private String email;

    public Customer(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName  = lastName;
        this.email     = email;
    }

    public String getFirstName() { return firstName; }
    public String getLastName()  { return lastName; }
    public String getEmail()     { return email; }
}

```

```java

// 고객 리포트를 생성하는 클래스 - 문제: Customer의 데이터를 위해 여럿 getter를 호출
public class ReportGenerator {
    // 고객 정보를 이용해 리포트를 생성하는 함수 (여러 getter 호출)
    public String generateCustomerReport(Customer customer) {
        // 고객 데이터에 지나치게 의존(기능 편애)
        String fullName = customer.getFirstName() + " " + customer.getLastName();
        String email    = customer.getEmail();

        return "Customer Report: \n" +
               "Name: " + fullName + "\n" +
               "Email: " + email;
    }
}

```

- 문제점
    - ReportGenerator의 generateCustomerReport() 함수는 고객의 데이터에 깊이 의존하고 있습니다.
    - 이 경우 고객 데이터와 관련된 동작(리포트 생성)이 오히려 Customer 클래스에 가까워야 함에도 다른 모듈에 흩어져 있어, 변경 시 여러 곳을 동시에 수정해야 할 위험이 있습니다.

---

### 리팩터링 후 - 기능 편애를 해결한 개선 코드

- 고객 데이터를 더 잘 알고 있는 곳, 즉 Customer 클래스 내부에 리포트 생성 함수를 옮깁니다.

```java

public class Customer {
    private String firstName;
    private String lastName;
    private String email;

    public Customer(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName  = lastName;
        this.email     = email;
    }

    public String getFirstName() { return firstName; }
    public String getLastName()  { return lastName; }
    public String getEmail()     { return email; }

    // 기능 편애 해결: 고객 데이터에 가까운 곳으로 리포트 생성 기능을 이동
    public String generateReport() {
        String fullName = firstName + " " + lastName;
        return "Customer Report: \n" +
               "Name: " + fullName + "\n" +
               "Email: " + email;
    }
}
```

```java

// ReportGenerator 클래스는 더 이상 고객 데이터에 대해 일일이 접근할 필요가 없음
public class ReportGenerator {
    public void printReport(Customer customer) {
        // 이제 Customer 클래스 내부의 generateReport()를 호출함
        System.out.println(customer.generateReport());
    }
}
```

```java

// 클라이언트 코드
public class Main {
    public static void main(String[] args) {
        Customer customer = new Customer("Alice", "Smith", "alice@example.com");
        ReportGenerator reportGen = new ReportGenerator();
        reportGen.printReport(customer);
    }
}
```

- 개선 효과:
    - **관련 로직 집중:** 고객 정보를 다루는 리포트 생성 기능이 Customer 클래스 내부에 모여,기능 편애 냄새를 제거했습니다.
    - **변경 국한:** 고객 데이터가 변경되거나 리포트 형식이 수정되어야 할 경우,Customer 클래스 내부만 수정하면 되므로 유지보수가 쉬워집니다.
    - **모듈 경계 준수:** 모듈(클래스) 간 상호작용은 최소화되고,데이터와 관련된 동작은 데이터가 있는 곳에 가까이 있게 되어,전체적인 코드 구조가 깔끔해집니다.

# 3.10 데이터 뭉치 (Data Clumps)

<aside>
💡

여러 곳에서 항상 함께 등장하는 데이터 항목은 하나의 클래스로 묶어 관리하는 것이 코드의 간결성과 유지보수성을 크게 향상시키는 좋은 리팩터링 방법입니다.

</aside>

- **데이터 뭉치(Data Clump)**
    - 여러 클래스의 필드나 메서드의 시그니처에서 항상 함께 등장하는 데이터 항목들이 있다면, 이들은 함께 어울려 놀고 싶어하는 “어린아이”와 같아서 하나의 객체로 묶어줘야 한다는 뜻입니다.
- **리팩터링 방법**
    - **클래스 추출하기**
        - 필드 형태로 묶여있는 데이터 뭉치를 하나의 클래스(객체)로 추출합니다.
        - 예를 들어, 주소 관련 필드(거리, 도시, 주, 우편번호)가 매번 같이 쓰인다면 Address 클래스로 묶습니다.
    - **매개변수 객체 만들기 / 객체 통째로 넘기기:**
        - 메서드 시그니처에 함께 등장하는 데이터 뭉치를 하나의 객체로 만들어 전달하면, 호출 코드가 훨씬 간결해집니다.
    - **판별 기준:**
        - 데이터 항목 중 하나를 삭제해보았을 때 나머지 값들만으로는 의미가 없으면, 이 데이터들은 반드시 함께 다루어져야 하므로 하나의 객체로 묶어야 함을 뜻합니다.
- **추가 팁:**

  단순한 레코드 구조가 아닌 클래스로 뽑으면, 나중에 해당 데이터와 관련된 동작(예: 포매팅, 검증)을 같은 클래스에 옮겨 기능 편애(Feature Envy) 문제를 제거할 기회가 생깁니다. 이렇게 하면 중복이 줄어들고, 향후 개발도 가속화됩니다.


### **리팩터링 전: 데이터 뭉치가 흩어진 코드**

- 예를 들어, 주문(Order) 클래스에 고객의 이름과 주소 관련 필드가 따로 흩어져 있다고 가정해봅니다.

```java

// 데이터 뭉치가 흩어진 상태: 주소 관련 필드들이 여러 곳에 산재함
public class Order {
    private String customerName;
    private String street;
    private String city;
    private String state;
    private String zip;

    public Order(String customerName, String street, String city, String state, String zip) {
        this.customerName = customerName;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }

    // 각 필드에 대한 Getter들
    public String getCustomerName() { return customerName; }
    public String getStreet() { return street; }
    public String getCity() { return city; }
    public String getState() { return state; }
    public String getZip() { return zip; }
}

```

- 그리고 주문 정보를 출력하는 서비스에서는 여러 개의 필드를 따로따로 사용합니다.

```java

public class ShippingService {
    // 여러 데이터 항목을 개별적으로 사용
    public void printShippingLabel(Order order) {
        System.out.println("Shipping to: " + order.getCustomerName());
        System.out.println(order.getStreet());
        System.out.println(order.getCity() + ", " + order.getState() + " " + order.getZip());
    }
}

```

- 문제점
    - 주소 관련 데이터가 매번 개별 필드로 다뤄져, 호출 코드도 복잡해집니다.
    - 만약 주소 데이터에 관련된 검증이나 포매팅 로직이 추가되어야 한다면,여러 곳에서 수정해야 할 가능성이 높습니다.

---

### **리팩터링 후: 데이터 뭉치를 하나의 클래스로 추출**

- 주소 관련 데이터를 `Address` 클래스로 묶고, 주문은 고객 이름과 `Address` 객체만 갖도록 수정합니다.

```java

// Address 클래스로 주소 관련 데이터 뭉치를 하나로 묶음
public class Address {
    private String street;
    private String city;
    private String state;
    private String zip;

    public Address(String street, String city, String state, String zip) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }

    // Getter들
    public String getStreet() { return street; }
    public String getCity() { return city; }
    public String getState() { return state; }
    public String getZip() { return zip; }

    // 주소 정보를 포맷팅하는 동작을 추가할 수 있음 (기능 편애 제거)
    public String format() {
        return street + "\n" + city + ", " + state + " " + zip;
    }
}

```

- 이제 주문 클래스는 다음과 같이 변경됩니다.

```java

public class Order {
    private String customerName;
    private Address address;  // 주소 데이터는 이제 Address 객체로 묶임

    public Order(String customerName, Address address) {
        this.customerName = customerName;
        this.address = address;
    }

    public String getCustomerName() { return customerName; }
    public Address getAddress() { return address; }
}

```

- 배송 라벨 출력 서비스도 단순해집니다.

```java

public class ShippingService {
    public void printShippingLabel(Order order) {
        System.out.println("Shipping to: " + order.getCustomerName());
        // Address 클래스 내부의 format() 메서드를 활용하여 출력
        System.out.println(order.getAddress().format());
    }
}

```

- **실행 예시 (Main 클래스)**

```java

public class Main {
    public static void main(String[] args) {
        Address address = new Address("123 Main St", "Springfield", "IL", "62704");
        Order order = new Order("John Doe", address);

        ShippingService shippingService = new ShippingService();
        shippingService.printShippingLabel(order);
    }
}
```

- 개선 효과:
    - **데이터 뭉치 집중:** 주소 관련 데이터가 하나의 Address 객체에 모여 있으므로,주소와 관련된 추가 기능(예: 검증, 포매팅)을 Address 클래스에 쉽게 추가할 수 있습니다.
    - **코드 간결성:** 메서드 시그니처와 호출 코드가 훨씬 간단해져, 가독성이 좋아집니다.
    - **유지보수성 향상:** 데이터 뭉치를 하나의 클래스로 관리함으로써,변경이 발생할 때 수정해야 할 범위가 한정되고 중복 수정 위험이 줄어듭니다.

# 3.11 기본형 집착 (Primitive Obsession)

<aside>
💡

단순한 기본형 대신 도메인 객체를 도입하면, 코드의 의미가 분명해지고, 향후 기능 확장 및 유지보수도 훨씬 쉬워집니다.

</aside>

- **기본형 집착(Basic Type Obsession) 냄새란?**
    - 대부분의 언어는 정수, 부동소수점, 문자열 같은 기본형을 제공하는데, 개발자가 문제 도메인에 맞는 ‘화폐’, ‘좌표’, ‘구간’ 같은 개념을 직접 클래스로 정의하기를 꺼리는 경우가 많습니다.
    - 그 결과, 금액 계산을 단순 숫자(double, int)로 하거나, 전화번호를 단순 문자열로 다루게 되어 의미와 제약, 포매팅 등이 코드에 녹아들지 못하는 문제가 발생합니다.
- **문제점**
    - **의미 부재:** 단순 숫자나 문자열은 해당 값의 의미(예: 화폐 단위, 소수점 자리수, 포매팅 규칙)를 담지 못합니다.
    - **검증 및 기능 부족:** 예를 들어 전화번호나 화폐를 단순 문자열로 표현하면,올바른 형식인지 검증하거나 일관된 출력 포맷을 제공하기 어렵습니다.
    - **데이터 뭉치(Data Clump):** 관련 있는 기본형들이 여러 곳에 함께 등장한다면,이들은 반드시 한 객체로 묶여서 관리되어야 하는데 그렇지 않으면 유지보수성이 떨어집니다.
- **해결책**
    - **기본형을 객체로 바꾸기 (Replace Primitive with Object):**금액, 좌표, 전화번호 등 의미 있는 도메인 타입을 별도의 클래스로 만들어해당 도메인에 필요한 검증, 포매팅, 연산 등의 동작을 함께 제공하도록 합니다.
    - **데이터 뭉치 리팩터링:** 자주 함께 등장하는 기본형들을 클래스 추출하기나 매개변수 객체 만들기를 사용해 하나의 객체로 묶어줍니다.

### 리팩토링 전 - 기본형 집착이 있는 코드

- 아래 코드는 금액을 단순 `double`로 다루어, 의미나 포매팅 같은 기능이 전혀 반영되어 있지 않습니다.

```java

// Invoice 클래스는 금액을 단순 숫자로 보관
public class Invoice {
    private double amount; // 금액을 단순 숫자로 표현

    public Invoice(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }
}

// 금액을 출력할 때 단순 숫자를 그대로 출력함
public class InvoicePrinter {
    public void printInvoice(Invoice invoice) {
        // 포매팅이나 단위에 대한 정보가 전혀 없음
        System.out.println("Amount: $" + invoice.getAmount());
    }
}

// 사용 예시
public class MainBefore {
    public static void main(String[] args) {
        Invoice invoice = new Invoice(1234.5);
        InvoicePrinter printer = new InvoicePrinter();
        printer.printInvoice(invoice);
    }
}
```

- 문제점
    - 금액에 대한 단위(currency)나 소수점 자리수, 포매팅 규칙이 코드에 반영되어 있지 않음
    - 만약 화폐 연산(더하기, 비교 등)이나 다른 출력 형식이 필요할 때,여러 곳에서 별도의 조건문과 처리가 필요하게 되어 유지보수가 어려워짐

---

### 리팩터링 후 - 기본형을 객체로 바꾼 개선 코드

- 아래 예제에서는 `Money` 클래스를 만들어 금액과 관련된 정보를 캡슐화하고, 검증 및 포매팅 등의 동작을 제공하도록 합니다.

```java

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

// Money 클래스: 금액을 의미 있는 객체로 표현
public class Money {
    private final BigDecimal amount;
    private final Currency currency;

    public Money(BigDecimal amount, Currency currency) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        this.amount = amount;
        this.currency = currency;
    }

    // 금액을 더하는 예제 메서드
    public Money add(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Currency mismatch");
        }
        return new Money(this.amount.add(other.amount), this.currency);
    }

    // 금액을 출력하는 메서드
    public String format() {
		    // 화폐별 기본 소수점 자리수를 가져옴
		    int fractionDigits = currency.getDefaultFractionDigits();
		    // 해당 자리수로 반올림 후, 화폐 기호와 함께 문자열로 반환
		    return currency.getSymbol() + amount.setScale(fractionDigits, RoundingMode.HALF_UP).toString();
		}

    // 필요시 getter 제공
    public BigDecimal getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }
}

```

- 그리고 Invoice 클래스와 출력 클래스를 수정합니다.

```java

// Invoice 클래스는 이제 Money 객체를 보유
public class Invoice {
    private Money amount;

    public Invoice(Money amount) {
        this.amount = amount;
    }

    public Money getAmount() {
        return amount;
    }
}

// InvoicePrinter는 Money 클래스의 format() 메서드를 사용해 일관된 출력 제공
public class InvoicePrinter {
    public void printInvoice(Invoice invoice) {
        System.out.println("Amount: " + invoice.getAmount().format());
    }
}

// 사용 예시
public class MainAfter {
    public static void main(String[] args) {
        // Currency.getInstance("USD")를 사용해 통화 정보를 포함시킴
        Money money = new Money(new BigDecimal("1234.5"), Currency.getInstance("USD"));
        Invoice invoice = new Invoice(money);
        InvoicePrinter printer = new InvoicePrinter();
        printer.printInvoice(invoice);
    }
}

```

- 개선 효과:
    - **의미 부여:** 금액이 단순 숫자가 아니라 Money라는 도메인 객체로 표현되어,통화, 소수점 자리수, 포매팅 규칙 등 의미 있는 정보를 함께 다룰 수 있습니다.
    - **유지보수성 향상:** 금액 관련 동작(예: 더하기, 비교, 포매팅)을 Money 클래스에 집중시켜,코드가 변경될 때 한 곳만 수정하면 됩니다.
    - **데이터 뭉치 해소:** 여러 기본형(여기서는 숫자와 통화)이 한 객체에 묶여,관련 데이터가 흩어지지 않고 문명사회(객체 지향적 설계)로 탈바꿈됩니다.

# 3.12 반복되는 switch문

- **조건부 로직과 switch 문 문제**
    - 순수 객체지향을 주장하는 사람들은 switch문(혹은 if/else로 길게 나열된 조건문)이 문제의 원인이며, 모든 조건부 로직은 다형성으로 대체해야 한다고 주장합니다.
    - 특히, 동일한 조건부 로직이 여러 곳에 반복되면 새로운 조건을 추가할 때마다 모든 switch문을 찾아 수정해야 하는 부담이 발생합니다.
- **해결책 – 다형성을 활용한 리팩터링**
    - 반복되는 switch문을 다형성으로 대체하면, 관련 동작을 각 서브클래스로 캡슐화하여 하나의 중앙 집중식 인터페이스를 통해 호출할 수 있습니다.
    - 이 경우 새로운 조건(혹은 새로운 타입)이 추가되더라도, 해당 서브클래스만 구현하면 되므로 코드 변경이 한 곳으로 국한됩니다.

### 리팩토링 전 - switch 문을 이용한 할인 계산

- 아래 코드는 고객의 멤버십 종류에 따라 할인율을 계산하는 로직으로, switch문이 직접 사용되고 있습니다.
- 새로운 멤버십 종류가 추가되면 모든 관련 switch문을 수정해야 합니다.

```java

public class DiscountCalculatorSwitch {
    public double calculateDiscount(String membershipType, double purchaseAmount) {
        switch(membershipType) {
            case "SILVER":
                return purchaseAmount * 0.05;
            case "GOLD":
                return purchaseAmount * 0.10;
            case "PLATINUM":
                return purchaseAmount * 0.15;
            default:
                return 0;
        }
    }

    // 예시 실행
    public static void main(String[] args) {
        DiscountCalculatorSwitch calculator = new DiscountCalculatorSwitch();
        double discount = calculator.calculateDiscount("GOLD", 200.0);
        System.out.println("Discount: " + discount);
    }
}

```

- 문제점:
    - 만약 새로운 멤버십 타입(예: "DIAMOND")이 추가된다면, 여러 곳의 switch문을 모두 찾아 수정해야 합니다.

---

### 리팩토링 후 - 다형성을 활용해 조건부 로직을 캡슐화하기

- 다음은 추상 클래스와 서브클래스를 이용해 각 멤버십의 할인율을 개별적으로 정의한 예제입니다.

```java

// 추상 클래스: 각 멤버십에 대한 할인율을 반환하는 메서드를 선언
public abstract class Membership {
    public abstract double getDiscountRate();
}

// 각 멤버십 타입을 별도의 클래스로 구현
public class SilverMembership extends Membership {
    @Override
    public double getDiscountRate() {
        return 0.05;
    }
}

public class GoldMembership extends Membership {
    @Override
    public double getDiscountRate() {
        return 0.10;
    }
}

public class PlatinumMembership extends Membership {
    @Override
    public double getDiscountRate() {
        return 0.15;
    }
}

// 새로운 멤버십 타입 추가 시, 해당 클래스만 구현하면 됨
public class DiamondMembership extends Membership {
    @Override
    public double getDiscountRate() {
        return 0.20;
    }
}

```

- 이제 DiscountCalculator는 다형성을 이용하여, 각 멤버십 객체가 스스로 할인율을 제공하도록 합니다.

```java

public class DiscountCalculatorPolymorphic {
    public double calculateDiscount(Membership membership, double purchaseAmount) {
        return purchaseAmount * membership.getDiscountRate();
    }

    // 예시 실행
    public static void main(String[] args) {
        DiscountCalculatorPolymorphic calculator = new DiscountCalculatorPolymorphic();

        Membership gold = new GoldMembership();
        double discountGold = calculator.calculateDiscount(gold, 200.0);
        System.out.println("Gold Membership Discount: " + discountGold);

        Membership diamond = new DiamondMembership();
        double discountDiamond = calculator.calculateDiscount(diamond, 200.0);
        System.out.println("Diamond Membership Discount: " + discountDiamond);
    }
}

```

- 개선 효과:
    - 새로운 멤버십 타입이 추가되더라도, 해당 서브클래스만 추가하면 되어 switch문을 여러 곳에서 수정할 필요가 없습니다.
    - 각 클래스가 자신의 할인율을 캡슐화하므로, 관련 로직이 한 곳에 모여 유지보수가 용이합니다.
    - 다형성을 활용하여 조건부 로직을 분산시키면, 코드의 가독성과 확장성이 향상됩니다.

# 3.13 반복문

<aside>
💡

전통적인 반복문 대신 파이프라인 연산을 사용하면 코드의 의도가 명확해지고, 각 단계의 처리가 독립적으로 표현되어 읽기 쉽고 관리하기 쉬운 코드로 탈바꿈할 수 있습니다.

</aside>

- **반복문의 문제점**
    - 반복문은 프로그래밍 초창기부터 존재한 핵심 요소지만, 코드의 각 요소가 어떻게 처리되는지 한눈에 파악하기 어렵고, 복잡한 조건과 내부 로직이 중첩되어 가독성이 떨어집니다.
- **파이프라인 방식의 장점**
    - 일급 함수와 스트림 API 같은 기능을 활용하면, 필터(filter), 매핑(map) 등으로 데이터를 순차적으로 처리할 수 있어 각 단계의 처리가 명확해지고 코드가 간결해집니다.
    - 즉, 반복문 대신 파이프라인 연산을 사용하면 처리 과정이 체계적으로 표현되어 유지보수와 이해가 쉬워집니다.

### 리팩터링 전 - 전통적인 반복문 사용

- 아래 코드는 리스트의 숫자 중에서 10보다 큰 값들을 찾아 제곱한 결과를 새로운 리스트에 저장하고, 출력하는 예제입니다.

```java

import java.util.*;

public class LoopExample {
    public static void main(String[] args) {
        // 예제 데이터: 1부터 12까지의 숫자 리스트
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
        List<Integer> result = new ArrayList<>();

        // 10보다 큰 숫자를 찾아 제곱한 값을 result 리스트에 추가
        for (Integer num : numbers) {
            if (num > 10) {
                result.add(num * num);
            }
        }

        // 결과 출력
        for (Integer value : result) {
            System.out.println(value);
        }
    }
}

```

- 문제점:
    - 조건 검사가 반복문 내부에 섞여 있어, "어떤 숫자가 10보다 큰지", "제곱하는 로직"이 한눈에 파악되기 어렵습니다.
    - 처리 과정이 여러 단계로 나뉘어 코드 전체의 가독성이 떨어집니다.

---

### 리팩터링 후 - 파이프라인(스트림 API) 사용

- Java 8 이상의 스트림 API를 사용하면, 데이터 흐름이 명확하게 드러나고 각 연산이 분리되어 표현됩니다.

```java

import java.util.*;
import java.util.stream.Collectors;

public class PipelineExample {
    public static void main(String[] args) {
        // 예제 데이터: 1부터 12까지의 숫자 리스트
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);

        // 스트림을 이용해 10보다 큰 숫자를 필터링하고, 제곱한 후 결과 리스트로 수집
        List<Integer> result = numbers.stream()
            .filter(num -> num > 10)       // 10보다 큰 숫자만 추출
            .map(num -> num * num)         // 각 숫자를 제곱
            .collect(Collectors.toList()); // 결과를 리스트로 수집

        // 결과 출력: 각 요소에 대해 System.out::println 적용
        result.forEach(System.out::println);
    }
}

```

- 개선 효과:
    - **명확한 처리 단계:**
        - **filter:** 10보다 큰 숫자만 선택
        - **map:** 선택된 숫자를 제곱
        - **collect:** 최종 결과를 리스트로 모음
    - 각 연산이 체인 형태로 이어져 있어, 데이터가 어떻게 변환되는지 한눈에 파악할 수 있습니다.
    - 코드가 간결해지고, 유지보수 및 확장이 용이해집니다.

# 3.14 성의 없는 요소

- **배경**
    - 코드를 구조화하기 위해 함수나 클래스를 만들어 의미 있는 이름을 붙이고, 재활용이나 변형을 염두에 두는 경우가 많습니다.
    - 하지만 계획과 달리, 어떤 함수는 단순히 본문 코드를 그대로 호출하는 래퍼(wrapper)에 불과하거나,
    - 메서드가 하나뿐인 클래스가 되어 제 역할을 하지 못하는 경우도 있습니다.
- **문제**
    - 불필요하게 분리된 함수나 클래스는 코드의 복잡성을 증가시키고,실제로는 본문 코드와 큰 차이가 없으므로 오히려 가독성을 해칩니다.
    - 원래는 추가적인 기능이 들어갈 여지가 있었지만, 여러 사정으로 본문이 채워지지 않았거나,리팩터링 후에 역할이 축소되어 쓸모없어진 경우가 이에 해당합니다.
- **해결 방법:**
    - **함수 인라인하기(Inline Function):**불필요한 래퍼 함수의 본문을 호출하는 쪽에 직접 삽입하여, 중간 단계를 제거합니다.
    - **클래스 인라인하기(Inline Class):**기능이 거의 없는(메서드가 하나뿐인) 클래스를 해당 기능을 사용하는 곳으로 합쳐서 제거합니다.
    - **상속 계층 합치기(Merge Hierarchy):**상속 구조에서 서브클래스와 슈퍼클래스가 거의 동일한 역할을 할 때, 이를 합쳐서 계층 구조를 단순화합니다.

## 1. 함수 인라인하기 전 후 예제

### **리팩터링 전 -  불필요한 래퍼 함수가 있는 경우**

```java

public class Calculator {
    // public 메서드가 내부의 래퍼 함수 addInternal()를 호출하는 형태
    public int add(int a, int b) {
        return addInternal(a, b);
    }

    // 단순히 두 숫자를 더하는 함수
    private int addInternal(int a, int b) {
        return a + b;
    }
}

public class MainBefore {
    public static void main(String[] args) {
        Calculator calc = new Calculator();
        int result = calc.add(3, 5);
        System.out.println("Result: " + result);
    }
}

```

- 문제점:
    - `add()` 메서드가 단순히 `addInternal()`을 호출하는 래퍼 역할만 함
    - 중간 단계가 불필요하게 존재하여 코드가 복잡해짐

### **리팩터링 후 - 함수 인라인하기 적용**

```java

public class Calculator {
    // 래퍼 함수를 제거하고, 직접 덧셈 연산 수행
    public int add(int a, int b) {
        return a + b;
    }
}

public class MainAfter {
    public static void main(String[] args) {
        Calculator calc = new Calculator();
        int result = calc.add(3, 5);
        System.out.println("Result: " + result);
    }
}

```

- 개선 효과:
    - 불필요한 함수 호출이 제거되어 코드가 단순해지고,덧셈 연산의 의도가 바로 드러납니다.

---

## 2. 클래스 인라인하기 전 후 예제

### **리팩터링 전 - 단 하나의 메서드만 가진 불필요한 클래스**

- 예를 들어, 단순한 인사 메시지를 반환하는 클래스가 있다고 가정해봅니다.

```java

// 별도의 클래스로 인사 메시지를 반환하는 래퍼 클래스
public class GreetingService {
    public String getGreeting() {
        return "Hello, world!";
    }
}

public class MainBefore {
    public static void main(String[] args) {
        GreetingService service = new GreetingService();
        System.out.println(service.getGreeting());
    }
}

```

- 문제점:
    - `GreetingService` 클래스는 단 하나의 메서드만 제공하며,별도의 클래스가 없어도 본문에 직접 인사 메시지를 작성할 수 있는 수준입니다.

### **리팩터링 후 - 클래스 인라인하기 적용**

```java

public class MainAfter {
    public static void main(String[] args) {
        // 불필요한 클래스를 제거하고, 직접 인사 메시지를 출력
        System.out.println("Hello, world!");
    }
}

```

- 개선 효과:
    - 불필요한 클래스를 제거함으로써, 코드의 구조가 단순해지고불필요한 추상화가 사라집니다.

# 3.15 추측성 일반화

<aside>
💡

**추측성 일반화** 냄새가 감지되면 지금 당장 사용되지 않는 코드나 매개변수를 제거하는 리팩터링(함수/클래스 인라인, 계층 합치기, 함수 선언 바꾸기 등)을 적용하여, 불필요한 복잡성을 없애고 코드베이스를 깔끔하게 유지하는 것이 좋습니다.

</aside>

- **추측성 일반화란?**
    - "나중에 필요할 거야"라는 생각으로 현재는 사용되지 않는 후킹 포인트, 추상 클래스, 특이 케이스 처리 로직, 그리고 필요 없는 매개변수 등을 미리 추가하는 것을 말합니다.
    - 이런 코드는 실제로 사용되지 않으면 코드베이스를 복잡하게 만들고 이해와 유지보수를 어렵게 합니다.
- **문제점:**
    - 불필요한 추상화나 위임이 남아 있으면 코드가 산만해지고,실질적인 역할을 하는 본문 코드와 차이가 거의 없으면서도 관리해야 할 요소가 늘어납니다.
    - 사용되지 않는 매개변수나 후킹 포인트가 여러 곳에 존재하면,나중에 변경사항을 찾거나 적용하기가 번거로워집니다.
- **해결 방법:**
    - **함수 인라인하기/클래스 인라인하기:** 쓸데없이 위임하는 코드나 메서드가 있다면 본문으로 합칩니다.
    - **계층 합치기:** 역할이 거의 없는 추상 클래스나 상속 구조가 있다면 합쳐서 단순화합니다.
    - **함수 선언 바꾸기:** 본문에서 사용되지 않는 매개변수는 삭제합니다.
    - **죽은 코드 제거하기:** 테스트 코드 외에 사용되지 않는 코드부터 제거합니다.

### 리팩터링 전 - 추측성 일반화가 적용된 코드

- 아래 예제는 미래 확장용으로 작성한 불필요한 후킹 포인트와 추상 클래스로 구성되어 있습니다.
- 예를 들어, 주문을 처리하는 과정에서 나중에 다양한 전처리/후처리 로직이 필요할 거라 생각해 추상 클래스를 만들었지만, 현재는 실제로 아무런 확장도 이루어지지 않아 단순히 본문 코드를 감싸는 용도로만 사용되고 있습니다.

```java

// 미래 확장을 대비하여 만든 추상 클래스 (현재는 단순 래퍼 역할만 함)
abstract class OrderProcessor {
    // 추후 전처리 로직을 추가할 후킹 포인트 (현재는 아무런 동작도 없음)
    protected void preProcess(Order order) {
        // "나중에 필요할 거야"라고 추가한 코드 (실제로는 사용되지 않음)
    }

    // 추후 후처리 로직을 추가할 후킹 포인트 (현재는 아무런 동작도 없음)
    protected void postProcess(Order order) {
        // 불필요한 위임
    }

    // 실제 주문 처리 로직 (하지만 매개변수에 불필요한 enableLogging 포함)
    public abstract void processOrder(Order order, boolean enableLogging);
}

// 현재 사용되는 유일한 구현체 (하지만 추상 클래스의 후킹 포인트와 매개변수까지 상속받음)
class SimpleOrderProcessor extends OrderProcessor {
    @Override
    public void processOrder(Order order, boolean enableLogging) {
        preProcess(order);  // 아무런 역할도 하지 않음
        // 핵심 주문 처리 로직
        System.out.println("Processing order: " + order.getId());
        postProcess(order); // 아무런 역할도 하지 않음

        // enableLogging 매개변수는 실제로 아무 의미 없이 존재함
        if (enableLogging) {
            System.out.println("Logging is enabled (but not really used)");
        }
    }
}

class Order {
    private int id;

    public Order(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}

// 테스트용 Main (실제 사용 시에도 단 한 곳에서만 사용됨)
public class MainBefore {
    public static void main(String[] args) {
        Order order = new Order(1001);
        OrderProcessor processor = new SimpleOrderProcessor();
        // enableLogging 매개변수는 항상 false 혹은 필요 없는 값으로 전달됨
        processor.processOrder(order, false);
    }
}

```

- 문제점:
    - 추상 클래스와 후킹 포인트가 미래 확장을 위해 미리 만들어졌지만, 현재는 전혀 사용되지 않아코드 복잡도만 증가시킵니다.
    - 불필요한 매개변수 `enableLogging` 역시 실제로 활용되지 않습니다.

---

### 리팩터링 후 - 불필요한 추상화와 매개변수 제거

- 불필요한 추상 클래스를 제거하고, 핵심 주문 처리 로직만 남도록 단순화합니다.
- 즉, 후킹 포인트 없이 본문 코드가 직접 구현된 단일 클래스가 됩니다.

```java

// 불필요한 추상 클래스를 제거하고, 단일 클래스로 통합
class OrderProcessor {
    // 핵심 주문 처리 로직만 남김
    public void processOrder(Order order) {
        // 단순 주문 처리
        System.out.println("Processing order: " + order.getId());
    }
}

class Order {
    private int id;

    public Order(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}

public class MainAfter {
    public static void main(String[] args) {
        Order order = new Order(1001);
        OrderProcessor processor = new OrderProcessor();
        processor.processOrder(order);
    }
}

```

- 개선 효과:
    - 추측성 일반화로 인해 추가된 불필요한 추상 클래스와 후킹 포인트, 그리고 사용되지 않는 매개변수를 모두 제거하여,코드가 단순해지고 명확해졌습니다.
    - 주문 처리 로직이 한 곳에 집중되어 있어 유지보수성이 높아졌으며,미래에 실제로 필요한 확장이 있다면 그때 추가하는 방식으로 코드를 관리할 수 있습니다.

# 3.16 임시 필드

<aside>
💡

임시 필드가 존재하는 경우에는, 해당 필드를 별도의 클래스로 추출하고 관련 로직을 이동시켜 코드의 구조와 가독성을 개선할 수 있습니다.

</aside>

- **문제 상황**
    - 어떤 클래스에 특정 상황에서만 값이 설정되는 임시 필드가 존재하면, 객체를 사용할 때 모든 필드가 유효한 값으로 채워져 있다고 기대하는 사용자 입장에서 혼란이 발생합니다.
    - 또한, 임시 필드와 관련된 조건부 로직(예: 필드의 유효성을 검사하는 코드)이 산발적으로 존재하면, 왜 그 필드가 존재하는지 파악하기 어려워지고 코드 유지보수가 힘들어집니다.
- **해결책**
    - **클래스 추출하기:** 임시 필드들을 별도의 클래스로 추출하여,임시 필드와 관련된 데이터를 한 곳에 모읍니다.
    - **함수 옮기기:** 임시 필드와 관련된 로직(예: 유효성 검사, 출력 등)을 새 클래스에 모두 이동시킵니다.
    - (필요한 경우) 조건부 로직에 의해 임시 필드가 유효하지 않을 때의 대안을 처리하는 특이 케이스 클래스를 도입하여불필요한 조건부 분기를 제거할 수 있습니다.

### 리팩터링 전 -임시 필드가 그대로 존재하는 Employee 클래스

```java

// 리팩터링 전: 임시 필드가 Employee 클래스 내에 그대로 존재함
public class Employee {
    private String name;
    private String permanentAddress;
    // 임시 필드: 단기 근무 시에만 설정되는 임시 주소 (대부분 null일 수 있음)
    private String temporaryAddress;

    public Employee(String name, String permanentAddress) {
        this.name = name;
        this.permanentAddress = permanentAddress;
    }

    public String getName() {
        return name;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public String getTemporaryAddress() {
        return temporaryAddress;
    }

    public void setTemporaryAddress(String temporaryAddress) {
        this.temporaryAddress = temporaryAddress;
    }

    // 임시 필드의 유효성에 따라 출력하는 조건부 로직이 포함되어 있음
    public void printAddresses() {
        System.out.println("Permanent Address: " + permanentAddress);
        if (temporaryAddress != null && !temporaryAddress.isEmpty()) {
            System.out.println("Temporary Address: " + temporaryAddress);
        }
    }
}

// 테스트용 Main (리팩터링 전)
public class MainBefore {
    public static void main(String[] args) {
        Employee emp1 = new Employee("Alice", "123 Main St");
        // emp1은 임시 주소가 없음

        Employee emp2 = new Employee("Bob", "456 Oak Ave");
        // Bob은 임시 주소가 있음
        emp2.setTemporaryAddress("789 Pine Rd");

        System.out.println("Employee 1:");
        emp1.printAddresses();
        System.out.println();

        System.out.println("Employee 2:");
        emp2.printAddresses();
    }
}

```

> 문제점:
>
> - `temporaryAddress` 필드가 항상 존재하지만, 대부분의 경우에는 값이 설정되지 않아 왜 존재하는지 이해하기 어렵습니다.
> - 임시 필드와 관련된 조건부 로직(출력 시 필드의 유효성 검사)이 Employee 클래스 곳곳에 분산되어 코드가 산만해집니다.

---

### 리팩터링 후 - 임시 필드를 별도의 클래스로 추출하고 관련 로직을 이동

먼저, 임시 주소와 관련된 데이터를 관리할 별도의 클래스를 만듭니다.

```java

// 새 클래스: TemporaryAssignment
public class TemporaryAssignment {
    private String temporaryAddress;

    public TemporaryAssignment(String temporaryAddress) {
        this.temporaryAddress = temporaryAddress;
    }

    public String getTemporaryAddress() {
        return temporaryAddress;
    }

    // 임시 주소 관련 로직(예: 출력)도 캡슐화
    public void printTemporaryAddress() {
        System.out.println("Temporary Address: " + temporaryAddress);
    }
}

```

그 다음, Employee 클래스에서는 더 이상 임시 주소를 기본 필드로 갖지 않고,

필요한 경우에만 TemporaryAssignment 객체를 참조하도록 변경합니다.

```java

// 리팩터링 후: Employee 클래스에서는 임시 주소 관련 필드와 로직을 제거하고,
// TemporaryAssignment 객체를 참조하여 사용함
public class Employee {
    private String name;
    private String permanentAddress;
    // 임시 주소 관련 로직은 별도의 클래스에 캡슐화 (없을 수도 있음)
    private TemporaryAssignment temporaryAssignment;

    public Employee(String name, String permanentAddress) {
        this.name = name;
        this.permanentAddress = permanentAddress;
    }

    public String getName() {
        return name;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public TemporaryAssignment getTemporaryAssignment() {
        return temporaryAssignment;
    }

    public void setTemporaryAssignment(TemporaryAssignment temporaryAssignment) {
        this.temporaryAssignment = temporaryAssignment;
    }

    // 주소 출력 시, 임시 주소 관련 로직은 TemporaryAssignment 객체에 위임
    public void printAddresses() {
        System.out.println("Permanent Address: " + permanentAddress);
        if (temporaryAssignment != null) {
            temporaryAssignment.printTemporaryAddress();
        }
    }
}

```

테스트용 Main 클래스는 다음과 같이 사용합니다.

```java

// 테스트용 Main (리팩터링 후)
public class MainAfter {
    public static void main(String[] args) {
        Employee emp1 = new Employee("Alice", "123 Main St");
        // emp1은 임시 주소가 없음

        Employee emp2 = new Employee("Bob", "456 Oak Ave");
        // Bob은 임시 주소가 있음: TemporaryAssignment 객체를 생성하여 설정
        emp2.setTemporaryAssignment(new TemporaryAssignment("789 Pine Rd"));

        System.out.println("Employee 1:");
        emp1.printAddresses();
        System.out.println();

        System.out.println("Employee 2:");
        emp2.printAddresses();
    }
}

```

- 개선 효과:
    - **캡슐화:** 임시 필드와 관련된 데이터와 로직이 `TemporaryAssignment` 클래스에 집중되어,Employee 클래스는 항상 완전한 데이터를 갖고 있다는 가정 하에 동작합니다.
    - **명확성:** 임시 필드가 왜 존재하는지, 언제 사용되는지 명확하게 분리되어 코드 이해도가 높아집니다.
    - **유지보수성:** 임시 필드 관련 로직이 한 곳에 집중되어, 이후 해당 기능이 확장되거나 수정될 때변경 범위가 한정됩니다.

# 3.17 메시지 체인

<aside>
💡

**메시지 체인**을 발견하면 위임 숨기기(refactoring by hiding delegation) 기법을 적용하여, 클라이언트가 객체 내비게이션에 종속되지 않고, 단일 메서드를 통해 원하는 결과를 얻도록 코드를 개선할 수 있습니다.

</aside>

- **메시지 체인이란?**
    - 클라이언트가 한 객체를 통해 다른 객체를 얻고, 그 객체에서 다시 또 다른 객체를 요청하는 식으로(예: `aPerson.getDepartment().getManager().getName()`) 연쇄적으로 호출하는 코드를 말합니다.
    - 이런 체인은 클라이언트가 내부 객체 구조(내비게이션 구조)에 의존하게 만들어, 내부 구조가 바뀌면 클라이언트 코드도 함께 수정해야 하는 문제를 일으킵니다.
- **문제점:**
    - 클라이언트 코드가 객체 내비게이션(예: 부서, 관리자)을 직접 다루게 되어,내부 구조의 변경이 클라이언트에 파급됩니다.
    - 여러 곳에서 같은 메시지 체인을 사용하면, 동일한 연쇄 호출이 중복되어 나타납니다.
- **해결책 – 위임 숨기기(Hide Delegation):**
    - 체인의 최종 결과(예: 관리자 이름)를 직접 제공하는 메서드를 추가하여,클라이언트가 내부 구조를 거치지 않고도 원하는 결과를 얻도록 합니다.
    - 예를 들어, `aPerson.getManagerName()`처럼 체인을 단축하면,내부의 부서나 관리자 객체의 존재를 클라이언트에게 숨길 수 있습니다.

### 리팩터링 전 - 메시지 체인을 그대로 사용하는 코드

- 여기서는 `Person` 객체가 부서(`Department`)를 갖고 있고, 부서가 관리자를(`Manager`) 갖고 있어 최종적으로 부서장의 이름을 얻기 위해 아래와 같이 체인으로 접근하는 예제입니다.

```java

// Manager 클래스: 관리자의 이름을 가진다.
class Manager {
    private String name;

    public Manager(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

// Department 클래스: 부서가 관리자를 갖는다.
class Department {
    private Manager manager;

    public Department(Manager manager) {
        this.manager = manager;
    }

    public Manager getManager() {
        return manager;
    }
}

// Person 클래스: 사람은 부서를 갖는다.
class Person {
    private String name;
    private Department department;

    public Person(String name, Department department) {
        this.name = name;
        this.department = department;
    }

    public String getName() {
        return name;
    }

    public Department getDepartment() {
        return department;
    }
}

// 클라이언트 코드: 메시지 체인을 통해 부서장의 이름을 얻음.
public class MainBefore {
    public static void main(String[] args) {
        Manager manager = new Manager("John Doe");
        Department department = new Department(manager);
        Person person = new Person("Alice", department);

        // 메시지 체인을 사용하여 부서장의 이름을 얻음
        String managerName = person.getDepartment().getManager().getName();
        String report = managerName + "께 " + person.getName() + " 님의 작업 로그";
        System.out.println(report);
    }
}

```

- 문제점:

  클라이언트는 `Person` 내부의 부서와 관리자 객체에 대해 직접 알아야 하며,

  내부 구조가 바뀌면 이 체인도 수정되어야 합니다.


---

### 리팩터링 후 - 위임 숨기기를 적용하여 메시지 체인 숨기기

내부 구조를 감추기 위해 `Person` 클래스에 `getManagerName()` 메서드를 추가합니다.

이제 클라이언트는 `Person`에게 바로 부서장의 이름을 요청할 수 있습니다.

```java

// Manager 클래스는 그대로 유지
class Manager {
    private String name;

    public Manager(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

// Department 클래스는 그대로 유지
class Department {
    private Manager manager;

    public Department(Manager manager) {
        this.manager = manager;
    }

    public Manager getManager() {
        return manager;
    }
}

// Person 클래스: 내부 메시지 체인을 숨기기 위해 getManagerName() 추가
class Person {
    private String name;
    private Department department;

    public Person(String name, Department department) {
        this.name = name;
        this.department = department;
    }

    public String getName() {
        return name;
    }

    // 내부 구조를 감추고 최종 결과만 제공하는 메서드
    public String getManagerName() {
        // 내부적으로 부서와 관리자를 거쳐 관리자 이름을 얻지만, 클라이언트는 알 필요 없음.
        return department.getManager().getName();
    }

    // 기존 getDepartment()도 있을 수 있으나, 클라이언트가 직접 사용하지 않도록 주의.
    public Department getDepartment() {
        return department;
    }
}

// 클라이언트 코드: 단순히 Person의 getManagerName()을 사용하여 보고서를 생성
public class MainAfter {
    public static void main(String[] args) {
        Manager manager = new Manager("John Doe");
        Department department = new Department(manager);
        Person person = new Person("Alice", department);

        // 이제 내부 메시지 체인을 숨기고, 단순히 getManagerName()만 호출함
        String managerName = person.getManagerName();
        String report = managerName + "께 " + person.getName() + " 님의 작업 로그";

        // 또는, 보고서 생성 로직을 별도의 모듈(예: ReportAutoGenerator)로 추출할 수도 있음.
        System.out.println(report);
    }
}

```

- 개선 효과:
    - 클라이언트는 `Person` 내부의 부서와 관리자 객체를 알 필요 없이,단순히 `getManagerName()` 메서드만 호출하여 결과를 얻습니다.
    - 내부 구조가 변경되어도 `getManagerName()` 구현만 수정하면 되므로,클라이언트 코드는 영향을 받지 않습니다.
    - 코드가 간결해지고, 객체 간의 위임을 통해 책임이 올바르게 분배됩니다.

# 3.18 중개자

<aside>
💡

캡슐화 과정에서 위임 메서드가 지나치게 많아지면, "중개자 제거하기" 및 "함수 인라인하기" 기법을 적용하여 실제 일을 하는 객체와의 직접 소통을 유도하면 좋습니다.

</aside>

- **문제 상황**
    - 캡슐화는 외부에 세부사항을 숨기는 중요한 역할을 합니다.
    - 이를 위해 내부의 작업을 다른 객체에게 위임하는 경우가 많은데, 클래스의 메서드 중 상당 부분이 단순히 다른 객체에 작업을 위임하기만 한다면, 결국 호출자가 내부 객체(예: 팀장의 일정 관리 시스템) 구조에 지나치게 의존하게 됩니다.
- **문제점**
    - 예를 들어, 팀장 객체가 "내 일정 확인 후 회의 시간 반환" 같은 메서드를 제공하는데, 이 메서드가 단순히 일정 객체의 메서드를 호출하는 래퍼(wrapper) 역할만 한다면, 팀장의 내부 위임(중개) 메서드가 중간에 끼어 있는 것이 불필요한 복잡성을 초래합니다.
- **해결 방법**
    - **중개자 제거하기 (Remove Middleman):**단순 위임만 하는 메서드를 제거하여, 클라이언트가 실제 일을 하는 객체와 직접 소통하게 합니다.
    - **함수 인라인하기 (Inline Function):**위임 메서드를 제거한 후 남는 로직이 거의 없다면,호출하는 쪽에 해당 호출을 인라인시켜 코드를 단순화합니다.

### 리팩터링 전 - (위임 메서드가 있는 상태)

- 다음은 `TeamLeader` 클래스가 내부의 `Schedule` 객체에 단순 위임하는 메서드를 제공하는 예제입니다.

```java

// 실제 일정 정보를 관리하는 클래스
class Schedule {
    public String getAvailableMeetingTime() {
        // 실제로는 복잡한 일정 계산 로직이 있을 수 있음
        return "10:00 AM";
    }
}

// 팀장 클래스는 Schedule 객체를 캡슐화하고, 위임 메서드를 통해 외부에 제공함
class TeamLeader {
    private Schedule schedule;

    public TeamLeader(Schedule schedule) {
        this.schedule = schedule;
    }

    // 단순 위임 메서드: 내부 Schedule 객체의 메서드를 그대로 호출함
    public String getAvailableMeetingTime() {
        return schedule.getAvailableMeetingTime();
    }
}

// 클라이언트 코드: 팀장을 통해 회의 시간을 요청함
public class MainBefore {
    public static void main(String[] args) {
        Schedule schedule = new Schedule();
        TeamLeader leader = new TeamLeader(schedule);

        // 클라이언트는 팀장의 위임 메서드를 통해 회의 시간을 얻는다.
        String meetingTime = leader.getAvailableMeetingTime();
        System.out.println("Meeting time: " + meetingTime);
    }
}

```

- 문제
    - `TeamLeader`의 `getAvailableMeetingTime()` 메서드는 단순히 내부 `Schedule` 객체에 위임만 하므로,중개 역할만 수행하며 불필요한 레이어를 추가하고 있음.

---

### 리팩터링 후 - (중개자 제거 및 함수 인라인 적용)

- 위임 메서드가 단순 래퍼 역할만 한다면, 이를 제거하고 클라이언트가 직접 `Schedule` 객체의 메서드를 호출하도록 변경하거나, 필요하다면 호출부에 인라인하여 단순화할 수 있습니다.

**방법 1: 클라이언트가 직접 Schedule을 사용하도록 변경**

```java

// Schedule 클래스는 그대로 유지
class Schedule {
    public String getAvailableMeetingTime() {
        return "10:00 AM";
    }
}

// TeamLeader 클래스에서는 단순 위임 메서드를 제거한다.
class TeamLeader {
    // 팀장의 다른 역할이 있다면 남길 수 있지만, 회의 시간 요청과 관련된 위임은 제거됨.
    // 필요하다면 TeamLeader는 다른 정보나 로직만 제공.
}

// 클라이언트 코드: 이제 팀장의 위임 메서드를 거치지 않고, Schedule 객체를 직접 사용함
public class MainAfter {
    public static void main(String[] args) {
        Schedule schedule = new Schedule();

        // 클라이언트는 직접 Schedule의 메서드를 호출한다.
        String meetingTime = schedule.getAvailableMeetingTime();
        System.out.println("Meeting time: " + meetingTime);
    }
}

```

**방법 2: 팀장 클래스 내부에 위임 메서드를 인라인하여 클라이언트에 노출**

- 만약 팀장의 다른 역할 때문에 클라이언트를 TeamLeader를 사용하도록 강제해야 한다면, 위임 메서드를 인라인한 형태로 변경할 수도 있습니다.
- 예를 들어, 클라이언트가 "팀장"이라는 객체를 사용해야 하는 상황이라면, 팀장이 자신의 Schedule 객체를 외부에 노출하는 방식으로 수정할 수 있습니다.

```java

// Schedule 클래스는 그대로 유지
class Schedule {
    public String getAvailableMeetingTime() {
        return "10:00 AM";
    }
}

// TeamLeader 클래스는 Schedule을 직접 노출하는 메서드를 제공한다.
class TeamLeader {
    private Schedule schedule;

    public TeamLeader(Schedule schedule) {
        this.schedule = schedule;
    }

    // 인라인된 형태: getAvailableMeetingTime() 호출 대신 getSchedule()을 통해 직접 접근하도록 유도
    public Schedule getSchedule() {
        return schedule;
    }
}

// 클라이언트 코드: 팀장의 schedule을 통해 회의 시간을 직접 얻는다.
public class MainAfterInline {
    public static void main(String[] args) {
        Schedule schedule = new Schedule();
        TeamLeader leader = new TeamLeader(schedule);

        // 클라이언트는 팀장의 getSchedule()을 호출한 후, Schedule 객체의 메서드를 직접 사용
        String meetingTime = leader.getSchedule().getAvailableMeetingTime();
        System.out.println("Meeting time: " + meetingTime);
    }
}

```

- 개선 효과:
    - 위임 메서드가 제거되거나 인라인되면, 클라이언트가 내부 구조에 덜 의존하게 되어변경 시 수정 범위가 줄어듭니다.
    - 단순 위임만 하는 메서드를 제거함으로써 코드가 단순해지고,불필요한 중개 레이어를 없애서 가독성과 유지보수성이 향상됩니다.

# 3.19 내부자 거래

<aside>
💡

모듈 간 데이터 거래를 최소화하고, 불필요한 중개(위임) 또는 내부 필드 접근을 제거하면, 코드의 결합도를 낮추어 유지보수성이 높은 소프트웨어 구조를 만들 수 있습니다.

</aside>

- **문제의 배경**
    - 소프트웨어 개발자는 모듈 사이에 단단한 벽(캡슐화)을 세워서 내부 세부사항을 감추고자 합니다.
    - 하지만 실제로 여러 모듈 간에 데이터를 주고받으면 결합도가 높아져 변경이 어려워집니다.
    - 예를 들어, 한 모듈이 다른 모듈의 내부 필드나 함수를 계속 참조하거나 호출하면, 내부 구조가 바뀔 때마다 클라이언트 코드도 수정해야 하는 문제가 발생합니다.
- **해결책**
    - **함수 옮기기와 필드 옮기기:**모듈 간에 은밀하게 데이터를 주고받는(즉, 불필요하게 서로의 내부를 참조하는) 코드는해당 기능을 실제로 처리하는 모듈로 옮겨서, 외부에서는 간단한 위임만 하도록 만듭니다.
    - **공통 관심사의 처리:**여러 모듈이 같은 관심사를 공유한다면, 이를 전담하는 제3의 모듈을 새로 만들거나위임 숨기기 기법을 사용하여 중개자 역할을 하도록 합니다.
    - **상속 구조의 경우:**부모-자식 사이에 과도하게 결탁되어 자식이 부모의 내부 세부사항에 지나치게 의존하는 경우,서브클래스를 위임(delegate)으로 바꾸거나 슈퍼클래스를 위임으로 바꾸는 리팩터링 기법을사용하여 결합도를 낮춥니다.

## 모듈 간 데이터 거래를 줄이기 위한 함수/필드 옮기기와 위임 숨기기

### 리팩터링 전 - 모듈 간 직접 데이터 접근으로 인한 결합도 상승

- 아래 예제에서는 주문 처리 모듈(`OrderProcessor`)이 배송 계산 모듈(`ShippingCalculator`)의 내부 필드(예: `shippingRate`, `weightFactor`)에 직접 접근하여 배송비를 계산하고 있습니다. 이렇게 되면 두 모듈 간 결합도가 높아져, 배송 계산 로직이나 내부 데이터 구조가 바뀌면 주문 처리 코드도 함께 수정해야 합니다.

```java

// 배송 계산 모듈 – 내부 필드가 public(또는 package-private)으로 노출되어 있다고 가정
class ShippingCalculator {
    public double shippingRate;
    public double weightFactor;

    public ShippingCalculator(double shippingRate, double weightFactor) {
        this.shippingRate = shippingRate;
        this.weightFactor = weightFactor;
    }

    // 내부 계산 로직이 있을 수도 있으나, 클라이언트가 직접 필드에 접근함
    public double calculateShipping(double weight) {
        return weight * shippingRate * weightFactor;
    }
}

// 주문 처리 모듈 – 배송 계산 모듈의 내부 필드에 직접 접근하여 계산
class OrderProcessor {
    private ShippingCalculator shippingCalculator;

    public OrderProcessor(ShippingCalculator shippingCalculator) {
        this.shippingCalculator = shippingCalculator;
    }

    public double processOrder(double orderWeight) {
        // 직접 shippingCalculator의 필드에 접근하여 배송비 계산
        double rate = shippingCalculator.shippingRate;
        double factor = shippingCalculator.weightFactor;
        double shippingCost = orderWeight * rate * factor;
        // 주문 처리의 다른 로직들...
        return shippingCost;
    }
}

// 리팩터링 전 클라이언트 코드
public class MainBefore {
    public static void main(String[] args) {
        ShippingCalculator calculator = new ShippingCalculator(1.2, 0.8);
        OrderProcessor processor = new OrderProcessor(calculator);

        double cost = processor.processOrder(10); // 10 단위 무게의 주문
        System.out.println("Shipping Cost: " + cost);
    }
}

```

- 문제점:
    - `OrderProcessor`가 `ShippingCalculator`의 내부 데이터에 의존하여,배송 계산 로직의 세부사항이 외부로 노출되어 있습니다.
    - 만약 배송 계산 방식이 변경되면, 두 모듈 간의 직접적인 데이터 거래 때문에클라이언트 코드까지 영향을 받게 됩니다.

---

### 리팩터링 후 - 함수 옮기기/필드 옮기기와 위임 숨기기를 적용하여 결합도 낮추기

- 리팩터링 후에는 배송 계산 모듈 내부의 데이터를 외부에 노출하지 않고, 배송 계산을 위한 기능을 모듈 내에 캡슐화합니다.
- 즉, `OrderProcessor`는 단순히 `ShippingCalculator`의 공개된 메서드를 호출하여 배송비를 계산하게 합니다.

```java

// 리팩터링 후: ShippingCalculator – 내부 필드는 private으로 감추고, 계산 메서드를 공개
class ShippingCalculatorRefactored {
    private double shippingRate;
    private double weightFactor;

    public ShippingCalculatorRefactored(double shippingRate, double weightFactor) {
        this.shippingRate = shippingRate;
        this.weightFactor = weightFactor;
    }

    // 외부에는 단순히 이 메서드를 통해 배송비 계산 결과를 제공
    public double calculateShipping(double weight) {
        return weight * shippingRate * weightFactor;
    }
}

// 리팩터링 후: OrderProcessor – 내부 필드에 접근하지 않고, 위임을 통해 배송 계산을 요청
class OrderProcessorRefactored {
    private ShippingCalculatorRefactored shippingCalculator;

    public OrderProcessorRefactored(ShippingCalculatorRefactored shippingCalculator) {
        this.shippingCalculator = shippingCalculator;
    }

    public double processOrder(double orderWeight) {
        // 내부 세부 사항을 숨기고, calculateShipping() 메서드를 통해 결과만 얻음
        double shippingCost = shippingCalculator.calculateShipping(orderWeight);
        // 주문 처리의 다른 로직들...
        return shippingCost;
    }
}

// 리팩터링 후 클라이언트 코드
public class MainAfter {
    public static void main(String[] args) {
        ShippingCalculatorRefactored calculator = new ShippingCalculatorRefactored(1.2, 0.8);
        OrderProcessorRefactored processor = new OrderProcessorRefactored(calculator);

        double cost = processor.processOrder(10); // 10 단위 무게의 주문
        System.out.println("Shipping Cost: " + cost);
    }
}

```

- 개선 효과:
    - `ShippingCalculatorRefactored`는 내부 데이터를 캡슐화하고 계산 로직을 하나의 메서드에 모아둠으로써,모듈 간 데이터 거래를 최소화합니다.
    - `OrderProcessorRefactored`는 내부 데이터에 직접 접근하지 않고, 단순히 계산 메서드만 호출하여필요한 결과만 받아 사용하므로 결합도가 낮아집니다.
    - 만약 배송 계산 방식이 변경되더라도, `ShippingCalculatorRefactored` 내부만 수정하면 되므로클라이언트 코드는 영향을 받지 않습니다.

### 상속 구조에서의 결탁 해소 (참고)

- 텍스트에서는 상속 구조에서 부모와 자식 사이에 결탁(과도한 정보 공유)이 발생할 때, 서브클래스를 위임으로 바꾸거나 슈퍼클래스를 위임으로 바꾸는 기법을 권장합니다.
- 예를 들어, 자식 클래스가 부모의 내부 필드를 지나치게 참조하고 있다면, 부모의 내부 데이터를 외부에 공개하지 않고, 별도의 위임 객체로 대체하여 정보 은닉과 결합도 감소를 도모할 수 있습니다.

# 3.20 거대한 클래스

<aside>
💡

관련 있는 필드들을 하나의 클래스로 추출하여 관리하면, 클래스의 복잡도를 낮추고, 중복 코드를 줄이며, 전체 코드의 가독성과 유지보수성을 크게 향상시킬 수 있습니다.

</aside>

- **문제 상황**
    - 한 클래스가 너무 많은 일을 하다 보면, 관련 없는 여러 종류의 필드가 한 클래스에 모여 중복 코드나 이해하기 어려운 코드를 야기할 수 있습니다. 예를 들어, 금액과 통화가 따로 저장된 `depositAmount`와 `depositCurrency` 같은 필드들이 그렇습니다.
    - 이런 필드들은 같은 개념(금액)을 나타내므로 함께 묶어 관리하는 것이 바람직합니다.
- **해결책**
    - **클래스 추출하기 (Extract Class):**같은 컴포넌트에 속하는 필드들을 하나의 새로운 클래스(예: Money)로 묶어원래의 클래스에서는 이 새로운 객체만 관리하게 합니다.
    - **추가 리팩터링:**만약 원래 클래스 내에서 중복되는 로직이 여러 메서드에 퍼져 있다면,공통 부분을 작은 메서드들로 추출하여 중복을 제거합니다.
    - **클라이언트 사용 패턴 분석:**클라이언트들이 거대 클래스의 특정 기능 그룹만 주로 사용하는 경우,해당 기능들을 별도의 클래스로 추출하여 각각의 역할을 명확하게 분리합니다.

### 리팩터링 전

아래 코드는 `BankAccount` 클래스가 여러 종류의 필드를 직접 보유하고 있어 복잡해진 경우입니다.

```java

// 리팩터링 전: BankAccount 클래스는 입금과 출금 정보를 개별 필드로 관리
public class BankAccount {
    private String accountNumber;
    private String owner;
    // 입금 관련 필드
    private double depositAmount;
    private String depositCurrency;
    // 출금 관련 필드
    private double withdrawalAmount;
    private String withdrawalCurrency;

    public BankAccount(String accountNumber, String owner,
                       double depositAmount, String depositCurrency,
                       double withdrawalAmount, String withdrawalCurrency) {
        this.accountNumber = accountNumber;
        this.owner = owner;
        this.depositAmount = depositAmount;
        this.depositCurrency = depositCurrency;
        this.withdrawalAmount = withdrawalAmount;
        this.withdrawalCurrency = withdrawalCurrency;
    }

    // 입금과 출금 정보를 각각 출력하는 메서드
    public void printTransactionDetails() {
        System.out.println("Deposit: " + depositAmount + " " + depositCurrency);
        System.out.println("Withdrawal: " + withdrawalAmount + " " + withdrawalCurrency);
    }

    // 순 잔액 계산 (입금액 - 출금액, 단순 계산)
    public double calculateNetBalance() {
        return depositAmount - withdrawalAmount;
    }

    // ... 그 외에도 중복되는 로직이 여러 곳에 산재할 수 있음
}

public class MainBefore {
    public static void main(String[] args) {
        BankAccount account = new BankAccount("12345", "Alice",
                1000.0, "USD", 200.0, "USD");
        account.printTransactionDetails();
        System.out.println("Net Balance: " + account.calculateNetBalance());
    }
}

```

- 문제점
    - 입금과 출금에 관련된 정보가 각각 두 개의 기본형 필드로 분산되어 있음
    - 만약 입금이나 출금 관련 로직을 확장하거나 변경해야 한다면,관련 필드가 여기저기 흩어져 있어 유지보수가 어려워집니다.

---

### 리팩터링 후

**1단계: 관련 필드들을 하나의 클래스(Money)로 추출**

```java

// Money 클래스: 금액(amount)과 통화(currency)를 하나로 묶어 관리
public class Money {
    private double amount;
    private String currency;

    public Money(double amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    @Override
    public String toString() {
        return amount + " " + currency;
    }
}

```

**2단계: BankAccount 클래스에서는 Money 객체를 사용하도록 변경**

```java

// 리팩터링 후: BankAccount 클래스는 Money 객체를 사용하여 입금과 출금 정보를 관리
public class BankAccount {
    private String accountNumber;
    private String owner;
    private Money deposit;    // 입금 정보를 담은 Money 객체
    private Money withdrawal; // 출금 정보를 담은 Money 객체

    public BankAccount(String accountNumber, String owner,
                       Money deposit, Money withdrawal) {
        this.accountNumber = accountNumber;
        this.owner = owner;
        this.deposit = deposit;
        this.withdrawal = withdrawal;
    }

    // 입금과 출금 정보를 출력할 때 Money 객체의 toString()을 활용
    public void printTransactionDetails() {
        System.out.println("Deposit: " + deposit);
        System.out.println("Withdrawal: " + withdrawal);
    }

    // 순 잔액 계산: Money 객체의 금액만 사용 (여기서는 같은 통화라고 가정)
    public double calculateNetBalance() {
        return deposit.getAmount() - withdrawal.getAmount();
    }
}

public class MainAfter {
    public static void main(String[] args) {
        Money deposit = new Money(1000.0, "USD");
        Money withdrawal = new Money(200.0, "USD");
        BankAccount account = new BankAccount("12345", "Alice", deposit, withdrawal);

        account.printTransactionDetails();
        System.out.println("Net Balance: " + account.calculateNetBalance());
    }
}

```

- 개선 효과
    - **구조 단순화:** 입금과 출금 관련 필드가 `Money` 클래스로 묶여,관련 데이터와 로직(예를 들어, 포매팅이나 통화 검증)을 하나의 클래스에서 관리할 수 있습니다.
    - **유지보수성 향상:** 만약 금액 관련 로직을 확장하거나 수정해야 할 때,`Money` 클래스만 수정하면 되어, 변경 범위가 한정됩니다.
    - **중복 제거:** 여러 메서드에 반복되던 기본형 필드의 처리 로직을하나의 객체로 통합함으로써 코드 중복을 줄이고, 클라이언트가 해당 정보를쉽게 이해하고 사용할 수 있게 됩니다.

# 3.21 서로 다른 인터페이스의 대안 클래스들

<aside>
💡

**함수 선언 바꾸기(메서드 시그니처 일치)**와 **함수 옮기기(필요한 동작을 내부로 이동)**를 통해 인터페이스를 동일하게 만든 후,클래스 교체가 쉽도록 리팩터링할 수 있습니다.또한, 공통 코드가 있다면 **슈퍼클래스 추출하기**를 적용하여코드 중복을 줄이고 유지보수를 용이하게 할 수 있습니다.

</aside>

- **클래스 교체의 장점**
    - 클래스의 큰 장점 중 하나는 필요에 따라 언제든 다른 구현 클래스로 교체할 수 있다는 점입니다.
    - 이를 위해서는 교체될 클래스들이 같은 인터페이스(메서드 시그니처)를 가져야 합니다.
- **리팩터링 기법**
    1. **함수 선언 바꾸기**
        - 각 클래스의 메서드 이름과 매개변수를 일치시켜, 동일한 인터페이스를 갖도록 합니다.
    2. **함수 옮기기**
        - 필요한 동작(비즈니스 로직)을 해당 클래스 내부로 몰아 넣어,외부에 노출되는 인터페이스가 일관되도록 만듭니다.
    3. **슈퍼클래스 추출하기**
        - 여러 대안 클래스 사이에 중복 코드가 생기면, 공통 코드를 한 곳에 모으기 위해상위 클래스(추상 클래스)를 추출합니다.

### 리팩터링 전 -  각 클래스가 서로 다른 인터페이스를 제공하는 경우

- 예를 들어, 신용카드 결제와 은행 이체 결제가 각각 서로 다른 메서드 시그니처를 가지고 있다고 가정해 봅니다.

```java

// 리팩터링 전: 신용카드 결제 클래스
class CreditCardPayment {
    // 신용카드 결제는 카드 번호를 매개변수로 받아 처리한다.
    public boolean processPayment(double amount, String cardNumber) {
        System.out.println("Processing credit card payment of " + amount +
                           " using card " + cardNumber);
        return true;
    }
}

// 리팩터링 전: 은행 이체 결제 클래스
class BankTransferPayment {
    // 은행 이체 결제는 계좌번호와 은행 코드를 매개변수로 받아 처리한다.
    public boolean initiateTransfer(double amount, String bankAccount, String bankCode) {
        System.out.println("Initiating bank transfer of " + amount +
                           " from account " + bankAccount + " (bank code: " + bankCode + ")");
        return true;
    }
}

// 클라이언트는 두 클래스의 메서드 시그니처가 달라서, 결제 방식을 교체하려면 별도의 코드를 작성해야 함.
public class PaymentClientBefore {
    public static void main(String[] args) {
        CreditCardPayment ccPayment = new CreditCardPayment();
        ccPayment.processPayment(100.0, "1234-5678-9012-3456");

        BankTransferPayment bankPayment = new BankTransferPayment();
        bankPayment.initiateTransfer(200.0, "987654321", "001");
    }
}

```

- 문제점:
    - 각 결제 방식이 서로 다른 메서드 시그니처를 가지고 있으므로,클라이언트가 결제 방식을 교체하려면 코드 수정이 필요합니다.

---

### 리팩터링 후 - 동일한 인터페이스를 갖도록 변경하고, 필요한 동작을 내부로 옮김

1. **공통 인터페이스 정의:**

   우선 모든 결제 방식이 공통으로 구현해야 하는 메서드를 가진 인터페이스 `Payment`를 정의합니다.

2. **필요한 정보는 객체 내부에 저장:**

   예를 들어, 카드 결제는 카드 번호, 은행 이체는 계좌번호와 은행 코드를 생성 시에 객체에 저장합니다.

3. **동일한 인터페이스 메서드 `pay(double amount)`를 구현:**

   각 클래스는 내부에서 저장된 정보를 이용해 결제 처리를 수행합니다.

4. **공통 코드가 있다면 추출:**

   만약 결제 처리에 공통되는 로직이 있다면, 추상 클래스(`AbstractPayment`)를 추출하여 중복을 제거할 수 있습니다.


```java

// 공통 인터페이스
interface Payment {
    boolean pay(double amount);
}

// (선택 사항) 공통 로직이 있다면 추상 클래스 사용
abstract class AbstractPayment implements Payment {
    // 예: 결제 처리 전후에 공통적으로 로깅하는 로직 등
    protected void log(String message) {
        System.out.println("[LOG] " + message);
    }
}

// 리팩터링 후: 신용카드 결제 클래스 – 내부에 카드 번호를 저장하고 동일한 pay() 메서드 구현
class CreditCardPaymentRefactored extends AbstractPayment {
    private String cardNumber;

    public CreditCardPaymentRefactored(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public boolean pay(double amount) {
        // 내부에 저장된 카드 번호를 사용하여 결제 처리
        log("Processing credit card payment.");
        System.out.println("Processing credit card payment of " + amount +
                           " using card " + cardNumber);
        return true;
    }
}

// 리팩터링 후: 은행 이체 결제 클래스 – 내부에 계좌번호와 은행 코드를 저장하고 동일한 pay() 메서드 구현
class BankTransferPaymentRefactored extends AbstractPayment {
    private String bankAccount;
    private String bankCode;

    public BankTransferPaymentRefactored(String bankAccount, String bankCode) {
        this.bankAccount = bankAccount;
        this.bankCode = bankCode;
    }

    @Override
    public boolean pay(double amount) {
        // 내부에 저장된 정보를 사용하여 이체 처리
        log("Initiating bank transfer payment.");
        System.out.println("Initiating bank transfer of " + amount +
                           " from account " + bankAccount + " (bank code: " + bankCode + ")");
        return true;
    }
}

// 리팩터링 후 클라이언트: 동일한 Payment 인터페이스를 사용하여 결제 방식을 교체할 수 있음.
public class PaymentClientAfter {
    public static void main(String[] args) {
        Payment payment = new CreditCardPaymentRefactored("1234-5678-9012-3456");
        payment.pay(100.0);

        // 필요에 따라 쉽게 다른 결제 클래스로 교체 가능
        payment = new BankTransferPaymentRefactored("987654321", "001");
        payment.pay(200.0);
    }
}

```

- 개선 효과:
    - 클라이언트는 단지 `Payment` 인터페이스의 `pay(double amount)` 메서드만 호출하면 됩니다.
    - 내부 구현에 따라 신용카드 결제나 은행 이체 결제를 손쉽게 교체할 수 있으며,공통 인터페이스를 통해 결제 방식 간 일관성이 보장됩니다.
    - 만약 두 클래스 사이에 중복되는 로직이 있다면, 추상 클래스로 공통 코드를 추출해 재사용할 수 있습니다.

# 3.22 데이터 클래스

<aside>
💡

데이터 클래스의 필드와 관련된 동작을 한 클래스에 모으면, 데이터의 캡슐화가 이루어지고 클라이언트는 내부 구현에 의존하지 않고 일관된 인터페이스를 통해 객체를 다룰 수 있게 됩니다.

</aside>

- **문제점:**
    - 데이터 클래스는 단순히 데이터 필드와 게터/세터만 가진 클래스인데, 이런 클래스의 필드가 public이거나 너무 쉽게 외부에서 접근 가능하면, 클라이언트가 내부 세부사항(필드)을 함부로 다루게 되어 캡슐화가 깨집니다.
    - 또한, 데이터 클래스의 동작(예를 들어, “전체 이름”을 만드는 로직)이 원래 필요한 곳(데이터 클래스 자체)과 다른 곳에 흩어져 있으면 유지보수가 어려워집니다.
- **개선 방안:**
    1. **레코드 캡슐화하기:** public 필드를 private으로 바꾸고 게터/세터로 감추어 내부 데이터에 직접 접근하지 못하게 합니다.
    2. **세터 제거하기:** 변경하면 안 되는(불변이어야 하는) 필드는 세터를 제거하여 수정할 수 없게 합니다.
    3. **함수 옮기기/함수 추출하기:** 다른 클래스에서 데이터 클래스의 게터나 세터를 사용하여 구현한 동작(예: 전체 이름 생성, 인사 메시지 작성 등)을데이터 클래스 내부로 옮겨서, 클라이언트가 데이터 클래스에 집중하여 사용할 수 있도록 합니다.
    4. **클라이언트 코드를 데이터 클래스로 옮기기:** 필요한 동작이 엉뚱한 곳에 있다면 클라이언트 코드를 데이터 클래스 내부로 옮겨 구조를 단순화할 수 있습니다.
    5. **불변 데이터 예외:** 불변 데이터(예: 단계 쪼개기 결과의 중간 데이터 구조)는 게터 없이 public 필드로 공개해도 무방합니다.

### 리팩터링 전 - 단순 데이터 저장용 데이터 클래스와 별도의 유틸리티 클래스

- **데이터 클래스 (단순 필드 공개)**

```java

// 리팩터링 전: 단순히 데이터를 저장하는 클래스로, 필드가 public입니다.
public class Person {
    public String firstName;
    public String lastName;
    public int age;

    public Person(String firstName, String lastName, int age) {
        this.firstName = firstName;
        this.lastName  = lastName;
        this.age       = age;
    }
}

```

- **데이터 조작/출력 로직 (클라이언트 혹은 별도 유틸리티 클래스에 존재)**

```java

// 리팩터링 전: Person 객체의 데이터를 조합하여 전체 이름을 만드는 유틸리티 메서드
public class PersonUtils {
    public static String getFullName(Person p) {
        return p.firstName + " " + p.lastName;
    }

    public static void printGreeting(Person p) {
        System.out.println("Hello, " + getFullName(p) + "!");
    }
}

// 사용 예시
public class MainBefore {
    public static void main(String[] args) {
        Person person = new Person("Alice", "Smith", 30);
        // 외부 유틸리티를 통해 데이터를 다룸 → 클라이언트가 Person 클래스의 내부 구현에 직접 접근
        String fullName = PersonUtils.getFullName(person);
        System.out.println("Full Name: " + fullName);
        PersonUtils.printGreeting(person);
    }
}

```

- 문제점:
    - Person 클래스의 필드가 public으로 노출되어 있어 외부에서 직접 수정 가능
    - 전체 이름이나 인사 메시지 생성 로직이 Person 클래스와 분리되어 있어,데이터와 관련된 동작이 흩어져 있습니다.

---

### 리팩터링 후: 데이터 캡슐화 및 관련 동작을 Person 클래스 내부로 이동

- **데이터 클래스 (캡슐화 적용 및 동작 추가)**

```java

// 리팩터링 후: Person 클래스는 내부 필드를 private으로 감추고,
// 데이터 조작(전체 이름 생성, 인사 출력) 기능을 직접 제공한다.
public class Person {
    private String firstName;
    private String lastName;
    private int age;

    public Person(String firstName, String lastName, int age) {
        this.firstName = firstName;
        this.lastName  = lastName;
        this.age       = age;
    }

    // 게터 메서드 (필요 시)
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public int getAge() {
        return age;
    }

    // 데이터 클래스 내부에 동작을 추가: 전체 이름 생성
    public String getFullName() {
        return firstName + " " + lastName;
    }

    // 인사 메시지 출력 기능 추가
    public void printGreeting() {
        System.out.println("Hello, " + getFullName() + "!");
    }
}

```

**클라이언트 코드**

```java

// 리팩터링 후 클라이언트는 Person 클래스의 공개된 인터페이스만 사용합니다.
public class MainAfter {
    public static void main(String[] args) {
        Person person = new Person("Alice", "Smith", 30);
        // 이제 전체 이름과 인사 메시지 기능이 Person 클래스 내부에 있으므로,
        // 별도의 유틸리티 클래스 없이 바로 사용할 수 있습니다.
        System.out.println("Full Name: " + person.getFullName());
        person.printGreeting();
    }
}

```

- 개선 효과:
    - Person 클래스의 필드가 private으로 감춰져 있어 캡슐화가 잘 이루어집니다.
    - 전체 이름 생성, 인사 메시지 출력 등 데이터와 관련된 동작이 Person 클래스 내부로 모여,클라이언트가 데이터 클래스만 보면 필요한 기능을 쉽게 파악할 수 있습니다.
    - 불필요한 외부 의존성이 제거되어 유지보수성이 향상됩니다.

# 3.23 상속 포기

<aside>
💡

불필요한 상속(부모의 유산)으로 인해 자식 클래스가 필요하지 않은 기능까지 가지게 되는 문제는, 적절한 리팩터링(메서드 내리기/필드 내리기 또는 위임 전환)을 통해 해결할 수 있으며, 결과적으로 교체 가능한 일관된 인터페이스를 유지할 수 있게 됩니다.

</aside>

- **문제 상황**
    - 상속 구조에서는 자식이 부모의 모든 메서드와 필드를 물려받는데,만약 자식이 부모의 일부 기능(예: 특정 동작이나 데이터)은 필요로 하지 않는다면,자식은 불필요한 “유산”까지 포함하게 됩니다.
    - 이런 경우, 자식은 부모가 공개하는 인터페이스 전체를 따라야 하지만실제로는 관심 있는 부분만 사용하고 싶어집니다.
    - 특히, 서브클래스가 부모의 동작(구현)은 재활용하면서도 인터페이스(노출된 메서드)는따르고 싶지 않은 상황(상속 포기 냄새)이 발생하면,이때는 상속 대신 위임으로 전환하는 것도 고려해볼 수 있습니다.
- **해결 방법:**
    1. **메서드 내리기/필드 내리기**
        - 같은 계층의 서브클래스를 하나 새로 만들고, 부모에게서 물려받은불필요한(사용하지 않을) 메서드와 필드를 새 서브클래스로 “내려” 보냅니다.그러면 부모 클래스에는 공통된 부분만 남게 됩니다.
    2. **서브클래스(혹은 슈퍼클래스)를 위임으로 바꾸기**
        - 만약 자식이 부모의 구현은 활용하지만 인터페이스(노출된 메서드)는 따르고 싶지 않다면,상속 대신 위임(delegation) 방식으로 전환하여,자식이 내부적으로 필요한 기능만 호출하고, 불필요한 인터페이스는 숨깁니다.
    3. **항상 이렇게 해야 한다는 규칙은 아님**
        - 실무에서는 재활용 목적으로 상속을 사용하는 경우도 많으므로,모든 상속을 포기할 필요는 없으며, “냄새”가 미미한 경우에는 그대로 두어도 무방합니다.
    4. **상속 포기 냄새**
        - 서브클래스가 부모의 동작은 필요로 하지만, 인터페이스(공개된 메서드)는따라서는 안 되는 상황에서 특히 문제가 되며,이럴 때는 위임 기법을 적용하여 상속 메커니즘에서 벗어나도록 합니다.

### 리팩터링 전 - 부모 클래스의 모든 기능을 그대로 물려받은 경우

- 아래 예제에서는 `Employee` 클래스가 모든 공통 기능(일반 업무, 회의 참석, 보고서 작성, 팀 관리)을 제공하고, `RegularEmployee`와 `Manager` 모두가 이를 상속받습니다.
- 하지만 일반 직원(`RegularEmployee`)은 팀 관리 기능은 필요 없는데도 상속받게 됩니다.

```java

// 부모 클래스: Employee – 모든 기능(메서드)를 제공
class Employee {
    public String name;
    public double salary;

    public Employee(String name, double salary) {
        this.name = name;
        this.salary = salary;
    }

    public void work() {
        System.out.println(name + " is working.");
    }

    public void attendMeeting() {
        System.out.println(name + " is attending a meeting.");
    }

    public void fileReport() {
        System.out.println(name + " is filing a report.");
    }

    // 모든 직원에게 물려지지만, RegularEmployee에게는 불필요한 메서드
    public void manageTeam() {
        System.out.println(name + " is managing a team.");
    }
}

// 일반 직원: Employee를 상속받아 사용하지만, 팀 관리 기능은 필요 없음
class RegularEmployee extends Employee {
    public RegularEmployee(String name, double salary) {
        super(name, salary);
    }
    // RegularEmployee는 manageTeam()을 사용하지 않음에도 불필요하게 상속받음.
}

// 관리자: Employee를 상속받아 팀 관리 기능을 활용함
class Manager extends Employee {
    public Manager(String name, double salary) {
        super(name, salary);
    }
}

public class InheritanceBefore {
    public static void main(String[] args) {
        RegularEmployee emp = new RegularEmployee("Alice", 50000);
        Manager mgr = new Manager("Bob", 80000);

        emp.work();
        emp.attendMeeting();
        emp.fileReport();
        // RegularEmployee에도 manageTeam()가 존재하지만, 호출하면 어색한 결과가 나옴.
        emp.manageTeam();  // 원하지 않는 기능

        System.out.println();

        mgr.work();
        mgr.attendMeeting();
        mgr.fileReport();
        mgr.manageTeam();  // 관리자는 필요함.
    }
}

```

- 문제점:
    - 일반 직원은 팀 관리 기능(manageTeam())이 상속되어 있지만 전혀 사용하지 않음.
    - 불필요한 인터페이스(메서드)가 노출되어 있어, 부모의 유산 중 관심 없는 부분까지 다뤄야 함.

---

### 리팩터링 후 - 불필요한 유산을 제거하여 인터페이스를 일치시키기

- 이 경우, 팀 관리 기능은 관리자를 위한 기능이므로, `Employee` 부모 클래스에서 팀 관리 기능을 제거하고, 관리자 전용 서브클래스(`Manager`)에만 해당 기능을 정의합니다.

```java
// 리팩터링 후 부모 클래스: Employee – 공통 기능만 남김 (manageTeam() 제거)
class EmployeeRefactored {
    public String name;
    public double salary;

    public EmployeeRefactored(String name, double salary) {
        this.name = name;
        this.salary = salary;
    }

    public void work() {
        System.out.println(name + " is working.");
    }

    public void attendMeeting() {
        System.out.println(name + " is attending a meeting.");
    }

    public void fileReport() {
        System.out.println(name + " is filing a report.");
    }
}

// 일반 직원: EmployeeRefactored를 그대로 사용 (팀 관리 기능이 없음)
class RegularEmployeeRefactored extends EmployeeRefactored {
    public RegularEmployeeRefactored(String name, double salary) {
        super(name, salary);
    }
    // manageTeam()가 존재하지 않으므로, 일반 직원에 불필요한 기능이 제거됨.
}

// 관리자: EmployeeRefactored를 상속받되, 팀 관리 기능을 별도로 추가함.
class ManagerRefactored extends EmployeeRefactored {
    public ManagerRefactored(String name, double salary) {
        super(name, salary);
    }

    // 팀 관리 기능만 별도로 추가 – 이 메서드는 오직 관리자에게만 노출됨.
    public void manageTeam() {
        System.out.println(name + " is managing a team.");
    }
}

public class InheritanceAfter {
    public static void main(String[] args) {
        RegularEmployeeRefactored emp = new RegularEmployeeRefactored("Alice", 50000);
        ManagerRefactored mgr = new ManagerRefactored("Bob", 80000);

        emp.work();
        emp.attendMeeting();
        emp.fileReport();
        // emp.manageTeam();  // 호출 불가 – 불필요한 기능이 제거됨.

        System.out.println();

        mgr.work();
        mgr.attendMeeting();
        mgr.fileReport();
        mgr.manageTeam();  // 관리자는 여전히 이 기능을 사용함.
    }
}

```

- 개선 효과:
    - 부모 클래스(`EmployeeRefactored`)에는 모든 자식이 공통으로 사용할 기능만 남게 되어,일반 직원은 불필요한 팀 관리 기능 없이 깔끔한 인터페이스를 갖게 됩니다.
    - 관리자는 별도로 팀 관리 기능을 제공하는 `ManagerRefactored`에서만 해당 기능을 노출하므로,클라이언트는 각 객체의 실제 용도에 맞게 기능을 사용할 수 있습니다.
    - 이렇게 하면 자식이 부모의 불필요한 유산에 의존하지 않고,교체나 변경이 필요할 때도 관련 인터페이스만 일치시키면 되므로 유지보수가 쉬워집니다.

# 3.24 주석

<aside>
💡

**주석을 달아야 할 이유**가 생긴다면 먼저 코드를 리팩터링하여 **자체적으로 설명 가능한 코드**로 바꾸는 것이 좋습니다.

그래도 필요한 경우에 한해서, 현재 진행 상황이나 불확실한 부분에 주석을 달면 나중에 코드를 수정해야 할 프로그래머에게 유용한 정보를 제공할 수 있습니다.

</aside>

- **핵심 요점:**
    - **주석 자체는 나쁜 것이 아니라** 제대로 작성되면 코드의 의도를 보조하는 좋은 도구입니다.
    - **문제는 주석을 '탈취제'처럼 사용하여** 실제 문제(코드가 왜 이렇게 작성되었는지, 즉 코드 자체가 자명하지 않아서)가 가려지는 경우입니다.
    - 만약 특정 코드 블록이 무엇을 하는지 설명하기 위해 길고 자세한 주석이 필요하다면,이는 그 코드가 잘 구조화되지 않았다는 신호일 수 있습니다.
- **해결책:**
    1. **함수 추출하기:** 복잡한 코드 블록을 별도의 함수로 추출하여,함수 이름만 봐도 그 역할이 드러나게 합니다.
    2. **함수 이름 바꾸기(수 선언 바꾸기):** 추출한 함수의 이름을 보다 명확하게 바꿔,주석 없이도 함수의 목적을 알 수 있도록 합니다.
    3. **어설션 추가하기:** 시스템의 전제 조건이나 선행 조건을 주석 대신 어설션으로 명시합니다.
- **결과적으로,** 주석이 필요한 상황 자체를 줄이는 리팩터링을 먼저 시도하고,정말 필요한 경우(예: 진행 상황이나 불확실한 부분)만 주석을 남기는 것이 좋습니다.

### 리팩터링 전 - 주석으로 설명하는 코드

- 아래 코드는 회원 등급에 따라 할인율을 적용하여 할인 금액을 계산하는 메서드입니다.
- 코드 앞에 긴 주석으로 각 조건과 계산 이유를 상세하게 설명하고 있습니다.

```java

public class DiscountCalculator {

    /**
     * 회원 등급에 따라 할인 금액을 계산한다.
     *
     * 주석 설명:
     * - 만약 회원 등급이 "Gold"라면, 할인율은 20%이다.
     * - 만약 회원 등급이 "Silver"라면, 할인율은 10%이다.
     * - 그 외의 등급이면 할인율은 0%이다.
     * 이 메서드는 전달받은 가격에 해당 할인율을 곱하여 할인 금액을 반환한다.
     */
    public double calculateDiscount(double price, String membership) {
        double discount = 0;
        if (membership.equals("Gold")) {
            discount = price * 0.2;
        } else if (membership.equals("Silver")) {
            discount = price * 0.1;
        }
        return discount;
    }

    public static void main(String[] args) {
        DiscountCalculator calc = new DiscountCalculator();
        double discount = calc.calculateDiscount(100.0, "Gold");
        System.out.println("Discount: " + discount);
    }
}

```

- 문제점:
    - 긴 주석이 코드에 왜 그런 로직이 들어갔는지 설명하지만,반대로 코드 자체가 자명하지 않아 주석 없이는 이해하기 어렵습니다.

---

### 리팩터링 후 - 함수 추출 및 이름 바꾸기를 통해 주석이 필요 없는 코드로 개선

- 코드에서 할인율을 결정하는 부분을 별도의 메서드로 추출하고,
- 그 메서드의 이름을 명확하게 지정하여 주석 없이도 이해할 수 있도록 합니다.

```java

public class DiscountCalculatorRefactored {

    /**
     * 가격과 회원 등급에 따라 할인 금액을 계산한다.
     * 내부적으로 회원 등급에 따른 할인율을 결정하는 메서드를 호출한다.
     */
    public double calculateDiscount(double price, String membership) {
        return price * getDiscountRateForMembership(membership);
    }

    // 회원 등급에 따른 할인율을 결정하여 반환한다.
    private double getDiscountRateForMembership(String membership) {
        if ("Gold".equals(membership)) {
            return 0.2;
        } else if ("Silver".equals(membership)) {
            return 0.1;
        } else {
            return 0.0;
        }
    }

    public static void main(String[] args) {
        DiscountCalculatorRefactored calc = new DiscountCalculatorRefactored();
        double discount = calc.calculateDiscount(100.0, "Gold");
        System.out.println("Discount: " + discount);
    }
}

```

- 개선 효과:
    - **코드 자체가 설명적입니다:**`getDiscountRateForMembership()`라는 메서드 이름만 보아도,회원 등급에 따른 할인율을 반환한다는 의도가 드러납니다.
    - **불필요한 주석 제거:** 원래 긴 주석 대신, 함수 이름과 짧은 주석만으로 코드의 목적을 명확하게 전달합니다.
    - **유지보수 용이성:** 할인율 로직이 별도 메서드로 분리되어, 이후 수정할 때도 해당 함수만 보면 됩니다.