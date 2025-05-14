package com.shortner.url_shortner.service;

import com.shortner.url_shortner.repository.UrlMappingRepository;
import com.shortner.url_shortner.util.RandomStringGeneratorUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UrlShortenerService {
    private static final double THRESHOLD = 0.75;
    private long URL_LENGTH;
    private static final int BASE = 62;
    // Say we can max support 19 character url -> 62^19 ~ 3.76 * 10^33 unique combination in that length alone
    private static final double[] POWERS = new double[20];

    static {
        for (int i = 0; i < POWERS.length; i++) {
            POWERS[i] = Math.pow(BASE, i);
        }
    }

    @Autowired
    private RandomStringGeneratorUtil randomStringGeneratorUtil;

    @Autowired
    private UrlMappingService urlMappingService;

    @Autowired
    private CounterService counterService;

    @Transactional
    public String shortenUrl(String longUrl){
        int length = determineLength();
        String code;

        do{
            code = randomStringGeneratorUtil.generateRandomString(length);
        } while(urlMappingService.doesCodeExists(code));

        urlMappingService.saveUrlMapping(code, longUrl);

        counterService.getAndIncrementCounter();

        return code;
    }


    private int determineLength(){
        double count = counterService.getCounter();
        int length = counterService.getMaxLength();
        if(count > THRESHOLD * POWERS[length]){
            counterService.getAndIncrementMaxLength();
        }
        return counterService.getMaxLength();
    }


}
