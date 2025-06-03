package controller;

import client.WeatherApiClient;
import dto.WeatherData;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/weather")
public class WeatherDataController {
    
    private final WeatherApiClient weatherApiClient;
    
    public WeatherDataController(WeatherApiClient weatherApiClient) {
        this.weatherApiClient = weatherApiClient;
    }
    
    @GetMapping("/monthly")
    public List<WeatherData> getMonthlyWeatherData(
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam(defaultValue = "25.0330") double lat,
            @RequestParam(defaultValue = "121.5654") double lon) {
        
        YearMonth yearMonth = YearMonth.of(year, month);
        return weatherApiClient.fetchMonthlyData(yearMonth, lat, lon);
    }
}