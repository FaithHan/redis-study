package com.thoughtworks.redisstudy.redis.msg;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import java.util.concurrent.CountDownLatch;

@Configuration
public class RedisMsgConfig {
    /**
     * 详细教程见下链接
     * https://spring.io/guides/gs/messaging-redis/
     *
     * @param connectionFactory
     * @return
     */
    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter1(), new ChannelTopic("hanfei"));
        container.addMessageListener(listenerAdapter2(), new ChannelTopic("hanfei"));
        container.addMessageListener(listenerAdapter2(), new ChannelTopic("wangfei"));

        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter1() {
        return new MessageListenerAdapter(new ReceiverOne(), "receiveMessage");
    }

    @Bean
    MessageListenerAdapter listenerAdapter2() {
        return new MessageListenerAdapter(new ReceiverTwo(), "receiveMessage");
    }

}
