package io.ptushir.coronavirustracker.services;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import io.ptushir.coronavirustracker.models.LocationStats;

@Service
public class CoronaVirusDataService {

	private static String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_daily_reports/04-12-2020.csv";
	private List<LocationStats> allStats = new ArrayList<>();
	
	public List<LocationStats> getAllStats() {
		return allStats;
	}

	@PostConstruct
	@Scheduled(cron = "* * * * * *")
	public void fetchVirusDfata() throws IOException, InterruptedException{
		List<LocationStats> newStats = new ArrayList<>();
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(VIRUS_DATA_URL))
				.build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		
		StringReader csvBodyReader = new StringReader(response.body());
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);
		for (CSVRecord record : records) {
		    LocationStats locationStats = new LocationStats();
		    locationStats.setState(record.get("Province_State"));
		    locationStats.setCountry(record.get("Country_Region"));
		    locationStats.setLatestTotalCases(Integer.parseInt(record.get("Confirmed")));
		    locationStats.setDeaths(Integer.parseInt(record.get("Deaths")));
		    locationStats.setRecovered(Integer.parseInt(record.get("Recovered")));
		    locationStats.setActive(Integer.parseInt(record.get("Active")));
		    newStats.add(locationStats);
		}
		this.allStats = newStats;
	}
}
