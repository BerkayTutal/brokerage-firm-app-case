package com.brokerage.brokeragefirm.common.mapper;

import com.brokerage.brokeragefirm.repository.entity.AssetEntity;
import com.brokerage.brokeragefirm.repository.entity.CustomerEntity;
import com.brokerage.brokeragefirm.service.model.Asset;
import com.brokerage.brokeragefirm.service.model.Customer;

public class AssetMapper {

    public static Asset toModel(AssetEntity entity) {
        Asset model = new Asset();
        model.setId(entity.getId());
        model.setCustomerId(entity.getCustomer().getId()); // Ensure Customer is loaded
        model.setAssetName(entity.getAssetName());
        model.setSize(entity.getSize());
        model.setUsableSize(entity.getUsableSize());
        return model;
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
    public static AssetEntity toEntity(Asset model, Customer customer) {
        return toEntity(model, CustomerMapper.toEntity(customer));
    }
}