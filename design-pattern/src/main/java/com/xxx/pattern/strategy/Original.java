package com.xxx.pattern.strategy;

import org.springframework.stereotype.Component;

@Component
@PriceRegion(max = 1000000)
public class Original implements CalPrice {


    public Long calPrice(Long originalPrice) {
        return originalPrice;
    }
}
