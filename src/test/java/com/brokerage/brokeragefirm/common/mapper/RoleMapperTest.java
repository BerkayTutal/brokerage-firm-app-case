package com.brokerage.brokeragefirm.common.mapper;

import com.brokerage.brokeragefirm.repository.entity.RoleEntity;
import com.brokerage.brokeragefirm.service.model.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class RoleMapperTest {

    @Test
    void toModel_ValidRoleEntity_ReturnsRole() {
        // given
        RoleEntity roleEntity = RoleEntity.builder()
                .id(1L)
                .name("Admin")
                .build();

        // when
        Role role = RoleMapper.toModel(roleEntity);

        // then
        assertEquals(roleEntity.getId(), role.getId());
        assertEquals(roleEntity.getName(), role.getName());
    }

    @Test
    void toEntity_ValidRoleModel_ReturnsRoleEntity() {
        // given
        Role roleModel = Role.builder()
                .id(1L)
                .name("Admin")
                .build();

        // when
        RoleEntity roleEntity = RoleMapper.toEntity(roleModel);

        // then
        assertEquals(roleModel.getId(), roleEntity.getId());
        assertEquals(roleModel.getName(), roleEntity.getName());
    }

    @Test
    void toEntity_RoleModelWithNullFields_ReturnsRoleEntityWithNulls() {
        // given
        Role roleModel = Role.builder()
                .id(null)
                .name(null)
                .build();

        // when
        RoleEntity roleEntity = RoleMapper.toEntity(roleModel);

        // then
        assertEquals(roleModel.getId(), roleEntity.getId());
        assertEquals(roleModel.getName(), roleEntity.getName());
    }
}