package com.brokerage.brokeragefirm.rest;

import com.brokerage.brokeragefirm.common.aspect.annotations.ValidateOwnershipAsset;
import com.brokerage.brokeragefirm.common.aspect.annotations.ValidateOwnershipCustomer;
import com.brokerage.brokeragefirm.common.mapper.AssetRequestMapper;
import com.brokerage.brokeragefirm.common.mapper.AssetResponseMapper;
import com.brokerage.brokeragefirm.common.security.CustomUserDetails;
import com.brokerage.brokeragefirm.rest.dto.AssetRequest;
import com.brokerage.brokeragefirm.rest.dto.AssetResponse;
import com.brokerage.brokeragefirm.service.AssetService;
import com.brokerage.brokeragefirm.service.model.Asset;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/assets")
public class AssetController {
    private final AssetService assetService;

    @GetMapping
    public ResponseEntity<List<AssetResponse>> getAllAssets() {
        return ResponseEntity.ok(assetService.getAllAssets().stream().map(AssetResponseMapper::toResponse).toList());
    }

    @ValidateOwnershipAsset
    @GetMapping("/{assetId}")
    public ResponseEntity<AssetResponse> getAsset(@AuthenticationPrincipal CustomUserDetails loggedUser, @PathVariable Long assetId) {
        return ResponseEntity.ok(AssetResponseMapper.toResponse(assetService.getAsset(assetId)));
    }

    @ValidateOwnershipCustomer
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<AssetResponse>> getAssetsByCustomer(@AuthenticationPrincipal CustomUserDetails loggedUser, @PathVariable Long customerId) {
        List<Asset> assets = assetService.getAssetsByCustomerId(customerId);
        return ResponseEntity.ok(assets.stream().map(AssetResponseMapper::toResponse).toList());
    }

    //TODO 400 bad request exception handling
    @PostMapping
    public ResponseEntity<AssetResponse> addAsset(@Valid @RequestBody AssetRequest assetRequest) {
        Asset asset = assetService.createAsset(AssetRequestMapper.toModel(assetRequest));
        return ResponseEntity.ok(AssetResponseMapper.toResponse(asset));
    }

    @PutMapping
    public ResponseEntity<AssetResponse> updateAsset(@Valid @RequestBody AssetRequest assetRequest) {
        Asset asset = assetService.updateAsset(AssetRequestMapper.toModel(assetRequest));
        return ResponseEntity.ok(AssetResponseMapper.toResponse(asset));
    }

}
