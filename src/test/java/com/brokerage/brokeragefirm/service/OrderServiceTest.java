package com.brokerage.brokeragefirm.service;

import com.brokerage.brokeragefirm.common.constants.Constants;
import com.brokerage.brokeragefirm.common.enums.Error;
import com.brokerage.brokeragefirm.common.enums.Side;
import com.brokerage.brokeragefirm.common.enums.Status;
import com.brokerage.brokeragefirm.common.exception.NotFoundException;
import com.brokerage.brokeragefirm.common.exception.OperationNotAllowedException;
import com.brokerage.brokeragefirm.repository.OrderRepository;
import com.brokerage.brokeragefirm.repository.entity.CustomerEntity;
import com.brokerage.brokeragefirm.repository.entity.OrderEntity;
import com.brokerage.brokeragefirm.service.impl.OrderServiceImpl;
import com.brokerage.brokeragefirm.service.model.Asset;
import com.brokerage.brokeragefirm.service.model.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private AssetService assetService;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private OrderServiceImpl orderServiceImpl;

    @Test
    void create_ShouldThrowException_WhenAssetNameIsTRY() {
        // given
        Order order = Order.builder()
                .assetName(Constants.ASSET_TRY)
                .build();

        // when & then
        OperationNotAllowedException exception = assertThrows(OperationNotAllowedException.class, () -> {
            orderServiceImpl.create(order);
        });

        assertEquals(Error.ASSET_TRY_NOT_ALLOWED.getErrorDescription(), exception.getMessage());
    }

    @Test
    void create_ShouldCreateBuyOrder_WhenSufficientFundsAreAvailable() {
        // given
        Order order = Order.builder()
                .assetName("ASSET_ABC")
                .orderSide(Side.BUY)
                .customerId(1L)
                .size(BigDecimal.valueOf(10))
                .price(BigDecimal.valueOf(5))
                .build();

        Asset asset = Asset.builder()
                .assetName(Constants.ASSET_TRY)
                .customerId(1L)
                .usableSize(BigDecimal.valueOf(100))
                .build();

        when(assetService.get(1L, Constants.ASSET_TRY)).thenReturn(asset);
        when(orderRepository.save(any(OrderEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        Order createdOrder = orderServiceImpl.create(order);

        // then
        assertNotNull(createdOrder);
        verify(assetService, times(1)).update(asset);
        verify(orderRepository, times(1)).save(any(OrderEntity.class));
    }

    @Test
    void create_ShouldThrowException_WhenInsufficientFundsForBuyOrder() {
        // given
        String assetAbc = "ASSET_ABC";
        Order order = Order.builder()
                .assetName(assetAbc)
                .orderSide(Side.BUY)
                .customerId(1L)
                .size(BigDecimal.valueOf(10))
                .price(BigDecimal.valueOf(15))
                .build();

        Asset asset = Asset.builder()
                .assetName(Constants.ASSET_TRY)
                .customerId(1L)
                .usableSize(BigDecimal.valueOf(50))
                .build();

        when(assetService.get(1L, Constants.ASSET_TRY)).thenReturn(asset);

        // when & then
        OperationNotAllowedException exception = assertThrows(OperationNotAllowedException.class, () -> {
            orderServiceImpl.create(order);
        });

        assertEquals(new OperationNotAllowedException(Error.INSUFFICIENT_ASSET, Constants.ASSET_TRY, Side.BUY, assetAbc).getMessage(), exception.getMessage());
    }

    @Test
    void create_ShouldCreateSellOrder_WhenSufficientAssetsAreAvailable() {
        // given
        Order order = Order.builder()
                .assetName("ASSET_ABC")
                .orderSide(Side.SELL)
                .customerId(1L)
                .size(BigDecimal.valueOf(10))
                .build();

        Asset asset = Asset.builder()
                .assetName("ASSET_ABC")
                .customerId(1L)
                .usableSize(BigDecimal.valueOf(20))
                .build();

        when(assetService.get(1L, "ASSET_ABC")).thenReturn(asset);
        when(orderRepository.save(any(OrderEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        Order createdOrder = orderServiceImpl.create(order);

        // then
        assertNotNull(createdOrder);
        verify(assetService, times(1)).update(asset);
        verify(orderRepository, times(1)).save(any(OrderEntity.class));
    }

    @Test
    void create_ShouldThrowException_WhenInsufficientAssetsForSellOrder() {
        // given
        String assetAbc = "ASSET_ABC";
        Order order = Order.builder()
                .assetName(assetAbc)
                .orderSide(Side.SELL)
                .customerId(1L)
                .size(BigDecimal.valueOf(15))
                .build();

        Asset asset = Asset.builder()
                .assetName(assetAbc)
                .customerId(1L)
                .usableSize(BigDecimal.valueOf(10))
                .build();

        when(assetService.get(1L, assetAbc)).thenReturn(asset);

        // when & then
        OperationNotAllowedException exception = assertThrows(OperationNotAllowedException.class, () -> {
            orderServiceImpl.create(order);
        });

        assertEquals(new OperationNotAllowedException(Error.INSUFFICIENT_ASSET, assetAbc, Side.SELL, assetAbc).getMessage(), exception.getMessage());
    }

    @Test
    void match_ShouldMatchOrder_WhenSideIsBuyAndAssetsExist() {
        // given
        String assetAbc = "ASSET_ABC";
        OrderEntity orderEntity = OrderEntity.builder()
                .id(1L)
                .orderSide(Side.BUY)
                .assetName(assetAbc)
                .price(BigDecimal.valueOf(10))
                .size(BigDecimal.valueOf(5))
                .status(Status.PENDING)
                .customer(CustomerEntity.builder().id(1L).build())
                .build();

        Asset tryAsset = Asset.builder()
                .assetName(Constants.ASSET_TRY)
                .customerId(1L)
                .size(BigDecimal.valueOf(100))
                .build();

        Asset existingAsset = Asset.builder()
                .assetName(assetAbc)
                .customerId(1L)
                .size(BigDecimal.valueOf(10))
                .usableSize(BigDecimal.valueOf(10))
                .build();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(orderEntity));
        when(assetService.get(1L, Constants.ASSET_TRY)).thenReturn(tryAsset);
        when(assetService.get(1L, assetAbc)).thenReturn(existingAsset);
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(orderEntity);
        when(assetService.exists(orderEntity.getCustomer().getId(), orderEntity.getAssetName())).thenReturn(true);

        // when
        Order matchedOrder = orderServiceImpl.match(1L);

        // then
        assertNotNull(matchedOrder);
        assertEquals(Status.MATCHED, orderEntity.getStatus());
        verify(assetService, times(1)).update(tryAsset);
        verify(assetService, times(1)).update(existingAsset);
        verify(orderRepository, times(1)).save(any(OrderEntity.class));
    }

    @Test
    void match_ShouldMatchOrder_WhenSideIsSellAndAssetsExist() {
        // given
        String assetAbc = "ASSET_ABC";
        OrderEntity orderEntity = OrderEntity.builder()
                .id(1L)
                .orderSide(Side.SELL)
                .assetName(assetAbc)
                .price(BigDecimal.valueOf(10))
                .size(BigDecimal.valueOf(5))
                .status(Status.PENDING)
                .customer(CustomerEntity.builder().id(1L).build())
                .build();

        Asset tryAsset = Asset.builder()
                .assetName(Constants.ASSET_TRY)
                .customerId(1L)
                .size(BigDecimal.valueOf(100))
                .usableSize(BigDecimal.valueOf(100))
                .build();

        Asset sellingAsset = Asset.builder()
                .assetName(assetAbc)
                .customerId(1L)
                .size(BigDecimal.valueOf(20))
                .usableSize(BigDecimal.valueOf(20))
                .build();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(orderEntity));
        when(assetService.get(1L, Constants.ASSET_TRY)).thenReturn(tryAsset);
        when(assetService.get(1L, assetAbc)).thenReturn(sellingAsset);
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(orderEntity);

        // when
        Order matchedOrder = orderServiceImpl.match(1L);

        // then
        assertNotNull(matchedOrder);
        assertEquals(Status.MATCHED, orderEntity.getStatus());
        verify(assetService, times(1)).update(tryAsset);
        verify(assetService, times(1)).update(sellingAsset);
        verify(orderRepository, times(1)).save(any(OrderEntity.class));
    }


    @Test
    void match_ShouldMatchOrder_WhenSideIsBuyAndAssetsDoesNotExist() {
        // given
        String assetAbc = "ASSET_ABC";
        OrderEntity orderEntity = OrderEntity.builder()
                .id(1L)
                .orderSide(Side.BUY)
                .assetName(assetAbc)
                .price(BigDecimal.valueOf(10))
                .size(BigDecimal.valueOf(5))
                .status(Status.PENDING)
                .customer(CustomerEntity.builder().id(1L).build())
                .build();

        Asset tryAsset = Asset.builder()
                .assetName(Constants.ASSET_TRY)
                .customerId(1L)
                .size(BigDecimal.valueOf(100))
                .build();

        Asset newAsset = Asset.builder()
                .assetName(assetAbc)
                .customerId(1L)
                .size(BigDecimal.valueOf(5))
                .usableSize(BigDecimal.valueOf(5))
                .build();


        when(orderRepository.findById(1L)).thenReturn(Optional.of(orderEntity));
        when(assetService.get(1L, Constants.ASSET_TRY)).thenReturn(tryAsset);
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(orderEntity);
        when(assetService.exists(orderEntity.getCustomer().getId(), orderEntity.getAssetName())).thenReturn(false);

        // when
        Order matchedOrder = orderServiceImpl.match(1L);

        // then
        assertNotNull(matchedOrder);
        assertEquals(Status.MATCHED, orderEntity.getStatus());
        verify(assetService, times(1)).update(tryAsset);
        verify(assetService, times(1)).create(newAsset);
        verify(orderRepository, times(1)).save(any(OrderEntity.class));
    }


    @Test
    void match_ShouldThrowNotFoundException_WhenOrderDoesNotExist() {
        // given
        Long orderId = 1L;
        when(orderRepository.findById(orderId)).thenReturn(java.util.Optional.empty());

        // when & then
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            orderServiceImpl.match(orderId);
        });

        assertEquals(new NotFoundException(Error.ORDER_NOT_FOUND_ID, orderId).getMessage(), exception.getMessage());
    }

    @Test
    void match_ShouldThrowOperationNotAllowedException_WhenOrderStatusIsNotPending() {
        // given
        OrderEntity orderEntity = OrderEntity.builder()
                .status(Status.CANCELLED)
                .build();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(orderEntity));

        // when & then
        OperationNotAllowedException exception = assertThrows(OperationNotAllowedException.class, () -> {
            orderServiceImpl.match(1L);
        });

        assertEquals(new OperationNotAllowedException(Error.NOT_ALLOWED_STATE_CHANGE, Status.CANCELLED, Status.MATCHED).getMessage(), exception.getMessage());
    }


    @Test
    void getAll_ReturnsAllOrders() {
        // given
        OrderEntity orderEntity1 = OrderEntity.builder()
                .id(1L)
                .assetName("ASSET_ABC")
                .orderSide(Side.BUY)
                .status(Status.PENDING)
                .customer(CustomerEntity.builder().id(1L).build())
                .build();

        OrderEntity orderEntity2 = OrderEntity.builder()
                .id(2L)
                .assetName("ASSET_XYZ")
                .orderSide(Side.SELL)
                .status(Status.MATCHED)
                .customer(CustomerEntity.builder().id(1L).build())
                .build();

        when(orderRepository.findAll()).thenReturn(List.of(orderEntity1, orderEntity2));

        // when
        List<Order> orders = orderServiceImpl.getAll();

        // then
        assertNotNull(orders);
        assertEquals(2, orders.size());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void get_ShouldReturnOrder_WhenOrderExists() {
        // given
        Long orderId = 1L;
        OrderEntity orderEntity = OrderEntity.builder()
                .id(orderId)
                .assetName("ASSET_ABC")
                .orderSide(Side.BUY)
                .status(Status.PENDING)
                .customer(CustomerEntity.builder().id(1L).build())
                .build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(orderEntity));

        // when
        Order order = orderServiceImpl.get(orderId);

        // then
        assertNotNull(order);
        assertEquals(orderId, order.getId());
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void get_ShouldThrowNotFoundException_WhenOrderDoesNotExist() {
        // given
        Long orderId = 1L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // when & then
        NotFoundException exception = assertThrows(NotFoundException.class, () -> orderServiceImpl.get(orderId));

        assertEquals(new NotFoundException(Error.ORDER_NOT_FOUND_ID, orderId).getMessage(), exception.getMessage());
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void getAll_ShouldReturnAllOrders_WhenOrdersExist() {
        // given
        OrderEntity orderEntity1 = OrderEntity.builder()
                .id(1L)
                .assetName("ASSET_ABC")
                .orderSide(Side.BUY)
                .status(Status.PENDING)
                .customer(CustomerEntity.builder().id(1L).build())
                .build();

        OrderEntity orderEntity2 = OrderEntity.builder()
                .id(2L)
                .assetName("ASSET_XYZ")
                .orderSide(Side.SELL)
                .status(Status.MATCHED)
                .customer(CustomerEntity.builder().id(1L).build())
                .build();

        when(orderRepository.findAll()).thenReturn(List.of(orderEntity1, orderEntity2));

        // when
        List<Order> orders = orderServiceImpl.getAll();

        // then
        assertNotNull(orders);
        assertEquals(2, orders.size());
        assertEquals("ASSET_ABC", orders.get(0).getAssetName());
        assertEquals("ASSET_XYZ", orders.get(1).getAssetName());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void getAll_ShouldReturnEmptyList_WhenNoOrdersExist() {
        // given
        when(orderRepository.findAll()).thenReturn(List.of());

        // when
        List<Order> orders = orderServiceImpl.getAll();

        // then
        assertNotNull(orders);
        assertTrue(orders.isEmpty());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void cancel_ShouldCancelBuyOrder_WhenOrderIsPending() {
        // given
        Long orderId = 1L;
        OrderEntity orderEntity = OrderEntity.builder()
                .id(orderId)
                .orderSide(Side.BUY)
                .status(Status.PENDING)
                .size(BigDecimal.valueOf(10))
                .price(BigDecimal.valueOf(5))
                .customer(CustomerEntity.builder().id(1L).build())
                .build();

        Asset tryAsset = Asset.builder()
                .customerId(1L)
                .assetName(Constants.ASSET_TRY)
                .usableSize(BigDecimal.valueOf(50))
                .build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(orderEntity));
        when(orderRepository.save(orderEntity)).thenReturn(orderEntity);
        when(assetService.get(1L, Constants.ASSET_TRY)).thenReturn(tryAsset);

        // when
        Order cancelledOrder = orderServiceImpl.cancel(orderId);

        // then
        assertNotNull(cancelledOrder);
        assertEquals(Status.CANCELLED, cancelledOrder.getStatus());
        verify(assetService, times(1)).update(tryAsset);
        verify(orderRepository, times(1)).save(orderEntity);
    }

    @Test
    void cancel_ShouldCancelSellOrder_WhenOrderIsPending() {
        // given
        Long orderId = 1L;
        OrderEntity orderEntity = OrderEntity.builder()
                .id(orderId)
                .orderSide(Side.SELL)
                .status(Status.PENDING)
                .size(BigDecimal.valueOf(10))
                .customer(CustomerEntity.builder().id(1L).build())
                .assetName("ASSET_ABC")
                .build();

        Asset sellingAsset = Asset.builder()
                .customerId(1L)
                .assetName("ASSET_ABC")
                .usableSize(BigDecimal.valueOf(50))
                .build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(orderEntity));
        when(orderRepository.save(orderEntity)).thenReturn(orderEntity);
        when(assetService.get(1L, "ASSET_ABC")).thenReturn(sellingAsset);

        // when
        Order cancelledOrder = orderServiceImpl.cancel(orderId);

        // then
        assertNotNull(cancelledOrder);
        assertEquals(Status.CANCELLED, cancelledOrder.getStatus());
        verify(assetService, times(1)).update(sellingAsset);
        verify(orderRepository, times(1)).save(orderEntity);
    }

    @Test
    void cancel_ShouldThrowNotFoundException_WhenOrderDoesNotExist() {
        // given
        Long orderId = 1L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // when & then
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            orderServiceImpl.cancel(orderId);
        });

        assertEquals(new NotFoundException(Error.ORDER_NOT_FOUND_ID, orderId).getMessage(), exception.getMessage());
    }

    @Test
    void cancel_ShouldThrowOperationNotAllowedException_WhenOrderStatusIsNotPending() {
        // given
        Long orderId = 1L;
        OrderEntity orderEntity = OrderEntity.builder()
                .id(orderId)
                .status(Status.MATCHED)
                .build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(orderEntity));

        // when & then
        OperationNotAllowedException exception = assertThrows(OperationNotAllowedException.class, () -> {
            orderServiceImpl.cancel(orderId);
        });

        assertEquals(new OperationNotAllowedException(Error.NOT_ALLOWED_STATE_CHANGE, Status.PENDING, Status.MATCHED).getMessage(), exception.getMessage());
    }
}