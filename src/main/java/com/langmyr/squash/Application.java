package com.langmyr.squash;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import com.langmyr.squash.log.AuditLogFilter;

@SpringBootApplication
public class Application
{
	public static void main(String[] args) throws IOException {
		SpringApplication.run(Application.class, args);
	}

    @Bean
    public FilterRegistrationBean<AuditLogFilter> registerAuditLogFilter() {
        FilterRegistrationBean<AuditLogFilter> auditLogFilterRegistrationBean = new FilterRegistrationBean<>();
        auditLogFilterRegistrationBean.setFilter(new AuditLogFilter());
        auditLogFilterRegistrationBean.setName("AuditLogFilter");
        auditLogFilterRegistrationBean.addUrlPatterns("/*");
        auditLogFilterRegistrationBean.setOrder(2);
        return auditLogFilterRegistrationBean;
    }
}