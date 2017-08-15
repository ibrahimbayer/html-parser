package org.ibayer.html.parser.service.predicate;

import java.util.function.Predicate;

import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

/**
 * Predicate stream api function to find a html form is login form or not
 * 
 * @author ibrahim.bayer
 *
 */
@Component
public class LoginFormPredicate implements Predicate<Element> {

	private static final String INPUT_KEYWORD = "input";
	private static final String SIGNUP_KEYWORD = "signup";
	private static final String EMPTY_STRING = "";
	private static final String WHITESPACE_REGEX = " ";
	private static final String SIGNIN_KEYWORD = "signin";
	private static final String LOGIN_KEYWORD = "login";

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.function.Predicate#test(java.lang.Object)
	 */
	@Override
	public boolean test(Element form) {
		String formAction = form.attr("action");

		/// check if form has login action like <form action="login"> or <form
		/// action="signin">
		boolean hasLoginAction = formAction.toLowerCase().replaceAll(WHITESPACE_REGEX, EMPTY_STRING)
				.contains(LOGIN_KEYWORD)
				|| formAction.toLowerCase().replaceAll(WHITESPACE_REGEX, EMPTY_STRING).contains(SIGNIN_KEYWORD);

		// check if form has sign up action if there is sign up, form is a
		// signup form instead of login
		boolean hasSignupAction = formAction.contains(SIGNUP_KEYWORD);

		// login forms usually has single password input, however some signup
		// forms has single as well. so this field isn't enough to differentiate
		// signup and login. That is why there are other parameters
		boolean hasSinglePassword = form.getElementsByTag(INPUT_KEYWORD).stream()
				.filter(input -> input.attr("type").equals("password")).count() == 1;

		// checks form for login elements <input name="xxxloginxxx"> or <input
		// name="xxxsigninxxx">
		boolean hasLoginElement = form.getElementsByTag(INPUT_KEYWORD).stream()
				.filter(element -> element.attr("name").toLowerCase().replaceAll(WHITESPACE_REGEX, EMPTY_STRING)
						.contains(LOGIN_KEYWORD)
						|| element.attr("name").toLowerCase().replaceAll(WHITESPACE_REGEX, EMPTY_STRING)
								.contains(SIGNIN_KEYWORD))
				.count() > 0;
		// if there is sign-up action definitely form is sign-up otherwise rules
		// apply
		return !hasSignupAction && hasSinglePassword && (hasLoginElement || hasLoginAction);
	}
}
