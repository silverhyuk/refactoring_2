# 캡슐화

- **정보 은닉의 핵심:**

  각 모듈이 자신 외의 다른 부분에 내부 비밀을 노출하지 않도록 하는 것이 모듈 분리의 핵심입니다.

- **데이터 캡슐화:**
    - **레코드/컬렉션 캡슐화:** 데이터 구조를 숨기는 방법으로, 각각 '레코드 캡슐화하기'와 '컬렉션 캡슐화하기'가 사용됩니다.
    - **기본형 캡슐화:** 기본형 데이터도 객체로 감싸 비밀을 은닉할 수 있으며, 이로 인해 부수적인 효과가 발생합니다.
- **임시 변수 처리:**

  임시 변수는 계산 순서 유지와 이후 참조가 필요해 난감한 경우가 많습니다. 이때 '임시 변수를 질의 함수로 바꾸기' 기법이 길고 복잡한 함수를 분해하는 데 유용합니다.

- **클래스 활용:**
    - **정보 은닉 목적:** 클래스는 원래 내부 정보를 숨기기 위해 설계되었습니다.
    - **클래스 단위 리팩터링:** 여러 함수들을 하나의 클래스로 묶거나, 클래스 추출하기/인라인하기를 통해 보다 명확한 구조를 만듭니다.
- **클래스 간 연결 관계 은닉:**
    - **위임 숨기기:** 클래스 간의 복잡한 연결 관계를 감출 수 있습니다.
    - **중개자 제거:** 과도한 은닉은 인터페이스 비대를 초래할 수 있으므로, 필요 시 중개자 제거하기 기법을 사용합니다.
- **함수 캡슐화:**

  함수도 하나의 캡슐화 단위로, 때로는 알고리즘 전체를 추출해 새로운 함수로 만든 후, '알고리즘 교체하기' 기법을 적용할 수 있습니다.


# 7.1 레코드 캡슐화하기

## 배경

- **레코드의 장점과 한계:**

  대부분의 언어는 관련 데이터를 하나의 단위로 묶어 표현하는 레코드 구조(예: 구조체, 딕셔너리)를 제공합니다.

    - **장점:** 데이터를 직관적으로 묶어 의미 있는 단위로 전달할 수 있음.
    - **한계:** 계산으로 얻어지는 값과 단순 저장 값의 구분이 애매해, 예를 들어 값의 범위를 표현할 때 `{start: 1, end: 5}`나 `{start: 1, length: 5}` 등의 다양한 표현이 필요함.
- **객체를 활용한 캡슐화:**

  가변 데이터의 경우, 레코드 대신 객체를 사용하면 내부 저장 방식을 숨기면서 접근자(getter/setter)를 통해 데이터를 제공할 수 있습니다.

    - **이점:**
        - 내부 구현을 감추어 인터페이스만 공개함으로써, 이름 변경 등 점진적인 리팩터링이 용이함.
        - 클라이언트는 ‘무엇이 저장되어 있는지’ 대신 ‘어떻게 사용해야 하는지’에만 집중할 수 있음.
    - **적용 대상:** JSON이나 XML 직렬화처럼 복잡하거나 중첩된 데이터 구조도 캡슐화하면 수정 및 확장이 쉬워집니다.

---

```java
public class Organization {
    // 내부 데이터는 캡슐화를 위해 private으로 선언
    private String name;
    private String country;

    // 생성자: 초기 데이터를 전달받아 필드를 초기화함
    public Organization(String name, String country) {
        this.name = name;
        this.country = country;
    }

    // Getter: 조직 이름을 읽어옴
    public String getName() {
        return name;
    }

    // Setter: 조직 이름을 변경함
    public void setName(String name) {
        this.name = name;
    }

    // Getter: 국가 정보를 읽어옴
    public String getCountry() {
        return country;
    }

    // Setter: 국가 정보를 변경함
    public void setCountry(String country) {
        this.country = country;
    }
}

```

## 절차

1. **레코드를 담은 변수 캡슐화:**
    - 레코드를 직접 참조하는 대신, 해당 레코드를 반환하는 함수를 만들어 변수 접근을 중앙집중화합니다.
    - 이 함수의 이름은 검색하기 쉽도록 명확하게 지어야 합니다.
2. **단순 클래스 도입:**
    - 기존 레코드 변수를 단순한 클래스로 대체합니다.
    - 이 클래스는 내부 원본 레코드를 감싸며, 원본 데이터를 반환하는 접근자(게터)를 정의합니다.
    - 기존 캡슐화 함수들이 이 접근자를 사용하도록 수정합니다.
3. **테스트 수행:**
    - 변경 사항이 올바르게 작동하는지 테스트합니다.
4. **새로운 반환 함수 작성:**
    - 원본 레코드 대신 새로 정의한 클래스 타입의 객체를 반환하는 함수를 작성합니다.
5. **클라이언트 코드 수정:**
    - 기존에 레코드를 반환하던 함수를 새 함수로 교체합니다.
    - 클라이언트 코드는 직접 필드에 접근하지 않고 반드시 객체의 접근자(getter/setter)를 사용하도록 수정합니다.
    - 변경하는 부분마다 테스트를 수행합니다.
    - **참고:** 중첩 구조처럼 복잡한 레코드라면, 데이터를 갱신하는 클라이언트의 사용 패턴(읽기 전용 vs. 수정)을 면밀히 확인하여 복제본이나 읽기전용 프락시 제공 여부를 고려합니다.
6. **불필요한 접근자 제거:**
    - 클래스에서 원본 데이터를 반환하는 접근자와 초기 캡슐화 단계에서 만든 원본 레코드를 반환하는 함수를 제거합니다.
7. **최종 테스트:**
    - 전체 시스템이 의도한 대로 동작하는지 최종 테스트를 진행합니다.
8. **재귀적 적용:**
    - 레코드의 필드 자체가 또 다른 데이터 구조(중첩 구조)라면, 해당 필드에 대해 레코드 캡슐화하기와 컬렉션 캡슐화하기를 재귀적으로 적용합니다.

## 예시1. 간단한 레코드 캡슐화

### 1. 레코드를 단순히 변수로 사용

자바스크립트에서는 상수 객체(`organization`)를 직접 사용하다가, 캡슐화를 위해 게터 함수를 만들었습니다. 자바에서는 이를 `Map`으로 표현할 수 있습니다.

```java
import java.util.HashMap;
import java.util.Map;

public class Main {
    // 초기 레코드: Map을 사용하여 조직 데이터를 저장
    private static Map<String, Object> organization = new HashMap<>();
    static {
        organization.put("name", "Acme Gooseberries");
        organization.put("country", "GB");
    }

    // 캡슐화된 변수 접근자: 이름을 검색하기 쉽도록 의도적으로 지음
    public static Map<String, Object> getRawDataOfOrganization() {
        return organization;
    }

    public static void main(String[] args) {
        // 읽기 예시: 게터를 통해 조직 이름 접근
        System.out.println("<h1>" + getRawDataOfOrganization().get("name") + "</h1>");

        // 쓰기 예시: 게터를 통해 조직 이름 변경
        getRawDataOfOrganization().put("name", "NewName");
        System.out.println("<h1>" + getRawDataOfOrganization().get("name") + "</h1>");
    }
}

```

**설명:**

- `organization` 변수는 `HashMap`으로 정의되어 있으며, 초기 데이터가 저장되어 있습니다.
- `getRawDataOfOrganization()` 메서드를 통해 직접 변수에 접근하도록 함으로써, 나중에 캡슐화 대상이 될 부분을 한 곳에 집중시킵니다.

---

### 2. 캡슐화 – 레코드를 클래스로 대체

이제 레코드를 단순한 클래스(`Organization`)로 감싸고, 내부 데이터를 완전히 캡슐화합니다. 최종적으로 데이터 필드를 개별 필드로 분리하여, 원본 입력 데이터와의 연결을 끊습니다.

```java
// Organization.java
public class Organization {
    // 내부 데이터를 개별 필드로 캡슐화하여 저장
    private String name;
    private String country;

    // 생성자: 초기 데이터를 받아 필드를 설정
    public Organization(String name, String country) {
        this.name = name;
        this.country = country;
    }

    // Getter: 조직 이름 읽기
    public String getName() {
        return name;
    }

    // Setter: 조직 이름 변경
    public void setName(String name) {
        this.name = name;
    }

    // Getter: 국가 코드 읽기
    public String getCountry() {
        return country;
    }

    // Setter: 국가 코드 변경
    public void setCountry(String country) {
        this.country = country;
    }
}

```

```java
// Main.java
public class Main {
    // Organization 객체로 캡슐화된 레코드
    private static Organization organization = new Organization("Acme Gooseberries", "GB");

    // 캡슐화 함수: 객체를 반환하여 클라이언트가 직접 내부 데이터를 다루지 않도록 함
    public static Organization getOrganization() {
        return organization;
    }

    public static void main(String[] args) {
        // 읽기 예시: 게터를 통해 조직 이름 접근
        System.out.println("<h1>" + getOrganization().getName() + "</h1>");

        // 쓰기 예시: 세터를 통해 조직 이름 변경
        getOrganization().setName("NewName");
        System.out.println("<h1>" + getOrganization().getName() + "</h1>");
    }
}

```

**설명:**

- **클래스 도입:**
    - `Organization` 클래스는 초기 레코드 데이터를 받아 내부의 `name`과 `country` 필드로 저장합니다.
    - 이때 내부 필드는 `private`으로 선언되어 외부에서 직접 접근할 수 없으므로, 캡슐화가 이루어집니다.
- **접근자 제공:**
    - `getName()`, `setName()`, `getCountry()`, `setCountry()`와 같은 메서드를 통해 클라이언트는 내부 데이터에 안전하게 접근합니다.
- **클라이언트 코드 수정:**
    - `Main` 클래스는 더 이상 `Map`을 직접 다루지 않고, `getOrganization()` 메서드를 통해 `Organization` 객체를 사용합니다.
    - 이를 통해 내부 데이터 구조의 변경이 외부에 미치는 영향을 최소화합니다.

이처럼 레코드 캡슐화는 초기 변수 접근을 중앙집중화한 뒤, 점진적으로 객체로 전환하여 내부 구현을 숨기는 과정입니다. 마치 "내 비밀은 내 것!"이라고 선언하며, 외부에서는 단지 인터페이스만 사용하도록 만드는 기법이라 할 수 있습니다.

## 예제2. 중첩된 레코드를 캡슐화

---

### 1. 문제 상황

원래 자바스크립트에서는 고객 데이터를 JSON 문서처럼 중첩된 구조(고객 ID를 키로 하는 해시맵, 각 고객은 이름, ID, 그리고 연도/월별 사용량을 갖는 nested record)로 저장합니다.

예를 들어, 업데이트나 읽기는 아래와 같이 이루어집니다.

- **업데이트:**

    ```jsx
    customerData[customerID].usages[year][month] = amount;
    
    ```

- **읽기:**

    ```jsx
    function compareUsage(customerID, laterYear, month) {
      const later = customerData[customerID].usages[laterYear][month];
      const earlier = customerData[customerID].usages[laterYear - 1][month];
      return { laterAmount: later, change: later - earlier };
    }
    ```


캡슐화 과정에서는 먼저 변수(여기서는 전체 고객 데이터)를 캡슐화한 후, 업데이트와 읽기 부분을 전용 메서드로 뽑아내어 한 곳에서 관리하도록 합니다.

---

### 2. Java 코드 예제

### (1) 고객 개별 레코드를 캡슐화하는 클래스 (Customer)

```java
import java.util.HashMap;
import java.util.Map;

public class Customer {
    private String id;
    private String name;
    // usages: year -> (month -> usage amount)
    private Map<String, Map<String, Integer>> usages;

    public Customer(String id, String name) {
        this.id = id;
        this.name = name;
        this.usages = new HashMap<>();
    }

    // 고객 기본 정보 접근자
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // 중첩된 사용량 데이터에 대한 접근 및 수정

    // 사용량 읽기: 해당 연도, 월에 저장된 사용량을 반환 (없으면 0)
    public int getUsage(String year, String month) {
        if (usages.containsKey(year) && usages.get(year).containsKey(month)) {
            return usages.get(year).get(month);
        }
        return 0;
    }

    // 사용량 업데이트: 해당 연도, 월에 사용량을 기록
    public void setUsage(String year, String month, int amount) {
        usages.computeIfAbsent(year, y -> new HashMap<>()).put(month, amount);
    }
}

```

### (2) 전체 고객 데이터를 캡슐화하는 클래스 (CustomerData)

```java
import java.util.HashMap;
import java.util.Map;

public class CustomerData {
    // 고객 ID를 키로 하는 고객 객체의 맵
    private Map<String, Customer> data;

    public CustomerData(Map<String, Customer> data) {
        // 입력 데이터와의 연결을 끊기 위해 복사할 수 있음 (여기선 간단히 복사)
        this.data = new HashMap<>(data);
    }

    // 특정 고객 객체 반환
    public Customer getCustomer(String customerId) {
        return data.get(customerId);
    }

    // 업데이트를 위한 setter: 고객의 사용량을 갱신
    public void setUsage(String customerId, String year, String month, int amount) {
        Customer customer = data.get(customerId);
        if (customer != null) {
            customer.setUsage(year, month, amount);
        }
    }

    // 읽기를 위한 접근자: 특정 고객의 특정 기간 사용량 반환
    public int usage(String customerId, String year, String month) {
        Customer customer = data.get(customerId);
        if (customer != null) {
            return customer.getUsage(year, month);
        }
        return 0;
    }

    // (옵션) 원본 데이터를 복제하여 반환하는 메서드
    public Map<String, Customer> getRawData() {
        Map<String, Customer> copy = new HashMap<>();
        // 여기서는 간단한 복사만 수행 (실제 사용 시에는 깊은 복사가 필요할 수 있음)
        for (Map.Entry<String, Customer> entry : data.entrySet()) {
            copy.put(entry.getKey(), entry.getValue());
        }
        return copy;
    }
}

```

### (3) 캡슐화된 데이터를 활용하는 클라이언트 코드 (Main)

```java
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // 초기 고객 데이터 구성 (예: "1920"번 고객)
        Map<String, Customer> customerMap = new HashMap<>();

        Customer customer1920 = new Customer("1920", "martin");
        customer1920.setUsage("2016", "1", 50);
        customer1920.setUsage("2016", "2", 55);
        customer1920.setUsage("2015", "1", 70);
        customer1920.setUsage("2015", "2", 63);
        customerMap.put("1920", customer1920);

        // 다른 고객 추가 (예: "38673"번 고객)
        Customer customer38673 = new Customer("38673", "neal");
        // 필요한 경우 추가 사용량 데이터 입력
        customerMap.put("38673", customer38673);

        // CustomerData 객체 생성: 전체 데이터를 캡슐화
        CustomerData customerData = new CustomerData(customerMap);

        // 업데이트 예시: 고객 "1920"의 2016년 3월 사용량을 60으로 갱신
        customerData.setUsage("1920", "2016", "3", 60);

        // 읽기 예시: 고객 "1920"의 2016년 2월 사용량과 2015년 2월 사용량을 비교
        int later = customerData.usage("1920", "2016", "2");
        int earlier = customerData.usage("1920", "2015", "2");
        int change = later - earlier;
        System.out.println("Customer 1920, Month 2: later usage = " + later + ", change = " + change);

        // 또는 전체 데이터를 복제해서 읽을 수도 있음
        Map<String, Customer> rawData = customerData.getRawData();
        // rawData를 이용하여 추가 작업 가능 (수정은 피해야 함)
    }
}

```

---

### 3. 예제 설명

- **중첩된 데이터 구조 캡슐화:**
    - **Customer 클래스:**

      고객 한 명의 레코드를 캡슐화합니다. 이름과 ID 외에, `usages`라는 중첩 맵을 통해 연도별, 월별 사용량을 관리합니다.

      내부 데이터에 직접 접근하는 대신 `getUsage()`와 `setUsage()` 메서드를 통해 값을 읽고 쓰도록 합니다.

    - **CustomerData 클래스:**

      전체 고객 데이터를 하나의 클래스 안에 모아, 고객 레코드 전체의 업데이트와 읽기를 담당합니다.

      예를 들어, `setUsage()` 메서드는 고객 ID, 연도, 월을 받아 내부의 `Customer` 객체에 위임하여 값을 갱신합니다.

- **업데이트와 읽기의 중앙집중화:**
    - 클라이언트(여기서는 `Main` 클래스)는 더 이상 중첩된 구조에 직접 접근하지 않습니다.
    - 모든 수정은 `customerData.setUsage(...)`를, 모든 읽기는 `customerData.usage(...)`를 통해 수행되어, 향후 구조 변경에도 클라이언트 코드의 영향을 최소화합니다.
- **데이터 복사와 안전성:**
    - `getRawData()`와 같이 내부 데이터를 복제하여 반환하는 방법을 통해, 클라이언트가 원본 데이터를 직접 수정하지 못하도록 할 수 있습니다.
    - 복사 비용이나 원본과의 혼동 문제가 있을 수 있으므로, 경우에 따라 읽기 전용 프록시나 재귀적 캡슐화를 적용할 수 있습니다.

이처럼 중첩된 레코드를 캡슐화하면 데이터 업데이트와 읽기 부분을 명시적 API로 집중시켜 관리할 수 있으므로, 코드의 유지보수성과 안전성이 크게 향상됩니다.

마치 “모든 수정은 한 곳에서, 모든 읽기는 한 곳에서” 관리하는 비밀 요원처럼 말이죠!

# 7.2 컬렉션 캡슐화하기

## **배경**

- **가변 데이터 캡슐화의 필요성:**

  가변 데이터를 캡슐화하면 언제, 어떻게 데이터 구조가 수정되는지 파악하기 쉽고, 필요 시 구조 변경이 용이해집니다.

- **컬렉션 접근의 문제점:**

  컬렉션의 게터가 원본 컬렉션을 반환하면, 외부 클라이언트가 그 컬렉션을 직접 수정할 수 있어 캡슐화가 깨집니다.

- **해결책:**
    - **전용 변경자 메서드:**컬렉션을 소유한 클래스에 `add()`나 `remove()`와 같은 메서드를 만들어, 컬렉션의 원소 변경은 해당 메서드를 통해서만 이루어지게 합니다.
    - **읽기 전용/복제본 반환:**컬렉션 게터가 원본 대신 읽기 전용 프락시나 복제본을 반환하도록 하여, 클라이언트가 실수로 내부 데이터를 수정하지 못하게 합니다.
- **일관성 유지의 중요성:**

  코드베이스 내에서 한 가지 접근 방식을 선택해 일관되게 적용하는 것이 중요합니다.


## 절차

1.  **변수 캡슐화:**

    컬렉션 참조가 아직 캡슐화되지 않았다면, 먼저 Encapsulate Variable (132) 기법을 적용합니다.

2.  **수정자 메서드 추가:**

    컬렉션에 원소를 추가하거나 제거할 수 있도록 add(), remove() 등의 전용 함수를 만듭니다.

3.  **세터 처리:**
    - 컬렉션에 대해 세터가 있다면 Remove Setting Method (331)를 적용합니다.
    - 세터가 제거되지 않는 경우, 제공된 컬렉션의 복사본을 받아 내부 필드에 할당하도록 수정합니다.
4.  **정적 검사:**

    코드 정적 분석을 통해 캡슐화 적용에 문제가 없는지 확인합니다.

5.  **모든 참조점 수정:**

    컬렉션에 직접 수정자 메서드를 호출하는 부분을 찾아, 새로 정의한 add/remove 함수를 사용하도록 변경합니다. 각 변경 후에는 테스트를 진행합니다.

6.  **게터 수정:**

    컬렉션 게터를 수정하여 원본 컬렉션 대신 읽기 전용 프락시나 복사본을 반환하도록 합니다.

7.  **최종 테스트:**

    전체 시스템이 의도대로 동작하는지 최종적으로 테스트합니다.


## 예시

---

### 1. Person 클래스 (컬렉션 캡슐화 적용)

- 내부에 ArrayList로 코스 목록을 보관하고,
- 직접 리스트를 반환하거나 수정하지 못하도록,
- 수정자(addCourse, removeCourse)와 게터(getCourses)를 제공하며,
- 게터는 내부 컬렉션의 복사본을 반환합니다.

```java
import java.util.ArrayList;
import java.util.List;

public class Person {
    private String name;
    // 내부 컬렉션: 외부에 직접 노출되지 않도록 private로 관리
    private List<Course> courses;

    public Person(String name) {
        this.name = name;
        this.courses = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    // 내부 리스트를 직접 반환하지 않고, 복사본을 반환하여 수정 방지
    public List<Course> getCourses() {
        return new ArrayList<>(courses);
    }

    // 제거. 만약 setter가 필요하다면, 입력 컬렉션의 복사본을 저장
    //public void setCourses(List<Course> courses) {
    //    this.courses = new ArrayList<>(courses);
    //}

    // 컬렉션 수정자는 add, remove를 통해서만 내부 리스트를 변경
    public void addCourse(Course course) {
        courses.add(course);
    }

    // 제거 시, 해당 코스가 없으면 예외를 던짐 (클라이언트가 선택적으로 처리할 수 있도록)
    public void removeCourse(Course course) {
        if (!courses.remove(course)) {
            throw new IllegalArgumentException("Course not found: " + course.getName());
        }
    }
}

```

---

### 2. Course 클래스

- 각 Course 객체는 이름과 고급 여부를 보유합니다.
- equals()와 hashCode()를 재정의하면, 값 동등성에 기반한 비교가 가능하지만, 여기서는 기본 예제로 단순 구현합니다.

```java
import java.util.Objects;

public class Course {
    private String name;
    private boolean isAdvanced;

    public Course(String name, boolean isAdvanced) {
        this.name = name;
        this.isAdvanced = isAdvanced;
    }

    public String getName() {
        return name;
    }

    public boolean isAdvanced() {
        return isAdvanced;
    }

    // 값 비교를 위해 equals와 hashCode를 재정의
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course)) return false;
        Course course = (Course) o;
        return isAdvanced == course.isAdvanced &&
               Objects.equals(name, course.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, isAdvanced);
    }
}

```

---

### 3. 클라이언트 코드 (Main 클래스)

- 클라이언트는 직접 리스트에 접근해 수정하는 대신, addCourse()와 removeCourse()를 사용합니다.
- 또한 getCourses()는 내부 리스트의 복사본을 반환하므로, 클라이언트가 이를 수정해도 원본에 영향을 주지 않습니다.

```java
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Person 객체 생성
        Person person = new Person("Alice");

        // 코스 추가: 클라이언트는 내부 컬렉션에 직접 접근하지 않고 addCourse()를 사용
        person.addCourse(new Course("Math", false));
        person.addCourse(new Course("Physics", true));
        person.addCourse(new Course("Chemistry", false));

        // advanced 코스 수를 계산하는 예시: getCourses()는 복사본을 반환함
        List<Course> courses = person.getCourses();
        long numAdvancedCourses = courses.stream()
                                         .filter(Course::isAdvanced)
                                         .count();
        System.out.println("Number of advanced courses: " + numAdvancedCourses);

        // 코스 제거: removeCourse()를 통해서만 수정하도록 강제
        Course courseToRemove = new Course("Math", false);
        person.removeCourse(courseToRemove);

        System.out.println("Courses after removal:");
        person.getCourses().forEach(c -> System.out.println(c.getName()));
    }
}

```

---

### 예제 설명

- **캡슐화 적용:**
    - `Person` 클래스는 내부 컬렉션(`courses`)을 직접 외부에 노출하지 않고,
        - **수정자 메서드(addCourse, removeCourse):**
          컬렉션의 원소를 변경할 때 반드시 이 메서드를 통해 처리합니다.
        - **게터(getCourses):**
          내부 리스트의 복사본을 반환하여, 클라이언트가 실수로 원본을 수정하는 것을 방지합니다.
- **클라이언트 사용:**
    - 클라이언트는 직접 리스트에 `.add()`나 `.remove()`를 호출하지 않고, 제공된 수정자 메서드를 사용합니다.
    - 이로 인해, 예를 들어 코스 추가나 제거 시 내부 캡슐화 로직(예: 예외 처리)을 적용할 수 있습니다.

이와 같이 컬렉션 캡슐화는 데이터 수정의 경로를 한 곳에 집중시켜, 내부 데이터 구조의 안전성과 일관성을 보장하는 핵심 기법입니다. 마치 보안 금고에 보관된 비밀처럼, 내부 데이터를 안전하게 보호할 수 있습니다.

# 7.3 기본형을 객체로 바꾸기

## 배경

- **초기 단순 표현:**

  초기에는 전화번호, 우선순위 등의 간단한 정보를 문자열이나 숫자와 같은 기본형으로 표현합니다.

- **요구사항 변화:**

  시간이 지나면서 단순 데이터에 대해 포매팅, 지역 코드 추출 등 추가 기능이 필요해집니다. 이로 인해 동일한 로직이 여러 곳에서 중복되어 유지보수 비용이 증가합니다.

- **객체로 감싸기:**

  기본형 데이터를 전용 클래스로 감싸면, 나중에 필요한 특별한 동작을 해당 클래스에 추가할 수 있습니다.

    - 초기에는 단순한 래퍼(wrapper)에 불과하지만, 기능 확장이 용이해집니다.
    - 결과적으로 코드베이스 전반에 걸쳐 중복을 줄이고, 변경에 유연하게 대응할 수 있습니다.
- **효과:**

  초보 개발자에게는 다소 직관에 어긋날 수 있으나, 경험 많은 개발자들은 이 리팩터링 기법의 유용성을 높게 평가합니다.


## 절차

1. **변수 캡슐화 적용:**
    - 이미 캡슐화되지 않았다면, 캡슐화 적용합니다.
2. **전용 값 클래스 생성:**
    - 기존 기본형 데이터를 받아 내부에 저장하고, 해당 값을 반환하는 getter를 제공하는 단순 값 클래스를 만듭니다.
3. **정적 검사 수행:**
    - 코드의 정적 분석을 통해 변경 사항을 확인합니다.
4. **세터 수정:**
    - 세터를 수정해, 전달받은 기본형 데이터를 새 값 클래스의 인스턴스로 생성하여 필드에 저장하도록 변경합니다.
    - 필요하면 필드의 타입도 새 클래스 타입으로 변경합니다.
5. **게터 수정:**
    - 게터를 수정해, 내부 값 클래스의 getter를 호출하여 값을 반환하도록 합니다.
6. **테스트:**
    - 변경 사항이 올바르게 작동하는지 단위 테스트 등을 통해 확인합니다.
7. **함수 이름 변경 고려:**
    - Rename Function  기법을 적용하여 기존 접근자 함수의 이름을 보다 명확하게 수정할 수 있습니다.
8. **객체 역할 명확화 고려:**
    - 필요에 따라 Change Reference to Value  또는 Change Value to Reference  기법을 적용하여 새 객체의 역할(값 객체 혹은 참조 객체)을 명확히 합니다.

## 예제

---

```java
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // 여러 주문(Order) 객체 생성 (각각의 우선순위는 문자열로 전달)
        List<Order> orders = new ArrayList<>();
        orders.add(new Order("high"));
        orders.add(new Order("rush"));
        orders.add(new Order("normal"));
        orders.add(new Order("low"));
        orders.add(new Order("high"));

        // 우선순위가 "normal"보다 높은 주문만 걸러내기
        long highPriorityCount = orders.stream()
                .filter(o -> o.getPriority().higherThan(new Priority("normal")))
                .count();
        System.out.println("High priority count: " + highPriorityCount);
    }
}

class Order {
    // 내부 필드는 Priority 객체로 캡슐화됨
    private Priority priority;

    // 생성자: 입력받은 문자열을 setter를 통해 Priority 객체로 변환
    public Order(String priorityStr) {
        setPriority(priorityStr);
    }

    // Priority 객체를 직접 반환 (필요 시 클라이언트가 추가 기능 사용 가능)
    public Priority getPriority() {
        return this.priority;
    }

    // 우선순위 문자열을 반환하는 별도 접근자 (초기 리팩터링 단계의 명확한 표현)
    public String getPriorityString() {
        return this.priority.toString();
    }

    // 문자열을 받아 새로운 Priority 객체를 생성해 필드에 저장
    public void setPriority(String priorityStr) {
        this.priority = new Priority(priorityStr);
    }
}

class Priority {
    private final String value;
    private final int index;

    // 합법적인 우선순위 값들을 정의
    private static final List<String> LEGAL_VALUES = Arrays.asList("low", "normal", "high", "rush");

    // 기본 생성자: 전달된 문자열이 합법적인 값인지 확인 후 저장
    public Priority(String value) {
        if (!LEGAL_VALUES.contains(value)) {
            throw new IllegalArgumentException("<" + value + "> is invalid for Priority");
        }
        this.value = value;
        this.index = LEGAL_VALUES.indexOf(value);
    }

    // 복사 생성자: 이미 Priority 객체가 주어지면 복사해서 생성
    public Priority(Priority other) {
        this.value = other.value;
        this.index = other.index;
    }

    // 문자열 표현 반환 (주로 변환용으로 사용)
    @Override
    public String toString() {
        return this.value;
    }

    // equals, hashCode 재정의: 두 우선순위의 비교는 index 값 기준으로 처리
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Priority)) return false;
        Priority other = (Priority) obj;
        return this.index == other.index;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(index);
    }

    // 현재 우선순위가 다른 우선순위보다 높은지 비교
    public boolean higherThan(Priority other) {
        return this.index > other.index;
    }

    // 현재 우선순위가 다른 우선순위보다 낮은지 비교
    public boolean lowerThan(Priority other) {
        return this.index < other.index;
    }
}

```

---

### 예제 코드 설명

1. **Priority 클래스:**
    - **목적:**
      기본형인 문자열을 감싸서, 나중에 추가 기능(예, 유효성 검사, 비교 로직)을 넣을 수 있는 값 객체(value object)로 만듭니다.
    - **구현:**
        - 생성자에서 입력 문자열이 `"low"`, `"normal"`, `"high"`, `"rush"` 중 하나인지 확인하여, 유효하지 않은 값은 예외를 발생시킵니다.
        - 내부에서 합법적인 값 목록의 인덱스를 계산하여, `higherThan` 및 `lowerThan` 메서드를 통해 두 우선순위 간의 상대적 우선순위를 비교합니다.
        - `toString()` 메서드를 통해 문자열 표현을 제공합니다.
2. **Order 클래스:**
    - **목적:**
      주문(Order) 객체는 우선순위를 나타내는 필드를 원래 단순 문자열 대신 Priority 객체로 저장합니다.
    - **구현:**
        - 생성자에서 문자열 형태의 우선순위를 받아 `setPriority()`를 호출하여 Priority 객체로 변환합니다.
        - `getPriority()`는 Priority 객체 자체를 반환하여, 클라이언트가 비교 로직 등 추가 기능을 활용할 수 있도록 합니다.
        - `getPriorityString()`은 초기 리팩터링 단계에서 우선순위의 문자열 표현을 명시적으로 제공하기 위해 사용됩니다.
3. **클라이언트 코드 (Main 클래스):**
    - 여러 Order 객체를 생성한 후, Stream API를 사용해 우선순위가 `"normal"`보다 높은 주문의 개수를 계산합니다.
    - 비교는 `Priority.higherThan()` 메서드를 사용해 수행되므로, 우선순위 비교가 의미 있는 값 객체를 통해 처리됩니다.

이와 같이 기본형을 객체로 대체하면, 초기 단순 데이터 표현이 복잡한 요구사항(예, 유효성 검사, 비교 로직 등)으로 발전해도 해당 클래스를 확장하여 유지보수와 기능 확장이 용이해집니다.

# 7.4 임시 변수를 질의 함수로 바꾸기(Replace Temp with Query)

## 배경

- **임시 변수 사용의 장점과 한계:**

  함수 내에서 계산 결과를 재사용하기 위해 임시 변수를 사용하는 것은 중복 계산을 줄이고, 변수 이름을 통해 그 값의 의미를 명확하게 표현할 수 있다는 장점이 있습니다.

- **함수로 전환하는 이유:**
    - 긴 함수에서 일부 코드를 별도 함수로 추출할 때, 임시 변수를 그대로 전달하지 않아도 되므로 함수 간 경계가 명확해집니다.
    - 임시 변수를 함수(질의 함수)로 전환하면, 관련 계산 로직을 한 곳에 모아두어 재사용과 유지보수가 용이해집니다.
    - 단, 임시 변수는 한 번 계산된 후 계속 읽기 전용으로 사용되어야 하며, 여러 번 계산해도 항상 같은 결과를 내야 합니다. (스냅숏용 변수처럼 한 시점의 값을 기억해야 하는 경우에는 적용하면 안 됩니다.)
- **클래스 내부에서의 효과:**

  클래스는 공용 컨텍스트를 제공하기 때문에, 클래스 내부의 임시 변수를 질의 함수로 전환하면 다른 메서드에서도 동일한 계산을 손쉽게 재사용할 수 있습니다. 이는 코드 중복을 줄이고, 의존 관계와 부수 효과를 명확하게 파악하는 데 도움을 줍니다.


## 절차

1.  **변수 사용 전 결정 확인:**

    변수의 값이 사용되기 전에 전적으로 계산되어 결정되었으며, 호출할 때마다 동일한 값을 반환하는지 확인합니다.

2.  **읽기 전용으로 만들기:**

    변수의 값이 변경되지 않는다면, 읽기 전용(read-only)으로 만들 수 있도록 수정합니다.

    → 변경 후 테스트를 수행합니다.

3.  **함수로 추출:**

    임시 변수에 할당되는 계산 코드를 별도의 함수로 추출합니다.

    → 만약 변수와 함수가 동일한 이름을 사용할 수 없다면 임시 이름을 사용합니다.

4.  **부수 효과 제거:**

    추출한 함수가 부수 효과(side effect)가 없어야 합니다.

    → 만약 부수 효과가 있다면, Separate Query from Modifier (306) 기법을 적용합니다.

    → 변경 후 테스트를 수행합니다.

5.  **임시 변수 제거:**

    Inline Variable  기법을 사용하여, 임시 변수 대신 추출한 함수를 직접 호출하도록 수정합니다.


## 예제

---

### 1. 초기 코드 (임시 변수를 사용한 형태)

```java
public class Order {
    private int quantity;
    private Item item;

    public Order(int quantity, Item item) {
        this.quantity = quantity;
        this.item = item;
    }

    // price 계산 시, 임시 변수 basePrice와 discountFactor를 사용함
    public double getPrice() {
        double basePrice = quantity * item.getPrice();
        double discountFactor = 0.98;
        if (basePrice > 1000) {
            discountFactor -= 0.03;
        }
        return basePrice * discountFactor;
    }
}

```

*참고: Item 클래스는 단순히 가격 정보를 제공하는 객체입니다.*

```java
public class Item {
    private double price;

    public Item(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }
}

```

---

### 2. 리팩터링 후 코드 (임시 변수를 질의 함수로 대체)

아래 코드는 임시 변수들을 각각의 질의 함수로 추출한 최종 형태입니다.

```java
public class Order {
    private int quantity;
    private Item item;

    public Order(int quantity, Item item) {
        this.quantity = quantity;
        this.item = item;
    }

    // 최종적으로 getPrice()는 basePrice와 discountFactor 질의 함수의 결과를 곱하여 반환
    public double getPrice() {
        return getBasePrice() * getDiscountFactor();
    }

    // basePrice를 계산하는 질의 함수
    public double getBasePrice() {
        return this.quantity * this.item.getPrice();
    }

    // discountFactor를 계산하는 질의 함수
    public double getDiscountFactor() {
        double discountFactor = 0.98;
        if (getBasePrice() > 1000) {
            discountFactor -= 0.03;
        }
        return discountFactor;
    }
}

```

---

### 예제 코드 설명

1. **초기 구현:**
    - `getPrice()` 메서드 내부에서 `basePrice`와 `discountFactor`라는 임시 변수를 사용하여 가격을 계산합니다.
    - 이 방식은 짧은 함수에서는 괜찮지만, 복잡한 로직에서는 임시 변수의 재사용과 함수 추출이 어려워질 수 있습니다.
2. **리팩터링 단계:**
    - **단계 1:**
        - `basePrice` 계산식을 별도의 질의 함수인 `getBasePrice()`로 추출합니다.
        - 먼저, 임시 변수 `basePrice`를 `final` 변수(또는 상수처럼 한 번 계산되고 읽기 전용)로 바꾸어 테스트하여, 값이 매번 동일하게 계산되는지 확인합니다.
    - **단계 2:**
        - 이어서, 할인율 계산에 사용되던 `discountFactor` 역시 `getDiscountFactor()`라는 별도 메서드로 추출합니다.
    - **단계 3:**
        - 최종적으로 `getPrice()`에서는 추출한 두 질의 함수의 결과를 곱하는 단순한 형태로 변경하여, 임시 변수 없이 코드를 간결하게 만듭니다.
3. **장점:**
    - 계산 로직이 개별 메서드로 분리되어, 나중에 재사용하거나 수정할 때 한 곳만 변경하면 되므로 유지보수가 용이합니다.
    - 코드의 의도가 명확해지고, 함수 경계가 뚜렷해져 부수 효과를 최소화할 수 있습니다.

이처럼 임시 변수를 질의 함수로 전환하면 코드의 가독성이 높아지고, 추출된 함수들을 통해 중복 계산을 줄이며, 로직을 쉽게 테스트할 수 있게 됩니다.

# 7.5 클래스 추출하기

## 배경

- **책임 분리의 원칙:**

  원칙적으로, 클래스는 명확한 추상화와 한정된 책임을 가져야 합니다. 하지만 실제 개발에서는 점점 더 많은 기능과 데이터가 추가되어 클래스가 커지고 복잡해집니다.

- **문제점:**

  시간이 지나면서 한 클래스 내에 여러 책임이 섞이게 되면, 서로 관련 있는 데이터와 메서드들이 함께 변경되거나 의존성이 강해져 유지보수와 이해가 어려워집니다. 예를 들어, `Person` 클래스에 전화번호의 지역번호와 번호를 직접 관리하는 필드가 있다면, 이들은 서로 밀접하게 관련되어 있지만 Person 클래스의 다른 책임과 섞여 코드가 복잡해집니다.

- **클래스 추출의 필요성:**
    - 서로 밀접하게 관련된 데이터와 메서드를 한 곳에 모아두면, 그 일부를 제거했을 때 다른 부분이 무용지물이 되는지를 판단할 수 있습니다.
    - 만약 특정 데이터와 메서드가 자주 함께 변경된다면, 이를 별도의 클래스로 추출하는 것이 좋습니다.
    - 서브타입(subtyping) 등 클래스의 일부 기능만이 달라지는 경우에도, 책임을 분리할 신호로 볼 수 있습니다.
- **예시:**

    ```jsx
    class Person {
     get officeAreaCode() {return this._officeAreaCode;}
     get officeNumber() {return this._officeNumber;}
    }
    ```
    ```jsx
    class Person {
     get officeAreaCode() {return this._telephoneNumber.areaCode;}
     get officeNumber() {return this._telephoneNumber.number;}
    }
    class TelephoneNumber {
     get areaCode() {return this._areaCode;}
     get number() {return this._number;}
    }
    ```

    - 기존 `Person` 클래스가 `officeAreaCode`와 `officeNumber`를 독립적인 필드로 가지고 있다면, 이들을 하나의 `TelephoneNumber` 클래스로 묶어 Person 클래스에서는 전화번호에 대한 책임을 위임할 수 있습니다.
    - 이를 통해 Person 클래스는 본연의 책임에 집중하고, 전화번호 관련 로직은 TelephoneNumber 클래스가 독립적으로 관리하게 되어 전체 코드의 가독성과 유지보수성이 향상됩니다.

## 절차

1.  **책임 분할 결정:**
    - 원래 클래스에서 어떤 책임을 분리할지 결정합니다.
2.  **새로운 하위 클래스 생성:**
    - 분리할 책임을 표현할 새로운 하위 클래스를 만듭니다.
    - 만약 부모 클래스의 책임이 이름과 맞지 않다면, 부모 클래스의 이름을 변경합니다.
3.  **부모와 자식 클래스 연결:**
    - 부모 클래스의 생성자에서 하위 클래스 인스턴스를 생성하고, 부모 클래스에 하위 클래스와의 연결(link)을 추가합니다.
4.  **필드 이동:**
    - 이동할 각 필드에 대해 Move Field (207) 기법을 적용하고, 각 단계마다 테스트합니다.
5.  **메서드 이동:**
    - 하위(내부) 메서드부터 시작해, Move Function (198) 기법을 사용하여 새 하위 클래스로 메서드를 이동시킵니다.
    - 각 이동 후에는 반드시 테스트합니다.
6.  **인터페이스 정리:**
    - 두 클래스의 인터페이스를 검토하여, 불필요한 메서드를 제거하고, 이름을 새 상황에 맞게 수정합니다.
7.  **하위 클래스 노출 여부 결정:**
    - 새 하위 클래스를 외부에 노출할지 결정하고, 필요시 Change Reference to Value (252) 기법을 적용해 값 객체로 만들지 고려합니다.

## 예시

---

```java
// TelephoneNumber.java
public class TelephoneNumber {
    private String areaCode;
    private String number;

    public TelephoneNumber() {
        // 기본 생성자 (필요에 따라 초기값 설정 가능)
    }

    public TelephoneNumber(String areaCode, String number) {
        this.areaCode = areaCode;
        this.number = number;
    }

    // areaCode의 getter/setter
    public String getAreaCode() {
        return areaCode;
    }
    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    // number의 getter/setter
    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }

    // 전화번호의 포맷팅: 기존 'telephoneNumber' 메서드를 toString()으로 변경
    @Override
    public String toString() {
        return "(" + areaCode + ") " + number;
    }
}

```

```java
// Person.java
public class Person {
    private String name;
    // 전화번호 관련 책임을 TelephoneNumber 객체로 위임
    private TelephoneNumber telephoneNumber;

    public Person(String name) {
        this.name = name;
        // Person 생성 시 새로운 TelephoneNumber 객체 생성
        this.telephoneNumber = new TelephoneNumber();
    }

    // name 접근자
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    // 기존의 'officeAreaCode'와 'officeNumber' 접근자들은 이제 telephoneNumber 객체에 위임
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

    // 전화번호 전체를 문자열로 반환 (TelephoneNumber의 toString() 사용)
    public String getTelephoneNumber() {
        return telephoneNumber.toString();
    }

    // 필요에 따라 TelephoneNumber 객체 자체를 외부에 노출할 수도 있음
    public TelephoneNumber getTelephoneNumberObject() {
        return telephoneNumber;
    }
    public void setTelephoneNumber(TelephoneNumber telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }
}

```

---

### 예제 코드 설명

1. **초기 Person 클래스의 문제점**

   원래 Person 클래스는 전화번호 관련 데이터를(예: officeAreaCode, officeNumber) 별도의 필드로 가지고 있었고, 전화번호를 문자열로 조합하는 메서드를 제공했습니다. 그러나 전화번호와 관련된 책임(데이터와 포맷팅 로직)이 Person 클래스에 분산되어 있어, 관심사의 분리가 명확하지 않았습니다.

2. **클래스 추출하기**
    - **TelephoneNumber 클래스 생성:**
      전화번호의 영역(area code)과 번호(number)를 관리하고, 전화번호의 포맷팅을 담당하는 전용 클래스를 새로 만들었습니다.
    - **필드 이동:**
      Person 클래스의 기존 officeAreaCode와 officeNumber 필드를 TelephoneNumber 객체의 필드로 옮기고, Person 클래스에서는 해당 필드에 접근할 때 TelephoneNumber의 getter/setter를 호출하도록 변경했습니다.
    - **메서드 이동 및 이름 변경:**
      원래 Person 클래스에 있던 전화번호를 조합하는 로직은 TelephoneNumber 클래스의 toString() 메서드로 이동시켰습니다.
      또한, "office"라는 접두어가 더 이상 적절하지 않으므로, TelephoneNumber 클래스에서는 필드 이름을 areaCode와 number로 단순화하였습니다.
3. **부모와 자식 클래스의 연결**

   Person 클래스의 생성자에서 새로운 TelephoneNumber 객체를 생성하여, Person 객체가 전화번호에 대한 모든 책임을 TelephoneNumber 객체에 위임하도록 구성했습니다.

4. **최종 결과**

   이제 Person 클래스는 이름과 같은 기본 정보만을 관리하며, 전화번호 관련 로직은 TelephoneNumber 클래스에 캡슐화되어 있습니다. 이로 인해 Person 클래스는 더 간결해지고, 전화번호와 관련된 변경이 발생할 경우 TelephoneNumber 클래스만 수정하면 되므로 유지보수가 용이해집니다.


이와 같이 클래스 추출하기 기법을 적용하면, 서로 관련된 책임을 분리하여 각 클래스가 한 가지 역할에 집중하도록 만들 수 있습니다.

# 7.6 클래스 인라인하기

## 배경

**클래스 인라인하기**는 Extract Class의 반대 리팩터링으로, 클래스가 더 이상 충분한 책임을 지지 않거나 불필요해졌을 때 해당 클래스를 다른 클래스에 통합하는 기법입니다.

- **책임 축소:**

  리팩터링 과정에서 원래 클래스의 일부 책임이 다른 곳으로 이동되어 남은 역할이 거의 없을 경우, 그 클래스는 독립적으로 존재할 이유가 없어집니다. 이럴 때 해당 클래스를 다른 클래스에 합쳐서 코드의 복잡성을 줄일 수 있습니다.

- **불필요한 클래스 제거:**

  클래스가 "무게를 더 이상 견디지 못할" 정도로 기능이 축소되면, 이를 다른 클래스에 녹여내어 관리하는 것이 효과적입니다. 예를 들어, Person 클래스에서 전화번호 관련 기능이 거의 TelephoneNumber 클래스에 위임되었다면, 별도의 TelephoneNumber 클래스가 필요 없을 수 있습니다.

- **클래스 통합 후 재분리:**

  두 개의 클래스를 새로운 책임 분배로 재구성할 필요가 있을 때, 먼저 인라인하여 하나의 클래스로 통합한 후, 다시 Extract Class 기법을 사용해 적절히 분리하는 방법도 유용합니다. 이는 전체 구조를 재조정할 때 한 번에 모든 요소를 옮기기보다 단계적으로 처리할 수 있게 해줍니다.


## 절차

1.  **대상 클래스에 위임 메서드 생성:**

    소스 클래스의 모든 공개 메서드에 대해, 대상 클래스에 동일한 이름의 메서드를 만들고 내부에서 소스 클래스의 메서드를 호출하도록 합니다.

2.  **참조 수정:**

    기존에 소스 클래스의 메서드를 직접 호출하는 모든 코드를 찾아서, 대상 클래스의 위임 메서드를 호출하도록 변경합니다. 각 변경 후 테스트합니다.

3.  **기능 이동:**

    소스 클래스에 있는 모든 함수와 데이터를 하나씩 대상 클래스로 이동시키며, 각 이동 단계마다 테스트하여 올바르게 작동하는지 확인합니다. 소스 클래스가 완전히 비워질 때까지 진행합니다.

4.  **소스 클래스 삭제:**

    소스 클래스의 내용이 모두 대상 클래스에 통합되면, 소스 클래스를 삭제합니다.


## 예시

---

### 1. 초기 상태: 별도의 TrackingInformation 클래스 사용

먼저, 전화 운송 추적 정보를 담는 별도의 클래스를 만듭니다.

```java
// TrackingInformation.java
public class TrackingInformation {
    private String shippingCompany;
    private String trackingNumber;

    public TrackingInformation() {
        // 기본 생성자
    }

    public String getShippingCompany() {
        return shippingCompany;
    }

    public void setShippingCompany(String shippingCompany) {
        this.shippingCompany = shippingCompany;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    // display 메서드: 운송회사와 추적번호를 조합하여 반환
    public String getDisplay() {
        return shippingCompany + ": " + trackingNumber;
    }
}

```

그리고 Shipment 클래스에서는 TrackingInformation 객체를 사용합니다.

```java
// Shipment.java
public class Shipment {
    // TrackingInformation 인스턴스를 보유
    private TrackingInformation trackingInformation;

    public Shipment() {
        // Shipment 생성 시 TrackingInformation 객체를 생성
        this.trackingInformation = new TrackingInformation();
    }

    public TrackingInformation getTrackingInformation() {
        return trackingInformation;
    }

    public void setTrackingInformation(TrackingInformation trackingInformation) {
        this.trackingInformation = trackingInformation;
    }

    // 기존 display 기능을 제공하는 메서드
    public String getTrackingInfo() {
        return trackingInformation.getDisplay();
    }
}

```

클라이언트는 아래와 같이 사용합니다.

```java
// Client code
Shipment shipment = new Shipment();
shipment.getTrackingInformation().setShippingCompany("FedEx");
shipment.getTrackingInformation().setTrackingNumber("123456789");
System.out.println(shipment.getTrackingInfo());  // 출력: FedEx: 123456789

```

---

### 2. 인라인 단계: Shipment에 위임 메서드 추가

TrackingInformation의 기능을 Shipment로 옮기기 위해 먼저 Shipment 클래스에 위임(Delegator) 메서드를 추가합니다.

```java
// Shipment.java (위임 메서드 추가)
public class Shipment {
    private TrackingInformation trackingInformation;

    public Shipment() {
        this.trackingInformation = new TrackingInformation();
    }

    public TrackingInformation getTrackingInformation() {
        return trackingInformation;
    }

    public void setTrackingInformation(TrackingInformation trackingInformation) {
        this.trackingInformation = trackingInformation;
    }

    // 위임 메서드: shippingCompany에 대한 접근을 TrackingInformation으로 위임
    public String getShippingCompany() {
        return trackingInformation.getShippingCompany();
    }

    public void setShippingCompany(String shippingCompany) {
        trackingInformation.setShippingCompany(shippingCompany);
    }

    // 위임 메서드: trackingNumber에 대한 접근을 TrackingInformation으로 위임
    public String getTrackingNumber() {
        return trackingInformation.getTrackingNumber();
    }

    public void setTrackingNumber(String trackingNumber) {
        trackingInformation.setTrackingNumber(trackingNumber);
    }

    // 기존의 getTrackingInfo()도 위임 메서드를 사용하도록 변경
    public String getTrackingInfo() {
        return getShippingCompany() + ": " + getTrackingNumber();
    }
}

```

이 시점에서 클라이언트는 여전히 아래와 같이 사용할 수 있습니다.

```java
// Client code (변경 전)
Shipment shipment = new Shipment();
shipment.setShippingCompany("FedEx");
shipment.setTrackingNumber("123456789");
System.out.println(shipment.getTrackingInfo());  // 출력: FedEx: 123456789

```

---

### 3. TrackingInformation 클래스의 기능을 Shipment로 이동

위임 메서드를 통해 Shipment가 TrackingInformation의 기능을 대신하도록 한 후, 이제 TrackingInformation 클래스의 필드와 메서드를 Shipment로 직접 이동합니다.

```java
// Shipment.java (TrackingInformation 인라인 후 최종 상태)
public class Shipment {
    private String shippingCompany;
    private String trackingNumber;

    public Shipment() {
        // 필요한 초기화 수행 (예: 기본값 설정)
    }

    public String getShippingCompany() {
        return shippingCompany;
    }

    public void setShippingCompany(String shippingCompany) {
        this.shippingCompany = shippingCompany;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    // 인라인된 display 기능
    public String getTrackingInfo() {
        return shippingCompany + ": " + trackingNumber;
    }
}

```

이제 Shipment 클래스는 더 이상 TrackingInformation 클래스를 참조하지 않으며, 그 역할을 완전히 인라인(inline)했습니다.

마지막으로, TrackingInformation 클래스는 더 이상 필요 없으므로 삭제합니다.

---

### 4. 요약 및 설명

1. **초기 상태:**

   Shipment 클래스는 TrackingInformation이라는 별도 클래스를 사용하여 운송회사와 추적번호를 관리하고, display 기능을 통해 정보를 제공했습니다.

2. **위임 메서드 추가:**

   Shipment 클래스에 TrackingInformation의 public 메서드를 위임하는 메서드를 추가하여, 클라이언트가 직접 TrackingInformation에 접근하지 않고 Shipment의 인터페이스만 사용하도록 변경했습니다.

3. **필드 및 메서드 이동:**

   위임 메서드를 활용한 후, TrackingInformation 클래스의 필드와 메서드를 Shipment로 옮겨서 기능을 완전히 통합했습니다.

4. **클래스 삭제:**

   이제 인라인이 완료되어 TrackingInformation 클래스는 삭제됩니다.


이와 같이 클래스 인라인하기를 통해 불필요한 중간 클래스를 제거하고, 관련 기능을 한 클래스에 집중시킴으로써 코드의 단순성과 유지보수성을 높일 수 있습니다.

# 7.7 위임 숨기기

## 배경

```jsx
manager = aPerson.department.manager;
```
```jsx
manager = aPerson.manager;
class Person {
 get manager() {return this.department.manager;}
```

- **캡슐화의 핵심:**

  좋은 모듈 설계의 핵심은 캡슐화로, 이는 모듈들이 다른 부분에 대해 알아야 하는 정보를 줄여 변경 시 영향을 최소화합니다.

- **초기 캡슐화 개념:**

  객체 지향 프로그래밍 초기에는 필드를 숨기는 것이 캡슐화의 전부로 여겨졌습니다.

- **더 넓은 캡슐화:**

  발전한 캡슐화는 객체 내부의 대리(delegate) 객체에 대한 세부 사항도 감추는 것입니다.

    - 클라이언트 코드가 서버 객체의 필드에 있는 대리 객체의 메서드를 직접 호출하면, 클라이언트는 그 대리 객체의 인터페이스에 의존하게 됩니다.
    - 만약 대리 객체의 인터페이스가 변경되면, 이 대리 객체를 사용하는 모든 클라이언트에 변경사항이 전파되어야 합니다.
- **위임 숨기기의 목적:**

  서버 객체에 단순한 위임 메서드를 추가하여 대리 객체의 세부 사항을 감춤으로써,

    - 클라이언트는 서버 객체의 인터페이스만 알면 되고, 대리 객체의 변경에 직접적으로 의존하지 않게 됩니다.
    - 결과적으로, 변경이 발생해도 수정해야 할 부분이 서버 객체 내부로 국한되어 유지보수가 용이해집니다.

## 절차

- **1. 위임 메서드 생성:**
    - 대리(delegate) 객체의 각 메서드에 대해, 서버 객체에 동일한 기능을 수행하는 위임 메서드를 생성합니다.
- **2. 클라이언트 수정:**
    - 클라이언트가 직접 대리 객체의 메서드를 호출하는 대신, 서버 객체의 위임 메서드를 호출하도록 수정합니다.
    - 각 변경 후에는 반드시 테스트를 진행합니다.
- **3. 대리 객체 접근자 제거:**
    - 이제 모든 클라이언트가 서버의 위임 메서드를 사용하게 되었으면, 서버 객체에 있던 대리 객체에 대한 접근(accessor)을 제거합니다.
    - 최종적으로 전체 시스템이 올바르게 동작하는지 테스트합니다.

## 예시

---

### 초기 상태

Person 클래스는 Department 객체에 대한 접근자를 그대로 제공하며, 클라이언트는 이를 통해 담당 매니저(manager)를 조회합니다.

```java
// Department.java
public class Department {
    private String chargeCode;
    private String manager;

    public Department(String chargeCode, String manager) {
        this.chargeCode = chargeCode;
        this.manager = manager;
    }

    public String getChargeCode() {
        return chargeCode;
    }
    public void setChargeCode(String chargeCode) {
        this.chargeCode = chargeCode;
    }
    public String getManager() {
        return manager;
    }
    public void setManager(String manager) {
        this.manager = manager;
    }
}

```

```java
// Person.java (초기 버전)
public class Person {
    private String name;
    private Department department;  // 부서 정보를 그대로 노출

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

    public void setDepartment(Department department) {
        this.department = department;
    }
}

```

**클라이언트 코드 예:**

```java
// 클라이언트 코드: 부서 객체를 통해 매니저를 조회
Person aPerson = new Person("Alice", new Department("D001", "Bob"));
String manager = aPerson.getDepartment().getManager();
System.out.println("Manager: " + manager);  // 출력: Manager: Bob

```

이 경우 클라이언트는 Person의 내부 구성(Department 클래스)을 직접 알고 있어야 하며, Department의 인터페이스 변경이 클라이언트에 영향을 줍니다.

---

### 위임 숨기기 적용 후

Person 클래스에 단순 위임 메서드를 추가하여, 클라이언트가 Department에 직접 접근하지 않도록 만듭니다. 이후 필요시 getDepartment() 접근자를 제거할 수 있습니다.

```java
// Person.java (위임 숨기기 적용)
public class Person {
    private String name;
    private Department department;

    public Person(String name, Department department) {
        this.name = name;
        this.department = department;
    }

    public String getName() {
        return name;
    }

    // 위임 메서드: 부서의 매니저를 Person 객체가 대신 노출
    public String getManager() {
        return department.getManager();
    }

    // 기존의 getDepartment(), setDepartment()는 나중에 모두 제거할 수 있음.
    // 만약 다른 곳에서 부서 전체가 필요 없다면 제거하여, 클라이언트가 직접 부서에 접근하지 못하게 합니다.

    // public Department getDepartment() {
    //     return department;
    // }

    // public void setDepartment(Department department) {
    //     this.department = department;
    // }
}

```

**클라이언트 코드 수정 예:**

```java
// 클라이언트 코드: 이제 Person 객체의 위임 메서드를 사용하여 매니저를 조회
Person aPerson = new Person("Alice", new Department("D001", "Bob"));
String manager = aPerson.getManager();
System.out.println("Manager: " + manager);  // 출력: Manager: Bob

```

---

### 설명

1. **초기 상태:**
    - Person 클래스는 부서(Department) 객체에 대한 getter/setter를 그대로 제공하여, 클라이언트가 `aPerson.getDepartment().getManager()` 와 같이 호출하게 됩니다.
    - 이 경우 클라이언트는 Department 클래스의 세부 사항에 의존하게 됩니다.
2. **위임 숨기기 적용:**
    - Person 클래스에 `getManager()`라는 위임 메서드를 추가합니다. 이 메서드는 내부의 Department 객체로부터 매니저를 가져옵니다.
    - 클라이언트는 이제 `aPerson.getManager()` 만 호출하면 되므로, Department 클래스의 존재를 몰라도 매니저 정보를 얻을 수 있습니다.
    - 이렇게 하면 Department 클래스의 인터페이스가 변경되더라도 Person 클래스의 위임 메서드만 수정하면 되어, 클라이언트에 미치는 영향을 줄일 수 있습니다.
3. **후속 정리:**
    - 모든 클라이언트가 위임 메서드를 사용하도록 변경되면, Person 클래스의 getDepartment()와 같은 접근자들을 제거할 수 있습니다.
    - 이를 통해 내부 구현 세부사항이 완전히 캡슐화되어, 모듈 간 결합도를 낮출 수 있습니다.

이와 같이 위임 숨기기를 적용하면, 클라이언트는 더 단순한 인터페이스를 사용하게 되고 내부 구현 변경에 대한 의존성이 줄어들어 시스템의 유지보수성이 향상됩니다.

# 7.8 중개자 제거하기

## 배경

- **캡슐화의 양면성:**

  초기에는 대리(delegate) 객체의 사용을 감추어 클라이언트가 내부 구현에 의존하지 않도록 하는 것이 장점이었습니다. 하지만 클라이언트가 대리 객체의 새로운 기능을 사용하려면 서버 클래스에 계속 위임 메서드를 추가해야 합니다.

- **중개자의 부담:**

  서버 클래스가 단순히 모든 호출을 대리 객체로 전달하는 중개자 역할만 하게 되면, 불필요하게 코드가 복잡해지고 유지보수가 어려워집니다. 이 경우, 서버 클래스는 더 이상 의미 있는 책임을 지지 않고 단순한 중간 역할만 수행하게 됩니다.

- **적절한 은닉 수준의 조정:**

  시스템이 변화함에 따라 캡슐화 정도도 달라져야 합니다. 과거에 좋은 캡슐화 방식이 현재에는 오히려 불편하게 느껴질 수 있습니다. 리팩터링을 통해 필요 없는 중개자 역할을 제거하면, 클라이언트가 대리 객체의 기능을 직접 사용할 수 있게 되어 코드가 단순해지고, 변경의 영향 범위도 줄어듭니다.

- **리팩터링의 유연성:**

  리팩터링은 "사과할 필요 없이 고친다"는 철학 아래, 코드 구조를 상황에 맞게 조정할 수 있게 해줍니다. 중개자 제거하기는 이러한 유연성을 보여주는 대표적인 예입니다.


## 절차

1. 위임 객체를 얻는 게터를 만든다.
2. 위임 메서드를 호출하는 클라이언트가 모두 이 게터를 거치도록 수정한다. 하나씩 바꿀 때마다 테스트한다.
3. 모두 수정했다면 위임 메서드를 삭제한다.
    - 자동 리팩터링 도구를 사용할 때는 위임 필드를 캡슐화 한 다음 이를 사용하는 모든 메서드를 인라인 한다.

## 예시

---

### 초기 상태

**Department 클래스:**

Department는 매니저(manager)를 관리하는 간단한 클래스입니다.

```java
// Department.java
public class Department {
    private String manager;

    public Department(String manager) {
        this.manager = manager;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }
}

```

**Person 클래스 (초기 버전):**

Person 클래스는 내부에 Department 객체를 보유하며, getManager() 메서드를 통해 부서의 매니저를 반환합니다.

이때 클라이언트는 Person 객체의 getManager()를 호출하여 간접적으로 Department의 매니저에 접근합니다.

```java
// Person.java (초기 상태)
public class Person {
    private String name;
    private Department department;

    public Person(String name, Department department) {
        this.name = name;
        this.department = department;
    }

    public String getName() {
        return name;
    }

    // 위임 메서드: Person이 단순히 내부 department의 getManager()를 호출함
    public String getManager() {
        return department.getManager();
    }

    public void setManager(String manager) {
        department.setManager(manager);
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}

```

**클라이언트 코드 (초기 사용 예):**

```java
public class Main {
    public static void main(String[] args) {
        Person aPerson = new Person("Alice", new Department("Bob"));
        // 클라이언트는 Person의 getManager()를 통해 매니저 정보를 조회
        String manager = aPerson.getManager();
        System.out.println("Manager: " + manager);  // 출력: Manager: Bob
    }
}

```

---

### 중개자 제거 후

리팩터링 목표는 Person 클래스에 있는 getManager() 위임 메서드를 제거하고,

클라이언트가 직접 Department 객체를 통해 매니저 정보를 얻도록 하는 것입니다.

**Person 클래스 (중개자 제거 후):**

```java
// Person.java (중개자 제거 후)
public class Person {
    private String name;
    private Department department;

    public Person(String name, Department department) {
        this.name = name;
        this.department = department;
    }

    public String getName() {
        return name;
    }

    // getManager() 메서드를 제거하여 클라이언트가 부서에 직접 접근하도록 유도
    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}

```

**클라이언트 코드 (변경 후):**

```java
public class Main {
    public static void main(String[] args) {
        Person aPerson = new Person("Alice", new Department("Bob"));
        // 클라이언트는 이제 aPerson.getDepartment().getManager()를 사용함
        String manager = aPerson.getDepartment().getManager();
        System.out.println("Manager: " + manager);  // 출력: Manager: Bob
    }
}

```

---

### 예제 코드 설명

1. **초기 상태:**
    - `Person` 클래스는 내부에 `Department` 객체를 보유하며, `getManager()` 메서드가 단순히 `department.getManager()`를 호출합니다.
    - 클라이언트는 `aPerson.getManager()`를 사용해 매니저 정보를 얻으므로, Person이 대리 역할(중개자)만 수행합니다.
2. **중개자 제거:**
    - 리팩터링 후, `Person` 클래스에서 `getManager()`와 `setManager()`와 같은 위임 메서드를 제거하고 대신 `getDepartment()`를 제공하여,
    - 클라이언트가 직접 `Department` 객체에 접근하게 합니다.
    - 이를 통해 Person 클래스가 중개자 역할을 하지 않게 되어, 대리 객체(Department)의 인터페이스 변경 시 Person 클래스의 수정 부담을 줄일 수 있습니다.
3. **클라이언트 수정:**
    - 모든 클라이언트 코드를 `aPerson.getManager()` 대신 `aPerson.getDepartment().getManager()`를 호출하도록 변경합니다.
    - 이렇게 하면 Person 클래스에 의존하지 않고, 직접적으로 Department의 기능을 사용하게 되어 코드 구조가 간단해집니다.

이와 같이 중개자 제거하기를 통해 불필요한 위임 메서드를 제거하면, 클래스 간 의존성이 줄어들어 유지보수가 용이해지고 코드가 더 간결해집니다.

# 7.9 알고리즘 교체하기

```jsx
function foundPerson(people) {
 for(let i = 0; i < people.length; i++) {
 if (people[i] === "Don") {
 return "Don";
 }
 if (people[i] === "John") {
 return "John";
 }
 if (people[i] === "Kent") {
 return "Kent";
 }
 }
 return "";
}
```
```jsx
function foundPerson(people) {
 const candidates = ["Don", "John", "Kent"];
 return people.find(p => candidates.includes(p)) || '';
}
```

## 배경

- **여러 구현 방식:**

  알고리즘은 마치 고양이를 해부하는 방법처럼, 여러 방식으로 구현할 수 있습니다. 어떤 방식은 더 간단하고 명확할 수 있습니다.

- **복잡성 감소:**

  복잡한 알고리즘을 단순하고 명료한 방식으로 대체하면, 코드가 이해하기 쉬워지고 유지보수가 용이해집니다.

- **문제에 대한 이해 향상:**

  문제를 더 깊이 이해하면서 더 쉬운 해결책을 발견하게 되는 경우, 기존의 복잡한 알고리즘을 교체할 필요가 생깁니다.

  또는, 외부 라이브러리의 기능을 사용하여 중복 코드를 제거할 수 있습니다.

- **변경 용이성:**

  알고리즘의 동작을 약간 변경하고자 할 때, 복잡한 구조를 단순한 형태로 바꾸면 이후의 변경이 더 수월해집니다.

- **사전 분해의 중요성:**

  전체 알고리즘을 교체하기 전에 메서드를 최대한 분해해 복잡도를 줄여야, 새로운 알고리즘으로의 전환이 가능해집니다.


## 절차

1.  **전체 함수로 정리:**

    교체할 알고리즘 코드를 하나의 완전한 함수로 정리합니다.

2.  **테스트 준비:**

    해당 함수의 동작을 캡처할 수 있도록, 이 함수만을 대상으로 하는 테스트를 준비합니다.

3.  **대체 알고리즘 준비:**

    기존 알고리즘을 대체할 새로운 알고리즘을 구현합니다.

4.  **정적 검사:**

    코드 변경 후 정적 분석 도구를 사용하여 오류가 없는지 확인합니다.

5.  **테스트 비교:**

    기존 알고리즘과 새 알고리즘의 결과를 비교하는 테스트를 실행합니다.

    - 결과가 동일하면 교체 완료입니다.
    - 결과가 다르면, 테스트 및 디버깅 시 기존 알고리즘의 결과를 비교 기준으로 사용합니다.