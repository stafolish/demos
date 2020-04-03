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
        // 相当于计数器，当所有都准备好了，再一起执行，模仿多并发，保证并发量
        final CountDownLatch countDownLatch = new CountDownLatch(10);
        // 保证所有线程执行完了再打印atomicInteger的值
        final CountDownLatch countDownLatch2 = new CountDownLatch(10);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        try {
            for (int i = 0; i < 10; i++) {
                int tmp = i;
                executorService.execute(() -> {
                    try {
                        countDownLatch.await(); //一直阻塞当前线程，直到计时器的值为0,保证同时并发
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
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
                    countDownLatch2.countDown();
                });
                countDownLatch.countDown();
            }
            // 保证所有线程执行完
            countDownLatch2.await();
            executorService.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
