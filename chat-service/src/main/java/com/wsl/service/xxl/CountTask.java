package com.wsl.service.xxl;

import com.wsl.model.constant.RedisEnum;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;

/**
 * 清空redis统计数
 */
@Configuration
@EnableScheduling
@EnableAsync
public class CountTask {
    @Resource
    private RedisTemplate redisTemplate;

    @Async
    @Scheduled(cron = "0 0 0 1/1 * ?")
    public void cleanDay() {
        redisTemplate.boundValueOps(RedisEnum.NUM_DAY).set(0);
    }
    @Async
    @Scheduled(cron = "0 0 6 * * 7")
    public void cleanWeek() {
        redisTemplate.boundValueOps(RedisEnum.NUM_WEEK).set(0);
    }

    @Async
    @Scheduled(cron = "0 0 3 1 * ?")
    public void cleanMonth() {
        redisTemplate.boundValueOps(RedisEnum.NUM_MONTH).set(0);
    }


}
