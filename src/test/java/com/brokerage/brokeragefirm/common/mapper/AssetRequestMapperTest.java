package com.brokerage.brokeragefirm.common.mapper;

import com.brokerage.brokeragefirm.rest.dto.AssetRequest;
import com.brokerage.brokeragefirm.service.model.Asset;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class AssetRequestMapperTest {

    @Test
    void toModel_ValidAssetRequest_MapsCorrectly() {
        // Given
        AssetRequest assetRequest = new AssetRequest(
                1L,
                100L,
                "AssetName",
                new BigDecimal("500.0"),
                new BigDecimal("300.0")
        );

        // When
        Asset result = AssetRequestMapper.toModel(assetRequest);

        // Then
        assertEquals(1L, result.getId());
        assertEquals(100L, result.getCustomerId());
        assertEquals("AssetName", result.getAssetName());
        assertEquals(new BigDecimal("500.0"), result.getSize());
        assertEquals(new BigDecimal("300.0"), result.getUsableSize());
    }

    @Test
    void toModel_NullFields_MapsToNullFieldsInAsset() {
        // Given
        AssetRequest assetRequest = new AssetRequest(
                null,
                null,
                null,
                null,
                null
        );

        // When
        Asset result = AssetRequestMapper.toModel(assetRequest);

        // Then
        assertEquals(null, result.getId());
        assertEquals(null, result.getCustomerId());
        assertEquals(null, result.getAssetName());
        assertEquals(null, result.getSize());
        assertEquals(null, result.getUsableSize());
    }

    @Test
    void toModel_ZeroValues_MapsCorrectly() {
        // Given
        AssetRequest assetRequest = new AssetRequest(
                2L,
                200L,
                "AnotherAsset",
                BigDecimal.ZERO,
                BigDecimal.ZERO
        );

        // When
        Asset result = AssetRequestMapper.toModel(assetRequest);

        // Then
        assertEquals(2L, result.getId());
        assertEquals(200L, result.getCustomerId());
        assertEquals("AnotherAsset", result.getAssetName());
        assertEquals(BigDecimal.ZERO, result.getSize());
        assertEquals(BigDecimal.ZERO, result.getUsableSize());
    }
}