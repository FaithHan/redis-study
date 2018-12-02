package com.thoughtworks.redisstudy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
@RestController
@EnableSwagger2
public class RedisStudyApplication {


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    public static void main(String[] args) {
        SpringApplication.run(RedisStudyApplication.class, args);
    }


    @GetMapping("/")
    public String index() {
        ListOperations<String, String> opsForList = stringRedisTemplate.opsForList();
        String hanfei = opsForList.leftPop("hanfei", 10, TimeUnit.SECONDS);
        System.out.println("hehe");
        return "SUCCESS";
    }


    @GetMapping("/msg")
    public String msg(String msg) {
        stringRedisTemplate.convertAndSend("hanfei", msg);
        stringRedisTemplate.convertAndSend("wangfei", msg);
        return msg;
    }

}

