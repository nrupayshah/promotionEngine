package com.nrupay.util;

import java.util.HashMap;
import java.util.Map;

public class PriceList {
    private static Map<String, Double> products = new HashMap<>();

    static {
        products.put("A", 50.0);
        products.put("B", 30.0);
        products.put("C", 20.0);
        products.put("D", 15.0);
    }

    public static Double getPrice(String productName) {
        return products.get(productName);
    }
}
