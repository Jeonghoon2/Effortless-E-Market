package com.effortless.effortlessmarket.global.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.effortless.effortlessmarket.global.constants.HeaderConstant.*;

@Data
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

    public static LogDto toEntity(ContentCachingRequestWrapper cachingRequest,
                                  ContentCachingResponseWrapper cachingResponse) {

        /* 진입과 나간 시간 계산 */
        long requestTime = (Long) cachingRequest.getAttribute(KEY_REQUEST_LOGGER_REQUEST_INCOMING_DATETIME);
        long responseTime = (Long) cachingRequest.getAttribute(KEY_REQUEST_LOGGER_ELAPSED_MILLI);
        long elapsedTimeMillis = responseTime - requestTime;

        /* request 직렬화 */
        String requestJson = "";
        try {
            requestJson = objectMapper.readTree(cachingRequest.getContentAsByteArray()).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return LogDto.builder()
                .traceId(cachingResponse.getHeader(KEY_RESPONSE_HEADER_TRACE_ID))
                .clientIp(cachingRequest.getRemoteAddr())
                .path(cachingRequest.getRequestURI())
                .method(cachingRequest.getMethod())
                .statusCode(String.valueOf(cachingResponse.getStatus()))
                .time(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").format(new Date(requestTime)))
                .elapsedTimeMillis(elapsedTimeMillis)
                .request(requestJson)
                .response(byteArrayToString(cachingResponse.getContentAsByteArray()))
                .build();
    }

    private static String byteArrayToString(byte[] buf) {
        if (buf != null && buf.length > 0) {
            return new String(buf, StandardCharsets.UTF_8);
        }
        return "";
    }
}
