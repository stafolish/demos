package com.xxx.pattern.chain;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@ComponentScan("com.xxx.pattern.chain")
public class Main {

    public static void main(String[] args) throws InterruptedException {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(Main.class);

        AlarmService alarmService = applicationContext.getBean(AlarmService.class);


        ExecutorService es = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            int tmp = i;
            es.execute(() -> {
                alarmService.handlerAlarm(Alarm.builder()
                        .id(tmp)
                        .eventNumber(10)
                        .alarmName("光功率衰耗")
                        .alarmAddress("省政府23号楼")
                        .alarmAck(1)
                        .alarmLevel(1)
                        .alarmType(1)
                        .date(new Date())
                        .desc("割接")
                        .build());
                latch.countDown();
            });
        }
        latch.await();
        Thread.sleep(10000);
        // 1, 10, "光功率衰耗", "省政府23号楼", 1, 1, 1, new Date(), "割接"
        // public Alarm(Integer id, Integer eventNumber, String alarmName, String alarmAddress, Integer alarmAck,
        // Integer alarmLevel, Integer alarmType, Date date, String desc)
    }
}
