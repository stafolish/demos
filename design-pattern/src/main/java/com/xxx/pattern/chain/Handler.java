package com.xxx.pattern.chain;


public interface Handler {

    void handle(Alarm alarm, HandlerChain chain);
}
