package com.thoughtworks.redisstudy;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisStudyApplicationTests {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();//建议使用这种
    }

    @Test
    public void contextLoads() {
        ListOperations<String, String> opsForList = stringRedisTemplate.opsForList();
        stringRedisTemplate.delete("hanfei");
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            list.add(String.valueOf(i) + "韩斐");
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


    @Test
    public void controllertest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/")).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        System.out.println(1);
    }





}
