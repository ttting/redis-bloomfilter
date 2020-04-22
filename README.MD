# redis-bloomFilter
redis-bloomFilter是基于redis的bitset实现的bloomfilter.具体原理和实现思路可以参考[BloomFilter基于redis的实现](https://github.com/ttting/blog/blob/master/redis/BloomFilter%E5%9F%BA%E4%BA%8Eredis%E7%9A%84%E5%AE%9E%E7%8E%B0.md)

## 使用
redis-bloomFilter发布在JitPack，可以选择下载源码编译，或者通过jitpack源添加依赖。

### 使用Maven添加依赖
1. 添加jitpack源

```xml
<repositories>
	<repository>
		<id>jitpack.io</id>
		<url>https://jitpack.io</url>
    </repository>
</repositories>
```

2. 添加redis-bloomFilter依赖

```xml
<dependency>
    <groupId>com.github.ttting</groupId>
    <artifactId>redis-bloomfilter</artifactId>
    <version>1.1.0</version>
</dependency>
```

### 示例


####BloomFilter demo
Redis-bloomFilter基于Guava的BloomFilter改造，api基本完全和Guava BloomFilter相同。对于基于内存的Guava BloomFilter只需要替换类就可以完成切换。

```java
    @Test
    public void testLongBloomfilter() {
        //创建redis连接池
        JedisPool jedisPool = new JedisPool("localhost", 6379); 
        //创建BitArray, key为Bitset key
        JedisBitArray jedisBitArray = new JedisBitArray(jedisPool, "test-1"); 
        
        BloomFilter<Long> bloomFilter = BloomFilter.create((Funnel<Long>) (from, into)
                        -> into.putLong(from), 1_0000_0000, 0.0000001,
                BloomFilterStrategies.MURMUR128_MITZ_32, jedisBitArray);

        long testElement1 = 1;
        long testElement2 = 2;
        long testElement3 = 3;
    
        //插入元素
        bloomFilter.put(testElement1);
        bloomFilter.put(testElement2);

        //判断元素是否存在
        Assert.assertTrue(bloomFilter.mightContain(testElement1));
        Assert.assertTrue(bloomFilter.mightContain(testElement2));
        Assert.assertFalse(bloomFilter.mightContain(testElement3));
    }

```
更多详情可以参考 [redis-bloomfilter](https://github.com/ttting/redis-bloomfilter/blob/master/src/test/java/me/ttting/common/hash/JedisBloomfilterTest.java)

####基于jedis 创建

```
    @Bean
    BloomFilter bloomFilter(JedisPool jedisPool) {
        BitArray bitArray = new JedisBitArray(jedisPool, "jedis-pool-demo");
        return BloomFilter.create((Funnel<Long>) (from, into)
                        -> into.putLong(from), 1_0000_0000, 0.0000001,
                BloomFilterStrategies.MURMUR128_MITZ_32, bitArray);
    }
```

####基于spring redis template创建

```java
    @Bean
    BloomFilter bloomFilter(RedisTemplate redisTemplate) {
       BitArray bitArray = new SpringRedisTemplateBitArray(redisTemplate,"my-prefix");
        return BloomFilter.create((Funnel<Long>) (from, into)
                        -> into.putLong(from), 1_0000_0000, 0.0000001,
                BloomFilterStrategies.MURMUR128_MITZ_32, bitArray);
    }
```
更多详情可以参考 [RedisBloomFilterDemo.java](https://github.com/ttting/redis-bloomfilter/blob/master/src/test/java/me/ttting/common/hash/RedisBloomFilterDemo.java)

###版本说明

|  版本  | 说明 |
| --------- | ------ |
| 1.0.0-SNAPSHOT | 初始版本，基于jedis实现Bloomfilter |
| 1.1.0  | 1 基于redis pipeline优化redis set/get性能 2 支持基于spring-redis-template 创建Bloomfilter |

更多细节可以参考[BloomFilter基于redis的实现](https://github.com/ttting/blog/blob/master/redis/BloomFilter%E5%9F%BA%E4%BA%8Eredis%E7%9A%84%E5%AE%9E%E7%8E%B0.md)

License
-------

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

