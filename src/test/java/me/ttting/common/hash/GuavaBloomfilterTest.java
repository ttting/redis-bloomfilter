package me.ttting.common.hash;

import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by jiangtiteng
 */
public class GuavaBloomfilterTest {

    @Test
    public void testGuavaBloomFilter() {
        BloomFilter<String> bloomFilter = BloomFilter.create((Funnel<String>) (from, into) -> {
            into.putString(from, Charsets.UTF_8);
        }, 100_0000, 0.000_0001);

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
