package com.xxx.pattern.chain;

import org.springframework.stereotype.Component;

@Component
public class BHandler implements Handler {
    @Override
    public void handle(Alarm alarm, HandlerChain chain) {
        //规则内容：告警名称为：光功率衰耗，描述信息为:割接 则该告警变为确认状态
        if (alarm.getAlarmName().contains("光功率衰耗") && alarm.getDesc().contains("割接")) {
            alarm.setAlarmAck(0);
            System.out.println("执行规则2");
        }

        //注意回调FilterChain的doFilter方法，让FilterChain继续执行下一个Filter
        chain.doFilter(alarm);
    }
}
