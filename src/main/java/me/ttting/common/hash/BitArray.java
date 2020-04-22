package me.ttting.common.hash;

import java.util.List;

/**
 * Created by jiangtiteng
 */
public interface BitArray {
    void setBitSize(long bitSize);

    @Deprecated
    boolean set(long index);

    @Deprecated
    boolean get(long index);

    long bitSize();

    List<Boolean> batchGet(List<Long> indexs);

    List<Boolean> batchSet(List<Long> indexs);
}
