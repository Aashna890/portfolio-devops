package com.vit.portfolio.service;

import com.vit.portfolio.model.Alert;
import com.vit.portfolio.model.Asset;
import com.vit.portfolio.repository.AlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AlertService {

    @Autowired
    private AlertRepository alertRepository;

    @Autowired
    private AssetService assetService;

    public List<Alert> getAllAlerts() {
        return alertRepository.findAll();
    }

    public List<Alert> getTriggeredAlerts() {
        checkAndTriggerAlerts();
        return alertRepository.findByTriggeredTrue();
    }

    public Alert saveAlert(Alert alert) {
        return alertRepository.save(alert);
    }

    public void deleteAlert(Long id) {
        alertRepository.deleteById(id);
    }

    // Check every alert against current asset prices
    public void checkAndTriggerAlerts() {
        List<Alert> alerts = alertRepository.findByActiveTrue();
        for (Alert alert : alerts) {
            Asset asset = assetService.getAssetById(alert.getAsset().getId());
            boolean shouldTrigger = false;

            if ("LOSS_PERCENT".equals(alert.getAlertType())) {
                // Trigger if loss % exceeds threshold
                shouldTrigger = asset.getProfitLossPercent() <= -alert.getThreshold();
            } else if ("TARGET_PRICE".equals(alert.getAlertType())) {
                // Trigger if current price hits target
                shouldTrigger = asset.getCurrentPrice() >= alert.getThreshold();
            }

            if (shouldTrigger && !alert.isTriggered()) {
                alert.setTriggered(true);
                alertRepository.save(alert);
            }
        }
    }
}