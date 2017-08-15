package org.ibayer.html.parser.service.test;

import java.util.Set;
import java.util.concurrent.ExecutorService;

import org.ibayer.html.parser.domain.model.LinkElement;
import org.ibayer.html.parser.service.validator.LinksValidator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.google.common.collect.ImmutableSet;

public class LinksValidatorTest {

	@Mock
	ExecutorService executorService;

	@Mock
	RestTemplate restTemplate;

	@Spy
	LinksValidator linksValidator;

	@Before
	public void setup() {
		executorService = Mockito.mock(ExecutorService.class);
		restTemplate = Mockito.mock(RestTemplate.class);
		linksValidator = new LinksValidator(executorService, restTemplate);
	}

	@Test
	public void shouldReturnActiveLink() {
		Set<LinkElement> links = ImmutableSet.of(LinkElement.builder().href("http://www.example.com").build());

		Mockito.when(executorService.submit(Mockito.any(Runnable.class))).thenAnswer(answer -> {
			((Runnable) answer.getArguments()[0]).run();
			return null;
		});
		Mockito.when(restTemplate.getForEntity(Mockito.anyString(), Mockito.any()))
				.thenReturn(new ResponseEntity<>(Mockito.anyString(), HttpStatus.OK));
		linksValidator.validate(links);
		Assert.assertTrue(links.iterator().next().getActive());
	}

	@Test
	public void shouldReturnInActiveLink() {
		Set<LinkElement> links = ImmutableSet.of(LinkElement.builder().href("http://www.example.com").build());

		Mockito.when(executorService.submit(Mockito.any(Runnable.class))).thenAnswer(answer -> {
			((Runnable) answer.getArguments()[0]).run();
			return null;
		});
		Mockito.when(restTemplate.getForEntity(Mockito.anyString(), Mockito.any()))
				.thenReturn(new ResponseEntity<>(Mockito.anyString(), HttpStatus.BAD_REQUEST));
		linksValidator.validate(links);
		Assert.assertFalse(links.iterator().next().getActive());
	}

}
