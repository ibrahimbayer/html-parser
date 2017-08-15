package org.ibayer.html.parser.api;

import java.util.Base64;

import org.ibayer.html.parser.domain.model.WebPage;
import org.ibayer.html.parser.service.WebPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author ibrahim.bayer
 *
 */
@RestController
@RequestMapping(value = "/webpages", produces = "application/json")
public class WebPageApi {

	private final WebPageService webPageService;

	@Autowired
	public WebPageApi(final WebPageService webPageService) {
		this.webPageService = webPageService;
	}

	/**
	 * Api method to get a {@link WebPage}. <br/>
	 * The url parameter has to be encoded Base64.
	 * @param url
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{url}")
	@ApiOperation(value = "Parse web page", notes = "Parse web page", tags = { "WebPages" })
	@ApiResponses({ @ApiResponse(code = 200, message = "Succesfully parsed web page!", response = WebPage.class) })
	public ResponseEntity<Resource<WebPage>> get(@PathVariable("url") String url) {
		url = new String(Base64.getDecoder().decode(url));
		WebPage page = webPageService.get(url);
		return new ResponseEntity<>(new Resource<>(page), HttpStatus.OK);
	}

}
