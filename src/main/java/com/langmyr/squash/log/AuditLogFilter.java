package com.langmyr.squash.log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuditLogFilter implements Filter
{
    private static Logger logger = LoggerFactory.getLogger(AuditLogFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.trace("[format: date | loglevel | thread id | IP | action | response status | response time | (exception info if any) | request id ]");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        long startTime = System.currentTimeMillis();
        MdcUtil.clear(); 
        
        try {
            chain.doFilter(servletRequest, servletResponse);
        } 
        catch (Throwable ex) {
            logRequest(servletRequest, servletResponse, startTime, ex);
            throw ex;
        }
        
        logRequest(servletRequest, servletResponse, startTime, null);
    }

    private void logRequest(ServletRequest servletRequest, ServletResponse servletResponse, long startTime, Throwable ex) {
        
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse ;
        
        long responseTime = 0;
        
        String action = request.getMethod() + " " + request.getRequestURI();
        String ip = request.getHeader("X-Forwarded-For");
        if(ip == null)
        {
            try {
                InetAddress originIp = InetAddress.getByName(request.getRemoteAddr());
                ip = originIp.getHostAddress();
            } 
            catch (UnknownHostException e) {
                ip = "UnknownHost" ;
            }
            ip = setNAIfNull(ip);
        }
        
        String exeptionInfo = (ex != null ? " (Unhandled exeption: "+ ex.getMessage()+")" : "-");

        responseTime = getResponseTime(startTime);
        logger.trace(String.format(
                "%1$s %2$s %3$d %4$s %5$s", 
                ip, 
                action,
                response.getStatus(),
                responseTime,
                exeptionInfo));
    }
    
    private String setNAIfNull(String value) {
        if (value == null) {
            return "NA";
        }
        return value;
    }

    private long getResponseTime(long startTime) {
        return System.currentTimeMillis() - startTime;
    }

    @Override
    public void destroy() {
        logger.trace("Audit log filter destroyed");
    }
}