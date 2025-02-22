package com.brokerage.brokeragefirm.common.mapper;

import com.brokerage.brokeragefirm.rest.dto.AssetResponse;
import com.brokerage.brokeragefirm.service.model.Asset;

public class AssetResponseMapper {

    public static AssetResponse toResponse(Asset asset) {
        return new AssetResponse(
                asset.getId(),
                asset.getCustomerId(),
                asset.getAssetName(),
                asset.getSize(),
                asset.getUsableSize()
        );
    }
}
