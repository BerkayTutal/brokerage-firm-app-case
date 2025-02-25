package com.brokerage.brokeragefirm.common.mapper;

import com.brokerage.brokeragefirm.rest.dto.CustomerResponse;
import com.brokerage.brokeragefirm.service.model.Customer;
import com.brokerage.brokeragefirm.service.model.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class CustomerResponseMapperTest {

    @Test
    void toResponse_ValidCustomer_ReturnsCustomerResponse() {
        // given
        Long customerId = 1L;
        String email = "test@domain.com";
        Set<Role> roles = Set.of(Role.builder().id(1L).name("ROLE_USER").build());

        Customer customer = Customer.builder()
                .id(customerId)
                .email(email)
                .roles(roles)
                .build();

        // when
        CustomerResponse response = CustomerResponseMapper.toResponse(customer);

        // then
        assertNotNull(response);
        assertEquals(customerId, response.id());
        assertEquals(email, response.email());
        assertEquals(roles, response.roles());
    }
}