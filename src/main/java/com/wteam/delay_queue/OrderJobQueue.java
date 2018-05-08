package com.wteam.delay_queue;

import org.redisson.Redisson;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//延迟队列
public class OrderJobQueue {

    private static final Logger log = LoggerFactory.getLogger(OrderJobQueue.class);
    private static RedissonClient redissonClient;
    private static String key = "OrderJobQueue";

    //初始化redissonClient
    static {
        try {
            Config config = Config.fromYAML(OrderJobQueue.class.getClassLoader().getResource("redis.yaml"));
            redissonClient = Redisson.create(config);
        } catch (Exception e) {
            log.error("初始化RedissosnClient错误:", e);
        }
    }


    //添加任务
    public static void addOrderJob(OrderJob job) {
        redissonClient.getScoredSortedSet(key).add(job.getDelayTime(), job);
    }


    //获取任务列表
    static RScoredSortedSet<OrderJob> getOrderJobs() {
        return redissonClient.getScoredSortedSet(key);
    }
}
