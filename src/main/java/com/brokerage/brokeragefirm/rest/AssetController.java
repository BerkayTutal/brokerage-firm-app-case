package com.brokerage.brokeragefirm.rest;

import com.brokerage.brokeragefirm.common.mapper.AssetResponseMapper;
import com.brokerage.brokeragefirm.rest.dto.AssetResponse;
import com.brokerage.brokeragefirm.service.AssetService;
import com.brokerage.brokeragefirm.service.model.Asset;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/{assetId}")
    public ResponseEntity<AssetResponse> getAsset(@PathVariable Long assetId) {
        return ResponseEntity.ok(AssetResponseMapper.toResponse(assetService.getAsset(assetId)));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<AssetResponse>> getAssetsByCustomer(@PathVariable Long customerId) {
        List<Asset> assets = assetService.getAssetsByCustomerId(customerId);
        return ResponseEntity.ok(assets.stream().map(AssetResponseMapper::toResponse).toList());
    }

    @PostMapping
    public ResponseEntity<AssetResponse> addAsset(@RequestBody Asset asset) {
        return ResponseEntity.ok(AssetResponseMapper.toResponse(assetService.createAsset(asset)));
    }

}
