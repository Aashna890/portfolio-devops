package com.vit.portfolio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity
@Table(name = "assets")
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Ticker is required")
    private String ticker;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Type is required")
    private String type; // STOCK, MUTUAL_FUND, BOND

    @NotNull
    @Positive(message = "Quantity must be positive")
    private Double quantity;

    @NotNull
    @Positive(message = "Buy price must be positive")
    private Double buyPrice;

    private LocalDate buyDate;

    // Mock current price (in real app this comes from API)
    private Double currentPrice;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTicker() { return ticker; }
    public void setTicker(String ticker) { this.ticker = ticker.toUpperCase(); }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Double getQuantity() { return quantity; }
    public void setQuantity(Double quantity) { this.quantity = quantity; }

    public Double getBuyPrice() { return buyPrice; }
    public void setBuyPrice(Double buyPrice) { this.buyPrice = buyPrice; }

    public LocalDate getBuyDate() { return buyDate; }
    public void setBuyDate(LocalDate buyDate) { this.buyDate = buyDate; }

    public Double getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(Double currentPrice) { this.currentPrice = currentPrice; }

    // Calculated fields
    public Double getInvestedAmount() {
        return quantity * buyPrice;
    }

    public Double getCurrentValue() {
        if (currentPrice == null) return getInvestedAmount();
        return quantity * currentPrice;
    }

    public Double getProfitLoss() {
        return getCurrentValue() - getInvestedAmount();
    }

    public Double getProfitLossPercent() {
        if (getInvestedAmount() == 0) return 0.0;
        return (getProfitLoss() / getInvestedAmount()) * 100;
    }
}