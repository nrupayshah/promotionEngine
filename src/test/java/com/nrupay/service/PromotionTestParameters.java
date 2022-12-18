package com.nrupay.service;

import com.nrupay.model.Cart;
import com.nrupay.promotion.Promotion;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
class PromotionTestParameters {

    Cart cart;
    Double verifyCartAmount;
}
