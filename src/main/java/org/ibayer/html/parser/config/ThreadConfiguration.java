package org.ibayer.html.parser.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configures thread executor service
 * 
 * @author ibrahim.bayer
 *
 */
@Configuration
public class ThreadConfiguration {

	@Value("${org.ibayer.html.parser.threadcount}")
	Integer threadCount;

	/**
	 * returns new executor service with thread count.<br/>
	 * 
	 * @return
	 */
	@Bean
	public ExecutorService getExecutorService() {
		return Executors.newFixedThreadPool(threadCount);
	}

}
