package com.sdkj;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @Author wangshuo
 * @Date 2022/4/24, 17:34
 * 消息生产者
 */
public class MessageProvider extends Thread {

    public static final String MESSAGE_KEY = "message:queue";
    private volatile int count;

    public void putMessage(String message) {

        JedisClientConfig jedisClientConfig = new JedisClientConfig();
        JedisPool jedisPool = jedisClientConfig.getJedisPool();
        Jedis jedis = jedisClientConfig.getJedis(jedisPool);
        Long size = jedis.lpush(MESSAGE_KEY, message);
        System.out.println(Thread.currentThread().getName() + " put message, size = " + size + ", count = " + count);
        count++;
        //close
        jedisClientConfig.closeJedisAndJedisPool(jedisPool, jedis);
    }

    @Override
    public synchronized void run() {

        for (int i = 0; i < 5; i++) {

            putMessage("message" + count);
        }
    }

    public static void main(String[] args) {

        MessageProvider messageProvider = new MessageProvider();
        Thread thread1 = new Thread(messageProvider, "Thread1");
        Thread thread2 = new Thread(messageProvider, "Thread2");
        Thread thread3 = new Thread(messageProvider, "Thread3");
        Thread thread4 = new Thread(messageProvider, "Thread4");
        Thread thread5 = new Thread(messageProvider, "Thread5");
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();
    }
}
