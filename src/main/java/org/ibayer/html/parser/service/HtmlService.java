package org.ibayer.html.parser.service;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.ibayer.html.parser.domain.model.HeaderElements;
import org.ibayer.html.parser.domain.model.LinkElement;
import org.ibayer.html.parser.domain.model.LoginFormElement;
import org.ibayer.html.parser.domain.model.enumerator.HeaderType;
import org.ibayer.html.parser.domain.model.enumerator.LinkType;
import org.ibayer.html.parser.service.function.LoginFormFunction;
import org.ibayer.html.parser.service.predicate.LoginFormPredicate;
import org.ibayer.html.parser.service.validator.LinksValidator;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service method to execute and parse html details.<br/>
 * This implementation method is tightly coupled with JSoup api. <br/>
 * The reason to choose coupling is current requirement, for a long term project
 * a wrapper for all Jsoup dependencies is better choise to have a loosely
 * coupled architecture. <br/>
 * Html service is thread safe.
 * 
 * @author ibrahim.bayer
 *
 */
@Service
public class HtmlService {

	private final LoginFormPredicate loginFormPredicate;
	private final LoginFormFunction loginFormFunction;
	private final LinksValidator linksValidator;

	@Autowired
	public HtmlService(final LoginFormPredicate loginFormPredicate, final LoginFormFunction loginFormFunction,
			final LinksValidator linksValidator) {
		this.loginFormFunction = loginFormFunction;
		this.loginFormPredicate = loginFormPredicate;
		this.linksValidator = linksValidator;
	}

	private static final String HTML_VERSION_IDENTIFIER = "publicid";
	private static final String HTML_VERSION_5 = "5";
	private static final String EMPTY_STRING = "";

	/**
	 * Extracts document version by checking "publicid" document property.<br/>
	 * If there is no public id property html5 is returned<br/>
	 * 
	 * @param document
	 * @return
	 */
	public String getHtmlVersion(final Document document) {
		final List<Node> nodes = document.childNodes();
		return nodes.stream().filter(node -> node instanceof DocumentType).map(node -> {
			String htmlVersion = ((DocumentType) node).attr(HTML_VERSION_IDENTIFIER);
			return EMPTY_STRING.equals(htmlVersion) ? HTML_VERSION_5 : htmlVersion;
		}).collect(Collectors.joining());
	}

	/**
	 * Extract links from existing document by given {@link LinkType}<br/>
	 * &lt;a href="/xxx"&gt; is a valid link<br/>
	 * &lt;a href="http://www.anydomain.com"&gt; is a valid link<br/>
	 * &lt;a href="#"&gt; is an <b>invalid link</b><br/>
	 * &lt;a&gt; is an <b>invalid link</b>. Link is used for internal routing as
	 * well.
	 * @param url
	 *            page url. Is used to identify absolute and relative links.
	 * @param document
	 *            Document to search links
	 * @return
	 */
	public Set<LinkElement> getLinks(final Document document,final URI uri) {
		final Elements links = document.select("a[href~=[^#]*");
		Set<LinkElement> response = links.stream()
				.map(link -> LinkElement.builder().linkType(getLinkType(uri, link)).hrefLang(link.attr("hrefLang"))
						.href(link.attr("href")).target(link.attr("target")).rel(link.attr("rel"))
						.type(link.attr("type")).build())
				.collect(Collectors.toSet());
		linksValidator.validate(response);
		return response;
	}

	/**
	 * Determines link type based on uri comparison. <br/>
	 * Relative uri is directly considered as internal <br/>
	 * If host are equal considered as internal <br/>
	 * If link is sub domain of page uri considered as external.<br/>
	 * All other links are considered as external.
	 * 
	 * @param pageUri
	 *            main page {@link URI}
	 * @param element
	 *            element including the link. Applicable for a html element with
	 *            href attribute
	 * @return LinkType or null. Null is returned for invalid link types.
	 */
	private LinkType getLinkType(final URI pageUri, final Element element) {
		final String testHref = element.attr("href");
		final URI linkUri = CommonUtils.getUri(testHref, true);
		if (linkUri == null) {
			return null;
		}

		// if the page uri is relative, it is internal
		if (!linkUri.isAbsolute()) {
			return LinkType.INTERNAL;
		} else if (pageUri.getHost().equals(linkUri.getHost())) {
			// if the hosts are same link is relative
			return LinkType.INTERNAL;
		}
		// assumption : sub domains are external links
		// otherwise they are external links
		return LinkType.EXTERNAL;
	}

	/**
	 * A login form requires a password type input and one more text input for
	 * username,loginid,etc.<br/>
	 * Typical login form has single password input. If there are more form
	 * could be a signup for as well "password and repeat password"
	 * structure.<br/>
	 * 
	 * @param document
	 * @return
	 */
	public LoginFormElement detectLoginForm(Document document) {
		final Elements forms = document.getElementsByTag("form");
		final List<LoginFormElement> loginForms = forms.stream().filter(loginFormPredicate).map(loginFormFunction)
				.collect(Collectors.toList());
		if (loginForms.isEmpty()) {
			return null;
		} else {
			return loginForms.get(0);
		}
	}

	/**
	 * For each header type extracts elements in document. <br/>
	 * Groups each header type inside a list and orderly returns values <br/>
	 * 
	 * @param document
	 * @return
	 */
	public List<HeaderElements> getHeaderElements(Document document) {
		List<HeaderType> types = Arrays.asList(HeaderType.values());
		return types.stream()
				.map(type -> HeaderElements.builder().type(type)
						.texts(document.getElementsByTag(type.toString().toLowerCase()).stream()
								.map(element -> element.text()).collect(Collectors.toList()))
						.build())
				.collect(Collectors.toList());
	}

}
