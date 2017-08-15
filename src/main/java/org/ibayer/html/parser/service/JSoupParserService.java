package org.ibayer.html.parser.service;

import java.io.IOException;

import org.ibayer.html.parser.exception.ResourceNotFoundException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

/**
 * {@link Jsoup} access wrapper service. The implementation is tightly coupled
 * with Jsoup.<br/>
 * 
 * @author ibrahim.bayer
 *
 */
@Service
public class JSoupParserService {

	/**
	 * Returns {@link Document}
	 * @param url
	 * @return
	 */
	public Document getDocument(String url) {
		try {
			return Jsoup.connect(url).get();
		} catch (IOException e) {
			throw new ResourceNotFoundException("url",e);
		}
	}
	/**
	 * Returns title element content of document.<br/>
	 * Internally calls {@link Document} title() method. <br/>
	 * If there is no title present an empty string is returned instead of null
	 * value. <br/>
	 * 
	 * @param document
	 * @return
	 */
	public String getTitle(Document document) {
		return document.title();
	}

}
