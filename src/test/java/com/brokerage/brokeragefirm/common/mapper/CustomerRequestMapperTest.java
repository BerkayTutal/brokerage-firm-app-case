package com.brokerage.brokeragefirm.common.mapper;

import com.brokerage.brokeragefirm.rest.dto.CustomerRequest;
import com.brokerage.brokeragefirm.service.model.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class CustomerRequestMapperTest {

    @Test
    void toModel_ValidCustomerRequestAndId_ShouldReturnCustomerWithCorrectValues() {
        // given
        CustomerRequest customerRequest = new CustomerRequest("test@example.com", "securePassword");
        Long id = 1L;

        // when
        Customer result = CustomerRequestMapper.toModel(customerRequest, id);

        // then
        assertEquals(id, result.getId(), "Customer ID should match the provided ID");
        assertEquals(customerRequest.email(), result.getEmail(), "Customer email should match the email in CustomerRequest");
        assertEquals(customerRequest.password(), result.getPassword(), "Customer password should match the password in CustomerRequest");
    }

    @Test
    void toModel_NullCustomerRequest_ShouldThrowNullPointerException() {
        // given
        CustomerRequest customerRequest = null;
        Long id = 1L;

        // when & then
        NullPointerException exception = assertThrows(NullPointerException.class, () ->
                CustomerRequestMapper.toModel(customerRequest, id)
        );
        assertEquals("Cannot invoke \"com.brokerage.brokeragefirm.rest.dto.CustomerRequest.email()\" because \"customerRequest\" is null", exception.getMessage());
    }

    @Test
    void toModel_NullId_ShouldReturnCustomerWithNullId() {
        // given
        CustomerRequest customerRequest = new CustomerRequest("test@example.com", "securePassword");

        // when
        Customer result = CustomerRequestMapper.toModel(customerRequest, null);

        // then
        assertEquals(null, result.getId(), "Customer ID should be null");
        assertEquals(customerRequest.email(), result.getEmail(), "Customer email should match the email in CustomerRequest");
        assertEquals(customerRequest.password(), result.getPassword(), "Customer password should match the password in CustomerRequest");
    }
}