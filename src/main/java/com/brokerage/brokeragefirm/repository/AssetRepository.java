package com.brokerage.brokeragefirm.repository;

import com.brokerage.brokeragefirm.repository.entity.AssetEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AssetRepository extends JpaRepository<AssetEntity, Long> {
    Optional<AssetEntity> findByCustomerIdAndAssetName(Long customerId, String assetName);

    Page<AssetEntity> findAllByCustomerId(Long customerId, Pageable pageable);

    boolean existsByCustomerIdAndAssetName(Long customerId, String assetName);
}
