package com.effortless.effortlessmarket.common.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Setter
@Getter
@Builder
public class LogDto {
    String traceId;
    String clientIp;
    String time;
    String path;
    String method;
    String request;
    String response;
    String statusCode;
    Long elapsedTimeMillis;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String toString() {
        try {
          return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            log.error("[Error] Converting LogDto to JSON String: {}", e.getMessage());
            return null;
        }
    }
}
