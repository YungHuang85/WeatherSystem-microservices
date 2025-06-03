package client;

import dto.WeatherData;
import java.time.YearMonth;
import java.util.List;

public interface WeatherApiClient {
    List<WeatherData> fetchMonthlyData(YearMonth ym, double lat, double lon);
}