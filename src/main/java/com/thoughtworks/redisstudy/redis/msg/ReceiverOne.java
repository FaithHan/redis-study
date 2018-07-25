package com.thoughtworks.redisstudy.redis.msg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

public class ReceiverOne {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReceiverOne.class);

    private CountDownLatch latch;

    public void receiveMessage(String message) {
        LOGGER.info("第一个接收器----Received <" + message + ">");
    }
}