package com.shortner.url_shortner.service;

import com.shortner.url_shortner.event.AccessEvent;
import com.shortner.url_shortner.util.KafkaProducer;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDateTime;

@Service
public class EventService {
    @Autowired
    private KafkaProducer kafkaProducer;

    private static final Logger logger = LoggerFactory.getLogger(EventService.class);

    private static final String ACCESS_EVENT_TOPIC = "url-access-events";

    public AccessEvent convertServletToAccessEvent(HttpServletRequest request) {
        logger.info("Converting HttpServletRequest to AccessEvent");
        AccessEvent event = new AccessEvent();

        event.setIpAddress(getClientIp(request));
        event.setUserAgent(request.getHeader("User-Agent"));
        event.setReferrer(request.getHeader("Referer"));  // Note: Sometimes it's "Referer"
        event.setRequestUri(request.getRequestURI());
        event.setHttpMethod(request.getMethod());
        event.setTimestamp(LocalDateTime.now());
        event.setSessionId(request.getSession(false) != null ? request.getSession(false).getId() : null);
        event.setHeaders(getHeadersMap(request));
        logger.debug("AccessEvent built: {}", event);
        return event;
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty()) {
            return ip.split(",")[0]; // First IP in the chain
        }
        return request.getRemoteAddr();
    }

    private Map<String, String> getHeadersMap(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                headers.put(name, request.getHeader(name));
            }
        }
        return headers;
    }


    public void publishAccessEventToKafka(HttpServletRequest request, String code){
        logger.info("publishAccessEventToKafka function called");
        AccessEvent event = convertServletToAccessEvent(request);
        event.setCode(code);
        kafkaProducer.send(ACCESS_EVENT_TOPIC, code, event);
    }

}
