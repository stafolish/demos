package com.xxx.pattern.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CalStrategyContext {

    @Autowired
    private List<CalPrice> calPrices = new ArrayList<CalPrice>();

    public CalPrice getStrategy(Player player) {
        for (CalPrice calPrice : calPrices) {
            PriceRegion priceRegion = calPrice.getClass().getDeclaredAnnotation(PriceRegion.class);
            Long totalAmount = player.getTotalAmount();
            if (totalAmount >= priceRegion.min() && totalAmount < priceRegion.max()) {
                return calPrice;
            }
        }
        throw new RuntimeException("策略未获取到！！");
    }
}
