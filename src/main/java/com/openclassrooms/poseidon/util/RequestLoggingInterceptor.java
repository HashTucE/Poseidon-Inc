package com.openclassrooms.poseidon.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


import java.util.Collections;

public class RequestLoggingInterceptor implements HandlerInterceptor {

    private static final Logger logger = LogManager.getLogger(RequestLoggingInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // Log the request information
        logger.info("Received request: method=" + request.getMethod() + ", url=" + request.getRequestURL().toString());
        logger.info("Headers: " + getHeaders(request));
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) {
        // Do nothing
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        if (ex != null) {
            logger.error("An exception occurred during request processing: ", ex);
        }
    }

    private String getHeaders(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        for (String headerName : Collections.list(request.getHeaderNames())) {
            sb.append(headerName).append("=").append(request.getHeader(headerName)).append(", ");
        }
        return sb.toString();
    }

}
