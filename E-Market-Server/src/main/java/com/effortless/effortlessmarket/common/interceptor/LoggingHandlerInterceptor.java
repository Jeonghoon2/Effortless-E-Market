package com.effortless.effortlessmarket.common.interceptor;

import com.effortless.effortlessmarket.common.dto.LogDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.effortless.effortlessmarket.constants.HeaderConstant.*;

@Slf4j
public class LoggingHandlerInterceptor implements HandlerInterceptor {

    /* Kafka로 Interceptor로 보내기 위한 message의 임시 저장소*/
    public static Map<String ,String> sharedData = new HashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();


    private final Logger logger = LoggerFactory.getLogger("KafkaLogger");
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute(KEY_REQUEST_LOGGER_REQUEST_INCOMING_DATETIME, LocalDateTime.now().toString());
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    private String byteArraytoString(byte[] buf) {
        if (buf.length > 0) {
            return new String(buf, 0, buf.length, StandardCharsets.UTF_8);
        }
        return null;
    }



    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);

        /**
         * Annotation Check
         * @ExcludeLogging
         * @InCludeLogging
         * */
        if (request.getAttribute(KEY_REQUEST_LOGGER_DO_WRITE_LOG) == null){
            return;
        }

        ContentCachingRequestWrapper cachingRequest = (ContentCachingRequestWrapper) request;

        ContentCachingResponseWrapper cachingResponse = (ContentCachingResponseWrapper) response;



        LogDto message = LogDto.builder()
                .traceId(response.getHeader(KEY_RESPONSE_HEADER_TRACE_ID))
                .clientIp(request.getAttribute(KEY_REQUEST_LOGGER_CLIENT_IP).toString())
                .path(request.getRequestURI())
                .method(request.getMethod())
                .statusCode(String.valueOf(response.getStatus()))
                .time((String) request.getAttribute(KEY_REQUEST_LOGGER_REQUEST_INCOMING_DATETIME))
                .elapsedTimeMillis((Long) request.getAttribute(KEY_REQUEST_LOGGER_ELAPSED_MILLI))
                .request(String.valueOf(objectMapper.readTree(cachingRequest.getContentAsByteArray())))
                .response(byteArraytoString(cachingResponse.getContentAsByteArray()))
                .build();


        logger.info("{}",message.toString());
    }


}

