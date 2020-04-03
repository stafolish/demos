package com.xxx.pattern.strategy;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration()
@ComponentScan("com.xxx.pattern")
public class Main {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Main.class);
        PlayerShopService playerShopService = context.getBean(PlayerShopService.class);
        System.out.println(playerShopService.buy("1", 1000000L));
        System.out.println(playerShopService.buy("1", 1000000L));
        System.out.println(playerShopService.buy("1", 1000000L));
        System.out.println(playerShopService.buy("1", 1000000L));
        System.out.println(playerShopService.buy("1", 1000000L));
        System.out.println(playerShopService.buy("1", 1000000L));
    }

}
