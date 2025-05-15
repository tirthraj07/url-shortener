package com.shortner.url_shortner.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessEvent {
    private String ipAddress;
    private String userAgent;
    private String referrer;
    private String requestUri;
    private String httpMethod;
    private LocalDateTime timestamp;
    private Map<String, String> headers;
    private String sessionId;
    private String code;
}