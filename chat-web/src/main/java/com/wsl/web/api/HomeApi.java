package com.wsl.web.api;

import com.wsl.common.Result;
import com.wsl.model.constant.RedisEnum;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class HomeApi {

    @Resource
    private RedisTemplate redisTemplate;

    /***
     * 统计人数
     * @return : Map
     */
    @GetMapping("/getNum/v1")
    public Result<Map<String,String>> getNum(){
        Map<String,String> result = new HashMap<>(8);
        result.put("now",redisTemplate.boundValueOps(RedisEnum.NUM_NOW).get().toString());
        result.put("day",redisTemplate.boundValueOps(RedisEnum.NUM_DAY).get().toString());
        result.put("week",redisTemplate.boundValueOps(RedisEnum.NUM_WEEK).get().toString());
        result.put("month",redisTemplate.boundValueOps(RedisEnum.NUM_MONTH).get().toString());
        return Result.success(result);
    }
}
