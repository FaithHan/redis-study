package com.thoughtworks.redisstudy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisStudyApplicationTests {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Test
    public void contextLoads() {
        ListOperations<String, String> opsForList = stringRedisTemplate.opsForList();
        stringRedisTemplate.delete("hanfei");
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            list.add(String.valueOf(i)+"韩斐");
        }
        opsForList.rightPushAll("hanfei", list);
        System.out.println(111);
    }

    @Test
    public void redistest() {
        ListOperations<String, String> opsForList = stringRedisTemplate.opsForList();
        List<String> hanfei = opsForList.range("hanfei", 0, -1);
        hanfei.forEach(System.out::println);
    }

}
