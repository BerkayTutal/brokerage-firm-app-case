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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    //TODO return page
    @PreAuthorize("hasRole('" + Constants.ROLE_ADMIN + "')")
    @GetMapping
    public ResponseEntity<Page<OrderResponse>> getAll(Pageable pageable) {
        Page<Order> orders = orderService.getAll(pageable);
        Page<OrderResponse> orderResponses = orders.map(OrderResponseMapper::toResponse);
        return ResponseEntity.ok(orderResponses);
    }


    @ValidateOwnershipOrder
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> get(@AuthenticationPrincipal CustomUserDetails loggedUser, @PathVariable Long orderId) {
        return ResponseEntity.ok(OrderResponseMapper.toResponse(orderService.get(orderId)));
    }

    @ValidateOwnershipCustomer
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Page<OrderResponse>> getAll(@AuthenticationPrincipal CustomUserDetails loggedUser,
                                                      @PathVariable Long customerId,
                                                      Pageable pageable) {
        Page<Order> orders = orderService.getAll(customerId, pageable);
        Page<OrderResponse> orderResponses = orders.map(OrderResponseMapper::toResponse);
        return ResponseEntity.ok(orderResponses);
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
