package com.nrupay.service;

import com.nrupay.model.Cart;
import com.nrupay.model.Product;
import com.nrupay.promotion.Promotion;
import com.nrupay.util.PriceList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PromotionServiceImpl implements PromotionService {
    @Override
    public Double getRawPrice(Cart cart) {
        double firstPrice = 0.0;

        for (Map.Entry<Product, Integer> entry: cart.getContents().entrySet()) {
            firstPrice = firstPrice + entry.getValue() * PriceList.getPrice(entry.getKey().getName());
        }
        return firstPrice;
    }

    @Override
    public Double getPromotedPrice(Cart cart, List<Promotion> promotions) {
        double rawPrice = getRawPrice(cart);
        return applyPromotions(cart, promotions, rawPrice);
    }

    private Double applyPromotions(Cart cart, List<Promotion> promotions, Double checkoutPrice) {
        Promotion selectedPromotion = null;
        double discountedPrice = 0.0;

        // First, get available promotions over the cart.
        List<Promotion> availablePromotions = new ArrayList<>();
        for (Promotion promotion: promotions) {
            if (promotion.isAvailable(cart)) {
                availablePromotions.add(promotion);
            }
        }

        // No available promotion on the cart, so return with the raw price and exit condition for recursion.
        if (availablePromotions.isEmpty()) {
            return checkoutPrice;
        }

        // Find the promotion to apply on cart and the discounted price with it over the cart.
        for (Promotion promotion: availablePromotions) {
            double promotionDiscountedPrice = promotion.getDiscountedPrice();
            if (promotionDiscountedPrice > discountedPrice) {
                discountedPrice = promotionDiscountedPrice;
                selectedPromotion = promotion;
            }
        }

        // Adjust the items in the cart after promotions are applied
        Cart promotedCart = new Cart();
        if (selectedPromotion != null) {
            promotedCart = selectedPromotion.applyPromotion(cart);
        }
        // Subtract the discounted price from the cart price since promotion was applied.
        checkoutPrice = checkoutPrice - discountedPrice;
        // Recursive call to apply all available promotions on cart
        // Cart contents should be properly reduced so that the recursion loop will not occur.
        return applyPromotions(promotedCart, availablePromotions, checkoutPrice);
    }
}
