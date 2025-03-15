package com.david.chapter09;

import java.util.Objects;

/**
 * 9.4 Change Reference to Value (참조를 값으로 변경) 예제
 * 
 * 문제: 참조 객체는 변경 가능성이 있어 예기치 않은 부작용을 발생시킬 수 있습니다.
 * 해결: 불변 값 객체로 변경하여 안정성을 높이고 비교를 단순화합니다.
 */
public class ChangeReferenceToValue {

    public static void main(String[] args) {
        // 리팩토링 전 사용 예제
        PersonBefore person1 = new PersonBefore();
        person1.setOfficeAreaCode("02");
        person1.setOfficeNumber("1234-5678");
        System.out.println("Before: " + person1.getOfficeAreaCode() + "-" + person1.getOfficeNumber());
        
        // 리팩토링 후 사용 예제
        PersonAfter person2 = new PersonAfter("02", "1234-5678");
        System.out.println("After: " + person2.getOfficeAreaCode() + "-" + person2.getOfficeNumber());
        
        // 값 객체 비교 예제
        TelephoneNumberValue phone1 = new TelephoneNumberValue("02", "1234-5678");
        TelephoneNumberValue phone2 = new TelephoneNumberValue("02", "1234-5678");
        System.out.println("Same values equal? " + phone1.equals(phone2)); // true
    }
}

/**
 * 리팩토링 전: 참조로 사용되는 TelephoneNumber
 */
class TelephoneNumberReference {
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

class PersonBefore {
    private TelephoneNumberReference telephoneNumber = new TelephoneNumberReference();

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

/**
 * 리팩토링 후: 값 객체로 변경된 TelephoneNumber
 */
class TelephoneNumberValue {
    private final String areaCode;
    private final String number;

    public TelephoneNumberValue(String areaCode, String number) {
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
        TelephoneNumberValue that = (TelephoneNumberValue) obj;
        return Objects.equals(areaCode, that.areaCode) && 
               Objects.equals(number, that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(areaCode, number);
    }
}

class PersonAfter {
    private TelephoneNumberValue telephoneNumber;

    public PersonAfter(String areaCode, String number) {
        this.telephoneNumber = new TelephoneNumberValue(areaCode, number);
    }

    public String getOfficeAreaCode() {
        return telephoneNumber.getAreaCode();
    }

    public void setOfficeAreaCode(String areaCode) {
        // 값 객체는 불변이므로 새 객체를 생성하여 교체
        this.telephoneNumber = new TelephoneNumberValue(areaCode, telephoneNumber.getNumber());
    }

    public String getOfficeNumber() {
        return telephoneNumber.getNumber();
    }

    public void setOfficeNumber(String number) {
        // 값 객체는 불변이므로 새 객체를 생성하여 교체
        this.telephoneNumber = new TelephoneNumberValue(telephoneNumber.getAreaCode(), number);
    }
}