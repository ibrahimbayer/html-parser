package org.ibayer.html.parser.service;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.ibayer.html.parser.domain.model.HeaderElements;
import org.ibayer.html.parser.domain.model.LinkElement;
import org.ibayer.html.parser.domain.model.LoginFormElement;
import org.ibayer.html.parser.domain.model.WebPage;
import org.ibayer.html.parser.domain.model.enumerator.LinkType;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WebPageService {

	private final JSoupParserService jSoupService;

	private final HtmlService htmlService;

	@Autowired
	public WebPageService(final JSoupParserService jSoupService, final HtmlService htmlService) {
		this.jSoupService = jSoupService;
		this.htmlService = htmlService;
	}

	/**
	 * Parses the incoming url parameter and retursn all desired details of
	 * {@link WebPage}<br/>
	 * 
	 * @param url
	 *            Http(s) url to parse and retrieve details
	 * @return a new {@link WebPage} instance storing extracted details
	 * @throws InterruptedException
	 */
	public WebPage get(final String url) {
		final Document document = jSoupService.getDocument(url);
		final URI uri = CommonUtils.getUri(document.baseUri(), false);
		final String htmlVersion = htmlService.getHtmlVersion(document);

		// transform jsoup links to LinkElement set.
		final Set<LinkElement> links = htmlService.getLinks(document,uri);

		List<HeaderElements> headerElements = htmlService.getHeaderElements(document);
		
		LoginFormElement loginForm = htmlService.detectLoginForm(document);
		
		String title = jSoupService.getTitle(document);
		
		return WebPage.builder().url(document.baseUri()).htmlVersion(htmlVersion).title(title)
				.loginForm(loginForm).headerElements(headerElements)
				.externalLinks(links.stream().filter(link -> LinkType.EXTERNAL.equals(link.getLinkType()))
						.collect(Collectors.toSet()))
				.internalLinks(links.stream().filter(link -> LinkType.INTERNAL.equals(link.getLinkType()))
						.collect(Collectors.toSet()))
				.build();
	}

}
