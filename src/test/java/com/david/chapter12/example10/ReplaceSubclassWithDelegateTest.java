package com.david.chapter12.example10;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class ReplaceSubclassWithDelegateTest {

    @Test
    public void testBookingBeforeRefactoring() {
        // 리팩토링 전 테스트
        ShowBefore show = new ShowBefore("해밀턴", "2023-05-15", true, 10000);

        // 평일(화요일) - 성수기가 아님
        LocalDate weekday = LocalDate.of(2023, 5, 16);
        BookingBefore regularBooking = new BookingBefore(show, weekday);

        // 주말(토요일) - 성수기임
        LocalDate weekend = LocalDate.of(2023, 5, 20);
        BookingBefore weekendBooking = new BookingBefore(show, weekend);

        // 토크백 테스트
        assertTrue(regularBooking.hasTalkback()); // 평일이고 공연에 토크백이 있어서 가능
        assertFalse(weekendBooking.hasTalkback()); // 주말(성수기)에는 토크백 없음

        // 가격 테스트
        assertEquals(10000, regularBooking.basePrice()); // 평일 가격
        assertEquals(11500, weekendBooking.basePrice()); // 주말 프리미엄 15% 추가
    }

    @Test
    public void testPremiumBookingBeforeRefactoring() {
        // 리팩토링 전 테스트
        ShowBefore show = new ShowBefore("해밀턴", "2023-05-15", true, 10000);
        ExtrasForPremiumBookingBefore extras = new ExtrasForPremiumBookingBefore(true, 5000);

        // 평일(화요일) - 성수기가 아님
        LocalDate weekday = LocalDate.of(2023, 5, 16);
        PremiumBookingBefore premiumWeekday = new PremiumBookingBefore(show, weekday, extras);

        // 주말(토요일) - 성수기임
        LocalDate weekend = LocalDate.of(2023, 5, 20);
        PremiumBookingBefore premiumWeekend = new PremiumBookingBefore(show, weekend, extras);

        // 토크백 테스트 - 프리미엄은 언제나 토크백 가능
        assertTrue(premiumWeekday.hasTalkback());
        assertTrue(premiumWeekend.hasTalkback()); // 주말에도 가능

        // 가격 테스트 - 프리미엄 요금 추가
        assertEquals(15000, premiumWeekday.basePrice()); // 평일 가격 + 프리미엄 5000
        assertEquals(16500, premiumWeekend.basePrice()); // 주말 가격(11500) + 프리미엄 5000

        // 저녁 식사 테스트
        assertTrue(premiumWeekday.hasDinner()); // 평일에는 저녁 식사 제공
        assertFalse(premiumWeekend.hasDinner()); // 주말(성수기)에는 저녁 식사 제공 안 함
    }

    @Test
    public void testBookingAfterRefactoring() {
        // 리팩토링 후 테스트
        ShowAfter show = new ShowAfter("해밀턴", "2023-05-15", true, 10000);

        // 팩토리 메서드를 사용한 일반 예약 생성
        LocalDate weekday = LocalDate.of(2023, 5, 16); // 화요일
        BookingAfter regularWeekday = BookingFactoryAfter.createBooking(show, weekday);

        LocalDate weekend = LocalDate.of(2023, 5, 20); // 토요일
        BookingAfter regularWeekend = BookingFactoryAfter.createBooking(show, weekend);

        // 일반 예약 테스트
        assertTrue(regularWeekday.hasTalkback());
        assertFalse(regularWeekend.hasTalkback());
        assertEquals(10000, regularWeekday.basePrice());
        assertEquals(11500, regularWeekend.basePrice());
        assertFalse(regularWeekday.hasDinner()); // 일반 예약은 저녁 식사 제공 안 함
    }

    @Test
    public void testPremiumBookingAfterRefactoring() {
        // 리팩토링 후 테스트
        ShowAfter show = new ShowAfter("해밀턴", "2023-05-15", true, 10000);
        ExtrasForPremiumBookingAfter extras = new ExtrasForPremiumBookingAfter(true, 5000);

        // 팩토리 메서드를 사용한 프리미엄 예약 생성
        LocalDate weekday = LocalDate.of(2023, 5, 16); // 화요일
        BookingAfter premiumWeekday = BookingFactoryAfter.createPremiumBooking(show, weekday, extras);

        LocalDate weekend = LocalDate.of(2023, 5, 20); // 토요일
        BookingAfter premiumWeekend = BookingFactoryAfter.createPremiumBooking(show, weekend, extras);

        // 프리미엄 예약 테스트
        assertTrue(premiumWeekday.hasTalkback());
        assertTrue(premiumWeekend.hasTalkback()); // 주말에도 가능
        assertEquals(15000, premiumWeekday.basePrice());
        assertEquals(16500, premiumWeekend.basePrice());
        assertTrue(premiumWeekday.hasDinner());
        assertFalse(premiumWeekend.hasDinner());
    }

    @Test
    public void testDynamicTypeChangeAfterRefactoring() {
        // 리팩토링 후에는 동적으로 타입 변경 가능
        ShowAfter show = new ShowAfter("해밀턴", "2023-05-15", true, 10000);
        ExtrasForPremiumBookingAfter extras = new ExtrasForPremiumBookingAfter(true, 5000);

        // 일반 예약으로 시작
        LocalDate weekday = LocalDate.of(2023, 5, 16);
        BookingAfter booking = BookingFactoryAfter.createBooking(show, weekday);

        // 처음에는 일반 예약
        assertFalse(booking.hasDinner());
        assertEquals(10000, booking.basePrice());

        // 런타임에 프리미엄으로 업그레이드
        booking.becomePremium(extras);

        // 이제 프리미엄 동작 수행
        assertTrue(booking.hasDinner());
        assertEquals(15000, booking.basePrice());
    }
}