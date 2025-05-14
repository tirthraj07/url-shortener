package com.shortner.url_shortner.service;

import com.shortner.url_shortner.repository.UrlMappingRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CounterService {

    private final AtomicLong approximateCount = new AtomicLong(0);
    private final AtomicInteger maxLength = new AtomicInteger(5);

    private static final Logger logger = LoggerFactory.getLogger(CounterService.class);

    @Autowired
    private UrlMappingRepository urlMappingRepository;

    @PostConstruct
    public void init(){
        try {
            // Get the maximum length of the code field
            maxLength.set(urlMappingRepository.findMaxCodeLength());
            // Count how many records have the maximum code length
            long count = urlMappingRepository.countByCodeLength(maxLength.get());
            approximateCount.set(count);
            logger.info("Approximate Count Initialized : {}", approximateCount.get());
        } catch (Exception e) {
            logger.error("Error initializing approximate count", e);
        }
    }

    @Scheduled(fixedRate = 300000)
    public void updateApproximateCount() {
        try {
            // Get the maximum length of the code field
            maxLength.set(urlMappingRepository.findMaxCodeLength());
            // Count how many records have the maximum code length
            long count = urlMappingRepository.countByCodeLength(maxLength.get());
            approximateCount.set(count);
            logger.info("Approximate Count updated: {}", approximateCount.get());
        } catch (Exception e) {
            logger.error("Error updating approximate count", e);
        }
    }

    public int getMaxLength(){
        return maxLength.get();
    }

    public int getAndIncrementMaxLength(){
        return maxLength.getAndIncrement();
    }

    public long getAndIncrementCounter(){
        return approximateCount.getAndIncrement();
    }

    public long getCounter(){
        return approximateCount.get();
    }

}
