package com.effortless.effortlessmarket.global.filter;

import com.effortless.effortlessmarket.global.constants.HeaderConstant;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

import static com.effortless.effortlessmarket.global.constants.HeaderConstant.KEY_REQUEST_LOGGER_ELAPSED_MILLI;
import static com.effortless.effortlessmarket.global.constants.HeaderConstant.KEY_REQUEST_LOGGER_REQUEST_INCOMING_DATETIME;


@Component
public class ServletWrappingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        /* 요청 진입 시점 기록*/
        requestWrapper.setAttribute(KEY_REQUEST_LOGGER_REQUEST_INCOMING_DATETIME, System.currentTimeMillis());
        filterChain.doFilter(requestWrapper, responseWrapper);

        responseWrapper.copyBodyToResponse();
    }
}
