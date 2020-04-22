package me.ttting.common.hash;

/**
 * Created by jiangtiteng on 2020/4/22
 */
public abstract class AbstractBitArray implements BitArray {
    protected String key;

    protected long bitSize;

    public static final long MAX_REDIS_BIT_SIZE = 4294967296L;

    public static final String REDIS_PREFIX = "BLOOM_FILTER_";

    public AbstractBitArray(String prefix) {
        this.key = REDIS_PREFIX + prefix;
    }

    @Override
    public void setBitSize(long bitSize) {
        if (bitSize > MAX_REDIS_BIT_SIZE)
            throw new IllegalArgumentException("Invalid redis bit size, must small than 2 to the 32");

        this.bitSize = bitSize;
    }

    @Override
    public long bitSize() {
        return this.bitSize;
    }
}
