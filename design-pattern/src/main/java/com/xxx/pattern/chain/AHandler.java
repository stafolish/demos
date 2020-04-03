package com.xxx.pattern.chain;

import org.springframework.stereotype.Component;

@Component
public class AHandler implements Handler {
    @Override
    public void handle(Alarm alarm, HandlerChain chain) {
        //规则内容：如果是政府发生告警。告警等级设为最高
        if (alarm.getAlarmAddress().contains("政府")) {
            alarm.setAlarmLevel(4);
            System.out.println("执行规则1");
        }

        //注意回调FilterChain的doFilter方法，让FilterChain继续执行下一个Filter
        chain.doFilter(alarm);
    }
}
