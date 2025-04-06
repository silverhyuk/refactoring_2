package com.david.chapter12.example10;

import java.time.LocalDate;

// 리팩토링 전: 상속을 사용한 버전
class ShowBefore {
    private String title;
    private String date;
    private boolean hasTalkback;
    private int price;

    public ShowBefore(String title, String date, boolean hasTalkback, int price) {
        this.title = title;
        this.date = date;
        this.hasTalkback = hasTalkback;
        this.price = price;
    }

    public boolean hasTalkback() {
        return hasTalkback;
    }

    public int getPrice() {
        return price;
    }
}

class BookingBefore {
    protected ShowBefore show;
    protected LocalDate date;

    public BookingBefore(ShowBefore show, LocalDate date) {
        this.show = show;
        this.date = date;
    }

    public boolean hasTalkback() {
        return show.hasTalkback() && !isPeakDay();
    }

    public int basePrice() {
        int price = show.getPrice();
        if (isPeakDay())
            price += Math.round(price * 0.15f);
        return price;
    }

    public boolean isPeakDay() {
        return date.getDayOfWeek().getValue() >= 5; // 주말(금,토,일)은 성수기
    }
}

class ExtrasForPremiumBookingBefore {
    private boolean hasDinner;
    private int premiumFee;

    public ExtrasForPremiumBookingBefore(boolean hasDinner, int premiumFee) {
        this.hasDinner = hasDinner;
        this.premiumFee = premiumFee;
    }

    public boolean hasDinner() {
        return hasDinner;
    }

    public int getPremiumFee() {
        return premiumFee;
    }
}

class PremiumBookingBefore extends BookingBefore {
    private ExtrasForPremiumBookingBefore extras;

    public PremiumBookingBefore(ShowBefore show, LocalDate date, ExtrasForPremiumBookingBefore extras) {
        super(show, date);
        this.extras = extras;
    }

    @Override
    public boolean hasTalkback() {
        return show.hasTalkback(); // 프리미엄은 성수기에도 토크백 제공
    }

    @Override
    public int basePrice() {
        return super.basePrice() + extras.getPremiumFee();
    }

    public boolean hasDinner() {
        return extras.hasDinner() && !isPeakDay();
    }
}

// 리팩토링 후: 위임을 사용한 버전
class ShowAfter {
    private String title;
    private String date;
    private boolean hasTalkback;
    private int price;

    public ShowAfter(String title, String date, boolean hasTalkback, int price) {
        this.title = title;
        this.date = date;
        this.hasTalkback = hasTalkback;
        this.price = price;
    }

    public boolean hasTalkback() {
        return hasTalkback;
    }

    public int getPrice() {
        return price;
    }
}

class ExtrasForPremiumBookingAfter {
    private boolean hasDinner;
    private int premiumFee;

    public ExtrasForPremiumBookingAfter(boolean hasDinner, int premiumFee) {
        this.hasDinner = hasDinner;
        this.premiumFee = premiumFee;
    }

    public boolean hasDinner() {
        return hasDinner;
    }

    public int getPremiumFee() {
        return premiumFee;
    }
}

class PremiumBookingDelegate {
    private BookingAfter host;
    private ExtrasForPremiumBookingAfter extras;

    public PremiumBookingDelegate(BookingAfter host, ExtrasForPremiumBookingAfter extras) {
        this.host = host;
        this.extras = extras;
    }

    public boolean hasTalkback() {
        return host.getShow().hasTalkback(); // 프리미엄은 성수기에도 토크백 제공
    }

    public int extendBasePrice(int basePrice) {
        return basePrice + extras.getPremiumFee();
    }

    public boolean hasDinner() {
        return extras.hasDinner() && !host.isPeakDay();
    }
}

class BookingAfter {
    private ShowAfter show;
    private LocalDate date;
    private PremiumBookingDelegate premiumDelegate;

    public BookingAfter(ShowAfter show, LocalDate date) {
        this.show = show;
        this.date = date;
    }

    public void becomePremium(ExtrasForPremiumBookingAfter extras) {
        this.premiumDelegate = new PremiumBookingDelegate(this, extras);
    }

    public boolean hasTalkback() {
        return (premiumDelegate != null) ? premiumDelegate.hasTalkback() : show.hasTalkback() && !isPeakDay();
    }

    public int basePrice() {
        int result = show.getPrice();
        if (isPeakDay())
            result += Math.round(result * 0.15f);
        return (premiumDelegate != null) ? premiumDelegate.extendBasePrice(result) : result;
    }

    public boolean isPeakDay() {
        return date.getDayOfWeek().getValue() >= 5; // 주말(금,토,일)은 성수기
    }

    public boolean hasDinner() {
        return (premiumDelegate != null) ? premiumDelegate.hasDinner() : false;
    }

    public ShowAfter getShow() {
        return show;
    }
}

class BookingFactoryAfter {
    public static BookingAfter createBooking(ShowAfter show, LocalDate date) {
        return new BookingAfter(show, date);
    }

    public static BookingAfter createPremiumBooking(ShowAfter show, LocalDate date,
            ExtrasForPremiumBookingAfter extras) {
        BookingAfter booking = new BookingAfter(show, date);
        booking.becomePremium(extras);
        return booking;
    }
}

public class ReplaceSubclassWithDelegate {
    // 이 클래스는 리팩토링 예제를 담는 컨테이너 역할만 합니다
}