package com.effortless.effortlessmarket.global.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.effortless.effortlessmarket.global.constants.HeaderConstant.KEY_REQUEST_LOGGER_REQUEST_INCOMING_DATETIME;
import static com.effortless.effortlessmarket.global.constants.HeaderConstant.KEY_RESPONSE_HEADER_TRACE_ID;

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

    public static LogDto toEntity(HttpServletRequest request,
                                  HttpServletResponse response,
                                  ContentCachingRequestWrapper cachingRequest,
                                  ContentCachingResponseWrapper cachingResponse) {

        /* 진입 시간 수행 시간 계산 */
        long requestTime = (Long) request.getAttribute(KEY_REQUEST_LOGGER_REQUEST_INCOMING_DATETIME);
        long elapsedTimeMillis = System.currentTimeMillis() - requestTime;

        /* request 직렬화 */
        String requestJson= "";
        try{
            requestJson  = objectMapper.readTree(cachingRequest.getContentAsByteArray()).toString();
        }catch (Exception e){
            e.printStackTrace();
        }

        return LogDto.builder()
                .traceId(response.getHeader(KEY_RESPONSE_HEADER_TRACE_ID))
                .clientIp(request.getRemoteAddr())
                .path(request.getRequestURI())
                .method(request.getMethod())
                .statusCode(String.valueOf(response.getStatus()))
                .time(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").format(new Date(requestTime)))
                .elapsedTimeMillis(elapsedTimeMillis)
                .request(requestJson)
                .response(byteArraytoString(cachingResponse.getContentAsByteArray()))
                .build();
    }

    private static String byteArraytoString(byte[] buf) {
        if (buf != null && buf.length > 0) {
            return new String(buf, StandardCharsets.UTF_8);
        }
        return "";
    }
}
