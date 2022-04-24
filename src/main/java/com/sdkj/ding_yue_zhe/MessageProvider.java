package com.sdkj.ding_yue_zhe;

import com.sdkj.JedisClientConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @Author wangshuo
 * @Date 2022/4/24, 19:26
 * 订阅者模式 / 生产者
 */
public class MessageProvider extends Thread {

    public static final String CHANNEL_KEY = "channel:ws";
    private volatile int count;
    private static JedisClientConfig jedisClientConfig = new JedisClientConfig();

    public void putMessage(String message) {
        JedisPool jedisPool = jedisClientConfig.getJedisPool();
        Jedis jedis = jedisClientConfig.getJedis(jedisPool);
        //返回订阅者数量
        Long publish = jedis.publish(CHANNEL_KEY, message);
        System.out.println(Thread.currentThread().getName() + " put message , count = " + count + " , subscriberNum = " + publish);
        count++;
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
        Thread thread1 = new Thread(messageProvider, "thread1");
        Thread thread2 = new Thread(messageProvider, "thread2");
        Thread thread3 = new Thread(messageProvider, "thread3");
        Thread thread4 = new Thread(messageProvider, "thread4");
        Thread thread5 = new Thread(messageProvider, "thread5");
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();
    }
}
