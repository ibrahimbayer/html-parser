package org.ibayer.html.parser.domain.model;

import java.util.List;
import java.util.Set;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

/**
 * All web page statistics are stored at {@link WebPage} model. <br/>
 * A wrapper class for each website. Storing url,html version, title, header
 * elements, links and login form.<br/>
 * 
 * <b>title</b> is exported from &lt;html&gt; &lt;title&gt; tag.<br/>
 * <b>links</b> are exported through remaining links in web page and grouped by
 * web page.<br/>
 * 
 * <b>login form</b> is exported from &lt;form&gt; elements with meta keyword
 * search. Username and password properties are exported through different
 * language options on form elements.<br/>
 * <b>For example :</b>
 * <ul>
 * <li>username - password, login - pin , email address - password are valid
 * pairs in English.</li>
 * <li>email addresse - passwort is valid pair in German</li>
 * <li>if there is a form with login inside action. This form is a potential
 * login form</li>
 * </ul>
 * 
 * @author ibrahim.bayer
 *
 */
@Data
@Builder
@ApiModel
public class WebPage {
	/**
	 * Link of web site. Support http(s) protocols.
	 */
	private String url;
	/**
	 * 
	 */
	private String htmlVersion;
	private String title;
	private List<HeaderElements> headerElements;
	private Set<LinkElement> internalLinks;
	private Set<LinkElement> externalLinks;
	private LoginFormElement loginForm;

}
