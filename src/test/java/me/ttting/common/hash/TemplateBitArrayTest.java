package me.ttting.common.hash;

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;

import java.util.List;

/**
 * Created by jiangtiteng on 2020/4/22
 */
public class TemplateBitArrayTest {
    private RedisTemplate redisTemplate;

    private BitArray bitArray;

    @Before
    public void setUp() throws Exception {
        JedisShardInfo jedisShardInfo = new JedisShardInfo("localhost");
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(jedisPoolConfig);
        jedisConnectionFactory.setShardInfo(jedisShardInfo);

        RedisTemplate template = new RedisTemplate();
        template.setConnectionFactory(jedisConnectionFactory);

        redisTemplate = template;
        redisTemplate.afterPropertiesSet();
        bitArray = new SpringRedisTemplateBitArray(redisTemplate, "rtTest3");
    }

    @Test
    public void setBatchGetAndSet() {
        List indexs = Lists.newArrayList(0L, 1L, 2L, 3L, 4L);

        bitArray.batchSet(indexs);

        List<Boolean> result = bitArray.batchGet(indexs);

        result.forEach(Assert::assertTrue);
    }
}
