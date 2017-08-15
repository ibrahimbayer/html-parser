package org.ibayer.html.parser.service.validator;

import java.util.concurrent.CountDownLatch;

import org.ibayer.html.parser.domain.model.LinkElement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

/**
 * Validates links using threading.
 * 
 * @author ibrahim.bayer
 *
 */
@Slf4j
public class LinkValidator implements Runnable {
	private final LinkElement link;
	private final CountDownLatch latch;
	private final RestTemplate restTemplate;

	public LinkValidator(final LinkElement link, final CountDownLatch latch, final RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
		this.link = link;
		this.latch = latch;
	}

	@Override
	public void run() {
		try {
			ResponseEntity<String> response = restTemplate.getForEntity(link.getHref(), String.class);
			if (response.getStatusCode() != HttpStatus.OK) {
				throw new RestClientException("HttpStatus = " + response.getStatusCode().toString());
			}
			link.setActive(Boolean.TRUE);
		} catch (Exception e) {
			log.error("Get link execution problem" + link.getHref(), e);
			link.setActive(Boolean.FALSE);
			link.setStatusExplanation(e.getMessage());
		} finally {
			latch.countDown();
		}
	}

}
