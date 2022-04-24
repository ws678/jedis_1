package com.sdkj.ding_yue_zhe;

import com.sdkj.JedisClientConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

/**
 * @Author wangshuo
 * @Date 2022/4/24, 19:35
 * 订阅者模式 / 消费者
 */
public class MessageConsumer implements Runnable {

    public static final String CHANNEL_KEY = "channel:ws";
    public static final String EXIT_COMMAND = "exit";
    private static JedisClientConfig jedisClientConfig = new JedisClientConfig();
    //Subscribe模式
    private MyJedisPubSubscribe myJedisPubSub = new MyJedisPubSubscribe();
    //PSubscribe模式
    private MyJedisPubPSubscribe myJedisPubSubscribe = new MyJedisPubPSubscribe();

    @Override
    public void run() {
        while (true) {
            consumerMessage();
        }
    }

    private void consumerMessage() {

        JedisPool jedisPool = jedisClientConfig.getJedisPool();
        Jedis jedis = jedisClientConfig.getJedis(jedisPool);
        //jedis.subscribe(myJedisPubSub, CHANNEL_KEY);
        jedis.psubscribe(myJedisPubSubscribe, CHANNEL_KEY);
        jedisClientConfig.closeJedisAndJedisPool(jedisPool, jedis);
    }

    public static void main(String[] args) {
        MessageConsumer messageConsumer = new MessageConsumer();
        Thread thread6 = new Thread(messageConsumer, "thread6");
        Thread thread7 = new Thread(messageConsumer, "thread7");
        thread6.start();
        thread7.start();
    }
}

//重写JedisPubSub
class MyJedisPubSubscribe extends JedisPubSub {

    @Override
    public void onMessage(String channel, String message) {

        System.out.println(Thread.currentThread().getName() + "-接收到消息 ：channel = " + channel + " message = " + message);
        //接收到exit消息后退出
        if (MessageConsumer.EXIT_COMMAND.equals(message))
            System.exit(0);
    }
}

//重写JedisPubSub
class MyJedisPubPSubscribe extends JedisPubSub {

    @Override
    public void onPMessage(String pattern, String channel, String message) {

        System.out.println(Thread.currentThread().getName() + "-接收到消息 ：pattern = " + pattern + " channel = " + channel + " message = " + message);
        //接收到exit消息后退订并退出
        if (MessageConsumer.EXIT_COMMAND.equals(message)) {
            unsubscribe(MessageConsumer.CHANNEL_KEY);
            System.exit(0);
        }
    }

    @Override
    public void unsubscribe(String... channels) {
        super.unsubscribe(channels);
    }
}