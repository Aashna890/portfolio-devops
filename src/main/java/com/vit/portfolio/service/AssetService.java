package com.vit.portfolio.service;

import com.vit.portfolio.model.Asset;
import com.vit.portfolio.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class AssetService {

    @Autowired
    private AssetRepository assetRepository;

    // Mock prices — simulates a price feed
    private static final Map<String, Double> MOCK_PRICES = new HashMap<>();
    static {
        MOCK_PRICES.put("RELIANCE",  2850.0);
        MOCK_PRICES.put("TCS",       3920.0);
        MOCK_PRICES.put("INFY",      1756.0);
        MOCK_PRICES.put("HDFC",      1623.0);
        MOCK_PRICES.put("WIPRO",      498.0);
        MOCK_PRICES.put("SBIN",       812.0);
        MOCK_PRICES.put("ITC",        465.0);
        MOCK_PRICES.put("LT",        3540.0);
        MOCK_PRICES.put("BAJFINANCE",6890.0);
        MOCK_PRICES.put("HCLTECH",  1342.0);
    }

    public List<Asset> getAllAssets() {
        List<Asset> assets = assetRepository.findAll();
        assets.forEach(this::applyMockPrice);
        return assets;
    }

    public List<Asset> searchAssets(String query) {
        List<Asset> assets = query == null || query.isBlank()
                ? assetRepository.findAll()
                : assetRepository.searchAssets(query);
        assets.forEach(this::applyMockPrice);
        return assets;
    }

    public Asset getAssetById(Long id) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asset not found: " + id));
        applyMockPrice(asset);
        return asset;
    }

    public Asset saveAsset(Asset asset) {
        applyMockPrice(asset);
        return assetRepository.save(asset);
    }

    public Asset updateAsset(Long id, Asset updated) {
        Asset existing = getAssetById(id);
        existing.setTicker(updated.getTicker());
        existing.setName(updated.getName());
        existing.setType(updated.getType());
        existing.setQuantity(updated.getQuantity());
        existing.setBuyPrice(updated.getBuyPrice());
        existing.setBuyDate(updated.getBuyDate());
        applyMockPrice(existing);
        return assetRepository.save(existing);
    }

    public void deleteAsset(Long id) {
        assetRepository.deleteById(id);
    }

    // Summary KPIs
    public Map<String, Double> getSummary() {
        List<Asset> assets = getAllAssets();
        double totalInvested = assets.stream()
                .mapToDouble(Asset::getInvestedAmount).sum();
        double currentValue = assets.stream()
                .mapToDouble(Asset::getCurrentValue).sum();
        double profitLoss = currentValue - totalInvested;
        double plPercent = totalInvested == 0 ? 0
                : (profitLoss / totalInvested) * 100;

        Map<String, Double> summary = new LinkedHashMap<>();
        summary.put("totalInvested", Math.round(totalInvested * 100.0) / 100.0);
        summary.put("currentValue",  Math.round(currentValue  * 100.0) / 100.0);
        summary.put("profitLoss",    Math.round(profitLoss    * 100.0) / 100.0);
        summary.put("plPercent",     Math.round(plPercent     * 100.0) / 100.0);
        summary.put("assetCount",    (double) assets.size());
        return summary;
    }

    // Apply mock price — uses ticker if known, else adds 10% to buy price
    private void applyMockPrice(Asset asset) {
        Double price = MOCK_PRICES.get(asset.getTicker().toUpperCase());
        asset.setCurrentPrice(price != null ? price : asset.getBuyPrice() * 1.10);
    }
}