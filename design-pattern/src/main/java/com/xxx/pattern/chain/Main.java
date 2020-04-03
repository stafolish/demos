package com.xxx.pattern.chain;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@Configuration
@ComponentScan("com.xxx.pattern.chain")
public class Main {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(Main.class);

        AlarmService alarmService = applicationContext.getBean(AlarmService.class);
        alarmService.handlerAlarm(Alarm.builder()
                .id(1)
                .eventNumber(10)
                .alarmName("光功率衰耗")
                .alarmAddress("省政府23号楼")
                .alarmAck(1)
                .alarmLevel(1)
                .alarmType(1)
                .date(new Date())
                .desc("割接")
                .build());

        // 1, 10, "光功率衰耗", "省政府23号楼", 1, 1, 1, new Date(), "割接"
        // public Alarm(Integer id, Integer eventNumber, String alarmName, String alarmAddress, Integer alarmAck,
                // Integer alarmLevel, Integer alarmType, Date date, String desc)
    }
}
