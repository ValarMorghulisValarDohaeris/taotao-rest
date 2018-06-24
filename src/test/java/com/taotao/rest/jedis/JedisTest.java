package com.taotao.rest.jedis;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;

/**
 * Created by hao on 2018/6/24.
 */
public class JedisTest {
    @Test
    public void testJedisSingle(){
        //创建一个jedis的对象。
        Jedis jedis = new Jedis("101.69.255.134",6379);
        //调用jedis对象的方法，方法名称和redis的命令一致。
        jedis.set("key1","value1");
        String string = jedis.get("key1");
        System.out.println(string);
        //关闭jedis。
        jedis.close();
    }

    /**
     * 使用连接池
     */
    @Test
    public void testJedisPool(){
        //创建jedis连接池
        JedisPool pool = new JedisPool("101.69.255.134",6379);
        //从连接池中获得jedis对象
        Jedis jedis = pool.getResource();
        String string = jedis.get("key1");
        System.out.println(string);
        //关闭jedis对象
        jedis.close();
        pool.close();
    }

    /**
     * 集群版测试
     */
    @Test
    public void testJedisCluster(){
        HashSet<HostAndPort> nodes = new HashSet<>();
        nodes.add(new HostAndPort("101.69.255.134",7001));
        nodes.add(new HostAndPort("101.69.255.134",7002));
        nodes.add(new HostAndPort("101.69.255.134",7003));
        nodes.add(new HostAndPort("101.69.255.134",7004));
        nodes.add(new HostAndPort("101.69.255.134",7005));
        nodes.add(new HostAndPort("101.69.255.134",7006));

        JedisCluster cluster = new JedisCluster(nodes);

        cluster.set("k1","v1");
        String string = cluster.get("k1");
        System.out.println(string);

        cluster.close();
    }

    /**
     * 单机版测试
     */
    @Test
    public void testSpringJedisSingle(){
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-jedis.xml");
        JedisPool jedisPool = (JedisPool) context.getBean("redisClient");
        Jedis jedis = jedisPool.getResource();
        String string = jedis.get("key1");
        System.out.println(string);
        jedis.close();
        jedisPool.close();

    }

    /**
     * 集群版测试
     */
    @Test
    public void testSpringJedisCluster(){
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-jedis.xml");
        JedisCluster cluster = (JedisCluster) context.getBean("redisClient");
        String string = cluster.get("hello");
        System.out.println(string);
        cluster.close();
    }
}
