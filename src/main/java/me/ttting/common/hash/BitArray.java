package me.ttting.common.hash;

/**
 * Created by jiangtiteng
 */
public interface BitArray {
    void setBitSize(long bitSize);

    boolean set(long index);

    boolean get(long index);

    long bitSize();
}
