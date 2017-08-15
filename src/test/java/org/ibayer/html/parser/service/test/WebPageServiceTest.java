package org.ibayer.html.parser.service.test;

import java.net.URI;
import java.util.Arrays;

import org.ibayer.html.parser.domain.model.HeaderElements;
import org.ibayer.html.parser.domain.model.LinkElement;
import org.ibayer.html.parser.domain.model.WebPage;
import org.ibayer.html.parser.domain.model.enumerator.HeaderType;
import org.ibayer.html.parser.domain.model.enumerator.LinkType;
import org.ibayer.html.parser.service.HtmlService;
import org.ibayer.html.parser.service.JSoupParserService;
import org.ibayer.html.parser.service.WebPageService;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

public class WebPageServiceTest {

	private static final String FIVE = "5";

	@Mock
	private HtmlService htmlService;

	@Mock
	private JSoupParserService jSoupService;

	@Mock
	private Document document;

	@Spy
	private WebPageService webPageService;

	@Before
	public void setup() {
		htmlService = Mockito.mock(HtmlService.class);
		jSoupService = Mockito.mock(JSoupParserService.class);
		document = Mockito.mock(Document.class);
		Mockito.when(jSoupService.getDocument(Mockito.anyString())).thenReturn(document);
		webPageService = new WebPageService(jSoupService, htmlService);
	}

	@Test
	public void testGetWebPage() {

		Mockito.when(document.baseUri()).thenReturn("http://www.example.com");
		Mockito.when(htmlService.getHtmlVersion(Mockito.any(Document.class))).thenReturn(FIVE);
		Mockito.when(htmlService.getLinks(Mockito.any(Document.class), Mockito.any(URI.class)))
				.thenReturn(ImmutableSet.of(
						LinkElement.builder().active(Boolean.TRUE).linkType(LinkType.EXTERNAL)
								.href("http://www.example2.com").build(),
						LinkElement.builder().active(Boolean.TRUE).linkType(LinkType.INTERNAL).href("/test").build()));
		Mockito.when(htmlService.detectLoginForm(Mockito.any(Document.class))).thenReturn(null);
		Mockito.when(jSoupService.getTitle(Mockito.any(Document.class))).thenReturn(FIVE);

		Mockito.when(htmlService.getHeaderElements(Mockito.any(Document.class))).thenReturn(
				ImmutableList.of(HeaderElements.builder().texts(Arrays.asList("test")).type(HeaderType.H1).build()));
		WebPage webPage = webPageService.get(Mockito.anyString());
		Assert.assertNotNull(webPage);
		Assert.assertEquals(FIVE, webPage.getHtmlVersion());
		Assert.assertEquals(FIVE, webPage.getTitle());
		Assert.assertNotNull(webPage.getInternalLinks());
		Assert.assertEquals(1, webPage.getInternalLinks().size());
		Assert.assertNotNull(webPage.getExternalLinks());
		Assert.assertEquals(1, webPage.getExternalLinks().size());
		Assert.assertNotNull(webPage.getHeaderElements());
		Assert.assertEquals(1, webPage.getHeaderElements().size());
		Assert.assertEquals(HeaderType.H1, webPage.getHeaderElements().iterator().next().getType());
	}

}
