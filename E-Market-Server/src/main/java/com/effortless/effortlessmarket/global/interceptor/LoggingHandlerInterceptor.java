package com.effortless.effortlessmarket.global.interceptor;

import com.effortless.effortlessmarket.global.dto.LogDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import java.nio.charset.StandardCharsets;

import static com.effortless.effortlessmarket.global.constants.HeaderConstant.KEY_REQUEST_LOGGER_DO_WRITE_LOG;
import static com.effortless.effortlessmarket.global.constants.HeaderConstant.KEY_REQUEST_LOGGER_REQUEST_INCOMING_DATETIME;

@Component // Spring 컴포넌트로 등록하여 관리되도록 함
public class LoggingHandlerInterceptor implements HandlerInterceptor {


    private final Logger logger = LoggerFactory.getLogger("KafkaLogger");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute(KEY_REQUEST_LOGGER_REQUEST_INCOMING_DATETIME, System.currentTimeMillis());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws JsonProcessingException {

        if (request.getAttribute(KEY_REQUEST_LOGGER_DO_WRITE_LOG) == null) {
            return;
        }

        /**
         *
         * ContentCachingRequestWrapper와 ContentCachingResponseWrapper는 Spring Web에서 제공하는 클래스로,
         * HTTP 요청과 응답의 내용을 캐싱하기 위한 래퍼(wrapper)입니다.
         *
         * 이 래퍼들은 원본 HttpServletRequest와 HttpServletResponse 객체를 감싸며,
         * 요청 본문과 응답 본문의 내용을 읽거나 수정하지 않고도 저장할 수 있게 해줍니다.
         * 이렇게 캐싱된 내용은 나중에 로깅, 디버깅, 검사 등의 목적으로 사용될 수 있습니다.
         *
         * */

        ContentCachingRequestWrapper cachingRequest = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        ContentCachingResponseWrapper cachingResponse = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);

        if (cachingRequest != null && cachingResponse != null) {
            LogDto message = LogDto.toEntity(request, response, cachingRequest, cachingResponse);
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonLog = objectMapper.writeValueAsString(message);
            logger.info("{}", jsonLog);
        }
    }
}
