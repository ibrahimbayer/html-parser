package org.ibayer.html.parser.service.test;

import java.io.IOException;
import java.net.URL;

import org.ibayer.html.parser.exception.ResourceNotFoundException;
import org.ibayer.html.parser.service.JSoupParserService;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Jsoup.class)
public class JsoupParserServiceTest {

	@Mock
	private Connection connection;

	@Spy
	private JSoupParserService jSoupService = new JSoupParserService();

	private URL url = null;
			
	@Before
	public void before(){
		connection = Mockito.mock(Connection.class);
		url = Thread.currentThread().getContextClassLoader().getResource("Html5.html");
		PowerMockito.mockStatic(Jsoup.class);
		PowerMockito.when(Jsoup.connect(Mockito.anyString())).thenReturn(connection);
	}
	
	@Test
	public void shouldReturnDocumentWhenConnectionIsValid() throws IOException {
		Mockito.when(connection.get()).thenReturn(new Document(Mockito.anyString()));
		Document document = jSoupService.getDocument(url.getPath());
		Assert.assertNotNull(document);
	}

	@Test(expected = ResourceNotFoundException.class)
	public void shouldThrowIOExceptionWhenConnectionIsInvalid() throws IOException {
		Mockito.when(connection.get()).thenThrow(new IOException());
		Document document = jSoupService.getDocument(url.getPath());
		Assert.assertNull(document);
	}
}
