package com.effortless.effortlessmarket.global.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

import static com.effortless.effortlessmarket.global.constants.HeaderConstant.KEY_RESPONSE_HEADER_TRACE_ID;


public class MdcHandlerInterceptor implements HandlerInterceptor {

    private void setTranceId(HttpServletResponse response){
        String traceId = UUID.randomUUID().toString();
        MDC.put("traceId",traceId);
        response.setHeader(KEY_RESPONSE_HEADER_TRACE_ID, traceId);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        setTranceId(response);
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
        MDC.clear();
    }
}
