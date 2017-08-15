package org.ibayer.html.parser.domain.model;

import org.springframework.web.bind.annotation.RequestMethod;

import lombok.Builder;
import lombok.Data;

/**
 * Represents html login form element. <br/>
 * Storing basic required information for login form.
 * 
 * @author ibrahim.bayer
 *
 */
@Data
@Builder
public class LoginFormElement {
	private String name;
	private String action;
	private String target;
	private RequestMethod method;
}
