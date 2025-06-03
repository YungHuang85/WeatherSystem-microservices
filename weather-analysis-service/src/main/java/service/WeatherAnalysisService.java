package service;

import client.NotificationClient;
import client.WeatherDataClient;
import dto.WeatherData;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WeatherAnalysisService {

    private final WeatherDataClient weatherDataClient;
    private final NotificationClient notificationClient;
    
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("M月d日");

    public WeatherAnalysisService(WeatherDataClient weatherDataClient, 
                                NotificationClient notificationClient) {
        this.weatherDataClient = weatherDataClient;
        this.notificationClient = notificationClient;
    }

    public void reportAndStats(YearMonth ym) {
        List<WeatherData> data = weatherDataClient.getMonthlyWeatherData(
                ym.getYear(), ym.getMonthValue());

        Map<LocalDate, Double> dailyAvg = data.stream()
                .collect(Collectors.toMap(
                        WeatherData::date,
                        d -> (d.maxTemp() + d.minTemp()) / 2.0
                ));

        double avgOfDailyAvg = dailyAvg.values().stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(Double.NaN);

        LocalDate[] lowest3Dates = data.stream()
                .sorted(Comparator.comparingDouble(WeatherData::minTemp))
                .limit(3)
                .map(WeatherData::date)
                .sorted()
                .toArray(LocalDate[]::new);

        LocalDate[] highest3Dates = data.stream()
                .sorted(Comparator.comparingDouble(WeatherData::maxTemp).reversed())
                .limit(3)
                .map(WeatherData::date)
                .sorted()
                .toArray(LocalDate[]::new);

        double avgLow = Arrays.stream(lowest3Dates)
                .mapToDouble(date -> data.stream()
                        .filter(d -> d.date().equals(date))
                        .findFirst()
                        .map(WeatherData::minTemp)
                        .orElse(Double.NaN))
                .average()
                .orElse(Double.NaN);

        double avgHigh = Arrays.stream(highest3Dates)
                .mapToDouble(date -> data.stream()
                        .filter(d -> d.date().equals(date))
                        .findFirst()
                        .map(WeatherData::maxTemp)
                        .orElse(Double.NaN))
                .average()
                .orElse(Double.NaN);

        List<List<WeatherData>> rainyRuns = data.stream()
                .collect(Collectors.groupingBy(d -> d.date().toEpochDay() - data.indexOf(d)))
                .values().stream()
                .filter(group ->
                        group.size() >= 5 &&
                        group.stream().allMatch(d -> d.precipitation() > 0))
                .toList();

        StringBuilder msg = new StringBuilder();
        msg.append("每日天氣報告（").append(ym.getMonthValue()).append("月）\n\n");
        msg.append("日期 | 晴/雨 | 熱/冷 | 高溫 | 低溫 | 平均\n");
        msg.append("--- | --- | --- | --- | --- | ---\n");

        for (WeatherData d : data) {
            String date = d.date().format(DATE_FMT);
            String rain = d.precipitation() > 0 ? "雨" : "晴";
            double avg = dailyAvg.get(d.date());
            String hotCold = avg >= avgOfDailyAvg ? "熱" : "冷";
            msg.append(String.format(
                    "%s | %s | %s | %.1f℃ | %.1f℃ | %.1f℃\n",
                    date, rain, hotCold, d.maxTemp(), d.minTemp(), avg
            ));
        }

        msg.append("\n**統計結果**\n");
        msg.append("\n\t最低 3 天：\n").append(
                Arrays.stream(lowest3Dates)
                        .map(d -> d.format(DATE_FMT))
                        .collect(Collectors.joining(", "))
        ).append(String.format("（平均 %.1f℃）\n", avgLow));

        msg.append("\n\t最高 3 天：\n").append(
                Arrays.stream(highest3Dates)
                        .map(d -> d.format(DATE_FMT))
                        .collect(Collectors.joining(", "))
        ).append(String.format("\n\t（平均 %.1f℃）\n", avgHigh));

        if (!rainyRuns.isEmpty()) {
            for (List<WeatherData> run : rainyRuns) {
                String from = run.get(0).date().format(DATE_FMT);
                String to = run.get(run.size() - 1).date().format(DATE_FMT);
                msg.append(String.format("\n\t 連續 5 天下雨：%s ~ %s\n", from, to));
            }
        } else {
            msg.append("\n\t☀️ 本月無連續 5 天下雨\n");
        }

        msg.append("\n 週末天氣：\n");
        data.stream()
                .filter(d -> {
                    DayOfWeek dow = d.date().getDayOfWeek();
                    return dow == DayOfWeek.SATURDAY || dow == DayOfWeek.SUNDAY;
                })
                .sorted(Comparator.comparing(WeatherData::date))
                .forEach(d -> {
                    String date = d.date().format(DATE_FMT);
                    String dowName = d.date().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.TAIWAN);
                    String rain = d.precipitation() > 0 ? "雨" : "晴";
                    msg.append(String.format("- %s %s：%s\n", date, dowName, rain));
                });

        notificationClient.sendTeamsMessage(msg.toString());
    }
}