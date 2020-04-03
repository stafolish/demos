package com.xxx.pattern.strategy;

import org.springframework.stereotype.Component;

@Component
@PriceRegion(min = 3000000)
public class GoldVip implements CalPrice {


    public Long calPrice(Long originalPrice) {
        return originalPrice * 7 / 10;
    }
}
