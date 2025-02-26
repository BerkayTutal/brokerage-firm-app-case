package com.brokerage.brokeragefirm.service.impl;

import com.brokerage.brokeragefirm.common.constants.Constants;
import com.brokerage.brokeragefirm.common.enums.Error;
import com.brokerage.brokeragefirm.common.exception.OperationNotAllowedException;
import com.brokerage.brokeragefirm.service.AssetService;
import com.brokerage.brokeragefirm.service.CustomerService;
import com.brokerage.brokeragefirm.service.TransactionService;
import com.brokerage.brokeragefirm.service.model.Asset;
import com.brokerage.brokeragefirm.service.model.Transaction;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final CustomerService customerService;
    private final AssetService assetService;

    @Transactional
    @Override
    public Asset deposit(Transaction request) {
        log.info("Depositing {} amount of {} to customer : {}", request.amount(), request.assetName(), request.customerId());
        Asset asset = assetService.get(request.customerId(), Constants.ASSET_TRY);
        asset.setSize(asset.getSize().add(request.amount()));
        asset.setUsableSize(asset.getUsableSize().add(request.amount()));
        return assetService.update(asset);
    }

    @Transactional
    @Override
    public Asset withdraw(Transaction request) {
        log.info("Withdrawing {} amount of {} from customer : {}", request.amount(), request.assetName(), request.customerId());
        Asset asset = assetService.get(request.customerId(), Constants.ASSET_TRY);
        if (asset.getUsableSize().subtract(request.amount()).compareTo(BigDecimal.ZERO) < 0) {
            throw new OperationNotAllowedException(Error.INSUFFICIENT_FUNDS, Constants.ASSET_TRY, request.customerId());
        }
        asset.setSize(asset.getSize().subtract(request.amount()));
        asset.setUsableSize(asset.getUsableSize().subtract(request.amount()));
        return assetService.update(asset);
    }
}