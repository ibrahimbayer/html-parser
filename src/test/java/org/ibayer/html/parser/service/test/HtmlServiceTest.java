package org.ibayer.html.parser.service.test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.ibayer.html.parser.domain.model.HeaderElements;
import org.ibayer.html.parser.domain.model.LinkElement;
import org.ibayer.html.parser.domain.model.LoginFormElement;
import org.ibayer.html.parser.domain.model.enumerator.HeaderType;
import org.ibayer.html.parser.domain.model.enumerator.LinkType;
import org.ibayer.html.parser.service.HtmlService;
import org.ibayer.html.parser.service.function.LoginFormFunction;
import org.ibayer.html.parser.service.predicate.LoginFormPredicate;
import org.ibayer.html.parser.service.validator.LinksValidator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;

public class HtmlServiceTest {

	@Mock
	LoginFormFunction loginFormFunction;

	@Mock
	LoginFormPredicate loginFormPredicate;

	@Mock
	Document document;

	@Mock
	LinksValidator linksValidator;

	@Spy
	HtmlService htmlService;

	@SuppressWarnings("unchecked")
	@Before
	public void setup() {
		loginFormFunction = Mockito.mock(LoginFormFunction.class);
		loginFormPredicate = Mockito.mock(LoginFormPredicate.class);
		linksValidator = Mockito.mock(LinksValidator.class);
		document = Mockito.mock(Document.class);
		Mockito.doNothing().when(linksValidator).validate(Mockito.anySet());
		htmlService = new HtmlService(loginFormPredicate, loginFormFunction, linksValidator);
	}

	@Test
	public void testHtmlVersion5() throws IOException {
		InputStream html5File = Thread.currentThread().getContextClassLoader().getResourceAsStream("Html5.html");
		Document document = Jsoup.parse(html5File, "UTF-8", "Html5.html");
		Assert.assertNotNull(document);
		String htmlVersion = htmlService.getHtmlVersion(document);
		Assert.assertNotNull(htmlVersion);
		Assert.assertEquals("5", htmlVersion);
	}

	@Test
	public void shouldReturnALoginFormForDetection() throws IOException {
		Mockito.when(loginFormFunction.apply(Mockito.any(Element.class)))
				.thenReturn(LoginFormElement.builder().build());
		Mockito.when(loginFormPredicate.test(Mockito.any(Element.class))).thenReturn(Boolean.TRUE);
		Mockito.when(document.getElementsByTag("form"))
				.thenReturn(new Elements(Arrays.asList(new Element(Tag.valueOf("input"), "http://www.example.com"))));
		LoginFormElement form = htmlService.detectLoginForm(document);
		Assert.assertNotNull(form);
	}

	@Test
	public void shouldReturnNullForNonExistingLoginFormDetection() throws IOException {
		Mockito.when(loginFormFunction.apply(Mockito.any(Element.class)))
				.thenReturn(LoginFormElement.builder().build());
		Mockito.when(loginFormPredicate.test(Mockito.any(Element.class))).thenReturn(Boolean.FALSE);
		// return empty elements
		Mockito.when(document.getElementsByTag("form")).thenReturn(new Elements());
		LoginFormElement form = htmlService.detectLoginForm(document);
		Assert.assertNull(form);
	}

	@Test
	public void shouldReturnInternalAbsoluteLinkElementFromElements() throws URISyntaxException {
		String url = "http://www.example.com";
		Element linkElement = new Element(Tag.valueOf("a"), url);
		linkElement.attr("hrefLang", "hrefLang").attr("target", "target").attr("rel", "rel").attr("type", "type")
				.attr("href", url);
		Elements links = new Elements(Arrays.asList(linkElement));
		Mockito.when(document.select(Mockito.anyString())).thenReturn(links);
		Set<LinkElement> linksSet = htmlService.getLinks(document, new URI(url));
		Assert.assertNotNull(linksSet);
		Assert.assertFalse(linksSet.isEmpty());
		LinkElement link = linksSet.iterator().next();
		Assert.assertEquals("hrefLang", link.getHrefLang());
		Assert.assertEquals(url, link.getHref());
		Assert.assertEquals(LinkType.INTERNAL, link.getLinkType());

	}

	@Test
	public void shouldReturnExternalAbsoluteLinkElementFromElements() throws URISyntaxException {
		String url = "http://www.example.com";
		Element linkElement = new Element(Tag.valueOf("a"), url);
		linkElement.attr("hrefLang", "hrefLang").attr("target", "target").attr("rel", "rel").attr("type", "type")
				.attr("href", url);
		Elements links = new Elements(Arrays.asList(linkElement));
		Mockito.when(document.select(Mockito.anyString())).thenReturn(links);
		Set<LinkElement> linksSet = htmlService.getLinks(document, new URI("http://www.example2.com"));
		Assert.assertNotNull(linksSet);
		Assert.assertFalse(linksSet.isEmpty());
		LinkElement link = linksSet.iterator().next();
		Assert.assertEquals("hrefLang", link.getHrefLang());
		Assert.assertEquals(url, link.getHref());
		Assert.assertEquals(LinkType.EXTERNAL, link.getLinkType());

	}

	@Test
	public void shouldReturnInternalRelativeLinkElementFromElements() throws URISyntaxException {
		String url = "/pages";
		Element linkElement = new Element(Tag.valueOf("a"), url);
		linkElement.attr("hrefLang", "hrefLang").attr("target", "target").attr("rel", "rel").attr("type", "type")
				.attr("href", url);
		Elements links = new Elements(Arrays.asList(linkElement));
		Mockito.when(document.select(Mockito.anyString())).thenReturn(links);
		Set<LinkElement> linksSet = htmlService.getLinks(document, new URI("http://www.example2.com"));
		Assert.assertNotNull(linksSet);
		Assert.assertFalse(linksSet.isEmpty());
		LinkElement link = linksSet.iterator().next();
		Assert.assertEquals("hrefLang", link.getHrefLang());
		Assert.assertEquals(url, link.getHref());
		Assert.assertEquals(LinkType.INTERNAL, link.getLinkType());

	}

	@Test
	public void testGetHeaders() throws IOException {
		Arrays.asList(HeaderType.values()).forEach(type -> {
			Elements hElements = new Elements(
					new Element(Tag.valueOf(type.name().toLowerCase()), "http://www.example.com")
							.text(type.name().toLowerCase() + Mockito.anyString()));
			Mockito.when(document.getElementsByTag(type.name().toLowerCase())).thenReturn(hElements);
		});

		List<HeaderElements> headers = htmlService.getHeaderElements(document);
		Assert.assertNotNull(headers);
		Assert.assertTrue(headers.size() == 6);
		headers.forEach(header -> {
			Assert.assertNotNull(header);
			Assert.assertNotNull(header.getTexts());
			Assert.assertEquals(1, header.getTexts().size());
			Assert.assertNotNull(header.getType());
		});

	}
}
