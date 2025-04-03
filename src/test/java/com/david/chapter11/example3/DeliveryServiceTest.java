package com.david.chapter11.example3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DeliveryServiceTest {
    
    private DeliveryService deliveryService;
    private LocalDate orderDate;
    
    @BeforeEach
    void setUp() {
        deliveryService = new DeliveryService();
        orderDate = LocalDate.of(2023, 5, 15);
    }
    
    @Test
    void deliveryDate_withRushForMA_returns2DaysFromOrder() {
        Order order = new Order("MA", orderDate);
        LocalDate expected = orderDate.plusDays(2); // 1 (기본) + 1 (MA 긴급배송)
        assertEquals(expected, deliveryService.deliveryDate(order, true));
    }
    
    @Test
    void deliveryDate_withRushForNY_returns3DaysFromOrder() {
        Order order = new Order("NY", orderDate);
        LocalDate expected = orderDate.plusDays(3); // 1 (기본) + 2 (NY 긴급배송)
        assertEquals(expected, deliveryService.deliveryDate(order, true));
    }
    
    @Test
    void deliveryDate_withoutRushForMA_returns4DaysFromOrder() {
        Order order = new Order("MA", orderDate);
        LocalDate expected = orderDate.plusDays(4); // 2 (기본) + 2 (MA 일반배송)
        assertEquals(expected, deliveryService.deliveryDate(order, false));
    }
    
    @Test
    void rushDeliveryDate_forMA_returns2DaysFromOrder() {
        Order order = new Order("MA", orderDate);
        LocalDate expected = orderDate.plusDays(2); // 1 (기본) + 1 (MA 긴급배송)
        assertEquals(expected, deliveryService.rushDeliveryDate(order));
    }
    
    @Test
    void rushDeliveryDate_forNY_returns3DaysFromOrder() {
        Order order = new Order("NY", orderDate);
        LocalDate expected = orderDate.plusDays(3); // 1 (기본) + 2 (NY 긴급배송)
        assertEquals(expected, deliveryService.rushDeliveryDate(order));
    }
    
    @Test
    void regularDeliveryDate_forMA_returns4DaysFromOrder() {
        Order order = new Order("MA", orderDate);
        LocalDate expected = orderDate.plusDays(4); // 2 (기본) + 2 (MA 일반배송)
        assertEquals(expected, deliveryService.regularDeliveryDate(order));
    }
    
    @Test
    void regularDeliveryDate_forME_returns5DaysFromOrder() {
        Order order = new Order("ME", orderDate);
        LocalDate expected = orderDate.plusDays(5); // 2 (기본) + 3 (ME 일반배송)
        assertEquals(expected, deliveryService.regularDeliveryDate(order));
    }
} 