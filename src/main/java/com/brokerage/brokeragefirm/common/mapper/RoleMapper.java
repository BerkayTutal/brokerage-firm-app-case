package com.brokerage.brokeragefirm.common.mapper;

import com.brokerage.brokeragefirm.repository.entity.RoleEntity;
import com.brokerage.brokeragefirm.service.model.Role;

public class RoleMapper {
    public static Role toModel(RoleEntity roleEntity) {
        return Role.builder()
                .id(roleEntity.getId())
                .name(roleEntity.getName())
                .build();
    }

    public static RoleEntity toEntity(Role roleModel) {
        return RoleEntity.builder()
                .id(roleModel.getId())
                .name(roleModel.getName())
                .build();
    }
}
