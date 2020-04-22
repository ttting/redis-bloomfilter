package me.ttting.common.hash;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

/**
 * Created by jiangtiteng on 2020/4/22
 */
public class SpringRedisTemplateBitArray extends AbstractBitArray implements BitArray {
    private RedisTemplate redisTemplate;

    private SpringRedisTemplateBitArray() {
        super(null);
    }

    public SpringRedisTemplateBitArray(RedisTemplate redisTemplate, String prefix) {
        super(prefix);
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean set(long index) {
        return false;
    }

    @Override
    public boolean get(long index) {
        return false;
    }

    @Override
    public List<Boolean> batchSet(List<Long> indexs) {
        return (List<Boolean>) redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            indexs.forEach(index -> connection.setBit(this.key.getBytes(), index, true));
            return null;
        });
    }

    @Override
    public List<Boolean> batchGet(List<Long> indexs) {
        return (List<Boolean>) redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            indexs.forEach(index -> connection.getBit(this.key.getBytes(), index));
            return null;
        });
    }
}
