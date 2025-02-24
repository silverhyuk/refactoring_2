# 리팩터링과 테스트의 관계

```
1.	리팩터링만으로는 불충분하며, 견고한 테스트 스위트가 필수적
2.	자동화된 리팩터링 도구를 사용하더라도 테스트로 검증 필요
3.	테스트 작성은 시간이 들지만, 궁극적으로 개발 효율을 향상시킴
```

# 4.1 자가 테스트 코드의 가치

<aside>
💡

**리팩터링을 하기 위해서는 테스트 코드가 반드시 필요하다!**

**테스트 자동화는 디버깅 시간을 줄이고, 코드 안정성을 높여준다.**

**테스트 주도 개발(TDD)은 코드 품질을 높이는 강력한 개발 방식이다.**

</aside>

## **자가 테스트 코드의 효과**

### **버그 찾는 시간 단축**

- 자주 테스트를 실행하면 **버그 발생 시점을 정확히 알 수 있어** 원인을 찾는 시간이 단축된다.
- 최근에 작성한 코드에서 오류가 발생했음을 즉시 인지할 수 있다.
- 몇 시간이 걸릴 디버깅 작업을 **몇 분 만에 해결**할 수 있다.

### **생산성 향상**

- 테스트가 **컴파일만큼 쉬워지면서 개발 속도가 크게 향상**되었다.
- 새로운 기능을 추가할 때마다 테스트 코드를 함께 작성하여, **회귀 버그를 방지**할 수 있다.
- 테스트를 지속적으로 수행하면 **디버깅 시간이 크게 줄어들고, 신뢰할 수 있는 코드 작성이 가능**해진다.

### **테스트 코드 작성 습관**

- 처음에는 반복 주기마다 테스트를 추가했으나, 이후에는 **작은 기능을 작성할 때마다 바로 테스트를 추가**하는 방식으로 발전했다.
- 테스트 코드를 점진적으로 작성하면서 **기능과 테스트가 함께 쌓여가는 구조**가 되었다.

## **테스트 주도 개발 (TDD)과 리팩터링**

- 테스트를 작성하는 가장 좋은 시점은 **프로그래밍을 시작하기 전**이다.
- 테스트를 먼저 작성하면,
    - **기능을 설계하는 과정에서 인터페이스를 고민할 수 있고, 필요한 요소를 명확히 정리할 수 있다.**
    - **언제 코드가 완성되었는지를 판단할 수 있다. (테스트를 모두 통과하면 코드 완성!)**
- **켄트 백이 테스트 주도 개발(TDD)을 창시**, 이를 통해 개발 속도와 코드 품질을 동시에 높일 수 있다.
    1. 실패하는 테스트를 먼저 작성한다.
    2. 테스트를 통과하도록 최소한의 코드를 작성한다.
    3. 작성한 코드를 리팩터링하여 깔끔하게 정리한다.
- **테스트 → 코딩 → 리팩터링** 과정을 반복하며 코드를 점진적으로 발전시키는 방식.

# 4.2 테스트할 샘플 코드

Java의 객체지향 스타일을 유지하면서, JSON 데이터를 `ObjectMapper`를 활용해 처리할 수 있도록 구성했다.

### **Province.java (지역 정보 관리)**

```java
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Province {
    private String name;
    private List<Producer> producers;
    private int totalProduction;
    private int demand;
    private int price;

    public Province(String name, int demand, int price, List<Producer> producers) {
        this.name = name;
        this.demand = demand;
        this.price = price;
        this.producers = new ArrayList<>();
        this.totalProduction = 0;
        for (Producer p : producers) {
            addProducer(p);
        }
    }

    public void addProducer(Producer producer) {
        this.producers.add(producer);
        this.totalProduction += producer.getProduction();
    }

    public int getShortfall() {
        return this.demand - this.totalProduction;
    }

    public int getProfit() {
        return getDemandValue() - getDemandCost();
    }

    private int getDemandValue() {
        return getSatisfiedDemand() * this.price;
    }

    private int getSatisfiedDemand() {
        return Math.min(this.demand, this.totalProduction);
    }

    private int getDemandCost() {
        int remainingDemand = this.demand;
        int result = 0;
        this.producers.sort(Comparator.comparingInt(Producer::getCost));

        for (Producer p : this.producers) {
            int contribution = Math.min(remainingDemand, p.getProduction());
            remainingDemand -= contribution;
            result += contribution * p.getCost();
        }
        return result;
    }

    // Getters and Setters
    public String getName() { return name; }
    public List<Producer> getProducers() { return new ArrayList<>(producers); }
    public int getTotalProduction() { return totalProduction; }
    public void setTotalProduction(int totalProduction) { this.totalProduction = totalProduction; }
    public int getDemand() { return demand; }
    public void setDemand(int demand) { this.demand = demand; }
    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }
}

```

---

### **Producer.java (생산자 관리)**

```java
public class Producer {
    private Province province;
    private String name;
    private int cost;
    private int production;

    public Producer(Province province, String name, int cost, int production) {
        this.province = province;
        this.name = name;
        this.cost = cost;
        this.production = production;
    }

    public String getName() { return name; }
    public int getCost() { return cost; }
    public void setCost(int cost) { this.cost = cost; }
    public int getProduction() { return production; }

    public void setProduction(int newProduction) {
        int adjustedProduction = Math.max(0, newProduction); // 음수 방지
        this.province.setTotalProduction(this.province.getTotalProduction() + adjustedProduction - this.production);
        this.production = adjustedProduction;
    }
}

```

---

### **Sample Data (테스트 데이터 생성)**

```java
import java.util.Arrays;
import java.util.List;

public class SampleData {
    public static Province sampleProvinceData() {
        List<Producer> producers = Arrays.asList(
                new Producer(null, "Byzantium", 10, 9),
                new Producer(null, "Attalia", 12, 10),
                new Producer(null, "Sinope", 10, 6)
        );
        Province province = new Province("Asia", 30, 20, producers);

        // Producer 객체가 Province를 참조하도록 설정
        for (Producer producer : province.getProducers()) {
            producer.setProduction(producer.getProduction());
        }

        return province;
    }

    public static void main(String[] args) {
        Province province = sampleProvinceData();
        System.out.println("부족분: " + province.getShortfall());
        System.out.println("총수익: " + province.getProfit());
    }
}

```

---

### **테스트 코드 (JUnit 사용)**

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProvinceTest {

    @Test
    public void testShortfall() {
        Province province = SampleData.sampleProvinceData();
        assertEquals(5, province.getShortfall()); // 부족분 테스트
    }

    @Test
    public void testProfit() {
        Province province = SampleData.sampleProvinceData();
        assertEquals(230, province.getProfit()); // 총 수익 테스트
    }
}

```

### **설명 및 개선점**

1. **Java 객체로 변환**
    - Java의 **클래스 기반 설계**를 사용하여 `Province`와 `Producer` 클래스를 생성함.
    - `Province`는 지역 전체 데이터를 관리하고, `Producer`는 개별 생산자의 데이터를 관리함.
2. **데이터 관리 방식**
    - `Province`에서 `List<Producer>`로 여러 생산자를 관리하며, `addProducer()`를 통해 자동으로 총 생산량을 업데이트하도록 설계.
    - `getShortfall()`와 `getProfit()`을 사용하여 **생산 부족분과 총 수익을 계산**.
3. **테스트 코드 작성**
    - JUnit을 활용해 `testShortfall()` 및 `testProfit()` 테스트를 작성하여, **자가 테스트 코드의 자동화** 가능.
    - **테스트 주도 개발(TDD)** 를 위한 기초적인 셋업 제공.

# 4.3 첫 번째 테스트

<aside>
💡

**자가 테스트를 작성하면 개발 속도가 빨라지고 버그를 쉽게 찾을 수 있다.**

**JUnit을 사용하면 간편하게 테스트를 실행하고, 오류 발생 시 빠르게 확인할 수 있다.**

**테스트를 자주 실행하여 변경된 코드가 정상적으로 동작하는지 확인해야 한다.**

📌 **테스트는 리팩터링의 필수 요소!**

리팩터링을 하려면 **테스트가 반드시 필요**하므로, **항상 테스트를 먼저 작성하는 습관을 들이자!** 🚀

</aside>

### **1. 테스트의 중요성**

- 코드를 작성할 때 **실수 없이 동작하는지 검증**하는 것이 중요하다.
- 테스트가 없다면, 예상치 못한 버그가 발생해도 쉽게 찾을 수 없다.
- **자가 테스트 코드**를 작성하면 자동으로 검증할 수 있어 **개발 속도가 빨라지고 안정성이 높아진다.**

### **2. 테스트의 기본 구조**

- 테스트를 하기 위해서는 **테스트 프레임워크**가 필요하다.
- Java에서는 **JUnit**을 많이 사용한다. (JavaScript의 **Mocha**와 같은 역할)
- **테스트 구조**
    1. **픽스처(Fixture) 설정** → 테스트할 데이터를 준비
    2. **검증(Assertion)** → 실제 결과가 기대값과 같은지 확인

---

## **JUnit (Java)**

### **Mocha (JavaScript) 코드**

```jsx
describe('province', function() {
    it('shortfall', function() {
        const asia = new Province(sampleProvinceData());  // ① 픽스처 설정
        assert.equal(asia.shortfall, 5); // ② 검증
    });
});

```

**픽스처 설정:** `sampleProvinceData()`를 사용해 `Province` 객체를 생성

**검증:** `asia.shortfall` 값이 **5인지 확인**

---

### **JUnit (Java) 코드**

```java
java
복사편집
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProvinceTest {

    @Test
    public void testShortfall() {
        // 픽스처 설정
        Province asia = SampleData.sampleProvinceData();

        // 검증 (shortfall이 5인지 확인)
        assertEquals(5, asia.getShortfall());
    }
}

```

**변경된 사항**

- **Java에서는 `describe()` 대신 클래스(`ProvinceTest`)를 사용**
- **테스트는 `@Test` 어노테이션을 붙여 실행**
- **`assert.equal()` 대신 `assertEquals(expected, actual)` 사용**

---

## **테스트 실패 상황을 확인하는 방법**

테스트가 실패하는 모습을 확인하려면 **일부러 오류를 주입**하면 된다.

### **Mocha (JavaScript)에서 오류 주입**

```jsx

get shortfall() {
    return this.demand - this.totalProduction * 2; // 잘못된 계산
}

```

### **JUnit (Java)에서 오류 주입**

```java

public int getShortfall() {
    return this.demand - this.totalProduction * 2; // 잘못된 계산
}

```

**테스트 실행 후 예상 결과**

```
Expected :5
Actual   :-20
```

**테스트가 실패하면 어디에서 오류가 발생했는지 확인할 수 있다!**

이런 식으로 일부러 오류를 만들어 보고, **테스트가 정상적으로 실패하는지 확인**하는 것이 중요하다.

---

## **테스트를 자주 실행해야 하는 이유**

1. **코드를 몇 분 간격으로 테스트하면 버그 발생 즉시 확인 가능**
2. **테스트 개수가 많아도 프레임워크를 사용하면 한 번에 실행 가능**
3. **JUnit을 사용하면 실패한 테스트를 쉽게 찾을 수 있음**
4. **GUI 도구(예: IntelliJ, Eclipse)에서 실행하면 "초록 막대"와 "빨간 막대"로 결과를 확인 가능**
    - **초록 막대** → 모든 테스트 통과 ✅
    - **빨간 막대** → 테스트 실패 🚨 (원인 분석 필요)

# 4.4 테스트 추가하기

<aside>
💡

**테스트는 모든 메서드를 무조건 작성하는 것이 아니라, "위험 요소" 중심으로 작성해야 한다.**

**매번 새로운 테스트 데이터를 생성하여, 테스트 간 독립성을 유지해야 한다. (`beforeEach()` 활용)**

**일부러 오류를 삽입해서 테스트가 제대로 동작하는지 확인해야 한다.**

**JUnit을 사용하면 Java에서도 간결하고 안정적인 테스트를 작성할 수 있다!**

**리팩터링과 유지보수를 고려한다면, `beforeEach()`를 적극 활용하여 "깨끗한 테스트 코드"를 유지하자!** 🚀

</aside>

### **테스트는 "위험 요소" 중심으로 작성해야 한다**

- **모든 메서드를 테스트하는 것이 목표가 아니다!**
- 중요한 것은 **"실제 문제가 발생할 가능성이 높은 부분을 집중적으로 테스트"** 하는 것.
- 단순한 getter/setter는 오류 가능성이 낮기 때문에 **테스트할 필요 없음**.

### **새로운 테스트 추가: 총수익(profit) 테스트**

- 앞에서 **생산 부족분(shortfall)** 을 테스트했으므로, 이제는 **총수익(profit)** 을 검증하는 테스트를 추가한다.

### **테스트 작성 순서**

1. **기대값을 정확히 계산하기 어려우면, 일단 실행된 값을 사용**
2. **일부러 오류를 삽입하여, 테스트가 잘못된 값을 감지하는지 확인**
3. **테스트 코드에서 중복을 제거하기 위해 beforeEach(beforeAll) 사용**
4. **각 테스트는 독립적으로 실행되도록 구성**

## **Mocha (JavaScript) → JUnit (Java) 변환**

### **Mocha (JavaScript) 코드**

```jsx

describe('province', function() {
    it('shortfall', function() {
        const asia = new Province(sampleProvinceData());
        expect(asia.shortfall).equal(5);
    });

    it('profit', function() {
        const asia = new Province(sampleProvinceData());
        expect(asia.profit).equal(230);
    });
});

```

---

### **JUnit (Java) 코드**

```java
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProvinceTest {

    private Province asia;

    @BeforeEach
    public void setUp() {
        // 모든 테스트 실행 전 새로운 Fixture(테스트 데이터)를 생성
        asia = SampleData.sampleProvinceData();
    }

    @Test
    public void testShortfall() {
        // 생산 부족분 검증
        assertEquals(5, asia.getShortfall());
    }

    @Test
    public void testProfit() {
        // 총수익 검증
        assertEquals(230, asia.getProfit());
    }
}

```

**변경된 사항**

- **`beforeEach()`를 사용하여 매번 새로운 객체를 생성 → 테스트 간 독립성 유지**
- **`assertEquals(expected, actual)`를 사용하여 검증**
- **Java 스타일에 맞춰 `ProvinceTest` 클래스를 사용**

---

## **공유 픽스처의 위험성과 해결 방법**

### 🔴 **잘못된 방식 (공유 픽스처)**

```java

public class ProvinceTest {
    private static final Province asia = SampleData.sampleProvinceData(); // ❌ 공유 픽스처

    @Test
    public void testShortfall() {
        assertEquals(5, asia.getShortfall());
    }

    @Test
    public void testProfit() {
        assertEquals(230, asia.getProfit());
    }
}

```

💣 **문제점:**

- 모든 테스트가 **같은 객체를 공유**하므로, 한 테스트에서 변경이 발생하면 다른 테스트 결과가 달라질 수 있음!
- 예를 들어, 하나의 테스트에서 `asia`의 값을 변경하면 **다른 테스트에 영향을 줄 수도 있음.**
- 따라서 **각 테스트가 독립적으로 실행되도록 새로운 객체를 생성해야 함.**

---

### **올바른 방식 (매번 새로운 객체 생성)**

```java

public class ProvinceTest {
    private Province asia;

    @BeforeEach
    public void setUp() {
        asia = SampleData.sampleProvinceData(); // 테스트마다 새로운 객체 생성
    }

    @Test
    public void testShortfall() {
        assertEquals(5, asia.getShortfall());
    }

    @Test
    public void testProfit() {
        assertEquals(230, asia.getProfit());
    }
}

```

💡 **이렇게 하면 모든 테스트가 독립적으로 실행되므로, 예상치 못한 오류를 방지할 수 있음!**

---

## **테스트가 실제로 실패하는지 확인하는 방법**

1. **일부러 오류를 삽입하여, 테스트가 정상적으로 동작하는지 확인**
2. **예제 코드에서 `profit` 계산식을 변경해서 잘못된 결과가 나오게 만듦**
3. **테스트 실행 후, 실패하는지 확인**

### **Mocha (JavaScript)에서 오류 삽입**

```jsx

get profit() {
    return this.demandValue - this.demandCost * 2; // 잘못된 계산
}

```

### **JUnit (Java)에서 오류 삽입**

```java

public int getProfit() {
    return getDemandValue() - getDemandCost() * 2; // 잘못된 계산
}

```

**테스트 실행 후 예상 결과**

```
Expected :230
Actual   :30
```

이제 **테스트가 올바르게 실패하는지 확인**할 수 있음! 🚀

---

## **왜 `beforeEach()`를 사용해야 할까?**

### **문제점: 중복된 코드**

```java

@Test
public void testShortfall() {
    Province asia = SampleData.sampleProvinceData(); //  테스트마다 중복된 코드
    assertEquals(5, asia.getShortfall());
}

@Test
public void testProfit() {
    Province asia = SampleData.sampleProvinceData(); //  중복된 코드
    assertEquals(230, asia.getProfit());
}

```

💣 **문제점:**

- `Province asia = SampleData.sampleProvinceData();` 가 **모든 테스트에서 반복**됨.
- 테스트 코드가 길어지고 유지보수가 어려워짐.

---

### **해결책: `beforeEach()`를 사용하여 중복 제거**

```java

@BeforeEach
public void setUp() {
    asia = SampleData.sampleProvinceData();
}

@Test
public void testShortfall() {
    assertEquals(5, asia.getShortfall());
}

@Test
public void testProfit() {
    assertEquals(230, asia.getProfit());
}

```

💡 **이제 모든 테스트가 같은 초기 데이터를 사용하면서도, 독립적으로 실행됨!**

# 5. 픽스처 수정하기

---

## 📌 **테스트의 패턴: 준비 → 실행 → 검증 (Setup-Exercise-Verify)**

1. **설정 (Setup):**
    - `beforeEach` 블록에서 표준 픽스처(테스트용 기본 데이터)를 미리 생성해둔다.
    - 이 픽스처는 여러 테스트에서 공통적으로 사용되므로, 초기 상태가 일정하게 유지된다.
2. **실행 (Exercise):**
    - 테스트 코드 내에서, 실제로 동작시키고자 하는 메서드(여기서는 Producer의 `production` 세터)를 호출한다.
    - 예제에서는 `asia.producers[0].production = 20;`을 통해 특정 생산자의 생산량을 변경한다.
3. **검증 (Verify):**
    - 실행 결과로 픽스처의 상태가 올바르게 변경되었는지 확인한다.
    - 예제에서는 두 가지 검증이 이루어진다:
        - `asia.shortfall` 값이 예상대로 `6`이 되었는지
        - `asia.profit` 값이 예상대로 `292`가 되었는지

---

## 📌 **테스트 작성 시 고려사항**

- **단일 검증 원칙:**
    - 보통 `it` 블록 하나에는 한 가지 검증(assertion)을 넣는 것이 좋다.
    - 그러나 여기서는 두 속성(생산 부족분과 총수익)이 밀접하게 연관되어 있기 때문에 하나의 `it` 블록에 두 검증을 넣어도 문제가 없다고 판단한 것이다.
    - 만약 더 세분화하고 싶다면, 별도의 `it` 블록으로 나눌 수 있다.
- **실제 사용 시나리오 반영:**
    - 사용자가 값을 변경하는 상황을 테스트하기 때문에, 실제로 사용자 인터랙션에 따른 픽스처 변경을 잘 검증하는 것이 중요하다.
    - 세터가 단순한 경우는 테스트에서 생략할 수 있으나, 복잡한 로직이 들어간 세터는 반드시 테스트해봐야 한다.

---

## ✅ **요약**

- **테스트의 기본 패턴은 "준비(Setup) → 실행(Exercise) → 검증(Verify)"이다.**
- **`beforeEach`를 사용해 공통 픽스처를 설정하면 테스트 간 독립성을 보장할 수 있다.**
- **Producer의 `production` 세터처럼 복잡한 동작이 있는 경우, 값을 변경한 후 기대하는 상태(예: shortfall, profit)가 제대로 계산되는지 검증하는 테스트를 작성해야 한다.**
- **한 테스트 안에 여러 검증이 들어갈 수 있으나, 문제가 발생했을 때 실패 원인을 쉽게 파악하려면 검증 단위를 분리하는 것도 좋은 방법이다.**

이러한 방식으로 테스트를 구성하면, 코드 변경 시 예상치 못한 오류를 빠르게 찾아내고, 리팩터링이나 기능 개선 시 안정성을 확보할 수 있습니다.

# 6. 경계 조건 검사하기

아래는 위 내용을 정리한 내용입니다.

---

## 📌 **경계 조건 테스트의 중요성**

- **Happy Path 외의 상황**
    - 지금까지 작성한 테스트는 “꽃길” 상황, 즉 모든 입력이 정상일 때를 다뤘다.
    - 하지만 실제 사용에서는 사용자가 잘못된 값을 입력하거나, 경계 조건(예: 빈 배열, 0, 음수, 빈 문자열 등)이 발생할 수 있다.
    - 이러한 상황에 대해 테스트를 작성함으로써, 프로그램이 예상치 못한 입력에서도 어떻게 동작하는지 확인할 수 있다.

---

## 📌 **테스트 작성 패턴 (Setup → Exercise → Verify)**

1. **설정 (Setup):**
    - `beforeEach` 구문을 이용하여, 각 테스트마다 동일한 초기 픽스처(예: Province 객체)를 생성한다.
    - 이렇게 하면 테스트 간에 상태가 공유되지 않아, 한 테스트에서의 수정이 다른 테스트에 영향을 주지 않는다.
2. **실행 (Exercise):**
    - 테스트에서는 특정 조건(예: 생산자 목록이 비어 있는 경우, 수요가 0이거나 음수인 경우, 입력 값이 빈 문자열인 경우 등)을 만들어 실행한다.
3. **검증 (Verify):**
    - 실행 결과로 예상되는 값(예: shortfall, profit 등)을 assert를 통해 확인한다.
    - 예를 들어, 생산자가 없는 경우 shortfall은 수요와 같아야 하고, profit은 0이어야 한다.

---

## 📌 **예시 테스트 케이스**

1. **생산자가 없는 경우 (No Producers)**
    - **픽스처:**
        - `name: "No producers"`
        - `producers: []` (빈 배열)
        - `demand: 30, price: 20`
    - **검증:**
        - `shortfall`는 30
        - `profit`은 0
2. **수요가 0인 경우 (Zero Demand)**
    - **실행:**
        - `asia.demand = 0;`
    - **검증:**
        - `shortfall`는 -25 (이미 생산된 값이 25 초과일 경우)
        - `profit`은 0
3. **수요가 음수인 경우 (Negative Demand)**
    - **실행:**
        - `asia.demand = -1;`
    - **검증:**
        - `shortfall`는 -26
        - `profit`은 -10
    - **논의:**
        - 음수 수요에 대해 profit이 음수가 나오는 것이 고객 입장에서 타당한지 고민해볼 필요가 있다.
        - 수요의 최솟값은 0이어야 한다면, 수요 세터에서 음수를 0으로 처리하거나 에러를 던지는 방식으로 변경할 수도 있다.
4. **입력값이 빈 문자열인 경우 (Empty String Demand)**
    - **실행:**
        - UI에서 전달되는 값이 빈 문자열인 상황
    - **검증:**
        - `shortfall`은 `NaN` (또는 에러 처리를 통해 적절한 값)
        - `profit`도 `NaN`이 되는지 확인
5. **생산자 필드에 문자열을 전달한 경우 (String for Producers)**
    - **실행:**
        - producers 필드에 문자열을 전달하여, JSON 파싱에서 오류가 발생하는 상황 테스트
    - **검증:**
        - 프로퍼티 접근 시 `doc.producers.forEach` 호출 오류가 발생하도록 하여, 에러 메시지로 테스트 실패를 확인

---

## 📌 **경계 조건 테스트의 효과**

- **예기치 않은 입력 처리:**
    - 정상적인 상황뿐 아니라 경계 조건에서 프로그램이 어떻게 동작하는지 확인함으로써, 코드의 견고함을 확보할 수 있다.
- **에러 대응 설계:**
    - 경계 조건에서 발생하는 오류를 확인하고, 이를 처리하는 방법(예: 명확한 에러 메시지 출력, 기본값 적용 등)을 고민할 수 있다.
- **리팩터링 보호막:**
    - 리팩터링 과정 중 의도하지 않은 동작 변화가 있는지 경계 조건 테스트를 통해 빠르게 확인할 수 있다.

---

## 📌 **결론**

- **경계 조건에 집중:**
    - 프로그램의 “안전망”을 구축하기 위해, 정상 입력 외에도 경계 조건(빈 배열, 0, 음수, 빈 문자열 등)에 대한 테스트를 반드시 작성해야 한다.
- **테스트 작성의 목적:**
    - 단순히 필드 접근이나 간단한 메서드를 검증하는 것보다, 위험 요인과 복잡한 계산, 그리고 잘못된 입력에 대한 반응을 중점적으로 테스트하는 것이 중요하다.
- **테스트 실패와 에러 구분:**
    - 검증 단계에서 값이 예상과 다르면 실패로 처리하고, 설정 단계에서 발생하는 예외는 에러로 처리하여, 문제 발생 지점을 명확히 파악할 수 있도록 한다.

# 끝나지 않은 여정

## **테스트와 리팩터링: 핵심 요점**

1. **리팩터링의 본질과 테스트의 역할**
    - 이 책의 주제는 리팩터링이지만, 테스트는 리팩터링을 가능하게 하는 중요한 토대이다.
    - 예전엔 테스트를 별도의 팀에 맡겼다면, 지금은 모든 뛰어난 개발자가 최우선으로 관심을 가져야 할 주제로 자리 잡았다.
2. **단위 테스트(Unit Testing)의 중요성**
    - 단위 테스트는 코드의 작은 영역만 빠르게 검증하도록 설계된 테스트로, 자가 테스트 시스템의 핵심이다.
    - 제품의 기능뿐 아니라, 경계 조건(예: 빈 배열, 0, 음수, 잘못된 입력 등)에 대해서도 테스트하여 프로그램의 견고함을 높인다.
3. **테스트의 지속적 발전과 보강**
    - 기능을 새로 추가할 때마다 테스트를 함께 추가하고, 기존 테스트도 주기적으로 리팩터링하며 개선해 나가야 한다.
    - 버그가 발견되면, 우선 그 버그를 드러내는 단위 테스트를 작성하여, 동일한 문제가 재발하지 않도록 해야 한다.
4. **테스트의 충분함에 대한 평가**
    - 테스트 커버리지(coverage)는 참고할 만하지만, 테스트 스위트의 품질을 완전히 판단해주지는 않는다.
    - 핵심은 “테스트가 결함을 잡을 수 있다는 믿음”이며, 리팩터링 후 모든 테스트가 초록색이면 충분히 신뢰할 수 있다고 할 수 있다.
5. **테스트 과잉 vs. 테스트 부족**
    - 테스트를 너무 많이 작성해 개발 속도가 느려진다면 재검토할 필요가 있지만, 일반적으로는 부족한 테스트가 문제다.
    - 실전에서는 위험 요소에 집중하는 테스트를 작성하는 것이 생산성과 코드 품질 향상에 훨씬 효과적이다.

---

결론적으로, 테스트는 리팩터링을 안전하게 수행하기 위한 필수적인 보호막이자, 프로그래밍 실천법 중 하나로 자리 잡고 있다. 지속적으로 테스트 스위트를 보강하고 개선하는 습관은 리팩터링뿐 아니라 전반적인 소프트웨어 품질 향상에 큰 역할을 한다.