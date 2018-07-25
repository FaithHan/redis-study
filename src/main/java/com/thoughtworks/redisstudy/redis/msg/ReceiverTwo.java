package com.thoughtworks.redisstudy.redis.msg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

public class ReceiverTwo {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReceiverTwo.class);



    public void receiveMessage(String message) {
        LOGGER.info("第二哥接收器-----Received <" + message + ">");
    }
}