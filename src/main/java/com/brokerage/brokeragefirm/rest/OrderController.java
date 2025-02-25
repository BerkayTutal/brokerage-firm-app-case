package com.brokerage.brokeragefirm.rest;

import com.brokerage.brokeragefirm.common.aspect.annotations.ValidateOwnershipCustomer;
import com.brokerage.brokeragefirm.common.aspect.annotations.ValidateOwnershipOrder;
import com.brokerage.brokeragefirm.common.constants.Constants;
import com.brokerage.brokeragefirm.common.mapper.OrderRequestMapper;
import com.brokerage.brokeragefirm.common.mapper.OrderResponseMapper;
import com.brokerage.brokeragefirm.common.security.CustomUserDetails;
import com.brokerage.brokeragefirm.rest.dto.OrderRequest;
import com.brokerage.brokeragefirm.rest.dto.OrderResponse;
import com.brokerage.brokeragefirm.service.OrderService;
import com.brokerage.brokeragefirm.service.model.Order;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    //TODO return page
    @PreAuthorize("hasRole('" + Constants.ROLE_ADMIN + "')")
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders().stream().map(OrderResponseMapper::toResponse).toList());
    }

    @ValidateOwnershipOrder
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(@AuthenticationPrincipal CustomUserDetails loggedUser, @PathVariable Long orderId) {
        return ResponseEntity.ok(OrderResponseMapper.toResponse(orderService.getOrder(orderId)));
    }

    @ValidateOwnershipCustomer
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByCustomer(@AuthenticationPrincipal CustomUserDetails loggedUser, @PathVariable Long customerId) {
        return ResponseEntity.ok(orderService.getOrdersByCustomerId(customerId).stream().map(OrderResponseMapper::toResponse).toList());
    }


    @ValidateOwnershipOrder
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(@AuthenticationPrincipal CustomUserDetails loggedUser, @PathVariable Long orderId) {
        return ResponseEntity.ok(OrderResponseMapper.toResponse(orderService.cancelOrder(orderId)));
    }

    @PreAuthorize("hasRole('" + Constants.ROLE_ADMIN + "')")
    @PutMapping("/{orderId}/match")
    public ResponseEntity<OrderResponse> matchOrder(@AuthenticationPrincipal CustomUserDetails loggedUser, @PathVariable Long orderId) {
        return ResponseEntity.ok(OrderResponseMapper.toResponse(orderService.matchOrder(orderId)));
    }

    @ValidateOwnershipOrder
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@AuthenticationPrincipal CustomUserDetails loggedUser, @Valid @RequestBody OrderRequest orderRequest) {
        Order order = OrderRequestMapper.toModel(orderRequest);
        return ResponseEntity.ok(OrderResponseMapper.toResponse(orderService.createOrder(order)));
    }
}
