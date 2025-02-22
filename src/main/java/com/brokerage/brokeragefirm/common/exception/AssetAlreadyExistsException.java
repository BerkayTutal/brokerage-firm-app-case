package com.brokerage.brokeragefirm.common.exception;

public class AssetAlreadyExistsException extends RuntimeException {

    public AssetAlreadyExistsException(String assetName, Long existingAssetId) {
        super(String.format("Asset with name %s already exists with id %s", assetName, existingAssetId));
    }
}
