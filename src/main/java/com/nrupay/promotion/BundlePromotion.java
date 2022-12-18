package com.nrupay.promotion;

import com.nrupay.model.Cart;
import com.nrupay.model.Product;
import com.nrupay.util.PriceList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BundlePromotion implements Promotion {
    private final List<String> appliedItems = new ArrayList<>();
    private final Double promotedPrice;
    private final Map<String, Boolean> availabilityCheckMap = new HashMap<>();

    public BundlePromotion(List<String> items, Double promotedPrice) {
        this.appliedItems.addAll(items);
        this.promotedPrice = promotedPrice;
    }

    /**
     * Applies the bundle promotion to a cart and adjusts the cart contents once applied
     * @param cart cart
     * @return cart
     */
    @Override
    public Cart applyPromotion(Cart cart) {
        if(!isAvailable(cart)) {
            System.out.println("There is no available item in this cart.");
        }

        Cart promotedCart = new Cart(cart.getContents());
        Map<Product, Integer> cartContents = new HashMap<>(cart.getContents());

        for(String item: appliedItems){
            if(promotedCart.getQuantity(item)==1) {
                cartContents.remove(promotedCart.getEntryByItemName(item));
            }
            else {
                cartContents.putAll(promotedCart.adjustInventory(item,promotedCart.getQuantity(item)-1));
            }
        }
        promotedCart.setContents(cartContents);
        return promotedCart;
    }

    /*
    * Returns true if the bundle promotion is applicable to the current cart contents
    * */
    @Override
    public Boolean isAvailable(Cart cart) {
        appliedItems.forEach(i -> availabilityCheckMap.put(i, false));

        for (Map.Entry<Product, Integer> kv: cart.getContents().entrySet()) {
            if (appliedItems.contains(kv.getKey().getName())) {
                availabilityCheckMap.put(kv.getKey().getName(), true);
            }
        }
        return !availabilityCheckMap.containsValue(false);
    }

    /**
     * Returns the discounted price after promotion applied
     * @return discounted price
     */
    @Override
    public Double getDiscountedPrice() {
        double itemPrice = 0.0;
        for(String sku: appliedItems)
            itemPrice += PriceList.getPrice(sku);

        return itemPrice - this.promotedPrice;
    }
}
