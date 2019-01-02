package cn.no7player.config;


import cn.no7player.config.redis.FastJsonSerializer;
import cn.no7player.config.redis.RedisCaches;
import com.alibaba.fastjson.parser.ParserConfig;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.hash.Hashing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.ClassUtils;

import javax.annotation.Resource;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig extends CachingConfigurerSupport {
    Logger logger = LoggerFactory.getLogger(CacheConfig.class);

    @Override
    public KeyGenerator keyGenerator() {
        //return  new CacheKeyGenerator();
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
                //sb.append(target.getClass().getSimpleName());
                sb.append(method.getDeclaringClass().getSimpleName());
                sb.append(":");
                sb.append(method.getName());

                for (Object o : params) {
                    sb.append("_").append(o);
                }
                return sb.toString();
            }
        };
    }

    @Configuration
    public static class CaffeineConfiguration {
        public static final int DEFAULT_MAXSIZE = 50000;
        public static final int DEFAULT_TTL = 10;

        @Bean
        public CacheManager caffeineCacheManager() {
            SimpleCacheManager cacheManager = new SimpleCacheManager();

            ArrayList<CaffeineCache> caches = Lists.newArrayList();
            for (Caches c : Caches.values()) {
                caches.add(new CaffeineCache(c.name(),
                        Caffeine.newBuilder().recordStats()
                                .expireAfterWrite(c.getTtl(), TimeUnit.SECONDS)
                                .maximumSize(c.getMaxSize())
                                .build())
                );
            }

            cacheManager.setCaches(caches);
            return cacheManager;
        }

        /**
         * 定義cache名稱、超時時長（秒）、最大容量
         * 每个cache缺省：10秒超时、最多缓存50000条数据，需要修改可以在                构造方法的参数中指定。
         */
        public enum Caches {
            ETERNAL_CACHE(3600), //有效期5秒
            TEMPORARY_CACHE(10), //缺省10秒
            OTHER_CACHE(300, 1000), //5分钟，最大容量1000
            ;

            private int maxSize = DEFAULT_MAXSIZE;    //最大數量
            private int ttl = DEFAULT_TTL;        //过期时间（秒）

            Caches() {
            }

            Caches(int ttl) {
                this.ttl = ttl;
            }
            Caches(int ttl, int maxSize) {
                this.ttl = ttl;
                this.maxSize = maxSize;
            }

            public int getMaxSize() {
                return maxSize;
            }

            public int getTtl() {
                return ttl;
            }
        }
    }


    @Configuration
    public static class MyRedisCacheConfiguration {

        @Resource
        private LettuceConnectionFactory lettuceConnectionFactory;

        @Bean
        @Primary
        public CacheManager redisCacheManager() {
            RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
            RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.RedisCacheManagerBuilder
                    .fromConnectionFactory(lettuceConnectionFactory);
            Set<String> cacheNames = RedisCaches.mapValues().keySet();
            config = config.entryTtl(Duration.ofMinutes(1))  // 设置缓存的默认过期时间，也是使用Duration设置
                    .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer()))
                    .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer()))
                    .disableCachingNullValues();     // 不缓存空值

            ParserConfig.getGlobalInstance().addAccept("cn.csfz.gemi.");

            // 对每个缓存空间应用不同的配置
            Map<String, RedisCacheConfiguration> configMap = Maps.newHashMap();
            Map<String, Long> redisCachesMap = RedisCaches.mapValues();
            for (String cacheKey : redisCachesMap.keySet()) {
                configMap.put(cacheKey, config.entryTtl(Duration.ofSeconds(redisCachesMap.get(cacheKey))));
            }
            builder.initialCacheNames(cacheNames).withInitialCacheConfigurations(configMap);
            return builder.build();
        }

        @Bean
        public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
            RedisTemplate<Object, Object> template = new RedisTemplate<Object, Object>();
            template.setConnectionFactory(redisConnectionFactory);
            // Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
            // ObjectMapper om = new ObjectMapper();
            // om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
            // om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
            // jackson2JsonRedisSerializer.setObjectMapper(om);
            // template.setValueSerializer(jackson2JsonRedisSerializer);
            template.setKeySerializer(keySerializer());
            template.setHashKeySerializer(new StringRedisSerializer());
            template.setValueSerializer(valueSerializer());

            return template;
        }

        private RedisSerializer<String> keySerializer() {
            return new StringRedisSerializer();
        }

        private RedisSerializer<Object> valueSerializer() {
            return new FastJsonSerializer();
        }


    }

    class CacheKeyGenerator implements KeyGenerator {

        // custom cache key
        public static final int NO_PARAM_KEY = 0;
        public static final int NULL_PARAM_KEY = 53;

        @Override
        public Object generate(Object target, Method method, Object... params) {

            StringBuilder key = new StringBuilder();
            key.append(target.getClass().getSimpleName()).append(".").append(method.getName()).append(":");
            if (params.length == 0) {
                return key.append(NO_PARAM_KEY).toString();
            }
            for (Object param : params) {
                if (param == null) {
                    logger.warn("input null param for Spring cache, use default key={}", NULL_PARAM_KEY);
                    key.append(NULL_PARAM_KEY);
                } else if (ClassUtils.isPrimitiveArray(param.getClass())) {
                    int length = Array.getLength(param);
                    for (int i = 0; i < length; i++) {
                        key.append(Array.get(param, i));
                        key.append(',');
                    }
                } else if (ClassUtils.isPrimitiveOrWrapper(param.getClass()) || param instanceof String) {
                    key.append(param);
                } else {
                    logger.warn("Using an object as a cache key may lead to unexpected results. " +
                            "Either use @Cacheable(key=..) or implement CacheKey. Method is " + target.getClass() + "#" + method.getName());
                    key.append(param.hashCode());
                }
                key.append('-');
            }

            String finalKey = key.toString();
            long cacheKeyHash = Hashing.murmur3_128().hashString(finalKey, Charset.defaultCharset()).asLong();
            logger.debug("using cache key={} hashCode={}", finalKey, cacheKeyHash);
            return key.toString();
        }
    }


}
