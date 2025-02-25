package com.brokerage.brokeragefirm.common.mapper;

import com.brokerage.brokeragefirm.common.enums.Side;
import com.brokerage.brokeragefirm.rest.dto.OrderRequest;
import com.brokerage.brokeragefirm.service.model.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class OrderRequestMapperTest {

    @Test
    void toModel_ValidOrderRequest_ShouldReturnCorrectOrder() {
        // given
        OrderRequest orderRequest = new OrderRequest(
                123L,
                "AssetName",
                Side.BUY,
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(100)
        );

        // when
        Order order = OrderRequestMapper.toModel(orderRequest);

        // then
        assertEquals(123L, order.getCustomerId(), "Customer ID should match");
        assertEquals("AssetName", order.getAssetName(), "Asset name should match");
        assertEquals(Side.BUY, order.getOrderSide(), "Order side should match");
        assertEquals(BigDecimal.valueOf(10), order.getSize(), "Size should match");
        assertEquals(BigDecimal.valueOf(100), order.getPrice(), "Price should match");
    }
}