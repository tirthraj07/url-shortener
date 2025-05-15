package com.shortner.url_shortner.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
public class CacheService {

    private static final Logger logger = LoggerFactory.getLogger(CacheService.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String URL_PREFIX = "url:";

    public void saveUrlMapping(String code, String longUrl){
        String shortUrl = URL_PREFIX + code;
        logger.info("Saving new key-value pair in redis: {} -> {}", shortUrl, longUrl);
        redisTemplate.opsForValue().set(shortUrl, longUrl);
    }

    public void saveUrlMappingWithTTL(String code, String longUrl, long ttlSeconds){
        String shortUrl = URL_PREFIX + code;
        logger.info("Saving new key-value pair in redis: {} -> {} with ttl = {} sec ", shortUrl, longUrl, ttlSeconds);
        redisTemplate.opsForValue().set(shortUrl, longUrl, Duration.ofSeconds(ttlSeconds));
    }

    public void updateTTL(String code, long ttlSeconds) {
        logger.info("Updating ttl for key : {}", code);
        String shortUrl = URL_PREFIX + code;
        String longUrl = (String) redisTemplate.opsForValue().get(shortUrl);
        if (longUrl != null) {
            redisTemplate.opsForValue().set(shortUrl, longUrl, Duration.ofSeconds(ttlSeconds));
        }
    }

    public String getLongUrl(String code){
        String shortUrl = URL_PREFIX + code;
        return (String) redisTemplate.opsForValue().get(shortUrl);
    }

    public void deleteMapping(String code){
        String shortUrl = URL_PREFIX + code;
        redisTemplate.delete(shortUrl);
    }


    public Map<String, String> getAllUrlMappings() throws RuntimeException {
        Map<String, String> urlMappings = new HashMap<>();

        try (Cursor<byte[]> cursor = redisTemplate.getConnectionFactory().getConnection()
                .scan(ScanOptions.scanOptions().match("url:*").count(100).build())) {

            while (cursor.hasNext()) {
                String key = new String(cursor.next());
                String value = (String) redisTemplate.opsForValue().get(key);
                urlMappings.put(key, value);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error fetching URL mappings from Redis", e);
        }

        return urlMappings;
    }

}
