package com.brokerage.brokeragefirm.repository;

import com.brokerage.brokeragefirm.repository.entity.AssetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AssetRepository extends JpaRepository<AssetEntity, Long> {
    Optional<AssetEntity> findByCustomerIdAndAssetName(Long customerId, String assetName);

}
