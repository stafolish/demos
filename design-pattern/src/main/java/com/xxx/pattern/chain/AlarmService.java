package com.xxx.pattern.chain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlarmService {

    @Autowired
    private HandlerChain handlerChain;


    public void handlerAlarm(Alarm alarm) {
        handlerChain.doFilter(alarm);
    }

}
