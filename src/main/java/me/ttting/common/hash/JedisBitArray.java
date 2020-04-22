package me.ttting.common.hash;

import com.sun.org.apache.regexp.internal.REUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

import java.util.List;

/**
 * Created by jiangtiteng
 */
public class JedisBitArray extends AbstractBitArray {
    @FunctionalInterface
    public interface JedisRunable {
        Object run(Jedis jedis);
    }

    private JedisPool jedisPool;

    private JedisBitArray() {
        super(null);
    }

    public JedisBitArray(JedisPool jedisPool, String prefix) {
        super(prefix);
        this.jedisPool = jedisPool;
    }

    private Object execute(JedisRunable runnable) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return runnable.run(jedis);
        } catch (Exception e) {
            throw e;
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    @Override
    public boolean set(long index) {
        boolean result;
        result = (Boolean) execute(jedis -> {
            Boolean setbit = jedis.setbit(key, index, true);
            return !setbit;
        });
        return result;
    }

    @Override
    public boolean get(long index) {
        boolean result;
        result = (Boolean) execute(jedis -> jedis.getbit(key, index));
        return result;
    }

    @Override
    public List<Boolean> batchGet(List<Long> indexs) {

        return (List<Boolean>) execute(jedis -> {
            Pipeline pipeline = jedis.pipelined();
            indexs.forEach(index -> pipeline.getbit(key, index));
            return pipeline.syncAndReturnAll();
        });
    }

    @Override
    public List<Boolean> batchSet(List<Long> indexs) {
        return (List<Boolean>) execute(jedis -> {
            Pipeline pipeline = jedis.pipelined();
            indexs.forEach(index -> pipeline.setbit(key, index, true));
            return pipeline.syncAndReturnAll();
        });
    }
}
