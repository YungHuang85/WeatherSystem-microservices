package client;

import dto.WeatherData;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

import java.util.List;

@Component
public class WeatherDataClient {
    
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String WEATHER_DATA_SERVICE_URL = "http://localhost:8081/weather-data";
    
    public List<WeatherData> getMonthlyWeatherData(int year, int month) {
        String url = WEATHER_DATA_SERVICE_URL + "/api/weather/monthly?year=" + year + "&month=" + month;
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<WeatherData>>() {}
        ).getBody();
    }
}