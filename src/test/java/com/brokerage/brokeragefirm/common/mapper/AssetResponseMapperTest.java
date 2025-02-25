package com.brokerage.brokeragefirm.common.mapper;

import com.brokerage.brokeragefirm.rest.dto.AssetResponse;
import com.brokerage.brokeragefirm.service.model.Asset;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class AssetResponseMapperTest {

    @Test
    void toResponse_MapsAllFieldsCorrectly() {
        // given
        Asset asset = Asset.builder()
                .id(1L)
                .customerId(100L)
                .assetName("Real Estate")
                .size(new BigDecimal("12345.67"))
                .usableSize(new BigDecimal("1234.56"))
                .build();

        // when
        AssetResponse response = AssetResponseMapper.toResponse(asset);

        // then
        assertEquals(asset.getId(), response.id());
        assertEquals(asset.getCustomerId(), response.customerId());
        assertEquals(asset.getAssetName(), response.assetName());
        assertEquals(asset.getSize(), response.size());
        assertEquals(asset.getUsableSize(), response.usableSize());
    }

    @Test
    void toResponse_MapsDefaultValuesWhenFieldsAreNullOrDefault() {
        // given
        Asset asset = Asset.builder().build(); 

        // when
        AssetResponse response = AssetResponseMapper.toResponse(asset);

        // then
        assertEquals(asset.getId(), response.id());
        assertEquals(asset.getCustomerId(), response.customerId());
        assertEquals(asset.getAssetName(), response.assetName());
        assertEquals(asset.getSize(), response.size());
        assertEquals(asset.getUsableSize(), response.usableSize());
    }
}