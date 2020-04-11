package com.xxx.pattern.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PlayerShopService {

    @Autowired
    private CalStrategyContext calStrategyContext;

    // 把playerMap当作可以根据用户id查询的数据库。。。
    private Map<String, Player> playerMap = new ConcurrentHashMap<>();

    public Long calPrice(Player player, Long amount) {
        CalPrice calPrice = calStrategyContext.getStrategy(player.getTotalAmount().get());
        return calPrice.calPrice(amount);
    }

    public Player buy(String playerId, Long amount) {
        Player player = getPlayer(playerId);
        Long price = calPrice(player, amount);
        System.out.println("price=" + price);
        player.getTotalAmount().addAndGet(price);
        playerMap.put(playerId, player);
        return player;
    }


    public Player getPlayer(String playerId) {
        Player player = playerMap.get(playerId);
        if (player == null) {
            player = new Player(playerId, 0L);
            playerMap.put(playerId, player);
        }
        return player;
    }
}
