package org.ibayer.html.parser.service.function;

import java.util.function.Function;

import org.ibayer.html.parser.domain.model.LoginFormElement;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Function interface implementation for {@link LoginFormElement} building.
 * 
 * @author ibrahim.bayer
 *
 */
@Component
public class LoginFormFunction implements Function<Element, LoginFormElement> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.function.Function#apply(java.lang.Object)
	 */
	@Override
	public LoginFormElement apply(Element form) {
		LoginFormElement.LoginFormElementBuilder builder = LoginFormElement.builder();
		builder.action(form.attr("action")).method(RequestMethod.valueOf(form.attr("method").toUpperCase()))
				.target(form.attr("target")).name(form.attr("name"));
		return builder.build();
	}

}
