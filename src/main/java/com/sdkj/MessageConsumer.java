package com.sdkj;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

/**
 * @Author wangshuo
 * @Date 2022/4/24, 17:59
 * 消息生产者
 */
public class MessageConsumer implements Runnable {

    public static final String MESSAGE_KEY = "message:queue";
    private volatile int count;

    public void consumerMessage() {

        JedisClientConfig jedisClientConfig = new JedisClientConfig();
        JedisPool jedisPool = jedisClientConfig.getJedisPool();
        Jedis jedis = jedisClientConfig.getJedis(jedisPool);
        List<String> rpop = jedis.brpop(0, MESSAGE_KEY);
        System.out.println(rpop);
        System.out.println(Thread.currentThread().getName() + " consumer message, message = " + rpop.get(1) + ", count = " + count);
        count++;
        jedisClientConfig.closeJedisAndJedisPool(jedisPool, jedis);
    }

    @Override
    public void run() {

        while (true) {
            consumerMessage();
        }
    }

    public static void main(String[] args) {

        MessageConsumer messageConsumer = new MessageConsumer();
        Thread thread6 = new Thread(messageConsumer, "thread6");
        Thread thread7 = new Thread(messageConsumer, "thread7");
        thread6.start();
        thread7.start();
    }
}
