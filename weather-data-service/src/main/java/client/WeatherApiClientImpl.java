package client;

import dto.MonthlyWeatherResponse;
import dto.WeatherData;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Component
public class WeatherApiClientImpl implements WeatherApiClient {

    private final WebClient webClient;

    public WeatherApiClientImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public List<WeatherData> fetchMonthlyData(YearMonth ym, double lat, double lon) {
        LocalDate startDate = ym.atDay(1);
        LocalDate endDate = LocalDate.now().isBefore(ym.atEndOfMonth())
                ? LocalDate.now()
                : ym.atEndOfMonth();

        String uri = "/v1/forecast"
                + "?latitude=" + lat
                + "&longitude=" + lon
                + "&daily=temperature_2m_min,temperature_2m_max,precipitation_sum"
                + "&start_date=" + startDate
                + "&end_date=" + endDate
                + "&timezone=Asia/Taipei";

        System.out.println("[WeatherApiClient] 呼叫 URI：" + uri);
        
        try {
            MonthlyWeatherResponse response = webClient.get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(MonthlyWeatherResponse.class)
                    .block();

            List<WeatherData> weatherDataList = new ArrayList<>();
            for (int i = 0; i < response.daily.time.size(); i++) {
                weatherDataList.add(new WeatherData(
                        LocalDate.parse(response.daily.time.get(i)),
                        response.daily.temperature_2m_min.get(i),
                        response.daily.temperature_2m_max.get(i),
                        response.daily.precipitation_sum.get(i)
                ));
            }
            return weatherDataList;

        } catch (WebClientResponseException e) {
            System.err.println("[WeatherApiClient] HTTP " + e.getStatusCode());
            System.err.println("[WeatherApiClient] " + e.getResponseBodyAsString());
            throw e;
        }
    }
}