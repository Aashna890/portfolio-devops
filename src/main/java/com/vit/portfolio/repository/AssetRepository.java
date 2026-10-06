package com.vit.portfolio.repository;

import com.vit.portfolio.model.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface AssetRepository extends JpaRepository<Asset, Long> {

    @Query("SELECT a FROM Asset a WHERE " +
           "LOWER(a.ticker) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(a.name) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Asset> searchAssets(@Param("search") String search);

    List<Asset> findByType(String type);
}