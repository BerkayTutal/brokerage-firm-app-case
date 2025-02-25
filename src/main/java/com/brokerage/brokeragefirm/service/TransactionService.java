package com.brokerage.brokeragefirm.service;

import com.brokerage.brokeragefirm.service.model.Asset;
import com.brokerage.brokeragefirm.service.model.Transaction;
import jakarta.transaction.Transactional;

public interface TransactionService {
    @Transactional
    Asset deposit(Transaction request);

    @Transactional
    Asset withdraw(Transaction request);
}
