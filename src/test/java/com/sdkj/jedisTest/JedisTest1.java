package com.sdkj.jedisTest;

import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Objects;

/**
 * @Author wangshuo
 * @Date 2022/4/22, 10:47
 * Please add a comment
 */
public class JedisTest1 {

    //单实例的测试
    @Test
    public void demo1() {

        //1.设置IP地址和端口
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        //2.设置密码
        //jedis.auth("678678");
        //保存数据
        jedis.set("name1", "sdkj");
        //获取数据
        System.out.println(jedis.get("name1"));
        //释放资源
        jedis.close();
    }

    //使用连接池
    @Test
    public void LianJieChidemo() {

        //获取连接池对象
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        //设置最大连接数
        jedisPoolConfig.setMaxTotal(30);
        //设置最大连接空闲数
        jedisPoolConfig.setMaxIdle(10);
        //获得连接池
        //获得核心对象
        Jedis jedis = null;
        JedisPool jedisPool = null;
        try {
            jedisPool = new JedisPool(jedisPoolConfig, "127.0.0.1", 6379);
            //获得连接
            jedis = jedisPool.getResource();
            //jedis.auth("678678");
            System.out.println(jedis.get("name1"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Objects.requireNonNull(jedis).close();
            jedisPool.close();
        }
    }
}
