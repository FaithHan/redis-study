package com.thoughtworks.redisstudy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
@RestController
public class RedisStudyApplication {


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    public static void main(String[] args) {
        SpringApplication.run(RedisStudyApplication.class, args);
    }


    @RequestMapping("/")
    public String index() {
        ListOperations<String, String> opsForList = stringRedisTemplate.opsForList();
        String hanfei = opsForList.leftPop("hanfei", 10, TimeUnit.SECONDS);
        System.out.println("hehe");
        return hanfei;
    }

}
