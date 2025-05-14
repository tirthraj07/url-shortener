package com.shortner.url_shortner.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ShortenRequestDTO {
    @NotNull
    private String longUrl;
}
