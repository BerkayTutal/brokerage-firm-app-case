package com.brokerage.brokeragefirm.common.mapper;

import com.brokerage.brokeragefirm.repository.entity.AssetEntity;
import com.brokerage.brokeragefirm.repository.entity.CustomerEntity;
import com.brokerage.brokeragefirm.repository.entity.RoleEntity;
import com.brokerage.brokeragefirm.service.model.Asset;
import com.brokerage.brokeragefirm.service.model.Customer;
import com.brokerage.brokeragefirm.service.model.Role;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomerMapper {

    // Convert CustomerEntity to Customer
    public static Customer toModel(CustomerEntity entity) {
        if (entity == null) {
            return null;
        }

        return Customer.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .roles(getRoleSet(entity.getRoles()))
                .assets(getAssetList(entity.getAssets()))
                .build();
    }

    public static CustomerEntity toEntity(Customer model) {
        if (model == null) {
            return null;
        }

        return CustomerEntity.builder()
                .id(model.getId())
                .email(model.getEmail())
                .password(model.getPassword())
                .roles(getRoleEntitySet(model.getRoles())) // Map roles
                .assets(getAssetEntityList(model))
                .build();
    }

    private static Set<RoleEntity> getRoleEntitySet(Set<Role> roleSet) {
        return roleSet == null ? null : roleSet
                .stream()
                .map(RoleMapper::toEntity)
                .collect(Collectors.toSet());
    }

    private static Set<Role> getRoleSet(Set<RoleEntity> roleEntitySet) {
        return roleEntitySet == null ? null : roleEntitySet.stream()
                .map(RoleMapper::toModel)
                .collect(Collectors.toSet());
    }

    private static List<Asset> getAssetList(List<AssetEntity> assetEntityList) {
        return assetEntityList == null ? null : assetEntityList.stream()
                .map(AssetMapper::toModel)
                .collect(Collectors.toList());
    }

    private static List<AssetEntity> getAssetEntityList(Customer customer) {
        return customer.getAssets() == null ? null : customer.getAssets().stream()
                .map(asset -> AssetMapper.toEntity(asset, customer))
                .collect(Collectors.toList());
    }
}