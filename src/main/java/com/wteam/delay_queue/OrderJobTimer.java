package com.wteam.delay_queue;

import org.redisson.api.RScoredSortedSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 简单延时任务系统
 */
@Component
public class OrderJobTimer {

    private final OrderJobHandler orderJobHandler;
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Autowired
    public OrderJobTimer(OrderJobHandler orderJobHandler) {
        this.orderJobHandler = orderJobHandler;
    }

    public void startThread() {
        new Thread(new MyThread()).start();
    }

    public class MyThread implements Runnable {
        @Override
        public void run() {
            while (true) {
                RScoredSortedSet<OrderJob> jobs = OrderJobQueue.getOrderJobs();
                if (jobs.isEmpty()) {
                    sleep();
                    continue;
                }
                while (jobs.first().getDelayTime() < System.currentTimeMillis()) {
                    executorService.execute(orderJobHandler.getThread(jobs.pollFirst()));
                }
                sleep();
            }

        }
    }

    private void sleep() {
        try {
            TimeUnit.SECONDS.sleep(1L);
        } catch (InterruptedException e) {
            log.error("暂停扫描orderJob失败：", e);
        }
    }
}
