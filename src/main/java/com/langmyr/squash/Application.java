package com.langmyr.squash;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.apache.commons.lang.StringUtils.isNotEmpty;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.langmyr.squash.log.AuditLogFilter;

import okhttp3.OkHttpClient;

@SpringBootApplication
@PropertySources({
		@PropertySource(value = "classpath:environment.properties")
})
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

	@Bean
	public OkHttpClient okHttpClient(@Value(("${http.proxy:}")) String proxy) throws MalformedURLException {
		OkHttpClient.Builder builder = new OkHttpClient.Builder()
				.connectTimeout(10, SECONDS)
				.writeTimeout(10, SECONDS)
				.readTimeout(30, SECONDS);

		if (isNotEmpty(proxy)) {
			URL urlProxy = new URL(proxy);
			builder.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(urlProxy.getHost(), urlProxy.getPort())));
		}

		return builder.build();
	}

	@Bean
	public RestTemplate restTemplate(OkHttpClient okHttpClient) {
		return new RestTemplate(new OkHttp3ClientHttpRequestFactory(okHttpClient));
	}
}