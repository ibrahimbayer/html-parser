package org.ibayer.html.parser.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Configures jackson {@link ObjectMapper}
 * 
 * @author ibrahim.bayer
 *
 */
@Configuration
public class JacksonConfiguration {

	/**
	 * Returns new {@link ObjectMapper}
	 * @return
	 */
	@Bean
	public ObjectMapper getObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		// ignore null values in response
		mapper.setSerializationInclusion(Include.NON_NULL);
		return mapper;
	}
}
