package com.brokerage.brokeragefirm.common.mapper;

import com.brokerage.brokeragefirm.repository.entity.CustomerEntity;
import com.brokerage.brokeragefirm.repository.entity.RoleEntity;
import com.brokerage.brokeragefirm.service.model.Customer;
import com.brokerage.brokeragefirm.service.model.Role;

import java.util.Set;
import java.util.stream.Collectors;

public class CustomerMapper {

    public static Customer toModel(CustomerEntity entity) {
        if (entity == null) {
            return null;
        }

        return Customer.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .roles(getRoleSet(entity.getRoles()))
                .build();
    }

    private static Set<Role> getRoleSet(Set<RoleEntity> roleEntitySet) {
        return roleEntitySet == null ? null : roleEntitySet.stream()
                .map(RoleMapper::toModel)
                .collect(Collectors.toSet());
    }

}