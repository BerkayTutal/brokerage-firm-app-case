package com.brokerage.brokeragefirm.service;

import com.brokerage.brokeragefirm.common.constants.Constants;
import com.brokerage.brokeragefirm.common.enums.Error;
import com.brokerage.brokeragefirm.common.exception.OperationNotAllowedException;
import com.brokerage.brokeragefirm.service.impl.TransactionServiceImpl;
import com.brokerage.brokeragefirm.service.model.Asset;
import com.brokerage.brokeragefirm.service.model.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private AssetService assetService;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    void deposit_ValidTransaction_UpdatesAssetCorrectly() {
        // given
        Long customerId = 1L;
        BigDecimal depositAmount = BigDecimal.valueOf(100);
        Asset initialAsset = Asset.builder()
                .size(BigDecimal.valueOf(500))
                .usableSize(BigDecimal.valueOf(450))
                .build();
        Asset updatedAsset = Asset.builder()
                .size(initialAsset.getSize().add(depositAmount))
                .usableSize(initialAsset.getUsableSize().add(depositAmount))
                .build();
        Transaction transaction = new Transaction(customerId, Constants.ASSET_TRY, depositAmount);

        given(assetService.get(eq(customerId), eq(Constants.ASSET_TRY))).willReturn(initialAsset);
        given(assetService.update(any(Asset.class))).willReturn(updatedAsset);

        // when
        Asset result = transactionService.deposit(transaction);

        // then
        assertThat(result.getSize()).isEqualByComparingTo(BigDecimal.valueOf(600));
        assertThat(result.getUsableSize()).isEqualByComparingTo(BigDecimal.valueOf(550));
        verify(assetService).get(eq(customerId), eq(Constants.ASSET_TRY));
        verify(assetService).update(any(Asset.class));
        verifyNoMoreInteractions(assetService);
    }

    @Test
    void withdraw_ValidTransaction_UpdatesAssetCorrectly() {
        // given
        Long customerId = 1L;
        BigDecimal withdrawAmount = BigDecimal.valueOf(50);
        Asset initialAsset = Asset.builder()
                .size(BigDecimal.valueOf(500))
                .usableSize(BigDecimal.valueOf(450))
                .build();
        Asset updatedAsset = Asset.builder()
                .size(initialAsset.getSize().subtract(withdrawAmount))
                .usableSize(initialAsset.getUsableSize().subtract(withdrawAmount))
                .build();
        Transaction transaction = new Transaction(customerId, Constants.ASSET_TRY, withdrawAmount);

        given(assetService.get(eq(customerId), eq(Constants.ASSET_TRY))).willReturn(initialAsset);
        given(assetService.update(any(Asset.class))).willReturn(updatedAsset);

        // when
        Asset result = transactionService.withdraw(transaction);

        // then
        assertThat(result.getSize()).isEqualByComparingTo(BigDecimal.valueOf(450));
        assertThat(result.getUsableSize()).isEqualByComparingTo(BigDecimal.valueOf(400));
        verify(assetService).get(eq(customerId), eq(Constants.ASSET_TRY));
        verify(assetService).update(any(Asset.class));
        verifyNoMoreInteractions(assetService);
    }

    @Test
    void withdraw_InsufficientFunds_ThrowsException() {
        // given
        Long customerId = 1L;
        BigDecimal withdrawAmount = BigDecimal.valueOf(500);
        Asset initialAsset = Asset.builder()
                .size(BigDecimal.valueOf(500))
                .usableSize(BigDecimal.valueOf(450))
                .build();
        Transaction transaction = new Transaction(customerId, Constants.ASSET_TRY, withdrawAmount);

        given(assetService.get(eq(customerId), eq(Constants.ASSET_TRY))).willReturn(initialAsset);

        // when - then
        OperationNotAllowedException exception = org.junit.jupiter.api.Assertions.assertThrows(
                OperationNotAllowedException.class,
                () -> transactionService.withdraw(transaction)
        );

        // then
        assertEquals(new OperationNotAllowedException(Error.INSUFFICIENT_FUNDS, Constants.ASSET_TRY, customerId).getMessage(), exception.getMessage());
        verify(assetService).get(eq(customerId), eq(Constants.ASSET_TRY));
        verifyNoMoreInteractions(assetService);
    }
}