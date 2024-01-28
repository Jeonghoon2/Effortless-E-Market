package com.effortless.effortlessmarket.common.component;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;

import static com.effortless.effortlessmarket.constants.HeaderConstant.*;


@Component
public class LoggerComponent {

    private String getClientIP(HttpServletRequest request) {
        String clientIP = request.getHeader(KEY_REQUEST_CLIENT_IP);
        return (clientIP != null) ? clientIP : request.getRemoteAddr();
    }

    public Object aroundRequestMapping(HttpServletRequest request, ProceedingJoinPoint joinPoint) throws Throwable {

        request.setAttribute(KEY_REQUEST_LOGGER_DO_WRITE_LOG, true);
        request.setAttribute(KEY_REQUEST_LOGGER_CLIENT_IP, getClientIP(request));

        long startMilli = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endMilli = System.currentTimeMillis();
        request.setAttribute(KEY_REQUEST_LOGGER_ELAPSED_MILLI, endMilli - startMilli);

        return result;

    }
}
