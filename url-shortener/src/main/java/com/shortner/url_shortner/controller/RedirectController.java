package com.shortner.url_shortner.controller;

import com.shortner.url_shortner.entity.UrlMapping;
import com.shortner.url_shortner.service.EventService;
import com.shortner.url_shortner.service.UrlMappingService;
import com.shortner.url_shortner.service.UrlRedirectService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


import java.net.URI;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
public class RedirectController {

    @Autowired
    private UrlRedirectService urlRedirectService;

    @Autowired
    private EventService eventService;

    @GetMapping("/{shortUrl}")
    public ResponseEntity<Void> redirectToLongUrl(@PathVariable("shortUrl") String shortUrl, HttpServletRequest request) {
        String longUrl = urlRedirectService.getLongUrl(shortUrl).orElse(null);

        CompletableFuture.runAsync(()->{
            eventService.publishAccessEventToKafka(request, shortUrl);
        });


        if (longUrl != null) {
            return ResponseEntity
                    .status(HttpStatus.FOUND)
                    .location(URI.create(longUrl))
                    .build();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Short URL not found");
        }
    }

}
