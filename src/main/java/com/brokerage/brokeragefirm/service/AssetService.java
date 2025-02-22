package com.brokerage.brokeragefirm.service;

import com.brokerage.brokeragefirm.service.model.Asset;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;

public interface AssetService {
    @Transactional
    Asset createAsset(Asset asset);

    List<Asset> getAllAssets();
    List<Asset> getAssetsByCustomerId(Long customerId);
    void updateUsableSizeForBuyOrder(Long customerId, BigDecimal requiredTry);

    Asset getAsset(Long assetId);
}
