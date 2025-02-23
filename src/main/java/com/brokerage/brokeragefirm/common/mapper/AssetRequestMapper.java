package com.brokerage.brokeragefirm.common.mapper;

import com.brokerage.brokeragefirm.rest.dto.AssetRequest;
import com.brokerage.brokeragefirm.service.model.Asset;

public class AssetRequestMapper {

    public static Asset toModel(AssetRequest assetRequest) {
        return Asset.builder()
                .id(assetRequest.id())
                .customerId(assetRequest.customerId())
                .assetName(assetRequest.assetName())
                .size(assetRequest.size())
                .usableSize(assetRequest.usableSize())
                .build();
    }
}
