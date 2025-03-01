# 기본적인 리팩토링

- **초기 리팩터링 기법:**

  카탈로그는 가장 기본적이고 자주 사용하는 리팩터링부터 시작한다.

    - **함수 추출하기 & 변수 추출하기:**코드를 모듈화하고, 가독성을 높이는 데 필수적인 기법.
    - **함수 인라인하기 & 변수 인라인하기:**추출의 반대 작업으로, 불필요한 중첩을 제거하거나 단순화할 때 사용.
- **이름 짓기와 관련 리팩터링:**
    - **함수 선언 바꾸기:**함수의 이름을 변경하거나, 인수를 추가/제거할 때 활용.
    - **변수 이름 바꾸기:**변수 캡슐화와 밀접하게 연관되어, 변수의 의미를 명확히 하기 위해 사용.
    - **매개변수 객체 만들기:**자주 함께 사용되는 인수들을 하나의 객체로 묶어 관리하면 편리하다.
- **저수준 리팩터링 후 고수준 모듈화:**
    - 기본적으로 함수를 구성하고 이름 짓는 작업은 저수준 리팩터링이다.
    - 이후 여러 함수를 하나의 클래스로 묶거나, 읽기 전용 데이터를 다룰 때 변환 함수로 묶어 모듈화한다.
    - 또한, 묶은 모듈의 작업 처리 과정을 더 명확하게 단계로 쪼개는 단계 쪼개기도 자주 사용한다.

이와 같이, 카탈로그는 기본적인 리팩터링 기법부터 모듈화 및 단계 분리와 같은 고수준 기법까지 체계적으로 정리하여, 개발자가 상황에 맞게 안전하고 효율적으로 리팩터링할 수 있도록 돕는다.

# 6.1 함수추출하기

1. **기본 개념**
    - **목적:**
        - 코드의 특정 부분(예: 출력, 계산 등)이 어떤 일을 하는지 명확히 하기 위해 별도의 함수로 분리한다.
        - “무엇을 하는지”를 잘 드러내는 이름을 붙여 코드 이해도를 높인다.
    - **적용 배경:**
        - 함수(또는 서브루틴)가 길어지거나, 한 눈에 기능을 파악하기 어려울 때 적용한다.
        - 재사용성이 높거나, 목적과 구현을 분리하면 가독성이 크게 향상된다.
2. **리팩터링 전/후 예시**
    - **Before:**
        - 하나의 함수 내에서 배너 출력, 미해결 채무 계산, 세부 사항 출력 등을 모두 수행.
    - **After:**
        - 각 기능(배너 출력, 채무 계산, 세부 출력 등)을 독립된 함수(예: printBanner, printDetails, calculateOutstanding, recordDueDate)로 분리하여 호출.
        - 이를 통해 각 함수가 한 가지 역할만 담당하게 되어 코드가 단순해지고, 테스트와 유지보수가 용이해짐.
3. **절차 및 단계**
    - **함수 생성 및 이름 지정:**
        - 추출할 코드 덩어리를 식별하고, 그 목적을 드러내는 이름을 부여한다.
        - 간단한 코드라도 의미 있는 이름이 떠오른다면 추출한다.
    - **코드 복사:**
        - 원본 함수에서 추출할 부분을 새 함수로 복사한다.
    - **지역 변수 처리:**
        - 원본 함수의 지역 변수를 참조하는 경우, 매개변수로 전달하거나 새 함수 내에서 재선언하도록 수정한다.
    - **대체 및 테스트:**
        - 원본 함수에서 추출한 코드 부분을 새 함수를 호출하는 문장으로 교체한 후, 컴파일과 테스트를 진행한다.
    - **중복 코드 검토:**
        - 다른 부분에 비슷한 코드가 있다면 새 함수로 대체할 수 있는지 검토한다.
4. **예시에서의 고려사항**
    - **지역 변수의 변경:**
        - 단순히 읽기만 하는 변수는 매개변수로 전달하면 되지만, 값이 변경되는 경우 반환값을 이용해 원래 변수에 대입한다.
    - **코드 스타일 개선:**
        - 함수 추출 후 함수들을 가능한 한 짧게 작성하여, 각 함수의 역할과 목적이 명확하게 드러나도록 한다.
    - **성능 문제:**
        - 현대 컴파일러 최적화 덕분에, 짧은 함수 호출이 성능 저하로 이어지는 경우는 거의 없다.

## 리팩토링 이전의 코드

```java
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

class Order {
    private double amount;
    public Order(double amount) { this.amount = amount; }
    public double getAmount() { return amount; }
}

class Invoice {
    private String customer;
    private List<Order> orders;
    private LocalDate dueDate;
    
    public Invoice(String customer, List<Order> orders) {
        this.customer = customer;
        this.orders = orders;
    }
    public String getCustomer() { return customer; }
    public List<Order> getOrders() { return orders; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
}

public class InvoicePrinter {
    
    public static void printOwing(Invoice invoice) {
        // 배너 출력
        System.out.println("******************");
        System.out.println("**** 고객 채무 ****");
        System.out.println("******************");
        
        // 미해결 채무 계산
        double outstanding = 0;
        for (Order order : invoice.getOrders()) {
            outstanding += order.getAmount();
        }
        
        // 마감일 설정
        LocalDate today = LocalDate.now();
        invoice.setDueDate(today.plusDays(30));
        
        // 세부 사항 출력
        System.out.println("고객명: " + invoice.getCustomer());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        System.out.println("채무액: " + outstanding);
        System.out.println("마감일: " + invoice.getDueDate().format(formatter));
    }
    
    public static void main(String[] args) {
        List<Order> orders = List.of(new Order(100.0), new Order(200.0), new Order(50.0));
        Invoice invoice = new Invoice("홍길동", orders);
        printOwing(invoice);
    }
}

```

## 리팩토링 이후의 코드

```java
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

// Invoice와 Order 클래스는 도메인 모델의 일부로 가정
class Order {
    private double amount;
    
    public Order(double amount) {
        this.amount = amount;
    }
    
    public double getAmount() {
        return amount;
    }
}

class Invoice {
    private String customer;
    private List<Order> orders;
    private LocalDate dueDate;
    
    public Invoice(String customer, List<Order> orders) {
        this.customer = customer;
        this.orders = orders;
    }
    
    public String getCustomer() {
        return customer;
    }
    
    public List<Order> getOrders() {
        return orders;
    }
    
    public LocalDate getDueDate() {
        return dueDate;
    }
    
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}

public class InvoicePrinter {
    
    // 메인 리팩터링 대상 메서드
    public static void printOwing(Invoice invoice) {
        printBanner();  // 배너 출력
        double outstanding = calculateOutstanding(invoice);  // 미해결 채무 계산
        recordDueDate(invoice);  // 마감일 기록
        printDetails(invoice, outstanding);  // 세부 사항 출력
    }
    
    // 배너 출력 로직을 별도의 메서드로 추출
    private static void printBanner() {
        System.out.println("******************");
        System.out.println("**** 고객 채무 ****");
        System.out.println("******************");
    }
    
    // 미해결 채무 계산 로직 추출 (Invoice의 모든 Order의 금액을 합산)
    private static double calculateOutstanding(Invoice invoice) {
        double result = 0;
        for (Order order : invoice.getOrders()) {
            result += order.getAmount();
        }
        return result;
    }
    
    // 마감일 설정 로직 추출: 오늘 날짜에서 30일 후로 마감일 설정
    private static void recordDueDate(Invoice invoice) {
        LocalDate today = LocalDate.now();  // 시스템 시계 대신 테스트 가능한 Clock 클래스를 사용할 수도 있음.
        invoice.setDueDate(today.plusDays(30));
    }
    
    // 세부 사항 출력 로직 추출
    private static void printDetails(Invoice invoice, double outstanding) {
        System.out.println("고객명: " + invoice.getCustomer());
        System.out.println("채무액: " + outstanding);
        // dueDate를 포맷팅하여 출력
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        System.out.println("마감일: " + invoice.getDueDate().format(formatter));
    }
    
    // 간단한 실행 예시
    public static void main(String[] args) {
        List<Order> orders = List.of(new Order(100.0), new Order(200.0), new Order(50.0));
        Invoice invoice = new Invoice("홍길동", orders);
        
        printOwing(invoice);
    }
}

```

# **6.2** 함수 인라인하기

---

## 1. 배경 및 목적

함수 인라인하기는 이름만 보고도 충분히 이해할 수 있는 간단한 함수를 호출 대신 함수 본문의 내용으로 대체하는 기법입니다.

**핵심 포인트:**

- **가독성 개선:** 불필요한 간접 호출을 제거해 코드가 한눈에 들어오도록 합니다.
- **과도한 위임 제거:** 이름만 좋은 함수가 실제로는 별다른 부가가치를 주지 않는다면, 직접 코드를 사용하는 편이 명확합니다.
- **유지보수:** 코드가 단순해지면 테스트 및 유지보수가 쉬워집니다.

하지만, 모든 경우에 인라인이 좋은 선택은 아닙니다. 다형성이나 서브클래스에서 오버라이딩하는 경우에는 인라인하면 안 되며, 함수의 역할이 명확한 경우라면 그대로 두는 것도 나쁘지 않습니다.

---

## 2. 리팩토링 절차

1. **메서드가 다형 메서드인지 확인:** 서브클래스에서 오버라이드 되는 메서드는 인라인하지 않습니다.
2. **호출하는 곳 모두 찾기:** 인라인할 함수를 호출하는 모든 위치를 찾아서, 그 호출문을 함수 본문으로 대체합니다.
3. **하나씩 치환하면서 테스트:** 인라인할 때마다 테스트를 진행하여 기능이 유지되는지 확인합니다.
4. **원래 함수 삭제:** 모든 호출부에서 함수 본문으로 치환한 후, 기존의 함수를 삭제합니다.

복잡한 경우엔 한 문장씩 옮기면서 테스트하는 것이 안전합니다.

---

## 3. 예시 코드

### (1) 리팩토링 전

```java
// Driver.java
public class Driver {
    private int numberOfLateDeliveries;

    public Driver(int numberOfLateDeliveries) {
        this.numberOfLateDeliveries = numberOfLateDeliveries;
    }

    public int getNumberOfLateDeliveries() {
        return numberOfLateDeliveries;
    }

    public void setNumberOfLateDeliveries(int numberOfLateDeliveries) {
        this.numberOfLateDeliveries = numberOfLateDeliveries;
    }
}

```

```java
// RatingService.java - 리팩토링 전
public class RatingService {

    public int getRating(Driver driver) {
        return moreThanFiveLateDeliveries(driver) ? 2 : 1;
    }

    // 간단하지만 호출을 하나 더 만들어 불필요한 간접 호출을 하고 있음.
    private boolean moreThanFiveLateDeliveries(Driver driver) {
        return driver.getNumberOfLateDeliveries() > 5;
    }
}

```

### (2) 리팩토링 후

```java
// RatingService.java - 리팩토링 후
public class RatingService {

    public int getRating(Driver driver) {
        // 함수 인라인으로 더 이상 불필요한 메서드 호출 없이 바로 판단
        return (driver.getNumberOfLateDeliveries() > 5) ? 2 : 1;
    }
}

```

---

## 4. 설명

- **리팩토링 전:**

  `getRating()` 메서드는 내부에서 `moreThanFiveLateDeliveries()`라는 별도의 메서드를 호출합니다. 이 메서드는 단순히 `driver.getNumberOfLateDeliveries()` 값을 비교하는 간단한 로직을 가지고 있어, 기능은 명확하지만 불필요한 간접 호출로 인해 코드가 한 단계 더 복잡해집니다.

- **리팩토링 후:**

  함수 인라인하기를 적용하여 `moreThanFiveLateDeliveries()`의 코드를 `getRating()` 내부로 직접 옮겼습니다. 결과적으로 불필요한 메서드 호출을 제거해, 코드는 더 간결하고 직관적으로 바뀌었습니다.

  > "불필요한 껍질을 벗겨내자"라는 말처럼, 코드의 본질만 남겨 가독성을 높인 셈입니다.
>

---

이와 같이 간단한 로직이라면 인라인을 통해 코드가 더 명확해지고 유지보수가 용이해집니다. 다만, 함수 인라인은 단순한 경우에만 사용하는 것이 좋으며, 복잡한 로직이나 다형성이 적용된 메서드는 그대로 유지하는 편이 안전합니다.

# **6.3** 변수 추출하기

---

## 1. 배경 및 목적

복잡한 수식은 마치 코드를 읽는 사람에게 “너 혹시 외계어라도 하는 거야?”라고 말하는 것과 같습니다.

변수 추출하기는 복잡한 표현식을 여러 개의 의미 있는 변수로 나눠서 코드의 의도를 명확하게 만들어줍니다.

- **가독성 개선:** 각 계산 단계를 이름 붙여서 읽기 쉽도록 만듭니다.
- **디버깅 용이:** 중간 값들을 확인하기 쉬워 문제를 찾는 데 도움이 됩니다.
- **유지보수:** 의미 있는 변수 덕분에 코드 변경이나 확장이 훨씬 편해집니다.

---

## 2. 리팩토링 절차

1. **원본 표현식 파악:** 복잡한 수식을 읽고, 각각의 의미 있는 부분을 구분합니다.
2. **불변 변수 생성:** 각 부분에 적절한 이름을 가진 변수를 선언하고, 해당 부분의 계산 결과를 할당합니다.
3. **원본 표현식 교체:** 기존 수식에서 해당 부분을 변수로 대체합니다.
4. **테스트:** 단계별로 기능이 그대로 유지되는지 확인합니다.

---

## 3. JAVA 코드 예시

### (1) 리팩토링 전

```java
// Order.java
public class Order {
    private int quantity;
    private double itemPrice;

    public Order(int quantity, double itemPrice) {
        this.quantity = quantity;
        this.itemPrice = itemPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getItemPrice() {
        return itemPrice;
    }
}

```

```java
// PriceCalculatorBefore.java
public class PriceCalculatorBefore {

    public double calculatePrice(Order order) {
        // 복잡한 표현식을 한 줄에 모두 작성하여 한눈에 파악하기 어렵다.
        return order.getQuantity() * order.getItemPrice()
            - Math.max(0, order.getQuantity() - 500) * order.getItemPrice() * 0.05
            + Math.min(order.getQuantity() * order.getItemPrice() * 0.1, 100);
    }

    public static void main(String[] args) {
        Order order = new Order(600, 10.0);
        PriceCalculatorBefore calculator = new PriceCalculatorBefore();
        System.out.println("리팩토링 전 가격: " + calculator.calculatePrice(order));
    }
}

```

### (2) 리팩토링 후

```java
// PriceCalculatorAfter.java
public class PriceCalculatorAfter {

    public double calculatePrice(Order order) {
        // 의미 있는 변수 이름을 붙여 각 계산 단계를 분리
        double basePrice = order.getQuantity() * order.getItemPrice();
        double quantityDiscount = Math.max(0, order.getQuantity() - 500) * order.getItemPrice() * 0.05;
        double shipping = Math.min(basePrice * 0.1, 100);
        return basePrice - quantityDiscount + shipping;
    }

    public static void main(String[] args) {
        Order order = new Order(600, 10.0);
        PriceCalculatorAfter calculator = new PriceCalculatorAfter();
        System.out.println("리팩토링 후 가격: " + calculator.calculatePrice(order));
    }
}

```

---

## 4. 설명

- **리팩토링 전 코드:**

  복잡한 수식이 한 줄에 몰려 있어 “이게 무슨 계산인지 한눈에 알겠냐?”라는 질문을 던집니다.

  예를 들어, `order.getQuantity() * order.getItemPrice()` 부분이 기본 가격 계산임에도 불구하고 다른 계산들과 섞여 있어 해석하기 어렵습니다.

- **리팩토링 후 코드:**

  복잡한 표현식을 `basePrice`, `quantityDiscount`, `shipping`과 같이 의미 있는 변수로 추출했습니다.

  이렇게 하면 코드가 “아, 기본 가격은 이러하고, 할인은 이러하며, 배송비는 이러구나!”라는 식으로 명확해집니다.

  디버깅할 때도 각 변수의 값을 쉽게 확인할 수 있으니, 문제 발생 시 “어디서 이상 생겼냐?”를 한눈에 파악할 수 있습니다.


결론적으로, 변수 추출하기는 코드의 가독성과 유지보수성을 높여주는 아주 유용한 리팩토링 기법입니다. 복잡한 계산을 단순한 단계별 변수로 쪼개는 작업은, 개발자가 “어쩜 이렇게 못 본 척했지?” 하고 후회하게 만들 정도로 중요한 작업입니다.

# **6.4** 변수 인라인하기

---

## 1. 배경 및 목적

변수 인라인하기는 이미 충분히 명확한 표현식을 굳이 변수에 할당하지 않고, 직접 그 표현식을 사용하는 기법입니다.

- **불필요한 중간 단계 제거:** 이름만 붙여 놓은 변수는 원래 표현식을 그대로 전달할 때 오히려 코드를 장황하게 만들 수 있습니다.
- **가독성 개선:** 간단한 표현식이라면 인라인하여 코드를 간결하게 만듭니다.
- **디버깅 고려:** 부작용이 없고 값이 한 번만 할당되는 경우, 변수 선언은 오히려 디버깅에 방해될 수 있습니다.

---

## 2. 리팩토링 절차

1. **부작용 확인:** 대입문의 우변(표현식)이 부작용 없이 안전한지 확인합니다.
2. **불변성 검증:** 변수에 값이 한 번만 대입되었는지 확인하고, 필요하다면 불변으로 만듭니다.
3. **변수 사용부 교체:** 변수가 사용되는 부분을 대입문의 우변으로 직접 교체합니다.
4. **테스트 및 제거:** 모든 사용부를 교체한 후, 변수 선언문과 대입문을 삭제하고 테스트합니다.

---

## 3. JAVA 코드 예시

### (1) 리팩토링 전

```java
// Order.java
public class Order {
    private double basePrice;

    public Order(double basePrice) {
        this.basePrice = basePrice;
    }

    public double getBasePrice() {
        return basePrice;
    }
}

```

```java
// PriceCheckerBefore.java
public class PriceCheckerBefore {

    // 주문의 기본 가격이 1000보다 큰지 여부를 판단하는 메서드
    public boolean isHighValueOrder(Order anOrder) {
        double basePrice = anOrder.getBasePrice();
        return (basePrice > 1000);
    }

    public static void main(String[] args) {
        Order order = new Order(1200);
        PriceCheckerBefore checker = new PriceCheckerBefore();
        System.out.println("리팩토링 전: " + checker.isHighValueOrder(order));
    }
}

```

### (2) 리팩토링 후

```java
// PriceCheckerAfter.java
public class PriceCheckerAfter {

    // 불필요한 중간 변수 없이 직접 비교
    public boolean isHighValueOrder(Order anOrder) {
        return anOrder.getBasePrice() > 1000;
    }

    public static void main(String[] args) {
        Order order = new Order(1200);
        PriceCheckerAfter checker = new PriceCheckerAfter();
        System.out.println("리팩토링 후: " + checker.isHighValueOrder(order));
    }
}

```

---

## 4. 설명

- **리팩토링 전:**
    - `basePrice`라는 변수를 선언하여 `anOrder.getBasePrice()`의 값을 할당한 후, 그 값을 사용해 비교합니다.
    - 이 경우 변수의 이름도 원래 표현식과 큰 차이가 없어, "이름붙이기"가 그다지 의미가 없습니다.
- **리팩토링 후:**
    - 단순히 `anOrder.getBasePrice() > 1000`으로 표현식을 직접 사용합니다.
    - 불필요한 중간 단계를 제거하여 코드가 더 직관적이고 간결해졌습니다.

**결론:**

간단한 표현식을 굳이 변수에 대입해 놓지 않고 바로 사용하는 것이 코드를 더 깔끔하게 만들어 줍니다. 물론, 표현식이 복잡하거나 의미를 강조하기 위해 이름을 붙이는 경우에는 변수를 그대로 두는 편이 낫지만, 이 예제처럼 단순한 경우라면 인라인하는 것이 현명한 선택입니다.

정말 "불필요한 포장재는 버려라!"라는 개발자의 철학을 다시 한 번 확인할 수 있는 리팩토링 기법입니다.

# **6.5** 함수 선언 바꾸기

---

## 1. 배경 및 목적

함수 선언 바꾸기는 함수의 이름(혹은 매개변수 등)과 관련된 선언을 더 명확하고 직관적으로 변경하는 기법입니다.

- **명확성 향상:** 짧거나 모호한 이름 대신, 함수가 하는 일을 한눈에 알 수 있는 이름을 사용합니다.
- **유지보수성 개선:** 호출하는 모든 곳에서 함수의 의도를 바로 이해할 수 있게 되어, 코드 변경 시 혼란을 줄입니다.
- **연결부 개선:** 함수 선언은 모듈 간 연결을 정의하는 핵심 요소이므로, 명확한 선언은 전체 시스템의 조립에도 긍정적인 영향을 줍니다.

예시에서는 원래 함수 이름인 `circum`이 다소 축약되어 의미 전달이 부족하므로, 이를 보다 명확한 `circumference`로 변경합니다.

---

## 2. 리팩터링 절차

1. **함수 선언 변경:**
    - 기존 함수 `circum(radius)`를 `circumference(radius)`로 이름을 바꿉니다.
2. **호출문 수정:**
    - 해당 함수를 호출하는 모든 곳에서 새 이름(`circumference`)을 사용하도록 수정합니다.
3. **테스트:**
    - 변경 후 기능이 동일하게 동작하는지 테스트하여 문제 없음을 확인합니다.

이처럼 간단한 이름 변경이지만, 코드 전반에 영향을 미치는 중요한 연결부이므로 신중히 처리해야 합니다.

---

## 3. JAVA 코드 예시

### (1) 리팩터링 전

```java
// CircleUtility.java (리팩터링 전)
public class CircleUtility {

    // 축약된 이름: circum -> 원의 둘레를 구하는 함수임에도 불구하고 직관성이 떨어짐.
    public static double circum(double radius) {
        return 2 * Math.PI * radius;
    }
}

```

```java
// MainBefore.java (리팩터링 전)
public class MainBefore {
    public static void main(String[] args) {
        double radius = 5;
        // 함수 호출 시에도 여전히 축약된 이름 사용
        double result = CircleUtility.circum(radius);
        System.out.println("Circumference: " + result);
    }
}

```

### (2) 리팩터링 후

```java
// CircleUtility.java (리팩터링 후)
public class CircleUtility {

    // 보다 명확한 이름: circumference는 함수가 무엇을 하는지 명확하게 전달함.
    public static double circumference(double radius) {
        return 2 * Math.PI * radius;
    }
}

```

```java
// MainAfter.java (리팩터링 후)
public class MainAfter {
    public static void main(String[] args) {
        double radius = 5;
        // 모든 호출문에서 새로운 이름 사용
        double result = CircleUtility.circumference(radius);
        System.out.println("Circumference: " + result);
    }
}

```

---

## 4. 설명

- **리팩터링 전:**

  함수 이름 `circum`은 너무 축약되어 있어, 함수를 처음 접하는 개발자나 팀원들이 "이게 무슨 계산을 하는 거지?"라는 의문을 가질 수 있습니다.

  또한, 호출부에서도 동일한 축약된 이름을 사용하다 보면, 코드의 연결부가 불명확해집니다.

- **리팩터링 후:**

  함수 이름을 `circumference`로 변경하여, 함수가 원의 둘레를 계산한다는 의도를 명확히 전달합니다.

  호출하는 모든 곳에서도 새 이름을 사용하게 되어, 코드 전체가 일관되고 이해하기 쉬워집니다.

  이런 단순한 이름 변경이 결국 유지보수와 확장에 큰 도움이 됩니다.


결론적으로, 함수 선언 바꾸기는 단순히 이름만 바꾸는 것이 아니라, 전체 시스템에서 해당 함수가 수행하는 역할과 연결 관계를 명확하게 만들어 주는 중요한 리팩터링 기법입니다.

이제 코드가 “이 함수가 뭘 하는지 한눈에 알겠네”라고 말할 수 있도록 정리되었습니다.

# **6.6** 변수 캡슐화하기

---

## 1. 배경 및 목적

프로그램 전역이나 넓은 범위에서 사용되는 데이터(예, 전역 변수)는

- 직접 참조되거나 수정되기 쉽기 때문에, 코드 전체에서 데이터에 대한 결합도가 높아집니다.
- 데이터 변경 시 모든 참조 부분을 일일이 확인해야 하므로 유지보수가 어려워집니다.

**변수 캡슐화하기**는

- 데이터에 접근하거나 변경하는 “유일한 통로(게터/세터 메서드)”를 제공하여
- 데이터에 대한 직접적인 접근을 막고, 향후 변경 전 검증이나 추가 로직을 쉽게 끼워 넣을 수 있도록 합니다.

예시에서는 원래 `defaultOwner`라는 전역 변수를 직접 참조하는 대신,

이 변수에 접근하고 갱신하는 전담 함수를 만들어 변수의 직접 접근을 제한합니다.

---

## 2. 리팩터링 절차

1. **캡슐화 함수 작성:**
    - 우선, 변수의 값을 읽는 게터(getter)와 수정하는 세터(setter) 메서드를 작성합니다.
2. **기존 참조 수정:**
    - 변수에 직접 접근하던 모든 코드(읽기, 대입)를 새로 만든 게터/세터 메서드 호출로 변경합니다.
    - 한 번에 하나씩 변경하면서 테스트합니다.
3. **변수 접근 범위 제한:**
    - 캡슐화가 완료되면, 변수를 `private` 등으로 감춰 외부에서 직접 접근할 수 없도록 만듭니다.
    - 필요시 변수명을 눈에 띄게 바꾸어 직접 접근이 남아있는지 확인합니다.
4. **추가 개선 (옵션):**
    - 게터에서 원본 객체 대신 복제본을 반환하도록 수정하여 외부에서 내부 데이터를 직접 변경하지 못하도록 할 수도 있습니다.

---

## 3. JAVA 코드 예시

### (1) 리팩터링 전

아래 코드는 전역 데이터(여기서는 static 변수)를 직접 노출하여

다른 클래스에서 직접 읽거나 수정할 수 있는 상태입니다.

```java
// Owner.java - 소유자 정보를 담는 간단한 클래스
public class Owner {
    public String firstName;
    public String lastName;

    public Owner(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}

```

```java
// DefaultOwnerHolder.java - 리팩터링 전: 전역 변수를 직접 노출
public class DefaultOwnerHolder {
    // 전역 데이터에 직접 접근할 수 있음
    public static Owner defaultOwner = new Owner("마틴", "파울러");

    // 다른 클래스에서 직접 접근하여 사용 및 변경 가능
    // 예) spaceship.setOwner(DefaultOwnerHolder.defaultOwner);
}

```

사용 예시:

```java
// MainBefore.java
public class MainBefore {
    public static void main(String[] args) {
        // 직접 접근하여 읽기
        System.out.println("Before: " + DefaultOwnerHolder.defaultOwner.lastName);

        // 직접 접근하여 수정
        DefaultOwnerHolder.defaultOwner = new Owner("레베카", "파슨스");
        System.out.println("After: " + DefaultOwnerHolder.defaultOwner.lastName);
    }
}

```

---

### (2) 리팩터링 후

변수 캡슐화를 적용하여 내부 변수는 `private`으로 숨기고,

접근과 갱신은 전담 메서드(getDefaultOwner, setDefaultOwner)를 통해서만 이루어지도록 변경합니다.

```java
// Owner.java (변경 없음)
public class Owner {
    private String firstName;
    private String lastName;

    public Owner(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // 필요에 따라 게터만 제공하거나, 방어적 복사를 고려할 수 있음.
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    // setter는 내부 로직에서만 사용하거나, 검증 로직을 추가할 수 있음.
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}

```

```java
// DefaultOwnerHolder.java - 리팩터링 후: 데이터 캡슐화 적용
public class DefaultOwnerHolder {
    // 내부 변수는 private으로 숨김
    private static Owner defaultOwnerData = new Owner("마틴", "파울러");

    // 읽기 전용 접근 메서드 (필요시 방어적 복사도 고려)
    public static Owner getDefaultOwner() {
        // 방어적 복사: 원본 변경 방지 (여기서는 간단히 새 객체로 복제)
        return new Owner(defaultOwnerData.getFirstName(), defaultOwnerData.getLastName());
    }

    // 데이터 갱신을 위한 세터 메서드
    public static void setDefaultOwner(Owner newOwner) {
        // 필요 시 검증 로직 추가 가능
        defaultOwnerData = newOwner;
    }
}

```

사용 예시:

```java
// MainAfter.java
public class MainAfter {
    public static void main(String[] args) {
        // 게터를 통해 데이터 읽기
        Owner ownerBefore = DefaultOwnerHolder.getDefaultOwner();
        System.out.println("Before: " + ownerBefore.getLastName());

        // 세터를 통해 데이터 갱신
        DefaultOwnerHolder.setDefaultOwner(new Owner("레베카", "파슨스"));

        // 다시 게터를 통해 변경된 데이터 읽기
        Owner ownerAfter = DefaultOwnerHolder.getDefaultOwner();
        System.out.println("After: " + ownerAfter.getLastName());
    }
}

```

---

## 4. 리팩터링 절차 정리 및 설명

1. **캡슐화 함수 작성:**
    - 기존 전역 변수 `defaultOwner`를 `defaultOwnerData`로 내부에 숨기고,
    - 이를 읽기 위한 `getDefaultOwner()`와 수정하기 위한 `setDefaultOwner(Owner newOwner)` 메서드를 작성합니다.
2. **기존 참조 수정:**
    - 코드 전체에서 `DefaultOwnerHolder.defaultOwner`로 직접 접근하던 부분을
    - `DefaultOwnerHolder.getDefaultOwner()` (읽기) 또는 `DefaultOwnerHolder.setDefaultOwner(...)` (갱신)으로 변경합니다.
    - 이 과정에서 한 줄씩 변경 후 테스트하여 올바르게 동작하는지 확인합니다.
3. **변수의 접근 범위 제한:**
    - 내부 변수를 `private`으로 선언하여 외부에서 직접 접근하지 못하도록 합니다.
    - 혹시 직접 접근하는 코드가 남아있다면 변수명을 눈에 띄게 바꾸거나, 컴파일 에러를 통해 확인합니다.
4. **추가 개선 (방어적 복사):**
    - 게터에서는 원본 객체 대신 복제본을 반환하여, 클라이언트가 반환된 객체를 변경해도
      내부 데이터에 영향을 주지 않도록 할 수 있습니다.

**설명:**

변수 캡슐화하기는 데이터에 직접 접근하는 대신, 전담 메서드를 통해서만 읽고 수정하게 하여

데이터 변경 시 검증이나 추가 로직을 쉽게 삽입할 수 있게 만듭니다.

이 기법을 사용하면 나중에 데이터 구조가 변경되더라도, 캡슐화 함수의 내부 로직만 수정하면

되므로 코드의 결합도를 낮추고 유지보수를 용이하게 합니다.

---

이처럼 전역 변수를 캡슐화하면,

- 데이터 변경 전후에 필요한 검증 로직을 추가하거나,
- 데이터 복제본을 반환하여 외부에서 내부 데이터가 직접 변경되는 것을 막을 수 있으며,
- 결국 코드 전체의 안전성과 유지보수성이 크게 향상됩니다.

# **6.7** 변수 이름 바꾸기

---

## 1. 배경 및 목적

프로그래밍에서 **이름 짓기**는 코드의 의도를 명확하게 전달하는 핵심입니다.

- **명확성:**
  변수 이름이 그 역할이나 의미를 바로 드러내면, 코드를 읽는 사람(혹은 미래의 자신)이 “이 변수는 무엇을 나타내는가?” 고민할 필요가 없어집니다.
- **유지보수성:**
  임시 변수나 지역 변수라도, 의미 없는 이름(a, b 등) 대신 그 역할을 나타내는 이름(area, total 등)을 사용하면 코드가 한눈에 이해되고, 나중에 변경이나 확장이 용이해집니다.

예시에서는 단순히 `a`라는 이름 대신, 직사각형의 넓이를 의미하는 `area`라는 이름으로 변경합니다.

---

## 2. 리팩터링 절차

1. **대상 변수 확인:**
    - 우선 이름을 바꿀 변수가 어떤 의미로 사용되는지 확인합니다.
    - 해당 변수가 단순 임시 변수라면 직접 변경해도 무방하지만, 여러 곳에서 사용되거나 외부에 노출된 경우는 **변수 캡슐화하기**를 먼저 고려합니다.
2. **참조 위치 찾기:**
    - 변수의 선언과 모든 참조(읽기, 대입)를 IDE나 검색 도구를 통해 찾아냅니다.
3. **이름 변경:**
    - 각 참조 부분에서 변수 이름을 새로운 이름(예: `area`)으로 하나씩 변경합니다.
4. **테스트:**
    - 하나씩 변경할 때마다, 그리고 전체 변경 후에 기능이 그대로 동작하는지 테스트합니다.
5. **정리:**
    - 모든 참조가 변경되었다면, 이전 이름과 관련된 주석이나 문서도 업데이트합니다.

---

## 3. JAVA 코드 예시

### (1) 리팩터링 전

```java
// Rectangle.java - 리팩터링 전
public class Rectangle {

    // 주어진 높이와 너비를 곱한 값을 임시 변수 a에 할당하여 반환
    public double calculate(double height, double width) {
        double a = height * width;
        return a;
    }

    public static void main(String[] args) {
        Rectangle rect = new Rectangle();
        double result = rect.calculate(5, 10);
        System.out.println("Result: " + result);
    }
}

```

### (2) 리팩터링 후

```java
// Rectangle.java - 리팩터링 후
public class Rectangle {

    // 변수 a의 이름을 area로 바꿔, 계산 결과가 넓이임을 명확히 함
    public double calculate(double height, double width) {
        double area = height * width;
        return area;
    }

    public static void main(String[] args) {
        Rectangle rect = new Rectangle();
        double result = rect.calculate(5, 10);
        System.out.println("Area: " + result);
    }
}

```

---

## 4. 리팩터링 절차 정리 및 설명

1. **대상 변수 확인:**
    - 원래 코드에서는 `double a = height * width;`와 같이 단순한 계산 결과를 임시 변수 `a`에 저장하고 있습니다.
    - 이 변수는 직사각형의 넓이를 계산하는 역할을 하므로, 이름이 `a`보다는 `area`가 더 의미를 잘 전달합니다.
2. **참조 위치 찾기:**
    - 이 예제처럼 지역 변수로 한 함수 내에만 존재하는 경우, 참조 위치는 해당 함수 내부뿐입니다.
    - 만약 전역 변수나 클래스 필드라면, 외부에서 접근하는 부분도 함께 변경해야 하므로 캡슐화 여부를 고려합니다.
3. **이름 변경:**
    - `a`를 `area`로 바꾸고, 변수의 역할(넓이 계산)을 보다 직관적으로 표현하도록 합니다.
    - 모든 참조(할당 및 반환)를 새로운 이름으로 수정합니다.
4. **테스트:**
    - 이름만 변경한 경우 로직은 동일하므로, 기존 테스트가 통과하면 올바르게 변경된 것입니다.
    - IDE의 리팩터링 도구를 사용하면 참조를 안전하게 변경할 수 있습니다.
5. **정리:**
    - 변수 이름이 바뀐 만큼, 코드 읽는 입장에서는 “이 변수는 단순히 곱셈 결과가 아니라, 직사각형의 넓이를 의미한다”라는 점이 명확해집니다.
    - 다른 개발자나 미래의 자신이 코드를 봤을 때, 변수의 의미를 바로 파악할 수 있어 유지보수가 쉬워집니다.

**결론:**

변수 이름 바꾸기는 코드의 의미 전달력을 극대화하기 위한 아주 단순하지만 중요한 리팩터링 기법입니다.

특히 넓은 범위에서 사용되거나 외부에 노출되는 변수의 경우, 캡슐화 후에 이름을 바꾸면 더 안전하게 진행할 수 있습니다.

이 예제처럼 지역 변수라도 이름 하나가 코드 전체의 명료도를 크게 좌우할 수 있습니다.

# **6.8** 매개변수 객체 만들기

---

## 1. 배경 및 목적

여러 함수가 함께 사용되는 데이터 항목(예: 시작일과 종료일)이 항상 함께 전달된다면

이를 별도의 데이터 구조(매개변수 객체)로 묶어 전달하면 여러 가지 이점이 있습니다.

- **매개변수 수 감소:**

  여러 개의 관련 매개변수를 하나의 객체로 묶으면 함수 시그니처가 간결해집니다.

- **의미 명료화:**

  관련된 데이터가 하나의 객체로 캡슐화되면, 이 데이터들이 서로 연관되어 있음을 명확하게 드러냅니다.

- **추후 동작 추가 용이:**

  새 객체에 관련 검증이나 부가 동작(예: 기간 유효성 검사)을 추가할 수 있어 확장성이 높아집니다.


예시에서는 기존에 각각 `startDate`와 `endDate`를 받던 함수들을 하나의 매개변수 객체(예, `DateRange`)로 변경합니다.

---

## 2. 리팩터링 절차

1. **데이터 구조 생성:**
    - 관련 매개변수를 담을 새 클래스(예: `DateRange`)를 정의합니다.
    - 클래스에는 두 필드(시작일, 종료일)와 그에 따른 게터(및 필요시 검증/부가동작)를 추가합니다.
2. **함수 시그니처 변경:**
    - 기존 함수들의 선언을 변경하여 매개변수 2개 대신 새 객체를 받도록 수정합니다.
3. **함수 본문 수정:**
    - 함수 내부에서 기존 매개변수를 사용하던 부분을 새 객체의 게터를 사용하도록 변경합니다.
4. **호출부 수정:**
    - 함수를 호출하는 모든 곳에서 개별 값을 전달하던 부분을, 새 `DateRange` 객체 인스턴스로 대체합니다.
5. **테스트:**
    - 각 단계마다 기능이 정상 동작하는지 테스트하여 리팩터링의 안전성을 확인합니다.
6. **정리:**
    - 모든 호출부와 함수 본문이 새 매개변수 객체를 사용하면, 기존 개별 매개변수를 제거합니다.

---

## 3. JAVA 코드 예시

### (1) 리팩터링 전

아래 코드는 여러 함수가 개별 매개변수로 `startDate`와 `endDate`를 받는 상태입니다.

```java
// InvoiceCalculator.java (리팩터링 전)
import java.time.LocalDate;

public class InvoiceCalculator {

    // 청구 금액 계산 (예시: 단순 로직)
    public static double amountInvoiced(LocalDate startDate, LocalDate endDate) {
        // 실제 로직은 startDate와 endDate 사이의 계산을 수행
        return 1000.0; // 예시 리턴값
    }

    // 수금 금액 계산
    public static double amountReceived(LocalDate startDate, LocalDate endDate) {
        return 800.0;
    }

    // 연체 금액 계산
    public static double amountOverdue(LocalDate startDate, LocalDate endDate) {
        return 200.0;
    }

    // 테스트용 main 메서드
    public static void main(String[] args) {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate   = LocalDate.of(2024, 1, 31);

        System.out.println("Amount Invoiced: " + amountInvoiced(startDate, endDate));
        System.out.println("Amount Received: " + amountReceived(startDate, endDate));
        System.out.println("Amount Overdue: "  + amountOverdue(startDate, endDate));
    }
}

```

---

### (2) 리팩터링 후

먼저 **매개변수 객체**로 사용할 `DateRange` 클래스를 만들고,

함수들의 시그니처를 해당 객체를 받도록 변경합니다.

```java
// DateRange.java - 매개변수 객체 (값 객체)
import java.time.LocalDate;

public class DateRange {
    private final LocalDate startDate;
    private final LocalDate endDate;

    public DateRange(LocalDate startDate, LocalDate endDate) {
        // (필요시 여기서 startDate가 endDate보다 이후인지 등 검증 로직 추가 가능)
        this.startDate = startDate;
        this.endDate   = endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}

```

```java
// InvoiceCalculator.java (리팩터링 후)
public class InvoiceCalculator {

    // 청구 금액 계산: 매개변수 객체(DateRange)를 사용
    public static double amountInvoiced(DateRange aDateRange) {
        // aDateRange.getStartDate()와 aDateRange.getEndDate()를 사용
        return 1000.0; // 예시 리턴값
    }

    // 수금 금액 계산
    public static double amountReceived(DateRange aDateRange) {
        return 800.0;
    }

    // 연체 금액 계산
    public static double amountOverdue(DateRange aDateRange) {
        return 200.0;
    }

    // 테스트용 main 메서드
    public static void main(String[] args) {
        DateRange range = new DateRange(
                java.time.LocalDate.of(2024, 1, 1),
                java.time.LocalDate.of(2024, 1, 31)
        );

        System.out.println("Amount Invoiced: " + amountInvoiced(range));
        System.out.println("Amount Received: " + amountReceived(range));
        System.out.println("Amount Overdue: "  + amountOverdue(range));
    }
}

```

---

## 4. 리팩터링 절차 정리 및 설명

1. **데이터 구조 생성:**
    - 새 클래스 `DateRange`를 생성하여 `startDate`와 `endDate`를 필드로 보관하고, 게터를 제공합니다.
    - 이 클래스는 단순한 값 객체로, 두 날짜 사이의 관계(예: 범위 검증)도 추가할 수 있습니다.
2. **함수 시그니처 변경:**
    - `amountInvoiced`, `amountReceived`, `amountOverdue` 함수의 선언을 변경하여,
      이제 개별 매개변수 대신 `DateRange` 객체를 전달받도록 합니다.
    - 초기에는 새로운 매개변수를 추가한 상태에서 기존 매개변수를 유지할 수도 있으나,
      최종적으로 기존 매개변수를 모두 제거합니다.
3. **함수 본문 수정:**
    - 함수 내부에서 `startDate`와 `endDate`에 접근하던 코드를,
      새 객체의 `getStartDate()`와 `getEndDate()` 호출로 바꿉니다.
4. **호출부 수정:**
    - 함수를 호출하는 모든 곳에서, 이제 개별 값 대신`new DateRange(startDate, endDate)`와 같이 인스턴스를 생성하여 전달합니다.
5. **테스트 및 검증:**
    - 각 단계마다 기존 기능이 그대로 유지되는지 테스트합니다.
    - 모든 호출부가 변경된 후, 함수 시그니처에서 기존 매개변수를 완전히 제거합니다.

**설명:**

매개변수 객체 만들기는 관련 매개변수들을 하나의 단위로 묶어,

함수 시그니처를 간결하게 만들고 코드 전반에서 일관된 이름을 사용할 수 있도록 해줍니다.

이렇게 하면 데이터 사이의 관계가 명확해지고, 이후 이 객체에 공통 동작(예: 범위 검증, 기간 계산 등)을 추가하기도 쉬워집니다.

또한, 여러 함수에서 동일한 데이터 그룹을 사용할 때, 호출하는 측과 구현하는 측 모두

코드의 의도를 더 명확하게 이해할 수 있게 되어 유지보수성이 크게 향상됩니다.

---

이와 같이 매개변수 객체를 도입하면,

함수의 매개변수 수를 줄이고, 관련 데이터 항목들 간의 관계를 명확히 하며,

향후 추가 동작 및 검증 로직을 쉽게 확장할 수 있는 강력한 추상화를 얻을 수 있습니다.

# 6.9 여러 함수를 클래스로 묶기

---

## 1. 배경 및 목적

여러 함수가 동일한 데이터(예: 계량기(reading) 데이터)를 인수로 받아 처리하는 경우,

이 함수들이 각각 독립적으로 존재하면 코드의 중복이나 혼란이 발생하기 쉽습니다.

이를 하나의 클래스로 묶으면 다음과 같은 장점이 있습니다.

- **공통 데이터의 캡슐화:**

  여러 함수가 공유하는 데이터(예: customer, quantity, month, year)를 하나의 객체에 넣어

  데이터와 관련된 동작을 한 곳에 모을 수 있습니다.

- **매개변수 수 감소:**

  기존 함수들이 각기 같은 데이터를 매개변수로 받던 것을, 객체 내부에서 데이터에 직접 접근하여

  메서드를 호출할 수 있으므로 매개변수가 줄어듭니다.

- **응집도 향상 및 클라이언트 코드 단순화:**

  함수 호출이 읽기 쉽게 되고, 데이터 관련 동작이 한 클래스에 집중되어 유지보수가 쉬워집니다.


---

## 2. 리팩터링 절차

1. **공통 데이터를 캡슐화하기**
    - 먼저, 기존에 읽기 데이터를 담고 있던 레코드(예: ReadingData)를 클래스로 정의합니다.
2. **함수 옮기기 (메서드로 전환)**
    - 기존의 독립 함수들(`base()`, `taxableCharge()`, `calculateBaseCharge()`)을 새 클래스 내부의
      인스턴스 메서드로 옮깁니다.
    - 이때, 함수의 매개변수로 전달되던 `aReading`은 이제 클래스의 인스턴스 데이터로 대체됩니다.
3. **함수 호출부 수정**
    - 기존에 함수를 호출할 때 인수로 전달하던 부분을, 새로운 클래스로 객체를 생성한 후 메서드를 호출하도록 변경합니다.
4. **테스트 및 정리**
    - 리팩터링 후 동작이 변경되지 않았음을 테스트합니다.

---

## 3. JAVA 코드 예시

### (1) 리팩터링 전

아래 코드는 별도의 함수들이 `ReadingData` 객체(또는 단순 데이터)를 인수로 받아 처리하는 형태입니다.

```java
// ReadingData.java - 읽기 데이터를 담은 단순 POJO
public class ReadingData {
    private String customer;
    private int quantity;
    private int month;
    private int year;

    public ReadingData(String customer, int quantity, int month, int year) {
        this.customer = customer;
        this.quantity = quantity;
        this.month = month;
        this.year = year;
    }

    public String getCustomer() {
        return customer;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }
}

```

```java
// ReadingOperations.java - 함수들이 개별적으로 존재하는 상태
public class ReadingOperations {

    // 기본 요금 계산: 예시로 baseRate와 quantity를 곱한다.
    public static double base(ReadingData aReading) {
        return baseRate(aReading.getMonth(), aReading.getYear()) * aReading.getQuantity();
    }

    // 과세 대상 요금 계산: 기본 요금에서 일정 기준을 넘은 금액에 대해 계산
    public static double taxableCharge(ReadingData aReading) {
        double baseCharge = base(aReading);
        return Math.max(0, baseCharge - taxThreshold(aReading.getYear()));
    }

    // 기본 요금 계산을 한 번에 수행하는 함수
    public static double calculateBaseCharge(ReadingData aReading) {
        return base(aReading);
    }

    // 예시로 제공되는 보조 함수들
    private static double baseRate(int month, int year) {
        // 실제 로직은 달과 연도에 따라 다른 요율을 적용한다고 가정
        return 1.0; // 단순 예시
    }

    private static double taxThreshold(int year) {
        // 연도에 따른 과세 기준
        return 100.0; // 단순 예시
    }

    // 테스트용 main 메서드
    public static void main(String[] args) {
        ReadingData reading = new ReadingData("Ivan", 10, 5, 2017);
        System.out.println("Base Charge: " + calculateBaseCharge(reading));
        System.out.println("Taxable Charge: " + taxableCharge(reading));
    }
}

```

---

### (2) 리팩터링 후

이제 위의 함수들을 하나의 클래스 `Reading`으로 묶어, 데이터와 관련된 동작을 캡슐화합니다.

```java
// Reading.java - 데이터를 캡슐화하고 관련 동작을 메서드로 제공하는 클래스
public class Reading {
    private String customer;
    private int quantity;
    private int month;
    private int year;

    public Reading(String customer, int quantity, int month, int year) {
        this.customer = customer;
        this.quantity = quantity;
        this.month = month;
        this.year = year;
    }

    // 기본 요금을 계산하는 메서드 (객체 자신의 데이터를 사용)
    public double base() {
        return baseRate() * this.quantity;
    }

    // 과세 대상 요금을 계산하는 메서드
    public double taxableCharge() {
        return Math.max(0, base() - taxThreshold());
    }

    // 기본 요금을 계산하는 동작 (여기서는 base()와 동일한 결과)
    public double calculateBaseCharge() {
        return base();
    }

    // 보조 메서드: 내부에서만 사용되는 기본 요율 계산
    private double baseRate() {
        // 달과 연도에 따른 요율 적용 (여기서는 단순 예시)
        return 1.0;
    }

    // 보조 메서드: 내부에서만 사용되는 과세 기준 계산
    private double taxThreshold() {
        return 100.0;
    }

    // 게터 (필요시)
    public String getCustomer() {
        return customer;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    // 테스트용 main 메서드
    public static void main(String[] args) {
        Reading reading = new Reading("Ivan", 10, 5, 2017);
        System.out.println("Base Charge: " + reading.calculateBaseCharge());
        System.out.println("Taxable Charge: " + reading.taxableCharge());
    }
}

```

---

## 4. 리팩터링 절차 정리 및 설명

1. **함수 각각을 새 클래스로 옮기기:**
    - 기존에 `ReadingData`와 관련된 데이터를 다루던 함수들을 하나의 클래스 `Reading`으로 통합합니다.
    - 새 클래스는 읽기 데이터를 필드로 보유하고, 해당 데이터를 사용하는 동작(메서드)을 내부에 정의합니다.
2. **공통 레코드의 멤버 제거:**
    - 원래 함수들의 인수로 전달되던 `aReading`을, 클래스의 인스턴스 데이터(멤버 변수)로 대체합니다.
    - 함수 내부에서 `aReading.getQuantity()`, `aReading.getMonth()` 등이 아니라,`this.quantity`, `this.month` 등으로 직접 접근합니다.
3. **함수 호출부 변경:**
    - 클라이언트 코드(또는 테스트 코드)에서는 이제 단순히 `new Reading(...)`을 통해 객체를 생성한 후,
      해당 객체의 메서드를 호출합니다.
    - 예를 들어, 기존에는 `calculateBaseCharge(aReading)`을 호출했으나,
      이제는 `reading.calculateBaseCharge()`로 변경됩니다.
4. **테스트 및 검증:**
    - 각 단계마다 기능이 그대로 유지되는지 테스트합니다.
    - 데이터와 함수가 한 클래스 내에 묶이면서 응집도가 높아지고, 변경 시 한 곳만 수정하면 되는 장점이 생깁니다.

**설명:**

함수를 하나의 클래스로 묶는 리팩터링은, 데이터와 해당 데이터를 처리하는 함수들이 서로 긴밀하게 연결되어 있을 때

더욱 명확한 도메인 모델을 만들 수 있게 합니다.

이러한 접근 방식은 객체지향 설계의 핵심 원칙(캡슐화, 단일 책임 원칙, 균일 접근 원칙 등)을 따르며,

프로그램의 다른 부분에 이 객체를 전달하여 관련 동작을 일관되게 수행할 수 있는 참조를 제공하게 됩니다.

---

이와 같이 여러 함수를 하나의 클래스로 묶으면,

- 데이터와 함수 간의 결합이 명확해지고,
- 클라이언트는 객체의 메서드를 호출하는 방식으로 동작을 이용하게 되어,
  전체 시스템의 구조와 유지보수성이 크게 향상됩니다.

# 6.10 여러 함수를 변환 함수로 묶기

---

## 1. 배경 및 목적

소프트웨어는 입력 데이터를 받아 여러 가지 정보를 도출합니다.

예를 들어, “청구 기본 요금”과 “과세 대상 요금”을 계산하는 함수들이 여러 곳에서 각각 호출된다면,

각 클라이언트마다 같은 도출 로직이 반복될 위험이 있습니다.

- **변환 함수(Transformation Function)**를 만들면…
- **중복 제거:** 동일한 도출 로직을 한 곳에 모아 관리할 수 있습니다.
- **일관성:** 도출된 부가 정보(예: 기본 요금, 과세 대상 요금)를 항상 같은 방식으로 계산하여 반환합니다.
- **유지보수 용이성:** 도출 로직에 수정이 필요할 때 변환 함수만 변경하면 되므로 코드 전체의 수정 부담이 줄어듭니다.

변환 함수는 원본 데이터를 입력받아 복사(깊은 복사)를 수행한 후, 필요한 추가 정보를 계산하여 그 복사본에 덧붙여 반환합니다.

원본 데이터가 수정되지 않도록 보장하는 테스트도 함께 마련해야 합니다.

---

## 2. 리팩터링 절차

1. **입력 데이터를 그대로 복사하여 반환하는 기본 변환 함수 작성:**
    - 원본 객체에 영향을 주지 않도록 깊은 복사를 수행하는 함수를 만듭니다.
2. **하나의 도출 로직부터 옮기기:**
    - 예를 들어, `base(aReading)` 함수를 호출하여 그 결과를 새 복사본의 필드(예: `baseCharge`)에 기록합니다.
3. **클라이언트 코드 수정:**
    - 기존에는 별도의 함수 호출로 값을 도출하던 코드를, 변환 함수를 호출하여 부가 정보가 포함된 데이터를 사용하도록 변경합니다.
4. **나머지 도출 로직도 동일한 방식으로 처리:**
    - `taxableCharge(aReading)` 등 다른 도출 함수도 같은 변환 함수 내부에 포함시키고,
    - 도출된 값을 각각 새로운 필드에 저장합니다.
5. **테스트:**
    - 변환 함수가 원본 데이터를 변경하지 않고(불변성 유지) 새로운 객체에 부가 정보를 올바르게 추가하는지 확인합니다.

---

## 3. JAVA 코드 예시

### (1) 리팩터링 전

**(a) 데이터 클래스 & 개별 도출 함수**

기존에는 Reading 객체를 전달받아 개별 함수에서 도출 값을 계산합니다.

```java
// Reading.java - 단순 데이터 클래스
public class Reading {
    private String customer;
    private int quantity;
    private int month;
    private int year;

    public Reading(String customer, int quantity, int month, int year) {
        this.customer = customer;
        this.quantity = quantity;
        this.month = month;
        this.year = year;
    }

    public String getCustomer() { return customer; }
    public int getQuantity() { return quantity; }
    public int getMonth() { return month; }
    public int getYear() { return year; }
}

```

```java
// ReadingUtils.java - 도출 함수를 개별적으로 제공
public class ReadingUtils {

    // 기본 요금 계산 함수
    public static double base(Reading reading) {
        return baseRate(reading.getMonth(), reading.getYear()) * reading.getQuantity();
    }

    // 과세 대상 요금 계산 함수
    public static double taxableCharge(Reading reading) {
        double baseCharge = base(reading);
        return Math.max(0, baseCharge - taxThreshold(reading.getYear()));
    }

    // 보조 함수들 (예시)
    private static double baseRate(int month, int year) {
        return 1.0; // 예시: 실제 로직에서는 달, 연도에 따라 다른 요율 적용
    }

    private static double taxThreshold(int year) {
        return 100.0; // 예시: 연도별 면세 기준
    }
}

```

**(b) 클라이언트 코드**

여러 클라이언트가 각각 아래와 같이 도출 함수를 호출합니다.

```java
// ClientBefore.java
public class ClientBefore {
    public static void main(String[] args) {
        // 원본 측정값 획득 (예시)
        Reading reading = new Reading("Ivan", 10, 5, 2017);

        double baseCharge = ReadingUtils.base(reading);
        double taxable = ReadingUtils.taxableCharge(reading);

        System.out.println("Base Charge: " + baseCharge);
        System.out.println("Taxable Charge: " + taxable);
    }
}

```

---

### (2) 리팩터링 후

**(a) 변환 함수(Enriching Function) 작성**

원본 Reading 객체를 깊은 복사한 후, 도출된 값들을 새로운 필드로 기록한 객체(EnrichedReading)를 반환합니다.

※ JAVA에서는 원본 객체에 동적으로 필드를 추가할 수 없으므로, 별도의 EnrichedReading 클래스를 만듭니다.

```java
// EnrichedReading.java - 부가 정보가 포함된 데이터 클래스
public class EnrichedReading extends Reading {
    private double baseCharge;
    private double taxableCharge;

    // Reading의 필드는 부모 클래스 생성자를 통해 초기화
    public EnrichedReading(String customer, int quantity, int month, int year,
                           double baseCharge, double taxableCharge) {
        super(customer, quantity, month, year);
        this.baseCharge = baseCharge;
        this.taxableCharge = taxableCharge;
    }

    public double getBaseCharge() { return baseCharge; }
    public double getTaxableCharge() { return taxableCharge; }
}

```

```java
// ReadingTransformer.java - 변환 함수를 제공하는 클래스
public class ReadingTransformer {

    // 원본 Reading을 받아 깊은 복사한 후 부가 정보를 추가하여 반환
    public static EnrichedReading enrichReading(Reading original) {
        // 깊은 복사: 읽기 데이터는 불변(또는 값 객체)로 취급하여 새로운 인스턴스로 생성
        Reading copy = new Reading(
                original.getCustomer(),
                original.getQuantity(),
                original.getMonth(),
                original.getYear()
        );

        // 도출 함수 호출: 기존 개별 함수들을 이용
        double baseCharge = ReadingUtils.base(copy);
        double taxable = ReadingUtils.taxableCharge(copy);

        // 부가 정보를 포함한 새 객체 반환
        return new EnrichedReading(copy.getCustomer(), copy.getQuantity(),
                                   copy.getMonth(), copy.getYear(),
                                   baseCharge, taxable);
    }
}

```

**(b) 클라이언트 코드 수정**

클라이언트는 이제 변환 함수를 호출하여 부가 정보가 포함된 객체를 사용합니다.

```java
// ClientAfter.java
public class ClientAfter {
    public static void main(String[] args) {
        // 원본 측정값 획득
        Reading reading = new Reading("Ivan", 10, 5, 2017);

        // 변환 함수를 호출하여 부가 정보가 포함된 EnrichedReading 객체를 획득
        EnrichedReading enriched = ReadingTransformer.enrichReading(reading);

        // 이후 부가 정보를 포함한 데이터를 사용
        System.out.println("Base Charge: " + enriched.getBaseCharge());
        System.out.println("Taxable Charge: " + enriched.getTaxableCharge());
    }
}

```

---

## 4. 리팩터링 절차 정리 및 설명

1. **입력 데이터 복사 함수 작성:**
    - 먼저, 원본 Reading 객체를 그대로 복사하는 기본 변환 함수를 만듭니다.
    - JAVA에서는 copy constructor나 별도의 복사 메서드를 사용하여 깊은 복사를 구현합니다.
2. **도출 로직 통합:**
    - 기존 개별 함수인 `base(Reading)`와 `taxableCharge(Reading)`를 그대로 활용하여,
    - 복사된 객체에 부가 정보(예: `baseCharge`, `taxableCharge`)를 계산한 후 기록합니다.
3. **새로운 데이터 클래스 생성:**
    - 원본 데이터에는 없는 부가 정보를 담기 위해, EnrichedReading과 같이 새 클래스를 만들거나,
    - 기존 클래스를 확장하는 방법으로 새로운 필드를 추가합니다.
4. **클라이언트 코드 수정:**
    - 모든 클라이언트는 이제 변환 함수인 `enrichReading()`을 통해 부가 정보가 포함된 객체를 받고,
    - 이 객체의 필드를 사용하여 계산 결과를 쉽게 접근합니다.
5. **테스트:**
    - 변환 함수가 원본 객체를 변경하지 않는지(불변성 유지)와, 올바른 부가 정보가 추가되었는지 테스트합니다.

**설명:**

여러 도출 함수가 산발적으로 존재하면, 동일 로직이 여러 곳에 중복되어 있을 위험이 있습니다.

변환 함수를 사용하여 입력 데이터를 한 곳에서 처리하고 부가 정보를 덧붙이면,

- 도출 로직을 중앙집중식으로 관리할 수 있어 향후 수정이나 검토가 훨씬 용이해집니다.
- 클라이언트는 단순히 부가 정보가 포함된 데이터 객체를 사용하므로, 코드가 깔끔해지고
  계산 결과를 일관되게 활용할 수 있습니다.

이러한 변환 함수 기법은, 특히 원본 데이터가 변경되지 않도록 보장해야 하는 상황에서 매우 유용하며,

필요한 경우 추후 클래스 방식으로 통합하는 방법과 비교하여 선택할 수 있는 옵션입니다.

---

이와 같이 리팩터링을 진행하면,

- 여러 함수에 분산되어 있던 도출 로직을 한 곳(변환 함수 내)에 모아 관리할 수 있고,
- 클라이언트 코드의 중복을 줄여 유지보수성을 크게 향상시킬 수 있습니다.

# 6.11 단계 쪼개기

---

## 1. 배경 및 목적

보통 프로그램은 입력 데이터(예: 주문, 계량값 등)를 받아 여러 가지 파생 정보를 도출합니다.

예를 들어, 독립적인 함수인 base( )와 taxableCharge( )가 각각 입력 데이터에서 기본 요금과 과세 대상 요금을 계산한다고 합시다.

이렇게 동일한 입력 데이터에 대해 여러 곳에서 반복되는 도출 로직이 있다면,

하나의 변환 함수(Transformation Function)로 묶어,

원본 데이터를 복제한 후 필요한 모든 파생 값을 한 번에 계산해 부가 정보로 덧붙이면,

• 중복 코드를 제거하고

• 도출 로직의 변경이 한 곳에서만 일어나도록 하여 유지보수를 용이하게 할 수 있습니다.

---

## 2. 리팩터링 절차

1. **입력 데이터 복사(깊은 복사) 함수 작성**
    - 원본 데이터가 변경되지 않도록, 입력 데이터를 깊은 복사하여 새 객체를 생성합니다.
2. **각 파생 정보를 도출하는 기존 함수들을 호출하여 새 객체에 기록**
    - 예를 들어, `base( )`와 `taxableCharge( )`를 각각 호출하여 계산 결과를 새 객체의 필드(예: baseCharge, taxableCharge)에 저장합니다.
3. **새로운 데이터 클래스 또는 기존 클래스를 확장하여 부가 정보를 포함한 객체 생성**
    - JAVA에서는 동적으로 필드를 추가할 수 없으므로, 부가 정보를 담을 별도의 클래스(예: EnrichedReading)를 정의합니다.
4. **클라이언트 코드를 변환 함수(예: enrichReading())를 호출하도록 수정**
    - 이제 클라이언트는 입력 데이터에 대해 개별 함수 호출 없이, 변환 함수 하나로 모든 파생 정보를 포함한 객체를 받게 됩니다.
5. **테스트**
    - 변환 함수가 원본 데이터를 변경하지 않고(불변성 유지) 올바른 부가 정보를 추가하는지 확인합니다.

---

## 3. JAVA 코드 예시

### (1) 리팩터링 전

여기서는 단순 데이터 클래스와 개별 도출 함수가 분리되어 있는 상황입니다.

**(a) Reading 데이터 클래스**

```java
// Reading.java - 단순 데이터 클래스
public class Reading {
    private String customer;
    private int quantity;
    private int month;
    private int year;

    public Reading(String customer, int quantity, int month, int year) {
        this.customer = customer;
        this.quantity = quantity;
        this.month = month;
        this.year = year;
    }

    public String getCustomer() { return customer; }
    public int getQuantity() { return quantity; }
    public int getMonth() { return month; }
    public int getYear() { return year; }
}

```

**(b) 개별 도출 함수들을 제공하는 유틸 클래스**

```java
// ReadingCalculations.java - 도출 함수들이 개별적으로 존재
public class ReadingCalculations {

    // 기본 요금 계산: 예시로 월, 연도에 따른 요율과 수량을 곱함
    public static double base(Reading reading) {
        return baseRate(reading.getMonth(), reading.getYear()) * reading.getQuantity();
    }

    // 과세 대상 요금 계산: 기본 요금에서 면세 기준을 뺀 값(음수면 0 처리)
    public static double taxableCharge(Reading reading) {
        double baseCharge = base(reading);
        return Math.max(0, baseCharge - taxThreshold(reading.getYear()));
    }

    // 보조 함수들 (예시)
    private static double baseRate(int month, int year) {
        return 1.0; // 실제 로직에서는 달, 연도에 따라 요율이 달라짐
    }

    private static double taxThreshold(int year) {
        return 100.0; // 예시: 연도별 면세 기준
    }
}

```

**(c) 클라이언트 코드**

```java
// ClientBefore.java
public class ClientBefore {
    public static void main(String[] args) {
        Reading reading = new Reading("Ivan", 10, 5, 2017);

        double baseCharge = ReadingCalculations.base(reading);
        double taxable = ReadingCalculations.taxableCharge(reading);

        System.out.println("Base Charge: " + baseCharge);
        System.out.println("Taxable Charge: " + taxable);
    }
}

```

---

### (2) 리팩터링 후

여러 도출 함수를 하나의 **변환 함수**로 묶어, 부가 정보가 포함된 새 객체(EnrichedReading)를 반환합니다.

**(a) EnrichedReading 클래스**

```java
// EnrichedReading.java - 부가 정보를 포함한 데이터 클래스
public class EnrichedReading {
    private final String customer;
    private final int quantity;
    private final int month;
    private final int year;
    private final double baseCharge;
    private final double taxableCharge;

    public EnrichedReading(String customer, int quantity, int month, int year,
                           double baseCharge, double taxableCharge) {
        this.customer = customer;
        this.quantity = quantity;
        this.month = month;
        this.year = year;
        this.baseCharge = baseCharge;
        this.taxableCharge = taxableCharge;
    }

    public String getCustomer() { return customer; }
    public int getQuantity() { return quantity; }
    public int getMonth() { return month; }
    public int getYear() { return year; }
    public double getBaseCharge() { return baseCharge; }
    public double getTaxableCharge() { return taxableCharge; }
}

```

**(b) 변환 함수를 제공하는 ReadingTransformer 클래스**

```java
// ReadingTransformer.java
public class ReadingTransformer {

    // 원본 Reading을 받아 깊은 복사 후 부가 정보를 추가하여 EnrichedReading 객체를 반환
    public static EnrichedReading enrichReading(Reading original) {
        // 깊은 복사: 새로운 Reading 인스턴스를 생성 (원본 데이터는 그대로 유지)
        Reading copy = new Reading(
                original.getCustomer(),
                original.getQuantity(),
                original.getMonth(),
                original.getYear()
        );

        // 기존 도출 함수를 사용하여 부가 정보를 계산
        double baseCharge = ReadingCalculations.base(copy);
        double taxable = ReadingCalculations.taxableCharge(copy);

        // 부가 정보가 포함된 새 객체 반환
        return new EnrichedReading(
                copy.getCustomer(),
                copy.getQuantity(),
                copy.getMonth(),
                copy.getYear(),
                baseCharge,
                taxable
        );
    }
}

```

**(c) 클라이언트 코드 수정**

```java
// ClientAfter.java
public class ClientAfter {
    public static void main(String[] args) {
        Reading reading = new Reading("Ivan", 10, 5, 2017);

        // 변환 함수를 사용하여 부가 정보가 포함된 데이터를 얻음
        EnrichedReading enriched = ReadingTransformer.enrichReading(reading);

        // 이제 클라이언트는 변환된 필드(baseCharge, taxableCharge)를 직접 사용
        System.out.println("Base Charge: " + enriched.getBaseCharge());
        System.out.println("Taxable Charge: " + enriched.getTaxableCharge());
    }
}

```

---

## 4. 리팩터링 절차 정리 및 설명

1. **입력 데이터를 복사하여 반환하는 기본 함수 작성**
    - 원본 객체를 그대로 변경하지 않도록, 새 Reading 인스턴스를 생성하는 복사 과정을 구현합니다.
2. **각 파생 정보를 도출하는 기존 함수를 호출**
    - `ReadingCalculations.base( )`와 `taxableCharge( )`를 호출하여 계산한 값을 저장합니다.
3. **부가 정보를 포함한 새 데이터 클래스 생성**
    - 기존 Reading 데이터에는 없는 도출 결과(예: baseCharge, taxableCharge)를 저장할 수 있도록
      EnrichedReading 클래스를 정의합니다.
4. **변환 함수(enrichReading) 작성**
    - 입력 데이터를 복사한 후, 도출 함수들을 호출해 부가 정보를 새 객체에 기록하고 반환합니다.
5. **클라이언트 코드 수정**
    - 기존에는 개별 함수 호출로 파생 정보를 얻었지만, 이제는 한 번의 변환 함수 호출로
      모든 정보를 포함한 객체를 사용하게 됩니다.
6. **테스트**
    - 변환 함수가 원본 데이터를 변경하지 않으면서 올바른 부가 정보를 추가하는지 검증합니다.

**설명:**

여러 함수로 분산되어 있던 도출 로직을 하나의 변환 함수로 묶으면,

- 도출 로직의 변경이 필요할 때 해당 함수만 수정하면 되고
- 코드 전체에서 동일한 방식으로 파생 정보를 제공받으므로 일관성이 높아집니다.
  또한, 입력 데이터를 깊은 복사하여 반환하기 때문에,
  원본 데이터의 불변성이 보장되어 후속 처리 시 예기치 않은 부작용을 방지할 수 있습니다.

---