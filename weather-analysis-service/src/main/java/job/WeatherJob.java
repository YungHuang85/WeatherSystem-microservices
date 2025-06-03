package job;

import service.WeatherAnalysisService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

import java.time.YearMonth;

@Component
public class WeatherJob implements Job {

    private final WeatherAnalysisService analysisService;

    public WeatherJob(WeatherAnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    @Override
    public void execute(JobExecutionContext context) {
        YearMonth ym = YearMonth.now();
        analysisService.reportAndStats(ym);
    }
}