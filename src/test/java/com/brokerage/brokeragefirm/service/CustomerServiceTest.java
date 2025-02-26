package com.brokerage.brokeragefirm.service;

import com.brokerage.brokeragefirm.common.constants.Constants;
import com.brokerage.brokeragefirm.common.enums.Error;
import com.brokerage.brokeragefirm.common.exception.DuplicateEntryException;
import com.brokerage.brokeragefirm.common.exception.NotFoundException;
import com.brokerage.brokeragefirm.common.mapper.RoleMapper;
import com.brokerage.brokeragefirm.repository.CustomerRepository;
import com.brokerage.brokeragefirm.repository.entity.CustomerEntity;
import com.brokerage.brokeragefirm.repository.entity.RoleEntity;
import com.brokerage.brokeragefirm.service.impl.CustomerServiceImpl;
import com.brokerage.brokeragefirm.service.model.Asset;
import com.brokerage.brokeragefirm.service.model.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.brokerage.brokeragefirm.common.constants.Constants.ROLE_CUSTOMER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private RoleService roleService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AssetService assetService;

    @InjectMocks
    private CustomerServiceImpl customerService;

    //register
    @Test
    void testRegister_WithValidCustomer_ShouldReturnRegisteredCustomer() {
        // given
        Customer customer = Customer.builder()
                .email("test@example.com")
                .password("password123")
                .build();

        CustomerEntity savedEntity = CustomerEntity.builder()
                .id(1L)
                .email(customer.getEmail())
                .password("encodedPassword")
                .roles(Set.of(RoleEntity.builder().name(ROLE_CUSTOMER).build()))
                .build();

        when(roleService.get(ROLE_CUSTOMER)).thenReturn(RoleMapper.toModel(savedEntity.getRoles().iterator().next()));
        when(passwordEncoder.encode(customer.getPassword())).thenReturn("encodedPassword");
        when(customerRepository.save(any(CustomerEntity.class))).thenReturn(savedEntity);

        // when
        Customer savedCustomer = customerService.register(customer);

        // then
        assertEquals(customer.getEmail(), savedCustomer.getEmail());
        verify(assetService, times(1))
                .create(Asset.builder().customerId(savedEntity.getId()).assetName(Constants.ASSET_TRY).build());
        verify(customerRepository, times(1)).save(any(CustomerEntity.class));
    }

    @Test
    void testRegister_WithDuplicateEmail_ShouldThrowDuplicateEntryException() {
        // given
        Customer customer = Customer.builder()
                .email("duplicate@example.com")
                .password("password123")
                .build();

        when(roleService.get(ROLE_CUSTOMER))
                .thenReturn(RoleMapper.toModel(RoleEntity.builder().name(ROLE_CUSTOMER).build()));
        when(passwordEncoder.encode(customer.getPassword())).thenReturn("encodedPassword");
        when(customerRepository.save(any(CustomerEntity.class)))
                .thenThrow(new RuntimeException("Duplicate entry"));

        // when
        DuplicateEntryException exception = assertThrows(DuplicateEntryException.class, () -> customerService.register(customer));

        // then
        assertEquals(Error.EMAIL_ALREADY_EXISTS.getErrorDescription(), exception.getMessage());
        verify(customerRepository, times(1)).save(any(CustomerEntity.class));
        verify(assetService, never()).create(any(Asset.class));
    }

    //get(String email)
    @Test
    void testGet_ExistingEmail_ReturnsCustomer() {
        // given
        String email = "existing@example.com";
        CustomerEntity customerEntity = CustomerEntity.builder()
                .id(1L)
                .email(email)
                .password("encodedPassword")
                .build();

        when(customerRepository.findCustomerByEmail(email)).thenReturn(Optional.of(customerEntity));

        // when
        Customer customer = customerService.get(email);

        // then
        assertEquals(email, customer.getEmail());
        verify(customerRepository, times(1)).findCustomerByEmail(email);
    }

    @Test
    void testGetByEmail_WithNonExistingEmail_ShouldThrowNotFoundException() {
        // given
        String email = "nonexistent@example.com";

        when(customerRepository.findCustomerByEmail(email)).thenReturn(Optional.empty());

        // when
        NotFoundException exception = assertThrows(NotFoundException.class, () -> customerService.get(email));

        // then
        assertEquals(String.format(Error.CUSTOMER_NOT_FOUND_EMAIL.getErrorDescription(), email), exception.getMessage());
        verify(customerRepository, times(1)).findCustomerByEmail(email);
    }


    //get(Long id)

    @Test
    void testGetById_WithExistingId_ShouldReturnCustomer() {
        // given
        Long id = 1L;
        CustomerEntity customerEntity = CustomerEntity.builder()
                .id(id)
                .email("test@example.com")
                .password("encodedPassword")
                .build();

        when(customerRepository.findById(id)).thenReturn(Optional.of(customerEntity));

        // when
        Customer customer = customerService.get(id);

        // then
        assertEquals(id, customer.getId());
        verify(customerRepository, times(1)).findById(id);
    }

    @Test
    void testGetById_WithNonExistingId_ShouldThrowNotFoundException() {
        // given
        Long id = 99L;

        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        // when
        NotFoundException exception = assertThrows(NotFoundException.class, () -> customerService.get(id));

        // then
        assertEquals(String.format(Error.CUSTOMER_NOT_FOUND_ID.getErrorDescription(), id), exception.getMessage());
        verify(customerRepository, times(1)).findById(id);
    }


    // updateEmailPassword
    @Test
    void testUpdateEmailPassword_WithValidCustomer_ShouldUpdateSuccessfully() {
        // given
        Customer customer = Customer.builder()
                .id(1L)
                .email("updated@example.com")
                .password("newPassword123")
                .build();

        CustomerEntity existingCustomerEntity = CustomerEntity.builder()
                .id(1L)
                .email("original@example.com")
                .password("originalPassword")
                .build();

        CustomerEntity updatedCustomerEntity = CustomerEntity.builder()
                .id(1L)
                .email(customer.getEmail())
                .password("encodedNewPassword")
                .build();

        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(existingCustomerEntity));
        when(passwordEncoder.encode(customer.getPassword())).thenReturn("encodedNewPassword");
        when(customerRepository.save(existingCustomerEntity)).thenReturn(updatedCustomerEntity);

        // when
        Customer updatedCustomer = customerService.updateEmailPassword(customer);

        // then
        assertEquals(customer.getEmail(), updatedCustomer.getEmail());
        verify(customerRepository, times(1)).findById(customer.getId());
        verify(customerRepository, times(1)).save(existingCustomerEntity);
        verify(passwordEncoder, times(1)).encode(customer.getPassword());
    }

    @Test
    void testUpdateEmailPassword_WithNonExistingCustomer_ShouldThrowNotFoundException() {
        // given
        Customer customer = Customer.builder()
                .id(99L)
                .email("nonexistent@example.com")
                .password("anyPassword")
                .build();

        when(customerRepository.findById(customer.getId())).thenReturn(Optional.empty());

        // when
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> customerService.updateEmailPassword(customer));

        // then
        assertEquals(String.format(Error.CUSTOMER_NOT_FOUND_ID.getErrorDescription(), customer.getId()), exception.getMessage());
        verify(customerRepository, times(1)).findById(customer.getId());
        verify(customerRepository, never()).save(any(CustomerEntity.class));
        verify(passwordEncoder, never()).encode(anyString());
    }

    // getAll

    @Test
    void testGetAll_WhenCustomersExist_ShouldReturnCustomerPage() {
        // Given
        Pageable pageable = PageRequest.of(0, 5); // First page, 5 items per page
        List<CustomerEntity> customerEntities = List.of(
                CustomerEntity.builder().id(1L).email("customer1@example.com").password("password1").build(),
                CustomerEntity.builder().id(2L).email("customer2@example.com").password("password2").build()
        );

        Page<CustomerEntity> pagedResult = new PageImpl<>(customerEntities, pageable, customerEntities.size());

        when(customerRepository.findAll(pageable)).thenReturn(pagedResult);

        // When
        Page<Customer> result = customerService.getAll(pageable);

        // Then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getTotalPages()).isEqualTo(1);
        assertThat(result.getContent().get(0).getEmail()).isEqualTo("customer1@example.com");
        assertThat(result.getContent().get(1).getEmail()).isEqualTo("customer2@example.com");
        verify(customerRepository, times(1)).findAll(pageable);
    }

    @Test
    void testGetAll_WhenNoCustomersExist_ShouldReturnEmptyPage() {
        // Given
        Pageable pageable = PageRequest.of(0, 5); // First page, 5 items per page
        Page<CustomerEntity> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(customerRepository.findAll(pageable)).thenReturn(emptyPage);

        // When
        Page<Customer> result = customerService.getAll(pageable);

        // Then
        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isZero();
        assertThat(result.getTotalPages()).isZero();
        verify(customerRepository, times(1)).findAll(pageable);
    }

    // exists
    @Test
    void testExists_WithExistingId_ShouldReturnTrue() {
        // given
        Long id = 1L;

        when(customerRepository.existsById(id)).thenReturn(true);

        // when
        boolean exists = customerService.exists(id);

        // then
        assertTrue(exists);
        verify(customerRepository, times(1)).existsById(id);
    }

    @Test
    void testExists_WithNonExistingId_ShouldReturnFalse() {
        // given
        Long id = 99L;

        when(customerRepository.existsById(id)).thenReturn(false);

        // when
        boolean exists = customerService.exists(id);

        // then
        assertFalse(exists);
        verify(customerRepository, times(1)).existsById(id);
    }
}