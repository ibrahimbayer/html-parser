package org.ibayer.html.parser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

/**
 * Web page parser application Spring Boot main class.
 *
 */
@SpringBootApplication
@Configuration
public class WebPageParserApplication {
	public static void main(String[] args) {
		SpringApplication.run(WebPageParserApplication.class, args);
	}
}
