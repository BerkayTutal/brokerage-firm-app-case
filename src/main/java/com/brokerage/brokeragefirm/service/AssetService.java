package com.brokerage.brokeragefirm.service;

import com.brokerage.brokeragefirm.service.model.Asset;
import jakarta.transaction.Transactional;

import java.util.List;

public interface AssetService {
    @Transactional
    Asset createAsset(Asset asset);

    @Transactional
    Asset updateAsset(Asset asset);

    List<Asset> getAllAssets();

    List<Asset> getAssetsByCustomerId(Long customerId);

    Asset getAsset(Long assetId);

    Asset getAsset(Long customerId, String assetName);

    boolean exists(Long customerId, String assetName);
}
