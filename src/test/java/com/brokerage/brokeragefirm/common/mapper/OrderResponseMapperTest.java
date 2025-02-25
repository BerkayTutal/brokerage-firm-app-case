package com.brokerage.brokeragefirm.common.mapper;

import com.brokerage.brokeragefirm.common.enums.Side;
import com.brokerage.brokeragefirm.common.enums.Status;
import com.brokerage.brokeragefirm.rest.dto.OrderResponse;
import com.brokerage.brokeragefirm.service.model.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class OrderResponseMapperTest {

    @Test
    void toResponse_ValidOrder_ReturnsCorrectOrderResponse() {
        // Given: a valid Order object
        Order order = Order.builder()
                .id(1L)
                .customerId(101L)
                .assetName("Asset A")
                .orderSide(Side.BUY)
                .size(new BigDecimal("10.00"))
                .price(new BigDecimal("100.50"))
                .status(Status.PENDING)
                .createDate(LocalDateTime.of(2023, 10, 12, 10, 0))
                .updateDate(LocalDateTime.of(2023, 10, 13, 12, 0))
                .build();

        // When: toResponse is called
        OrderResponse response = OrderResponseMapper.toResponse(order);

        // Then: returned OrderResponse matches the Order object
        assertEquals(1L, response.id());
        assertEquals(101L, response.customerId());
        assertEquals("Asset A", response.assetName());
        assertEquals(Side.BUY, response.orderSide());
        assertEquals(new BigDecimal("10.00"), response.size());
        assertEquals(new BigDecimal("100.50"), response.price());
        assertEquals(Status.PENDING, response.status());
        assertEquals(LocalDateTime.of(2023, 10, 12, 10, 0), response.createDate());
        assertEquals(LocalDateTime.of(2023, 10, 13, 12, 0), response.updateDate());
    }

    @Test
    void toResponse_NullOrder_ThrowsNullPointerException() {
        // Given: a null Order object
        Order order = null;

        // When/Then: calling toResponse with null Order should throw a NullPointerException
        try {
            OrderResponseMapper.toResponse(order);
        } catch (NullPointerException e) {
            assertEquals(null, order);
        }
    }

    @Test
    void toResponse_OrderWithNullFields_ReturnsOrderResponseWithNullFields() {
        // Given: an Order object with null fields
        Order order = Order.builder()
                .id(null)
                .customerId(null)
                .assetName(null)
                .orderSide(null)
                .size(null)
                .price(null)
                .status(null)
                .createDate(null)
                .updateDate(null)
                .build();

        // When: toResponse is called
        OrderResponse response = OrderResponseMapper.toResponse(order);

        // Then: returned OrderResponse's fields are also null
        assertNull(response.id());
        assertNull(response.customerId());
        assertNull(response.assetName());
        assertNull(response.orderSide());
        assertNull(response.size());
        assertNull(response.price());
        assertNull(response.status());
        assertNull(response.createDate());
        assertNull(response.updateDate());
    }
}