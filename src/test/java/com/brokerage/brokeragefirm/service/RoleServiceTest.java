package com.brokerage.brokeragefirm.service;

import com.brokerage.brokeragefirm.common.mapper.RoleMapper;
import com.brokerage.brokeragefirm.repository.RoleRepository;
import com.brokerage.brokeragefirm.repository.entity.RoleEntity;
import com.brokerage.brokeragefirm.service.impl.RoleServiceImpl;
import com.brokerage.brokeragefirm.service.model.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    @Test
    void get_ValidName_ReturnsRole() {
        // given
        String roleName = "ADMIN";
        RoleEntity repositoryRole = RoleEntity.builder().name(roleName).build();
        Role expectedRole = RoleMapper.toModel(repositoryRole);

        when(roleRepository.getByName(anyString())).thenReturn(repositoryRole);

        // when
        Role actualRole = roleService.get(roleName);

        // then
        verify(roleRepository, times(1)).getByName(roleName);
        assertEquals(expectedRole, actualRole);
    }

    @Test
    void get_RoleNotFound_ThrowsException() {
        // given
        String roleName = "NON_EXISTENT";
        when(roleRepository.getByName(anyString())).thenReturn(null);

        // when
        Exception exception = assertThrows(NullPointerException.class,
                () -> roleService.get(roleName));

        // then
        verify(roleRepository, times(1)).getByName(roleName);
    }
}