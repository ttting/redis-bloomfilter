package me.ttting.common.hash;

import com.google.common.hash.Funnel;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by jiangtiteng on 2020/4/22
 */
public class RedisBloomFilterDemo {
    @Bean
    BloomFilter bloomFilter(RedisTemplate redisTemplate) {
        BitArray bitArray = new SpringRedisTemplateBitArray(redisTemplate, "redis-temp-demo");
        return BloomFilter.create((Funnel<Long>) (from, into)
                        -> into.putLong(from), 1_0000_0000, 0.0000001,
                BloomFilterStrategies.MURMUR128_MITZ_32, bitArray);
    }

    @Bean
    BloomFilter bloomFilter(JedisPool jedisPool) {
        BitArray bitArray = new JedisBitArray(jedisPool, "jedis-pool-demo");
        return BloomFilter.create((Funnel<Long>) (from, into)
                        -> into.putLong(from), 1_0000_0000, 0.0000001,
                BloomFilterStrategies.MURMUR128_MITZ_32, bitArray);
    }

}
