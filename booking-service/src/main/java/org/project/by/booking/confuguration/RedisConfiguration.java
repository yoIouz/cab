package org.project.by.booking.confuguration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;

import java.time.Duration;

@Configuration
public class RedisConfiguration {

    private static final int REDIS_TTL_MINUTES = 30;

    @Bean
    public RedisCacheConfiguration redisCacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(REDIS_TTL_MINUTES))
                .prefixCacheNameWith("booking-cache");
    }

}
