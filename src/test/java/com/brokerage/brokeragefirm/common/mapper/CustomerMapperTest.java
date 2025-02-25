package com.brokerage.brokeragefirm.common.mapper;

import com.brokerage.brokeragefirm.repository.entity.CustomerEntity;
import com.brokerage.brokeragefirm.repository.entity.RoleEntity;
import com.brokerage.brokeragefirm.service.model.Customer;
import com.brokerage.brokeragefirm.service.model.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerMapperTest {

    @Test
    void toModel_NullEntity_ReturnNull() {
        // given
        CustomerEntity entity = null;

        // when
        Customer result = CustomerMapper.toModel(entity);

        // then
        assertNull(result);
    }

    @Test
    void toModel_ValidEntityWithoutRoles_ReturnCustomerWithoutRoles() {
        // given
        CustomerEntity entity = CustomerEntity.builder()
                .id(1L)
                .email("test@example.com")
                .password("password")
                .roles(null)
                .build();

        // when
        Customer result = CustomerMapper.toModel(entity);

        // then
        assertEquals(1L, result.getId());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("password", result.getPassword());
        assertNull(result.getRoles());
    }

    @Test
    void toModel_ValidEntityWithRoles_ReturnCustomerWithRoles() {
        // given
        RoleEntity roleEntity1 = mock(RoleEntity.class);
        RoleEntity roleEntity2 = mock(RoleEntity.class);

        Role role1 = Role.builder().id(1L).name("ROLE_USER").build();
        Role role2 = Role.builder().id(2L).name("ROLE_ADMIN").build();

        when(roleEntity1.getId()).thenReturn(1L);
        when(roleEntity1.getName()).thenReturn("ROLE_USER");
        when(roleEntity2.getId()).thenReturn(2L);
        when(roleEntity2.getName()).thenReturn("ROLE_ADMIN");

        CustomerEntity entity = CustomerEntity.builder()
                .id(1L)
                .email("test@example.com")
                .password("password")
                .roles(Set.of(roleEntity1, roleEntity2))
                .build();

        // when
        Customer result = CustomerMapper.toModel(entity);

        // then
        assertEquals(1L, result.getId());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("password", result.getPassword());
        assertEquals(2, result.getRoles().size());
        assertEquals(Set.of(role1, role2), result.getRoles());
    }

    @Test
    void testToModel_NullRolesInEntity_ReturnsCustomerWithNullRoles() {
        // given
        CustomerEntity entity = CustomerEntity.builder()
                .id(1L)
                .email("test@example.com")
                .password("password123")
                .roles(null)
                .build();

        // when
        Customer result = CustomerMapper.toModel(entity);

        // then
        assertEquals(1L, result.getId());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("password123", result.getPassword());
        assertNull(result.getRoles());
    }

    @Test
    void testToModel_EntityWithSingleRole_ReturnsCustomerWithSingleRole() {
        // given
        RoleEntity roleEntity = mock(RoleEntity.class);
        Role role = Role.builder().id(1L).name("ROLE_USER").build();

        // Mock RoleMapper behavior
        when(roleEntity.getId()).thenReturn(1L);
        when(roleEntity.getName()).thenReturn("ROLE_USER");

        CustomerEntity entity = CustomerEntity.builder()
                .id(1L)
                .email("single-role@example.com")
                .password("password123")
                .roles(Set.of(roleEntity))
                .build();

        // when
        Customer result = CustomerMapper.toModel(entity);

        // then
        assertEquals(1L, result.getId());
        assertEquals("single-role@example.com", result.getEmail());
        assertEquals("password123", result.getPassword());
        assertEquals(1, result.getRoles().size());
        assertEquals(Set.of(role), result.getRoles());
    }

    @Test
    void testToModel_EntityWithMultipleRoles_ReturnsCustomerWithMultipleRoles() {
        // given
        RoleEntity roleEntity1 = mock(RoleEntity.class);
        RoleEntity roleEntity2 = mock(RoleEntity.class);
        Role role1 = Role.builder().id(1L).name("ROLE_USER").build();
        Role role2 = Role.builder().id(2L).name("ROLE_ADMIN").build();

        when(roleEntity1.getId()).thenReturn(1L);
        when(roleEntity1.getName()).thenReturn("ROLE_USER");
        when(roleEntity2.getId()).thenReturn(2L);
        when(roleEntity2.getName()).thenReturn("ROLE_ADMIN");
        
        CustomerEntity entity = CustomerEntity.builder()
                .id(10L)
                .email("multi-roles@example.com")
                .password("secure123")
                .roles(Set.of(roleEntity1, roleEntity2))
                .build();

        // when
        Customer result = CustomerMapper.toModel(entity);

        // then
        assertEquals(10L, result.getId());
        assertEquals("multi-roles@example.com", result.getEmail());
        assertEquals("secure123", result.getPassword());
        assertEquals(2, result.getRoles().size());
        assertEquals(Set.of(role1, role2), result.getRoles());
    }
}