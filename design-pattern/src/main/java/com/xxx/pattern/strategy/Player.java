package com.xxx.pattern.strategy;

import lombok.Data;

@Data
public class Player {
    private String playerId;
    private Long totalAmount;//客户在鹅厂消费的总额

    public Player(String playerId, Long totalAmount) {
        this.playerId = playerId;
        this.totalAmount = totalAmount;
    }
}
