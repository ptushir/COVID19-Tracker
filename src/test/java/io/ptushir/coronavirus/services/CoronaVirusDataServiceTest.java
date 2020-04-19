package io.ptushir.coronavirus.services;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import io.ptushir.coronavirustracker.services.CoronaVirusDataService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CoronaVirusDataServiceTest {

	@Autowired
	CoronaVirusDataService dataService;

	@MockBean
	HttpClient client;

	@MockBean
	HttpResponse<String> response;

	@Test
	public void fetchVirusData() throws IOException, InterruptedException {
		Mockito.when(client.send(Mockito.any(HttpRequest.class), HttpResponse.BodyHandlers.ofString()))
				.thenReturn(response);

	}
}
