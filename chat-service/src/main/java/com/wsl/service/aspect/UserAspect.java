package com.wsl.service.aspect;

import com.wsl.dao.mapper.RoomMapper;
import com.wsl.model.constant.RedisEnum;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Aspect
@Component
public class UserAspect {

    @Resource
    private RedisTemplate redisTemplate;



    /**
     * 登录后redis计数器+1
     *
     * @author wangShilei
     * @date 2020/11/24 14:42
     */
    @Pointcut("execution(public * com.wsl.service.serviceImpl.LoginServiceImpl.login(..))")
    public void userLogin() {
    }

    @After("userLogin()")
    public void afterUserLogin() {
        redisTemplate.boundValueOps(RedisEnum.NUM_DAY).increment(1L);
        redisTemplate.boundValueOps(RedisEnum.NUM_WEEK).increment(1L);
        redisTemplate.boundValueOps(RedisEnum.NUM_MONTH).increment(1L);
    }


}
