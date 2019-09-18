package me.ttting.common.hash;

import com.google.common.base.Preconditions;
import com.google.common.hash.Funnel;

/**
 * Created by jiangtiteng
 */
public class BloomFilter<T> {
    interface Strategy {
        <T> boolean put(T object, Funnel<? super T> funnel, int numHashFunctions, BitArray bits);

        <T> boolean mightContain(T object, Funnel<? super T> funnel, int numHashFunctions, BitArray bits);

        int ordinal();
    }

    private final BitArray bits;

    private final int numHashFunctions;

    private final Funnel<? super T> funnel;

    private final Strategy strategy;

    public static <T> BloomFilter<T> create(Funnel<? super T> funnel, long expectedInsertions, double fpp, Strategy strategy, BitArray bits) {
        Preconditions.checkArgument(
                expectedInsertions >= 0, "Expected insertions (%s) must be >= 0", expectedInsertions);
        Preconditions.checkArgument(fpp > 0.0, "False positive probability (%s) must be > 0.0", fpp);
        Preconditions.checkArgument(fpp < 1.0, "False positive probability (%s) must be < 1.0", fpp);
        Preconditions.checkNotNull(strategy);

        if (expectedInsertions == 0) {
            expectedInsertions = 1;
        }

        long numBits = optimalNumOfBits(expectedInsertions, fpp);
        bits.setBitSize(numBits);
        int numHashFunctions = optimalNumOfHashFunctions(expectedInsertions, numBits);
        return new BloomFilter<>(bits, numHashFunctions, funnel, strategy);
    }

    private BloomFilter(BitArray bits,
                        int numHashFunctions,
                        Funnel<? super T> funnel,
                        Strategy strategy) {
        this.bits = bits;
        this.numHashFunctions = numHashFunctions;
        this.strategy = strategy;
        this.funnel = funnel;
    }

    public boolean mightContain(T object) {
        return strategy.mightContain(object, funnel, numHashFunctions, bits);
    }


    public boolean put(T object) {
        return strategy.put(object, funnel, numHashFunctions, bits);
    }

    static int optimalNumOfHashFunctions(long n, long m) {
        // (m / n) * log(2), but avoid truncation due to division!
        return Math.max(1, (int) Math.round((double) m / n * Math.log(2)));
    }

    static long optimalNumOfBits(long n, double p) {
        if (p == 0) {
            p = Double.MIN_VALUE;
        }
        return (long) (-n * Math.log(p) / (Math.log(2) * Math.log(2)));
    }

}
