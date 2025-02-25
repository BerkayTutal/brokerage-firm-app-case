package com.brokerage.brokeragefirm.service;

import com.brokerage.brokeragefirm.common.exception.DuplicateEntryException;
import com.brokerage.brokeragefirm.common.exception.NotFoundException;
import com.brokerage.brokeragefirm.repository.AssetRepository;
import com.brokerage.brokeragefirm.repository.CustomerRepository;
import com.brokerage.brokeragefirm.repository.entity.AssetEntity;
import com.brokerage.brokeragefirm.repository.entity.CustomerEntity;
import com.brokerage.brokeragefirm.service.impl.AssetServiceImpl;
import com.brokerage.brokeragefirm.service.model.Asset;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssetServiceTest {

    @Mock
    private AssetRepository assetRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private AssetServiceImpl assetServiceImpl;

    //create
    @Test
    void create_ValidAsset_ReturnsAsset() {
        // given
        Asset asset = Asset.builder()
                .size(BigDecimal.TEN)
                .usableSize(BigDecimal.TEN)
                .assetName("Asset 1")
                .customerId(1L)
                .build();

        AssetEntity assetEntity = new AssetEntity();
        assetEntity.setCustomer(CustomerEntity.builder().id(1L).build());
        assetEntity.setAssetName("Asset 1");

        when(customerRepository.existsById(asset.getCustomerId())).thenReturn(true);
        when(assetRepository.existsByCustomerIdAndAssetName(asset.getCustomerId(), asset.getAssetName())).thenReturn(false);
        when(assetRepository.save(any(AssetEntity.class))).thenReturn(assetEntity);

        // when
        Asset result = assetServiceImpl.create(asset);

        // then
        verify(customerRepository, times(1)).existsById(asset.getCustomerId());
        verify(assetRepository, times(1)).existsByCustomerIdAndAssetName(asset.getCustomerId(), asset.getAssetName());
        verify(assetRepository, times(1)).save(any(AssetEntity.class));
    }

    @Test
    void create_CustomerNotFound_ThrowsNotFoundException() {
        // given
        Asset asset = Asset.builder()
                .size(BigDecimal.TEN)
                .usableSize(BigDecimal.TEN)
                .assetName("Asset 1")
                .customerId(1L)
                .build();

        when(customerRepository.existsById(asset.getCustomerId())).thenReturn(false);

        // when & then
        assertThrows(NotFoundException.class, () -> assetServiceImpl.create(asset));
        verify(customerRepository, times(1)).existsById(asset.getCustomerId());
        verify(assetRepository, never()).existsByCustomerIdAndAssetName(anyLong(), anyString());
        verify(assetRepository, never()).save(any(AssetEntity.class));
    }

    @Test
    void create_AssetAlreadyExists_ThrowsDuplicateEntryException() {
        // given
        Asset asset = Asset.builder()
                .size(BigDecimal.TEN)
                .usableSize(BigDecimal.TEN)
                .assetName("Asset 1")
                .customerId(1L)
                .build();

        when(customerRepository.existsById(asset.getCustomerId())).thenReturn(true);
        when(assetRepository.existsByCustomerIdAndAssetName(asset.getCustomerId(), asset.getAssetName())).thenReturn(true);

        // when & then
        assertThrows(DuplicateEntryException.class, () -> assetServiceImpl.create(asset));
        verify(customerRepository, times(1)).existsById(asset.getCustomerId());
        verify(assetRepository, times(1)).existsByCustomerIdAndAssetName(asset.getCustomerId(), asset.getAssetName());
        verify(assetRepository, never()).save(any(AssetEntity.class));
    }

    //update
    @Test
    void update_ValidAsset_ReturnsUpdatedAsset() {
        // given
        Asset asset = Asset.builder()
                .size(BigDecimal.TEN)
                .usableSize(BigDecimal.ONE)
                .assetName("Asset 1")
                .customerId(1L)
                .build();

        AssetEntity existingAssetEntity = new AssetEntity();
        existingAssetEntity.setId(1L);
        existingAssetEntity.setCustomer(CustomerEntity.builder().id(1L).build());
        existingAssetEntity.setAssetName("Asset 1");

        AssetEntity updatedAssetEntity = new AssetEntity();
        updatedAssetEntity.setId(1L);
        updatedAssetEntity.setCustomer(existingAssetEntity.getCustomer());
        updatedAssetEntity.setAssetName("Asset 1");
        updatedAssetEntity.setSize(BigDecimal.TEN);
        updatedAssetEntity.setUsableSize(BigDecimal.ONE);

        when(assetRepository.findByCustomerIdAndAssetName(asset.getCustomerId(), asset.getAssetName()))
                .thenReturn(Optional.of(existingAssetEntity));
        when(assetRepository.save(any(AssetEntity.class))).thenReturn(updatedAssetEntity);

        // when
        Asset result = assetServiceImpl.update(asset);

        // then
        verify(assetRepository, times(1)).findByCustomerIdAndAssetName(asset.getCustomerId(), asset.getAssetName());
        verify(assetRepository, times(1)).save(any(AssetEntity.class));
    }

    @Test
    void update_AssetNotFound_ThrowsNotFoundException() {
        // given
        Asset asset = Asset.builder()
                .size(BigDecimal.TEN)
                .usableSize(BigDecimal.ONE)
                .assetName("Non-existent Asset")
                .customerId(1L)
                .build();

        when(assetRepository.findByCustomerIdAndAssetName(asset.getCustomerId(), asset.getAssetName()))
                .thenReturn(Optional.empty());

        // when & then
        assertThrows(NotFoundException.class, () -> assetServiceImpl.update(asset));
        verify(assetRepository, times(1)).findByCustomerIdAndAssetName(asset.getCustomerId(), asset.getAssetName());
        verify(assetRepository, never()).save(any(AssetEntity.class));
    }

    //getAll
    @Test
    void testGetAll_ReturnsAllAssets() {
        // Given
        Pageable pageable = PageRequest.of(0, 5); // First page, 5 items per page
        List<AssetEntity> assetEntities = List.of(
                new AssetEntity(), new AssetEntity(), new AssetEntity()
        );
        Page<AssetEntity> pagedResult = new PageImpl<>(assetEntities, pageable, assetEntities.size());

        when(assetRepository.findAll(pageable)).thenReturn(pagedResult);

        // When
        Page<Asset> result = assetServiceImpl.getAll(pageable);

        // Then
        assertThat(result.getContent()).hasSize(3);
        assertThat(result.getTotalElements()).isEqualTo(3);
        assertThat(result.getTotalPages()).isEqualTo(1);
        verify(assetRepository, times(1)).findAll(pageable);
    }


    @Test
    void testGetAll_NoAssets_ReturnsEmptyPage() {
        // Given
        Pageable pageable = PageRequest.of(0, 10); // First page, 10 items per page
        Page<AssetEntity> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(assetRepository.findAll(pageable)).thenReturn(emptyPage);

        // When
        Page<Asset> result = assetServiceImpl.getAll(pageable);

        // Then
        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isZero();
        assertThat(result.getTotalPages()).isZero();
        verify(assetRepository, times(1)).findAll(pageable);
    }


    @Test
    void testGetAllByCustomerId_ValidCustomerId_ReturnsAssets() {
        // Given
        Long customerId = 1L;
        Pageable pageable = PageRequest.of(0, 5); // First page, 5 items per page
        List<AssetEntity> assetEntities = List.of(
                new AssetEntity(), new AssetEntity()
        );
        Page<AssetEntity> pagedResult = new PageImpl<>(assetEntities, pageable, assetEntities.size());

        when(customerRepository.existsById(customerId)).thenReturn(true);
        when(assetRepository.findAllByCustomerId(customerId, pageable)).thenReturn(pagedResult);

        // When
        Page<Asset> result = assetServiceImpl.getAll(customerId, pageable);

        // Then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getTotalPages()).isEqualTo(1);
        verify(customerRepository, times(1)).existsById(customerId);
        verify(assetRepository, times(1)).findAllByCustomerId(customerId, pageable);
    }


    @Test
    void getAllByCustomerId_NoAssets_WithPagination_ReturnsEmptyPage
            () {
        // Given
        Long customerId = 1L;
        Pageable pageable = PageRequest.of(0, 5); // First page, 5 items per page
        Page<AssetEntity> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(customerRepository.existsById(customerId)).thenReturn(true);
        when(assetRepository.findAllByCustomerId(customerId, pageable)).thenReturn(emptyPage);

        // When
        Page<Asset> result = assetServiceImpl.getAll(customerId, pageable);

        // Then
        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isZero();
        assertThat(result.getTotalPages()).isZero();
        verify(customerRepository, times(1)).existsById(customerId);
        verify(assetRepository, times(1)).findAllByCustomerId(customerId, pageable);
    }


    //get(Long assetId)
    @Test
    void testGet_ValidAssetId_ReturnsAsset() {
        // given
        Long assetId = 1L;
        AssetEntity assetEntity = AssetEntity.builder().id(assetId).assetName("Asset 1").customer(CustomerEntity.builder().id(1L).build()).build();

        when(assetRepository.findById(assetId)).thenReturn(Optional.of(assetEntity));

        // when
        Asset result = assetServiceImpl.get(assetId);

        // then
        verify(assetRepository, times(1)).findById(assetId);
        assertNotNull(result);
        assertEquals("Asset 1", result.getAssetName());
    }

    @Test
    void testGet_InvalidAssetId_ThrowsNotFoundException() {
        // given
        Long assetId = 1L;

        when(assetRepository.findById(assetId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(NotFoundException.class, () -> assetServiceImpl.get(assetId));
        verify(assetRepository, times(1)).findById(assetId);
    }


    //get(Long assetId)
    @Test
    void testGet_ValidCustomerIdAssetName_ReturnsAsset() {
        // given
        Long assetId = 1L;
        Long customerId = 1L;
        String assetName = "Asset 1";

        AssetEntity assetEntity = AssetEntity.builder().id(assetId).assetName("Asset 1").customer(CustomerEntity.builder().id(1L).build()).build();

        when(assetRepository.findByCustomerIdAndAssetName(customerId, assetName)).thenReturn(Optional.of(assetEntity));

        // when
        Asset result = assetServiceImpl.get(customerId, assetName);

        // then
        verify(assetRepository, times(1)).findByCustomerIdAndAssetName(customerId, assetName);
        assertNotNull(result);
        assertEquals("Asset 1", result.getAssetName());
    }

    @Test
    void testGet_InvalidCustomerIdAssetName_ThrowsNotFoundException() {
        // given
        Long assetId = 1L;
        Long customerId = 1L;
        String assetName = "Asset 1";

        when(assetRepository.findByCustomerIdAndAssetName(customerId, assetName)).thenReturn(Optional.empty());

        // when & then
        assertThrows(NotFoundException.class, () -> assetServiceImpl.get(customerId, assetName));
        verify(assetRepository, times(1)).findByCustomerIdAndAssetName(customerId, assetName);
    }

    //exists
    @Test
    void testExists_AssetExists_ReturnsTrue() {
        // given
        Long customerId = 1L;
        String assetName = "Asset 1";

        when(assetRepository.existsByCustomerIdAndAssetName(customerId, assetName)).thenReturn(true);

        // when
        boolean result = assetServiceImpl.exists(customerId, assetName);

        // then
        verify(assetRepository, times(1)).existsByCustomerIdAndAssetName(customerId, assetName);
        assertTrue(result);
    }

    @Test
    void testExists_AssetDoesNotExist_ReturnsFalse() {
        // given
        Long customerId = 1L;
        String assetName = "Asset 1";

        when(assetRepository.existsByCustomerIdAndAssetName(customerId, assetName)).thenReturn(false);

        // when
        boolean result = assetServiceImpl.exists(customerId, assetName);

        // then
        verify(assetRepository, times(1)).existsByCustomerIdAndAssetName(customerId, assetName);
        assertFalse(result);
    }
}