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

    //Only Admin
    @GetMapping
    public ResponseEntity<List<AssetResponse>> getAll() {
        return ResponseEntity.ok(assetService.getAll().stream().map(AssetResponseMapper::toResponse).toList());
    }

    @ValidateOwnershipAsset
    @GetMapping("/{assetId}")
    public ResponseEntity<AssetResponse> get(@AuthenticationPrincipal CustomUserDetails loggedUser, @PathVariable Long assetId) {
        return ResponseEntity.ok(AssetResponseMapper.toResponse(assetService.get(assetId)));
    }

    @ValidateOwnershipCustomer
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<AssetResponse>> getAll(@AuthenticationPrincipal CustomUserDetails loggedUser, @PathVariable Long customerId) {
        List<Asset> assets = assetService.getAll(customerId);
        return ResponseEntity.ok(assets.stream().map(AssetResponseMapper::toResponse).toList());
    }

    //Only Admin
    @PostMapping
    public ResponseEntity<AssetResponse> add(@Valid @RequestBody AssetRequest assetRequest) {
        Asset asset = assetService.create(AssetRequestMapper.toModel(assetRequest));
        return ResponseEntity.ok(AssetResponseMapper.toResponse(asset));
    }


    //Only Admin
    @PutMapping
    public ResponseEntity<AssetResponse> update(@Valid @RequestBody AssetRequest assetRequest) {
        Asset asset = assetService.update(AssetRequestMapper.toModel(assetRequest));
        return ResponseEntity.ok(AssetResponseMapper.toResponse(asset));
    }

}
