package org.ibayer.html.parser.service.validator;

import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

import org.ibayer.html.parser.domain.model.LinkElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

/**
 * Validates http links in web page. <br/>
 * 
 * @author ibrahim.bayer
 *
 */
@Component
@Slf4j
public class LinksValidator {

	private final ExecutorService executorService;

	private final RestTemplate restTemplate;

	@Autowired
	public LinksValidator(final ExecutorService executorService, final RestTemplate restTemplate) {
		this.executorService = executorService;
		this.restTemplate = restTemplate;
	}

	public void validate(final Set<LinkElement> links) {
		final CountDownLatch latch = new CountDownLatch(links.size());
		log.debug("Link check execution started " + links.size());
		links.forEach(link -> {
			LinkValidator linkValidator = new LinkValidator(link, latch, restTemplate);
			executorService.submit(linkValidator);
		});
		log.debug("Waiting threads to complete process");
		try {
			latch.await();
		} catch (InterruptedException e) {
			log.info("Interrupted", e);
		}
		log.debug("All threads completed process");
	}
}
