package com.brokerage.brokeragefirm.common.mapper;

import com.brokerage.brokeragefirm.repository.entity.AssetEntity;
import com.brokerage.brokeragefirm.repository.entity.CustomerEntity;
import com.brokerage.brokeragefirm.service.model.Asset;

public class AssetMapper {

    public static Asset toModel(AssetEntity entity) {
        return Asset.builder()
                .id(entity.getId())
                .customerId(entity.getCustomer() == null ? null : entity.getCustomer().getId()) // Ensure Customer is loaded
                .assetName(entity.getAssetName())
                .size(entity.getSize())
                .usableSize(entity.getUsableSize())
                .build();
    }

    public static AssetEntity toEntity(Asset model, CustomerEntity customerEntity) {
        AssetEntity entity = new AssetEntity();
        entity.setId(model.getId());
        entity.setCustomer(customerEntity);
        entity.setAssetName(model.getAssetName());
        entity.setSize(model.getSize());
        entity.setUsableSize(model.getUsableSize());
        return entity;
    }

    public static AssetEntity toEntity(Asset model) {
        AssetEntity entity = new AssetEntity();
        entity.setId(model.getId());
        entity.setCustomer(CustomerEntity.builder().id(model.getCustomerId()).build());
        entity.setAssetName(model.getAssetName());
        entity.setSize(model.getSize());
        entity.setUsableSize(model.getUsableSize());
        return entity;
    }

}