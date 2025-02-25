package com.brokerage.brokeragefirm.common.mapper;

import com.brokerage.brokeragefirm.common.enums.Side;
import com.brokerage.brokeragefirm.common.enums.Status;
import com.brokerage.brokeragefirm.repository.entity.CustomerEntity;
import com.brokerage.brokeragefirm.repository.entity.OrderEntity;
import com.brokerage.brokeragefirm.service.model.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class OrderMapperTest {

    @Test
    void toModel_ValidOrderEntity_ReturnsOrderModel() {
        // Given
        LocalDateTime createDate = LocalDateTime.of(2023, 8, 20, 14, 0);
        LocalDateTime updateDate = LocalDateTime.of(2023, 8, 22, 16, 0);

        CustomerEntity customerEntity = CustomerEntity.builder()
                .id(100L)
                .build();

        OrderEntity orderEntity = OrderEntity.builder()
                .id(1L)
                .customer(customerEntity)
                .assetName("AAPL")
                .orderSide(Side.BUY)
                .size(new BigDecimal("10.5"))
                .price(new BigDecimal("150.75"))
                .status(Status.PENDING)
                .createDate(createDate)
                .updateDate(updateDate)
                .build();

        // When
        Order order = OrderMapper.toModel(orderEntity);

        // Then
        assertEquals(orderEntity.getId(), order.getId());
        assertEquals(orderEntity.getCustomer().getId(), order.getCustomerId());
        assertEquals(orderEntity.getAssetName(), order.getAssetName());
        assertEquals(orderEntity.getOrderSide(), order.getOrderSide());
        assertEquals(orderEntity.getSize(), order.getSize());
        assertEquals(orderEntity.getPrice(), order.getPrice());
        assertEquals(orderEntity.getStatus(), order.getStatus());
        assertEquals(orderEntity.getCreateDate(), order.getCreateDate());
        assertEquals(orderEntity.getUpdateDate(), order.getUpdateDate());
    }

    @Test
    void toModel_OrderEntityWithNullFields_ReturnsOrderModelWithNulls() {
        // Given
        OrderEntity orderEntity = OrderEntity.builder()
                .id(2L)
                .customer(CustomerEntity.builder().build())
                .assetName(null)
                .orderSide(null)
                .size(null)
                .price(null)
                .status(null)
                .createDate(null)
                .updateDate(null)
                .build();

        // When
        Order order = OrderMapper.toModel(orderEntity);

        // Then
        assertEquals(orderEntity.getId(), order.getId());
        assertNull(order.getCustomerId());
        assertNull(order.getAssetName());
        assertNull(order.getOrderSide());
        assertNull(order.getSize());
        assertNull(order.getPrice());
        assertNull(order.getStatus());
        assertNull(order.getCreateDate());
        assertNull(order.getUpdateDate());
    }

    @Test
    void toEntity_ValidOrderModel_ReturnsOrderEntity() {
        // Given
        Order order = Order.builder()
                .id(1L)
                .customerId(100L)
                .assetName("AAPL")
                .orderSide(Side.BUY)
                .size(new BigDecimal("10.5"))
                .price(new BigDecimal("150.75"))
                .status(Status.PENDING)
                .build();

        // When
        OrderEntity orderEntity = OrderMapper.toEntity(order);

        // Then
        assertEquals(order.getId(), orderEntity.getId());
        assertEquals(order.getCustomerId(), orderEntity.getCustomer().getId());
        assertEquals(order.getAssetName(), orderEntity.getAssetName());
        assertEquals(order.getOrderSide(), orderEntity.getOrderSide());
        assertEquals(order.getSize(), orderEntity.getSize());
        assertEquals(order.getPrice(), orderEntity.getPrice());
        assertEquals(order.getStatus(), orderEntity.getStatus());
    }

    @Test
    void toEntity_OrderModelWithNullFields_ReturnsOrderEntityWithNulls() {
        // Given
        Order order = Order.builder()
                .id(2L)
                .customerId(null)
                .assetName(null)
                .orderSide(null)
                .size(null)
                .price(null)
                .status(null)
                .build();

        // When
        OrderEntity orderEntity = OrderMapper.toEntity(order);

        // Then
        assertEquals(order.getId(), orderEntity.getId());
        assertNull(orderEntity.getCustomer().getId());
        assertNull(orderEntity.getAssetName());
        assertNull(orderEntity.getOrderSide());
        assertNull(orderEntity.getSize());
        assertNull(orderEntity.getPrice());
        assertNull(orderEntity.getStatus());
    }
}