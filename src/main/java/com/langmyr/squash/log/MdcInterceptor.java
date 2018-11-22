package com.langmyr.squash.log;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Clears the MDC (Mapped Diagnostic Context) on every request, and sets a request id.
 * @see http://logback.qos.ch/manual/mdc.html
 */
@Component
public class MdcInterceptor extends HandlerInterceptorAdapter
{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        MdcUtil.clear();
        MdcUtil.setRequestId(UUID.randomUUID().toString());
        return true;
    }
}