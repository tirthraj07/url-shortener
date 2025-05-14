package com.shortner.url_shortner.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShortenResponseDTO {
    private String shortUrl;
    private String longUrl;
}
