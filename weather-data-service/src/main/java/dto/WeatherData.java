package dto;

import java.time.LocalDate;

public record WeatherData(
        LocalDate date,
        double minTemp,
        double maxTemp,
        double precipitation
) {}