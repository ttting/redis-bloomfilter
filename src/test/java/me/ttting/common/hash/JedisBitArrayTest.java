package me.ttting.common.hash;

import com.google.common.collect.Lists;
import com.google.common.hash.Funnel;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.JedisPool;

import java.util.List;

/**
 * Created by jiangtiteng on 2020/4/22
 */
public class JedisBitArrayTest {



    @Test
    public void batchSetAndGet() {
        JedisPool jedisPool = new JedisPool("localhost", 6379);
        JedisBitArray jedisBitArray = new JedisBitArray(jedisPool, "rbl-");

        List indexs = Lists.newArrayList(0L,1L,2L,3L,4L);

        jedisBitArray.batchSet(indexs);

        List<Boolean> result = jedisBitArray.batchGet(indexs);

        result.forEach(Assert::assertTrue);
    }
}
