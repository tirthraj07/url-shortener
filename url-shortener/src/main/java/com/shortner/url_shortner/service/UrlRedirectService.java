package com.shortner.url_shortner.service;

import com.shortner.url_shortner.entity.UrlMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class UrlRedirectService {
    private static final Logger logger = LoggerFactory.getLogger(UrlRedirectService.class);

    @Autowired
    private CacheService cacheService;

    @Autowired
    private UrlMappingService urlMappingService;


    public Optional<String> getLongUrl(String code) {
        logger.info("UrlRedirectionService invoked : code : {}", code);

        String longUrl = cacheService.getLongUrl(code);
        if (longUrl != null) {
            logger.info("Url mapping found in cache: {}", longUrl);
            CompletableFuture.runAsync(()->{
                logger.info("Updating TTL Async");
                cacheService.updateTTL(code, 3600);
            });

            return Optional.of(longUrl);
        }

        logger.info("Cache miss for : {}", code);
        // Fetch from DB once
        Optional<UrlMapping> mappingOpt = urlMappingService.getUrlMappingByCode(code);
        mappingOpt.ifPresent(mapping -> {
            String url = mapping.getLongUrl();
            logger.info("Url mapping found in database: {} -> {}", code, url);
            // Async cache population -> micro-optimizations
            CompletableFuture.runAsync(() ->{
                    logger.info("Populating Cache Async");
                    cacheService.saveUrlMappingWithTTL(code, url, 3600);
            }).exceptionally(ex -> {
                logger.error("Failed to update cache for {}", code, ex);
                return null;
            });
        });

        return mappingOpt.map(UrlMapping::getLongUrl);
    }


}
