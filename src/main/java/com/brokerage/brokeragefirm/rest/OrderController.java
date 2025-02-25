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
    public ResponseEntity<List<OrderResponse>> getAll() {
        return ResponseEntity.ok(orderService.getAll().stream().map(OrderResponseMapper::toResponse).toList());
    }

    @ValidateOwnershipOrder
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> get(@AuthenticationPrincipal CustomUserDetails loggedUser, @PathVariable Long orderId) {
        return ResponseEntity.ok(OrderResponseMapper.toResponse(orderService.get(orderId)));
    }

    @ValidateOwnershipCustomer
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderResponse>> getAll(@AuthenticationPrincipal CustomUserDetails loggedUser, @PathVariable Long customerId) {
        return ResponseEntity.ok(orderService.getAll(customerId).stream().map(OrderResponseMapper::toResponse).toList());
    }


    @ValidateOwnershipOrder
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResponse> cancel(@AuthenticationPrincipal CustomUserDetails loggedUser, @PathVariable Long orderId) {
        return ResponseEntity.ok(OrderResponseMapper.toResponse(orderService.cancel(orderId)));
    }

    @PreAuthorize("hasRole('" + Constants.ROLE_ADMIN + "')")
    @PutMapping("/{orderId}/match")
    public ResponseEntity<OrderResponse> match(@AuthenticationPrincipal CustomUserDetails loggedUser, @PathVariable Long orderId) {
        return ResponseEntity.ok(OrderResponseMapper.toResponse(orderService.match(orderId)));
    }

    @ValidateOwnershipOrder
    @PostMapping
    public ResponseEntity<OrderResponse> create(@AuthenticationPrincipal CustomUserDetails loggedUser, @Valid @RequestBody OrderRequest orderRequest) {
        Order order = OrderRequestMapper.toModel(orderRequest);
        return ResponseEntity.ok(OrderResponseMapper.toResponse(orderService.create(order)));
    }
}
