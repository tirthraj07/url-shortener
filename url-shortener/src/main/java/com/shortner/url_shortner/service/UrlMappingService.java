package com.shortner.url_shortner.service;

import com.shortner.url_shortner.entity.UrlMapping;
import com.shortner.url_shortner.repository.UrlMappingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UrlMappingService {

    private static final Logger logger = LoggerFactory.getLogger(UrlMappingService.class);

    @Autowired
    private UrlMappingRepository urlMappingRepository;

    public List<UrlMapping> getAllMappings(){
        return urlMappingRepository.findAll();
    }

    public UrlMapping saveUrlMapping(String code, String longUrl){
        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setCode(code);
        urlMapping.setLongUrl(longUrl);
        return urlMappingRepository.save(urlMapping);
    }

    public Optional<UrlMapping> getUrlMappingByCode(String code){
        logger.info("Querying database for code : {}", code);
        return urlMappingRepository.findByCode(code);
    }

    public List<UrlMapping> getUrlMappingsByLongUrl(String longUrl){
        return urlMappingRepository.findByLongUrl(longUrl);
    }

    public boolean doesCodeExists(String code){
        return urlMappingRepository.findByCode(code).isPresent();
    }

}
