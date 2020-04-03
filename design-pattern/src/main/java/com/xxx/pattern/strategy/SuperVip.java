package com.xxx.pattern.strategy;

import org.springframework.stereotype.Component;

@Component
@PriceRegion(min = 2000000,max = 3000000)
public class SuperVip implements CalPrice {


    public Long calPrice(Long originalPrice) {
        return originalPrice * 8 / 10;
    }
}
