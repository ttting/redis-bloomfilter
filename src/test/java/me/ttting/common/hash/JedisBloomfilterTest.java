package me.ttting.common.hash;

import com.google.common.base.Charsets;
import com.google.common.hash.Funnel;
import org.junit.Assert;
import org.junit.Test;
import redis.clients.jedis.JedisPool;

/**
 * Created by jiangtiteng
 */
public class JedisBloomfilterTest {
    @Test
    public void testJedisBloomfilter() {
        JedisPool jedisPool = new JedisPool("localhost", 6379);
        JedisBitArray jedisBitArray = new JedisBitArray(jedisPool, "test-1");
        BloomFilter<String> bloomFilter = BloomFilter.create((Funnel<String>) (from, into)
                        -> into.putString(from, Charsets.UTF_8), 1_0000_0000, 0.0000001,
                BloomFilterStrategies.MURMUR128_MITZ_32, jedisBitArray);

        String testElement1 = "123";
        String testElement2 = "456";
        String testElement3 = "789";

        bloomFilter.put(testElement1);
        bloomFilter.put(testElement2);

        Assert.assertTrue(bloomFilter.mightContain(testElement1));
        Assert.assertTrue(bloomFilter.mightContain(testElement2));
        Assert.assertFalse(bloomFilter.mightContain(testElement3));
    }
}
