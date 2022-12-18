package com.nrupay.service;

import com.nrupay.model.Cart;
import com.nrupay.model.Product;
import com.nrupay.promotion.Promotion;
import com.nrupay.util.PromotionUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PromotionServiceImplTest {
    private static PromotionService promotionService;
    private static List<Promotion> promotions;
    private static Product productA;
    private static Product productB;
    private static Product productC;
    private static Product productD;

    @BeforeAll
    public static void setup() {
        promotionService = new PromotionServiceImpl();
        promotions = PromotionUtil.setupPromotions();
        productA = new Product("A");
        productB = new Product("B");
        productC = new Product("C");
        productD = new Product("D");
    }

    @ParameterizedTest
    @MethodSource("testCases")
    public void testPromotionsAppliedOnCart(PromotionTestParameters pt) {
        Double checkoutPrice = promotionService.getPromotedPrice(pt.cart, promotions);
        assertEquals(pt.verifyCartAmount, checkoutPrice);
    }

    private static Stream<Arguments> testCases() {
        Map<Product, Integer> scenarioA = new HashMap<>();
        scenarioA.put(productA, 1);
        scenarioA.put(productB, 1);
        scenarioA.put(productC, 1);
        Cart scenarioACart = new Cart();
        scenarioACart.setContents(scenarioA);
        Cart scenarioBCart = new Cart();
        Map<Product, Integer> scenarioB = new HashMap<>();
        scenarioB.put(productA, 5);
        scenarioB.put(productB, 5);
        scenarioB.put(productC, 1);
        scenarioBCart.setContents(scenarioB);
        Map<Product, Integer> scenarioC = new HashMap<>();
        scenarioC.put(productA, 3);
        scenarioC.put(productB, 5);
        scenarioC.put(productC, 1);
        scenarioC.put(productD, 1);
        Cart scenarioCCart = new Cart();
        scenarioCCart.setContents(scenarioC);

        Cart doublePromotionCart = new Cart();
        Map<Product, Integer> doublePromotion = new HashMap<>();
        doublePromotion.put(productC, 2);
        doublePromotion.put(productD, 2);
        doublePromotionCart.setContents(doublePromotion);

        Cart singleProductGroupingCart = new Cart();
        Map<Product, Integer> SingleProductGrouping = new HashMap<>();
        SingleProductGrouping.put(productA, 6);
        singleProductGroupingCart.setContents(SingleProductGrouping);

        Cart singleProductGroupingCartForTwoTimesCart = new Cart();
        Map<Product, Integer> singleProductGroupingCartForTwoTimes = new HashMap<>();
        singleProductGroupingCartForTwoTimes.put(productA, 7);
        singleProductGroupingCartForTwoTimesCart.setContents(singleProductGroupingCartForTwoTimes);

        return Stream.of(
                Arguments.arguments(Named.named("Scenario A", PromotionTestParameters.builder().cart(scenarioACart).verifyCartAmount(100.0).build())),
                Arguments.arguments(Named.named("Scenario B", PromotionTestParameters.builder().cart(scenarioBCart).verifyCartAmount(370.0).build())),
                Arguments.arguments(Named.named("Scenario C", PromotionTestParameters.builder().cart(scenarioCCart).verifyCartAmount(280.0).build())),
                Arguments.arguments(Named.named("Promotion should applied twice", PromotionTestParameters.builder().cart(doublePromotionCart).verifyCartAmount(60.0).build())),
                Arguments.arguments(Named.named("Single product grouping promotion applied on cart two times", PromotionTestParameters.builder().cart(singleProductGroupingCart).verifyCartAmount(260.0).build())),
                Arguments.arguments(Named.named("Single product grouping promotion applied on cart two times and one without promotion", PromotionTestParameters.builder().cart(singleProductGroupingCartForTwoTimesCart).verifyCartAmount(310.0).build()))
        );
    }

}
