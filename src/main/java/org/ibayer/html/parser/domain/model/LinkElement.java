package org.ibayer.html.parser.domain.model;

import org.ibayer.html.parser.domain.model.enumerator.LinkType;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Object representation of html &lt;a&gt; element<br/>
 * For more details refer to <a href="https://www.w3schools.com/tags/tag_a.asp">w3School</a><br/>
 * documentation.<br/>
 * Compatible with HTML5 specifications.<br/>
 * @author ibrahim.bayer
 *
 */
@Data
@Builder
@EqualsAndHashCode(of="href")
public class LinkElement {
	private String href;
	private LinkType linkType;
	private String hrefLang;
	private String target;
	private String type;
	private String rel;
	private Boolean active;
	private String statusExplanation;
}
