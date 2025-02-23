package com.brokerage.brokeragefirm.rest;

import com.brokerage.brokeragefirm.common.aspect.annotations.ValidateOwnershipCustomer;
import com.brokerage.brokeragefirm.common.mapper.CustomerRequestMapper;
import com.brokerage.brokeragefirm.common.mapper.CustomerResponseMapper;
import com.brokerage.brokeragefirm.common.security.CustomUserDetails;
import com.brokerage.brokeragefirm.rest.dto.CustomerRequest;
import com.brokerage.brokeragefirm.rest.dto.CustomerResponse;
import com.brokerage.brokeragefirm.service.CustomerService;
import com.brokerage.brokeragefirm.service.model.Customer;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{customerId}")
    @ValidateOwnershipCustomer
    public ResponseEntity<CustomerResponse> getCustomerInfo(@AuthenticationPrincipal CustomUserDetails loggedUser, @PathVariable Long customerId) {
        Customer customer = customerService.getCustomerById(customerId);
        return ResponseEntity.ok(CustomerResponseMapper.toResponse(customer));
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{customerId}")
    @ValidateOwnershipCustomer
    public ResponseEntity<CustomerResponse> updateCustomer(@AuthenticationPrincipal CustomUserDetails loggedUser, @PathVariable Long customerId, @RequestBody @Valid CustomerRequest customerRequest) {
        Customer customer = customerService.updateEmailPassword(CustomerRequestMapper.toModel(customerRequest, customerId));
        return ResponseEntity.ok(CustomerResponseMapper.toResponse(customer));
    }

    //Only ADMIN role
    @GetMapping
    public List<CustomerResponse> getAllCustomers() {
        return customerService.getAllCustomers().stream().map(CustomerResponseMapper::toResponse).collect(Collectors.toList());
    }
}
