package org.ibayer.html.parser.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Configures rest template behavior.<br/>
 * Rest template timeouts are configured via system parameters : <br/>
 * org.ibayer.html.parser.connection.request-timeout=(numeric milliseconds
 * value) <br/>
 * org.ibayer.html.parser.connection.connect-timeout=(numeric milliseconds
 * value) <br/>
 * org.ibayer.html.parser.connection.read-timeout=(numeric milliseconds value)
 * <br/>
 * for configuration details check <a href=
 * "https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html">Spring
 * Boot Configuration"</a>
 * 
 * @author ibrahim.bayer
 *
 */
@Configuration
public class RestTemplateConfiguration {

	/**
	 * Configuration for rest template http request, internally returns
	 * {@link HttpComponentsClientHttpRequestFactory}
	 * 
	 * @return
	 */
	@Bean
	@ConfigurationProperties(prefix = "org.ibayer.html.parser.connection")
	public HttpComponentsClientHttpRequestFactory getHttpRequestFactory() {
		return new HttpComponentsClientHttpRequestFactory();
	}

	/**
	 * Generates new rest template using http request factory
	 * 
	 * @return
	 */
	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate(getHttpRequestFactory());
	}
}
