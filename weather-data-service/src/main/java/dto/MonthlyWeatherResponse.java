package dto;

import java.util.List;

public class MonthlyWeatherResponse {
    public Daily daily;

    public static class Daily {
        public List<String> time;
        public List<Double> temperature_2m_min;
        public List<Double> temperature_2m_max;
        public List<Double> precipitation_sum;
    }
}