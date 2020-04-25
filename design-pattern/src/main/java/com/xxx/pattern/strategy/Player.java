package com.xxx.pattern.strategy;

import lombok.Data;

import java.util.concurrent.atomic.AtomicLong;

@Data
public class Player {
    private String playerId;
    private AtomicLong totalAmount;//客户在鹅厂消费的总额

    public Player(String playerId, Long totalAmount) {
        this.playerId = playerId;
        this.totalAmount = new AtomicLong(totalAmount);
    }
}
