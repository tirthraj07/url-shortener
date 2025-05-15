package com.shortner.url_shortner.controller;

import com.shortner.url_shortner.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/redis")
public class RedisController {

    @Autowired
    private CacheService cacheService;

    @GetMapping("/data")
    public ResponseEntity<Map<String, String>> getRedisData(){
        return ResponseEntity.ok(cacheService.getAllUrlMappings());
    }

}
