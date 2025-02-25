package com.brokerage.brokeragefirm.common.mapper;

import com.brokerage.brokeragefirm.repository.entity.AssetEntity;
import com.brokerage.brokeragefirm.repository.entity.CustomerEntity;
import com.brokerage.brokeragefirm.service.model.Asset;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(MockitoExtension.class)
class AssetMapperTest {

    @Test
    void toModel_ValidAssetEntity_ReturnsAssetModel() {
        // given
        CustomerEntity customerEntity = CustomerEntity.builder().id(123L).build();
        AssetEntity assetEntity = AssetEntity.builder()
                .id(456L)
                .customer(customerEntity)
                .assetName("Test Asset")
                .size(BigDecimal.valueOf(100.00))
                .usableSize(BigDecimal.valueOf(75.00))
                .build();

        // when
        Asset result = AssetMapper.toModel(assetEntity);

        // then
        assertEquals(assetEntity.getId(), result.getId());
        assertEquals(assetEntity.getCustomer().getId(), result.getCustomerId());
        assertEquals(assetEntity.getAssetName(), result.getAssetName());
        assertEquals(assetEntity.getSize(), result.getSize());
        assertEquals(assetEntity.getUsableSize(), result.getUsableSize());
    }

    @Test
    void toModel_NullCustomerInAssetEntity_ThrowsNullPointerException() {
        // given
        AssetEntity assetEntity = AssetEntity.builder()
                .id(456L)
                .customer(null)
                .assetName("Test Asset")
                .size(BigDecimal.valueOf(100.00))
                .usableSize(BigDecimal.valueOf(75.00))
                .build();

        // when and then
        assertThrows(NullPointerException.class, () -> AssetMapper.toModel(assetEntity));
    }

    @Test
    void toEntity_NullAssetModel_ThrowsNullPointerException() {
        // given
        Asset asset = null;
        CustomerEntity customerEntity = CustomerEntity.builder().id(123L).build();

        // when and then
        assertThrows(NullPointerException.class, () -> AssetMapper.toEntity(asset, customerEntity));
    }

    @Test
    void toEntity_ValidAssetWithPredefinedCustomerEntity_ReturnsValidAssetEntity() {
        // given
        Asset asset = Asset.builder()
                .id(456L)
                .assetName("Mapped Asset")
                .size(BigDecimal.TEN)
                .usableSize(BigDecimal.ONE)
                .build();

        CustomerEntity customerEntity = CustomerEntity.builder()
                .id(321L)
                .build();

        // when
        AssetEntity result = AssetMapper.toEntity(asset, customerEntity);

        // then
        assertEquals(asset.getId(), result.getId());
        assertEquals(customerEntity, result.getCustomer());
        assertEquals(asset.getAssetName(), result.getAssetName());
        assertEquals(asset.getSize(), result.getSize());
        assertEquals(asset.getUsableSize(), result.getUsableSize());
    }


    @Test
    void toEntity_ValidAssetAndCustomerEntity_ReturnsAssetEntity() {
        // given
        Asset asset = Asset.builder()
                .id(456L)
                .assetName("Test Asset")
                .size(BigDecimal.valueOf(100.00))
                .usableSize(BigDecimal.valueOf(75.00))
                .build();

        CustomerEntity customerEntity = CustomerEntity.builder().id(123L).build();

        // when
        AssetEntity result = AssetMapper.toEntity(asset, customerEntity);

        // then
        assertEquals(asset.getId(), result.getId());
        assertEquals(customerEntity, result.getCustomer());
        assertEquals(asset.getAssetName(), result.getAssetName());
        assertEquals(asset.getSize(), result.getSize());
        assertEquals(asset.getUsableSize(), result.getUsableSize());
    }

    @Test
    void toEntity_NullCustomerEntity_ReturnsAssetEntityWithNullCustomer() {
        // given
        Asset asset = Asset.builder()
                .id(456L)
                .assetName("Test Asset")
                .size(BigDecimal.valueOf(100.00))
                .usableSize(BigDecimal.valueOf(75.00))
                .build();

        // when
        AssetEntity result = AssetMapper.toEntity(asset, null);

        // then
        assertEquals(asset.getId(), result.getId());
        assertEquals(null, result.getCustomer());
        assertEquals(asset.getAssetName(), result.getAssetName());
        assertEquals(asset.getSize(), result.getSize());
        assertEquals(asset.getUsableSize(), result.getUsableSize());
    }

    @Test
    void toEntity_ValidAssetWithCustomerId_ReturnsAssetEntityWithCustomer() {
        // given
        Asset asset = Asset.builder()
                .id(456L)
                .customerId(123L)
                .assetName("Test Asset")
                .size(BigDecimal.valueOf(100.00))
                .usableSize(BigDecimal.valueOf(75.00))
                .build();

        // when
        AssetEntity result = AssetMapper.toEntity(asset);

        // then
        assertEquals(asset.getId(), result.getId());
        assertEquals(asset.getCustomerId(), result.getCustomer().getId());
        assertEquals(asset.getAssetName(), result.getAssetName());
        assertEquals(asset.getSize(), result.getSize());
        assertEquals(asset.getUsableSize(), result.getUsableSize());
    }
}