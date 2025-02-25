package com.brokerage.brokeragefirm.service;

import com.brokerage.brokeragefirm.service.model.Asset;
import jakarta.transaction.Transactional;

import java.util.List;

public interface AssetService {
    @Transactional
    Asset create(Asset asset);

    @Transactional
    Asset update(Asset asset);

    List<Asset> getAll();

    List<Asset> getAll(Long customerId);

    Asset get(Long assetId);

    Asset get(Long customerId, String assetName);

    boolean exists(Long customerId, String assetName);
}
