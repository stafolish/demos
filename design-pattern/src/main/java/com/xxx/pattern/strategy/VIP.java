package com.xxx.pattern.strategy;

import org.springframework.stereotype.Component;

@Component
@PriceRegion(min = 1000000, max = 2000000)
public class VIP implements CalPrice {


    public Long calPrice(Long originalPrice) {
        return originalPrice * 9 / 10;
    }
}
