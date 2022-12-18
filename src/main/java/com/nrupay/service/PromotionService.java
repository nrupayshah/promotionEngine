package com.nrupay.service;

import com.nrupay.model.Cart;
import com.nrupay.promotion.Promotion;

import java.util.List;

/**
 * Promotion Service Interface
 */
public interface PromotionService {
    /**
     * Returns the checkout total before applying the promotions.
     * @param cart cart
     * @return value
     */
    Double getRawPrice(Cart cart);

    /**
     * Returns the price after promotions are applied.
     * @param cart cart
     * @param promotions list of promotions
     * @return price
     */
    Double getPromotedPrice(Cart cart, List<Promotion> promotions);
}
