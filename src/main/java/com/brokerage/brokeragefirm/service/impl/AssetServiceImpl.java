package com.brokerage.brokeragefirm.service.impl;

import com.brokerage.brokeragefirm.common.exception.AssetAlreadyExistsException;
import com.brokerage.brokeragefirm.common.exception.AssetNotFoundException;
import com.brokerage.brokeragefirm.common.exception.CustomerNotFoundException;
import com.brokerage.brokeragefirm.common.exception.InsufficientFundsException;
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
    public void updateUsableSizeForBuyOrder(Long customerId, BigDecimal requiredTry) {
        final String assetName = "TRY";
        AssetEntity tryAssetEntity = assetRepository
                .findByCustomerIdAndAssetName(customerId, assetName)
                .orElseThrow(() -> new InsufficientFundsException(String.format("%s asset not found for userId %s", assetName, customerId)));

        Asset tryAsset = AssetMapper.toModel(tryAssetEntity);

        if (tryAsset.getUsableSize().compareTo(requiredTry) < 0) {
            throw new InsufficientFundsException(String.format("Insufficient %s for userId %s", assetName, customerId));
        }

        tryAsset.setUsableSize(tryAsset.getUsableSize().subtract(requiredTry));

        // Reuse existing CustomerEntity to avoid extra DB call
        CustomerEntity customer = tryAssetEntity.getCustomer();
        AssetEntity updatedEntity = AssetMapper.toEntity(tryAsset, customer);
        assetRepository.save(updatedEntity);
    }

    @Override
    @Transactional
    public Asset createAsset(Asset asset) {
        CustomerEntity customer = customerRepository.findById(asset.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException(asset.getCustomerId()));

        assetRepository.findByCustomerIdAndAssetName(customer.getId(), asset.getAssetName()).ifPresent(existingAsset -> {
            throw new AssetAlreadyExistsException(asset.getAssetName(), existingAsset.getId());
        });

        AssetEntity savedEntity = assetRepository.save(AssetMapper.toEntity(asset, customer));
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
            throw new CustomerNotFoundException(customerId);
        }
    }

    @Override
    public Asset getAsset(Long assetId) {
        return assetRepository.findById(assetId).map(AssetMapper::toModel).orElseThrow(() -> new AssetNotFoundException(assetId));
    }

}
