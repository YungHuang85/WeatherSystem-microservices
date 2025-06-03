package controller;

import service.WeatherAnalysisService;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;

@RestController
@RequestMapping("/api/analysis")
public class WeatherAnalysisController {
    
    private final WeatherAnalysisService analysisService;
    
    public WeatherAnalysisController(WeatherAnalysisService analysisService) {
        this.analysisService = analysisService;
    }
    
    @PostMapping("/report")
    public void generateReport(@RequestParam int year, @RequestParam int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        analysisService.reportAndStats(yearMonth);
    }
}