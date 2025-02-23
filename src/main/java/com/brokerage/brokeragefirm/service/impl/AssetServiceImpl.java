package com.brokerage.brokeragefirm.service.impl;

import com.brokerage.brokeragefirm.common.exception.DuplicateEntryException;
import com.brokerage.brokeragefirm.common.enums.Error;
import com.brokerage.brokeragefirm.common.exception.NotFoundException;
import com.brokerage.brokeragefirm.common.exception.PermissionException;
import com.brokerage.brokeragefirm.common.mapper.AssetMapper;
import com.brokerage.brokeragefirm.repository.AssetRepository;
import com.brokerage.brokeragefirm.repository.CustomerRepository;
import com.brokerage.brokeragefirm.repository.entity.AssetEntity;
import com.brokerage.brokeragefirm.repository.entity.CustomerEntity;
import com.brokerage.brokeragefirm.service.AssetService;
import com.brokerage.brokeragefirm.service.model.Asset;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AssetServiceImpl implements AssetService {

    private final AssetRepository assetRepository;
    private final CustomerRepository customerRepository;

    @Override
    @Transactional
    public Asset createAsset(Asset asset) {

        //Check customer exists
        if (!customerRepository.existsById(asset.getCustomerId())) {
            throw new NotFoundException(Error.CUSTOMER_NOT_FOUND_ID, asset.getCustomerId());
        }

        //check if asset exists by customer
        if (assetRepository.existsByCustomerIdAndAssetName(asset.getCustomerId(), asset.getAssetName())) {
            throw new DuplicateEntryException(Error.ASSET_ALREADY_EXISTS, asset.getCustomerId(), asset.getAssetName());
        }

        //Save asset
        return AssetMapper.toModel(assetRepository.save(AssetMapper.toEntity(asset)));
    }

    @Transactional
    @Override
    public Asset updateAsset(Asset asset) {

        //check if asset exists by customer
        AssetEntity existingAsset = assetRepository.findByCustomerIdAndAssetName(asset.getCustomerId(), asset.getAssetName())
                .orElseThrow(() -> new NotFoundException(Error.ASSET_NOT_FOUND_ASSET_CUSTOMER, asset.getAssetName(), asset.getCustomerId()));

        AssetEntity entity = AssetMapper.toEntity(asset, existingAsset.getCustomer());
        entity.setId(existingAsset.getId());

        //Save asset
        AssetEntity savedEntity = assetRepository.save(entity);
        return AssetMapper.toModel(savedEntity);
    }


    @Override
    public List<Asset> getAllAssets() {
        return assetRepository.findAll().stream().map(AssetMapper::toModel).toList();
    }

    @Override
    public List<Asset> getAssetsByCustomerId(Long customerId) {
        if (customerRepository.existsById(customerId)) {
            return assetRepository.findAllByCustomerId(customerId).stream().map(AssetMapper::toModel).toList();
        } else {
            throw new NotFoundException(Error.CUSTOMER_NOT_FOUND_ID, customerId);
        }
    }

    @Override
    public Asset getAsset(Long assetId) {
        return assetRepository.findById(assetId).map(AssetMapper::toModel).orElseThrow(() -> new NotFoundException(Error.ASSET_NOT_FOUND_ID, assetId));
    }

}
