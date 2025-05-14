package com.shortner.url_shortner.controller;

import com.shortner.url_shortner.dto.ApiResponse;
import com.shortner.url_shortner.dto.ShortenRequestDTO;
import com.shortner.url_shortner.dto.ShortenResponseDTO;
import com.shortner.url_shortner.service.UrlShortenerService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShortenerController {

    private static final Logger logger = LoggerFactory.getLogger(ShortenerController.class);

    @Autowired
    UrlShortenerService urlShortenerService;

    @PostMapping("/shorten")
    public ResponseEntity<ShortenResponseDTO> shortenUrl(@Valid @RequestBody ShortenRequestDTO shortenRequestDTO){
        String longUrl = shortenRequestDTO.getLongUrl();
        String shortUrl = urlShortenerService.shortenUrl(longUrl);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ShortenResponseDTO(shortUrl, longUrl));
    }

}
