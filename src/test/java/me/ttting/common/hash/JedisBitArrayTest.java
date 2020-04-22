package me.ttting.common.hash;

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;
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
