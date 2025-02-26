package com.brokerage.brokeragefirm.service.impl;

import com.brokerage.brokeragefirm.common.enums.Error;
import com.brokerage.brokeragefirm.common.exception.DuplicateEntryException;
import com.brokerage.brokeragefirm.common.exception.NotFoundException;
import com.brokerage.brokeragefirm.common.mapper.AssetMapper;
import com.brokerage.brokeragefirm.repository.AssetRepository;
import com.brokerage.brokeragefirm.repository.CustomerRepository;
import com.brokerage.brokeragefirm.repository.entity.AssetEntity;
import com.brokerage.brokeragefirm.service.AssetService;
import com.brokerage.brokeragefirm.service.model.Asset;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AssetServiceImpl implements AssetService {

    private final AssetRepository assetRepository;
    private final CustomerRepository customerRepository;

    @Override
    @Transactional
    public Asset create(Asset asset) {
        log.info("Creating asset: {}", asset);
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
    public Asset update(Asset asset) {
        log.info("Updating asset: {}", asset);
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
    public Page<Asset> getAll(Pageable pageable) {
        log.info("Fetching all assets with pageable: {}", pageable);
        return assetRepository.findAll(pageable).map(AssetMapper::toModel);
    }

    @Override
    public Page<Asset> getAll(Long customerId, Pageable pageable) {
        log.info("Fetching all assets for customer ID: {} with pageable: {}", customerId, pageable);
        if (customerRepository.existsById(customerId)) {
            return assetRepository.findAllByCustomerId(customerId, pageable).map(AssetMapper::toModel);
        } else {
            throw new NotFoundException(Error.CUSTOMER_NOT_FOUND_ID, customerId);
        }
    }


    @Override
    public Asset get(Long assetId) {
        log.info("Fetching asset with ID: {}", assetId);
        return assetRepository.findById(assetId).map(AssetMapper::toModel).orElseThrow(() -> new NotFoundException(Error.ASSET_NOT_FOUND_ID, assetId));
    }

    @Override
    public Asset get(Long customerId, String assetName) {
        log.info("Fetching asset with name: {} for customer ID: {}", assetName, customerId);
        return assetRepository.findByCustomerIdAndAssetName(customerId, assetName)
                .map(AssetMapper::toModel)
                .orElseThrow(() -> new NotFoundException(Error.ASSET_NOT_FOUND_ASSET_CUSTOMER, assetName, customerId));
    }

    @Override
    public boolean exists(Long customerId, String assetName) {
        log.info("Checking if asset with name: {} exists for customer ID: {}", assetName, customerId);
        return assetRepository.existsByCustomerIdAndAssetName(customerId, assetName);
    }
}
