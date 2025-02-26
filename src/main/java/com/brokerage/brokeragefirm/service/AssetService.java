package com.brokerage.brokeragefirm.service;

import com.brokerage.brokeragefirm.service.model.Asset;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AssetService {
    Asset create(Asset asset);

    Asset update(Asset asset);

    Page<Asset> getAll(Pageable pageable);

    Page<Asset> getAll(Long customerId, Pageable pageable);

    Asset get(Long assetId);

    Asset get(Long customerId, String assetName);

    boolean exists(Long customerId, String assetName);
}
