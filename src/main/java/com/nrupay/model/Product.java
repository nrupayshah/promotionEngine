package com.nrupay.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class Product {
    private String name;
    private double price;

    public Product(String name) {
        this.name = name;
    }
}
