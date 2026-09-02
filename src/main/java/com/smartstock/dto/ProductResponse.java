package com.smartstock.dto;

import java.math.BigDecimal;

public class ProductResponse {

    private Long id;
    private String name;
    private String sku;
    private BigDecimal price;
    private String description;

    public ProductResponse(Long id, String name, String sku,
                           BigDecimal price, String description) {
        this.id = id;
        this.name = name;
        this.sku = sku;
        this.price = price;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSku() {
        return sku;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }
}