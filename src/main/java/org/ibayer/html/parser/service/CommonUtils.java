package org.ibayer.html.parser.service;

import java.net.URI;
import java.net.URISyntaxException;

import org.ibayer.html.parser.exception.ResourceNotFoundException;

public final class CommonUtils {

	/**
	 * Gets {@link URI}, throws resource not found exception instead of
	 * {@link URISyntaxException}
	 * 
	 * @param document
	 * @return
	 */
	public static URI getUri(final String url,final boolean supressException) {
		final URI uri;
		try {
			uri = new URI(url);
		} catch (URISyntaxException e) {
			if (supressException){
				return null;
			}
			throw new ResourceNotFoundException("uri", e);
		}
		return uri;
	}
	
}
