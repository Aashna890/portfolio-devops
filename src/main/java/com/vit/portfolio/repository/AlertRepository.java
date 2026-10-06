package com.vit.portfolio.repository;

import com.vit.portfolio.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findByAssetId(Long assetId);
    List<Alert> findByTriggeredTrue();
    List<Alert> findByActiveTrue();
}